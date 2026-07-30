package com.lms.sch.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.StudentHomeworkAdapter1
import com.lms.sch.databinding.ActivityHomeWorkResultBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.response.HwSingleViewResponse
import com.lms.sch.response.TeacherHwSingleResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class HomeWorkResultActivity : BaseActivity() {
    private lateinit var binding : ActivityHomeWorkResultBinding
    var result = ArrayList<HwSingleViewResponse.Result>()
    var search = ""
    var count = 0
    var homeworkAttach = ArrayList<String>()
    var hwid = ""
    var marksts = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeWorkResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        hwid = intent.getStringExtra(Constants.IntentKeys.KEY) ?: ""
        marksts = intent.getStringExtra("status") ?: ""
        if (marksts == "completed") {
            UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
            UiUtils.textViewTextColor(binding.status, "#32B138", null)
            UiUtils.textViewTextColor(binding.points, "#32B138", null)
        } else if (marksts == "pending") {
            UiUtils.textViewBgTint(binding.status, "#DAE7FF", null)
            UiUtils.textViewTextColor(binding.status, "#3F8BFB", null)
            UiUtils.textViewTextColor(binding.points, "#3F8BFB", null)
        }
        Log.d("gfdhgsf", hwid)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        binding.doneHw.setOnClickListener {
            onBackPressed()
        }
        binding.cancel.setOnClickListener {
            onBackPressed()
        }
        ApiConnection.getInstance().stdSingleHwHw(this, hwid).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            result = it.result!!
                            binding.attach.setOnClickListener {
                                openDocList()
                            }
                            binding.makeasdone.setOnClickListener {
                                if (homeworkAttach.isNotEmpty()) {
                                    DialogUtils.showLoader(this)
                                    if (hwid != null) {
                                        ApiConnection.getInstance()
                                            .homeStsUpdate(this, hwid, homeworkAttach)
                                            .observe(this) {
                                                it.let {
                                                    DialogUtils.dismissLoader()
                                                    it.success.let { success ->
                                                        if (success) {
                                                            UiUtils.showSnack(it.msg, binding.root, true)
                                                            onBackPressed()
    //                                                        binding.root.visibility = View.GONE
                                                            homeworkAttach.clear()
                                                            binding.attach.text = ""
                                                        }
                                                        else {
                                                            UiUtils.showSnack(it.msg, binding.root, false)
                                                        }
                                                    }
                                                }
                                            }
                                    } else {
                                        UiUtils.showSnack("Homework id is not present", binding.root, false)
                                    }
                                } else {
                                    UiUtils.showSnack("Please upload your Homework", binding.root, false)
                                }
                            }

                            binding.noteCheck.setOnClickListener {
                                val homeworkId = hwid
                                if (homeworkId != null) {
                                    AlertDialog.Builder(this)
                                        .setTitle("Undo Homework")
                                        .setMessage("This will permanently delete your homework progress.")
                                        .setPositiveButton("Yes Undo") { dialog, _ ->
                                            dialog.dismiss()
                                            DialogUtils.showLoader(this)
                                            ApiConnection.getInstance().undoHomework(this, hwid)
                                                .observe(this) {
                                                    it.let {
                                                        DialogUtils.dismissLoader()
                                                        it.success.let { success ->
                                                            if (success) {
                                                                UiUtils.imageviewDrawable(binding.noteCheck, R.drawable.rectangle_checkbox)
                                                                UiUtils.showSnack(it.msg, binding.root, true)
                                                                binding.root.visibility = View.GONE
                                                            } else {
                                                                UiUtils.showSnack(it.msg, binding.root, false)
                                                            }
                                                        }
                                                    }
                                                }
                                        }
                                        .setNegativeButton("Cancel") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .create()
                                        .show()
                                } else {
                                    UiUtils.showSnack("Homework id is not present", binding.root, false)
                                }

                            }
                            binding.points.text = "+${result[0].credits} Points"
                            if (result[0].subject != null) {
                                binding.subject.text = "Subject : ${result[0].subject!!.name}"
                            } else {
                                binding.subject.text = "Subject : --/--"
                            }
                            if (result[0].homework != null) {
                                binding.que.text = result[0].homework!!.title
                                binding.desc.text = result[0].homework!!.description
                                if (result[0].homework!!.createdBy != null) {
                                    binding.teacher.text = "Teacher : ${result[0].homework!!.createdBy!!.firstName + " " + result[0].homework!!.createdBy!!.lastName}"
                                } else {
                                    binding.teacher.text = "Teacher : --/--"
                                }
                            } else {
                                binding.que.text = "--/--"
                                binding.desc.text = "--/--"
                            }
                            val givenDate = BaseUtils.getFormattedDate(result[0].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                            val dueDate = BaseUtils.getFormattedDate(result[0].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)

                            val status = result[0].status
                            val markStatus = result[0].markStatus

                            val role = sharedHelper.role
                            if (role == "PARENT") {
                                if (status == "completed" && markStatus == "completed") {
                                    // Submitted
                                    binding.uploadLay.visibility = View.GONE
                                    binding.okCancel.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.VISIBLE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    UiUtils.textViewTextColor(binding.points, "#32B138", null)
                                    val submittedOn = BaseUtils.getFormattedDate(
                                        result[0].submittedOn!!,
                                        Constants.ApiKeys.TIME_INPUT_FORMAT,
                                        Constants.ApiKeys.DATE_FORMAT
                                    )
                                    binding.submitOn.text = "Submitted On "
                                    binding.submitDate.text = ":- ${submittedOn}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    if (result[0].credits != null && result[0].credits!!.isNotEmpty() && result[0].credits!!.toInt() > 0) {
                                        binding.points.text = "+${result[0].credits} Points"
                                    } else {
                                        binding.points.text = "0 Point"
                                    }
                                    binding.noteCheck.visibility = View.GONE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.note.text = "Good job! Your Homework has been submitted successfully, You’ve gained points for your submission!"
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#EFFFF0", null)

                                    if (result[0].attachment!! != null && result[0].attachment!!.isNotEmpty()) {
                                        binding.attachRecycler.visibility = View.VISIBLE
                                        val adapter = AttachAdapter(this, result[0].attachment!!)
                                        val layoutManager = LinearLayoutManager(
                                            this,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        binding.attachRecycler.layoutManager = layoutManager
                                        binding.attachRecycler.adapter = adapter
                                    } else {
                                        binding.attachRecycler.visibility = View.GONE
                                    }
                                }
                                else if (status == "pending" && markStatus == "pending") {
                                    // Ongoing
                                    binding.uploadLay.visibility = View.GONE
                                    binding.okCancel.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.GONE
                                    binding.points.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.VISIBLE
                                    binding.doneHw.text = "Okay , I’ll Prepare for it"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Your child Homework not updated "
                                    binding.noteCheck.visibility = View.GONE
                                    binding.status.text = "Not Completed"
                                    UiUtils.textviewCustomDrawable(
                                        binding.status,
                                        R.drawable.border_curve_24dp
                                    )
                                    UiUtils.textViewBgTint(binding.status, "#fff2d9", null)
                                    UiUtils.textViewTextColor(binding.status, "#F69300", null)
                                    binding.points.text = "0 Point"
                                    UiUtils.textViewTextColor(binding.points, "#F69300", null)
                                    binding.submitOn.text = "Last Date"
                                    binding.submitDate.text = ":- ${dueDate}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    binding.note.text =
                                        "Mark as Done if you’ve completed this homework. This is just for you to remember what you’ve finished!"
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#FFF2F2", null)
                                }
                                else if (status == "completed" && markStatus == "pending") {
                                    // Completed, but markStatus not yet updated
                                    binding.uploadLay.visibility = View.GONE
                                    binding.okCancel.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.VISIBLE
                                    binding.doneHw.text = "Okay"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.GONE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    binding.remarks.text = "Your Points update later"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    val submittedOn = BaseUtils.getFormattedDate(result[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                    binding.submitOn.text = "Submitted On"
                                    binding.submitDate.text = ":- ${submittedOn}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    binding.noteCheck.visibility = View.VISIBLE
                                    UiUtils.imageviewDrawable(binding.noteCheck, R.drawable.green_tick)
                                    binding.note.text = "Good job! Your Homework has been submitted successfully, You’ve gained points not updated!"
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#EFFFF0", null)
                                    if (result[0].attachment!! != null && result[0].attachment!!.isNotEmpty()) {
                                        binding.attachRecycler.visibility = View.VISIBLE
                                        val adapter = AttachAdapter(this, result[0].attachment!!)
                                        val layoutManager = LinearLayoutManager(
                                            this,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        binding.attachRecycler.layoutManager = layoutManager
                                        binding.attachRecycler.adapter = adapter
                                    } else {
                                        binding.attachRecycler.visibility = View.GONE
                                    }
                                }
                                else if (status == "overdue") {
                                    // Overdue
                                    binding.uploadLay.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.okCancel.visibility = View.GONE
                                    binding.attachLay.visibility = View.GONE
                                    binding.doneHw.visibility = View.VISIBLE
                                    binding.doneHw.text = "Okay , I’ll Prepare for it"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Your child Homework not updated "
                                    binding.noteCheck.visibility = View.GONE
                                    binding.points.visibility = View.GONE
                                    binding.status.text = "Not Completed"
                                    UiUtils.textviewCustomDrawable(
                                        binding.status,
                                        R.drawable.border_curve_24dp
                                    )
                                    UiUtils.textViewBgTint(binding.status, "#fce6e6", null)
                                    UiUtils.textViewTextColor(binding.status, "#EA5455", null)
                                    binding.submitOn.text = "Last Date : "
                                    binding.submitDate.text = ":- ${dueDate}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    binding.note.text =
                                        "This Homework was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"
                                }
                            }
                            else{
                                if (status == "completed" && markStatus == "completed") {
                                    // Submitted
                                    binding.uploadLay.visibility = View.GONE
                                    binding.okCancel.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.VISIBLE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    UiUtils.textViewTextColor(binding.points, "#32B138", null)
                                    val submittedOn = BaseUtils.getFormattedDate(result[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                    binding.submitOn.text = "Submitted On "
                                    binding.submitDate.text = ":- ${submittedOn}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    if (result[0].credits != null && result[0].credits!!.isNotEmpty() && result[0].credits!!.toInt() > 0) {
                                        binding.points.text = "+${result[0].credits} Points"
                                    } else {
                                        binding.points.text = "0 Point"
                                    }
                                    binding.noteCheck.visibility = View.GONE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.note.text = "Good job! Your Homework has been submitted successfully, You’ve gained points for your submission!"
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#EFFFF0", null)

                                    if (result[0].attachment!! != null && result[0].attachment!!.isNotEmpty()) {
                                        binding.attachRecycler.visibility = View.VISIBLE
                                        val adapter = AttachAdapter(this, result[0].attachment!!)
                                        val layoutManager = LinearLayoutManager(
                                            this,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        binding.attachRecycler.layoutManager = layoutManager
                                        binding.attachRecycler.adapter = adapter
                                    } else {
                                        binding.attachRecycler.visibility = View.GONE
                                    }
                                }
                                else if (status == "pending" && markStatus == "pending") {
                                    // Ongoing
                                    binding.uploadLay.visibility = View.VISIBLE
                                    binding.okCancel.visibility = View.VISIBLE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.GONE
                                    binding.points.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.GONE
                                    binding.doneHw.text = "Okay"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Kindly upload your homework "
                                    binding.noteCheck.visibility = View.GONE
                                    binding.status.text = "Not Completed"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#fff2d9", null)
                                    UiUtils.textViewTextColor(binding.status, "#F69300", null)
                                    binding.points.text = "0 Point"
                                    UiUtils.textViewTextColor(binding.points, "#F69300", null)
                                    binding.submitOn.text = "Last Date"
                                    binding.submitDate.text = ":- ${dueDate}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    binding.note.text = "Mark as Done if you’ve completed this homework. This is just for you to remember what you’ve finished!"
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#FFF2F2", null)
                                }
                                else if (status == "completed" && markStatus == "pending") {
                                    // Completed, but markStatus not yet updated
                                    binding.uploadLay.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.VISIBLE
                                    binding.doneHw.text = "Okay"
                                    binding.okCancel.visibility = View.GONE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.GONE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    binding.remarks.text = "Your Points update later"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    val submittedOn = BaseUtils.getFormattedDate(result[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                    binding.submitOn.text = "Submitted On"
                                    binding.submitDate.text = ":- ${submittedOn}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    binding.noteCheck.visibility = View.VISIBLE
                                    UiUtils.imageviewDrawable(binding.noteCheck, R.drawable.green_tick)
                                    binding.note.text = "Good job! Your Homework has been submitted successfully, You’ve gained points not updated!"
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#EFFFF0", null)

                                    if (result[0].attachment!! != null && result[0].attachment!!.isNotEmpty()) {
                                        binding.attachRecycler.visibility = View.VISIBLE
                                        val adapter = AttachAdapter(this, result[0].attachment!!)
                                        val layoutManager = LinearLayoutManager(
                                            this,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        binding.attachRecycler.layoutManager = layoutManager
                                        binding.attachRecycler.adapter = adapter
                                    } else {
                                        binding.attachRecycler.visibility = View.GONE
                                    }
                                }
                                else if (status == "overdue") {
                                    // Overdue
                                    binding.uploadLay.visibility = View.VISIBLE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.GONE
                                    binding.doneHw.visibility = View.GONE
                                    binding.okCancel.visibility = View.VISIBLE
                                    binding.doneHw.text = "Okay"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Kindly upload your homework "
                                    binding.noteCheck.visibility = View.GONE
                                    binding.points.visibility = View.GONE
                                    binding.status.text = "Not Completed"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#fce6e6", null)
                                    UiUtils.textViewTextColor(binding.status, "#EA5455", null)
                                    binding.submitOn.text = "Last Date : "
                                    binding.submitDate.text = ":- ${dueDate}"
                                    binding.givenDate.text = ":- ${givenDate}"
                                    binding.note.text = "This Homework was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"
                                }
                            }
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun openDocList() {
        if (BaseUtils.isPermissionsEnabled(this, Constants.IntentKeys.STORAGE)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.putExtra(
                Intent.EXTRA_MIME_TYPES, arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg",
                    "application/pdf",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
                    "application/msword"
                )
            )
            startActivityForResult(intent, 12)
        } else {
            BaseUtils.permissionsEnableRequest(this, Constants.IntentKeys.STORAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK) {
            if (data != null){
                if (data.clipData != null){
                    var docs = data.clipData
                    for (items in 0 until  docs!!.itemCount){
                        count++
                        val uri = docs.getItemAt(items).uri
                        var filePart: MultipartBody.Part? = null
                        if (uri != null) {
                            val documentFile = DocumentFile.fromSingleUri(this, uri)
                            val fileInputStream = this.contentResolver.openInputStream(uri)
                            val mimeType = this.contentResolver.getType(uri)
                            val buffer = fileInputStream?.readBytes()
                            fileInputStream?.close()
                            if (buffer != null && mimeType != null) {
                                val fileSize = buffer.size
                                val fileSizeInMB = fileSize / (1024.0 * 1024.0)
                                val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                                filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                                val size = BaseUtils.convertBytes(fileSize.toLong())
                                Log.d("dv1", "" + documentFile?.name)
                                Log.d("dv0", "" + uri.path)
                                Log.d("dv3", "" + size)
                                Log.d("dv4", "" + mimeType)
                                if (fileSizeInMB <= 5){
                                    val json = JSONObject()
                                    json.put("name",documentFile?.name)
                                    json.put("size",size)
                                    json.put("type",mimeType)
//                                    urlName.add(json)
                                    upload(filePart)
                                } else {
                                    UiUtils.showSnack("File size exceeds 50 MB", binding.root,false)
                                }
                            }
                        }
                    }
                } else {
                    val uri = data?.data
                    var filePart: MultipartBody.Part? = null
                    count++
                    if (uri != null) {
                        val documentFile = DocumentFile.fromSingleUri(this, uri)
                        val fileInputStream = this.contentResolver.openInputStream(uri)
                        val mimeType = this.contentResolver.getType(uri)
                        val buffer = fileInputStream?.readBytes()
                        fileInputStream?.close()
                        if (buffer != null && mimeType != null) {
                            val fileSize = buffer.size
                            val fileSizeInMB = fileSize / (1024.0 * 1024.0)
                            val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                            filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                            val size = BaseUtils.convertBytes(fileSize.toLong())
                            Log.d("dv1", "" + documentFile?.name)
                            Log.d("dv0", "" + uri.path)
                            Log.d("dv3", "" + size)
                            Log.d("dv4", "" + mimeType)
                            if (fileSizeInMB <= 5){
                                val json = JSONObject()
                                json.put("name",documentFile?.name)
                                json.put("size",size)
                                json.put("type",mimeType)
//                                urlName.add(json)
                                binding.attach.text = documentFile?.name
                                upload(filePart)
                            } else {
                                UiUtils.showSnack("File size exceeds 5 MB", binding.root,false)
                            }
                        }
                    }
                }
            }
        }
    }
    fun upload(filepart:MultipartBody.Part){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().uploadFile(this, filepart).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        val url = it.result[0].location!!
                        homeworkAttach.add(url)
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }
}