package com.lms.sch.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import com.fasterxml.jackson.databind.ser.Serializers
import com.lms.sch.R
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.databinding.ActivityAssignmentResBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.AssSglViewResponse
import com.lms.sch.response.AssSglViewResponse.Result.Assignment.CreatedBy
import com.lms.sch.response.HwSingleViewResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class AssignmentResActivity : BaseActivity() {
    private lateinit var binding: ActivityAssignmentResBinding
    var result = ArrayList<AssSglViewResponse.Result>()
    var assid = ""
    var count = 0
    var assignmentAttach = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAssignmentResBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        assid = intent.getStringExtra(Constants.IntentKeys.KEY) ?: ""
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        binding.cancel.setOnClickListener {
            onBackPressed()
        }
        binding.doneAssingment.setOnClickListener {
            onBackPressed()
        }
        ApiConnection.getInstance().getCompletedStudents(this, assid).observe(this) {
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
                                if (assignmentAttach.isNotEmpty()) {
                                    DialogUtils.showLoader(this)
                                    val assignmentId = result[0].assignment!!._id
                                    if (assignmentId != null) {
                                        ApiConnection.getInstance().assignmentStsUpdate(this,assignmentId, assignmentAttach ).observe(this) {
                                            it.let {
                                                DialogUtils.dismissLoader()
                                                it.success.let { success ->
                                                    if (success) {
                                                        UiUtils.showSnack(  it.msg,   binding.root,  true )
//                                                        binding.root.visibility = View.GONE
//                                                        getStudentAssignment()
                                                        onBackPressed()
                                                    } else {
                                                        UiUtils.showSnack(   it.msg,  binding.root,   false)
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        UiUtils.showSnack( "Assignment id is not present",  binding.root, false)
                                    }
                                } else {
                                    UiUtils.showSnack("Please upload your Assignment",  binding.root, false )
                                }
                            }

                            if (result[0].assignment != null && result[0].assignment!!.createdBy != null) {
                                binding.teacher.text = "Teacher : ${result[0].assignment!!.createdBy!!.firstName} ${result[0].assignment!!.createdBy!!.lastName}"
                            } else {
                                binding.teacher.text = "--/--"
                            }
                            if (result[0].subject != null && result[0].subject!!.name!!.isNotEmpty()) {
                                binding.subject.text = "Subject  : ${result[0].subject!!.name}"
                            } else {
                                binding.subject.text = "--/--"
                            }
                            if (result[0].assignment != null && result[0].assignment!!.title != null && result[0].assignment!!.description != null) {
                                binding.assTitle.text = result[0].assignment!!.title
                                binding.tMarks.text = " / ${result[0].assignment!!.totalMarks}"
                                binding.assdsc.text = result[0].assignment!!.description
                            } else {
                                binding.assTitle.text = "--/--"
                                binding.assdsc.text = "--/--"
                            }
                            if (result[0].createdAt != null) {
                                binding.givendate.text = BaseUtils.getFormattedDate(  result[0].createdAt!!,  Constants.ApiKeys.TIME_INPUT_FORMAT,  Constants.ApiKeys.DATE_FORMAT )
                            } else {
                                binding.givendate.text = "--/--"
                            }
                            var dueDate = BaseUtils.getFormattedDate(result[0].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT  )
                            var submitDate = BaseUtils.getFormattedDate(result[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT )

                            val status = result[0].status
                            val markStatus = result[0].markStatus
                            val role = sharedHelper.role
                            if (role == "PARENT"){
                                if (status == "completed" && markStatus == "completed") {
                                    binding.resultLay.visibility = View.VISIBLE
                                    binding.okCancel.visibility = View.GONE
                                    binding.uploadLay.visibility = View.GONE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.doneAssingment.visibility = View.VISIBLE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    UiUtils.textviewCustomDrawable(  binding.status,  R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    UiUtils.textViewTextColor(binding.points, "#32B138", null)
                                    binding.submitOn.text = "Submitted On : "
                                    binding.submitdate.text = submitDate
                                    if (result[0].submittedOnTime) {
                                        binding.note.text =  "Good job! Your Assignment has been submitted successfully, You’ve gained points for your submission!"
                                        UiUtils.linearLayoutBgTint(binding.noteLay, "#EFFFF0", null)
                                    } else {
                                        binding.note.text = "Your Assignment was submitted late. Great effort! Aim to submit on time to maximize your points."
                                        UiUtils.linearLayoutBgTint(binding.noteLay, "#fafce3", null)
                                    }

                                    if (result[0].credits != null && result[0].credits!!.isNotEmpty() && result[0].credits!!.toInt() > 0) {
                                        binding.points.text = "+${result[0].credits} Points"
                                        UiUtils.textViewTextColor(binding.points, "#32B138", null)
                                    } else {
                                        binding.points.text = "0 Point"
                                        UiUtils.textViewTextColor(binding.points, "#EA5455", null)
                                    }
                                    if (result[0].markStatus == "pending") {
                                        binding.remarks.text = "Points Not yet updated"
                                        UiUtils.textViewTextColor( binding.remarks, "#333333",  null ) //orange
                                    } else {
                                        binding.sMarks.text = result[0].scored_marks
                                        if (result[0].remarks != null){
                                            when (result[0].remarks.toString()) {
                                                "verygood" -> { binding.remarks.text =    "Outstanding performance! You’re doing great."
                                                    UiUtils.textViewGradient(    binding.remarks,   "#32B138",  "#138f18"   )//green
                                                }

                                                "good" -> { binding.remarks.text =  "Great job! Keep improving steadily."
                                                    UiUtils.textViewTextColor(   binding.remarks,   "#3F8BFB",  null  ) //blue
                                                }

                                                "poor" -> {
                                                    binding.remarks.text = "Keep trying; you’ll get there soon."
                                                    UiUtils.textViewTextColor( binding.remarks,  "#F69300", null) //orange
                                                }

                                                "need_attention" -> {
                                                    binding.remarks.text ="Work harder; success is within reach."
                                                    UiUtils.textViewTextColor(  binding.remarks, "#F69300",  null) //orange
                                                }
                                            }
                                        }else{
                                            binding.remarks.text = "--/--"
                                        }
                                    }

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
                                    binding.uploadLay.visibility = View.GONE
                                    binding.okCancel.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.GONE
                                    binding.doneAssingment.visibility = View.VISIBLE
                                    binding.doneAssingment.text = "Okay , I’ll Prepare for it"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Your child Assignment not Submitted "
                                    binding.status.text = "Not Completed"
                                    binding.resultLay.visibility = View.GONE
                                    binding.points.visibility = View.GONE
                                    UiUtils.textviewCustomDrawable( binding.status,  R.drawable.border_curve_24dp )
                                    UiUtils.textViewBgTint(binding.status, "#fce6e6", null)
                                    UiUtils.textViewTextColor(binding.status, "#EA5455", null)
                                    UiUtils.textViewTextColor(binding.points, "#EA5455", null)
                                    binding.submitOn.text = "Last Date : "
                                    binding.submitdate.text = dueDate
                                    binding.note.text = "This Assingment was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"
                                }
                                else if (status == "pending" && markStatus == "pending") {
                                    binding.uploadLay.visibility = View.GONE
                                    binding.okCancel.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.GONE
                                    binding.doneAssingment.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.GONE
                                    binding.doneAssingment.text = "Okay , I’ll Prepare for it"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Your child Assignment not Submitted "
                                    binding.status.text = "Not Completed"
                                    binding.resultLay.visibility = View.GONE
                                    binding.points.visibility = View.GONE
                                    UiUtils.textviewCustomDrawable( binding.status,R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#fff2d9", null)
                                    UiUtils.textViewTextColor(binding.status, "#F69300", null)
                                    binding.submitOn.text = "Last Date : "
                                    binding.submitdate.text = dueDate
                                    binding.note.text = "Don't forget! You need to complete and submit before the Last date to earn points, if you miss the deadline, you won't receive any points."
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#FFF2F2", null)
                                }
                                else if (status == "completed" && markStatus == "pending") {
                                    binding.uploadLay.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.doneHw.visibility = View.VISIBLE
                                    binding.okCancel.visibility = View.GONE
                                    binding.doneHw.text = "Okay"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.GONE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    binding.remarks.text = "Points Not yet updated"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    val submittedOn = BaseUtils.getFormattedDate(result[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                    binding.submitOn.text = "Submitted On"
                                    binding.submitdate.text = ":- ${submittedOn}"
                                    binding.givendate.text = ":- ${dueDate}"
                                    binding.note.text = "Good job! Your Assignment has been submitted successfully, You’ve gained points not updated!"
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
                                binding.root.visibility = View.VISIBLE
                            }
                            else{
                                if (status == "completed" && markStatus == "completed") {
                                    binding.resultLay.visibility = View.VISIBLE
                                    binding.okCancel.visibility = View.GONE
                                    binding.uploadLay.visibility = View.GONE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.doneAssingment.visibility = View.VISIBLE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    UiUtils.textviewCustomDrawable(  binding.status,  R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    UiUtils.textViewTextColor(binding.points, "#32B138", null)
                                    binding.submitOn.text = "Submitted On: "
                                    binding.submitdate.text = submitDate
                                    if (result[0].submittedOnTime) {
                                        binding.note.text =  "Good job! Your Assignment has been submitted successfully, You’ve gained points for your submission!"
                                        UiUtils.linearLayoutBgTint(binding.noteLay, "#EFFFF0", null)
                                    } else {
                                        binding.note.text = "Your Assignment was submitted late. Great effort! Aim to submit on time to maximize your points."
                                        UiUtils.linearLayoutBgTint(binding.noteLay, "#fafce3", null)
                                    }

                                    if (result[0].credits != null && result[0].credits!!.isNotEmpty() && result[0].credits!!.toInt() > 0) {
                                        binding.points.text = "+${result[0].credits} Points"
                                        UiUtils.textViewTextColor(binding.points, "#32B138", null)
                                    } else {
                                        binding.points.text = "0 Point"
                                        UiUtils.textViewTextColor(binding.points, "#EA5455", null)
                                    }
                                    if (result[0].markStatus == "pending") {
                                        binding.remarks.text = "Not yet updated"
                                        UiUtils.textViewTextColor( binding.remarks, "#333333",  null ) //orange
                                    }
                                    else {
                                        binding.sMarks.text = result[0].scored_marks
                                        if (result[0].remarks != null){
                                            when (result[0].remarks.toString()) {
                                                "verygood" -> { binding.remarks.text =    "Outstanding performance! You’re doing great."
                                                    UiUtils.textViewGradient(    binding.remarks,   "#32B138",  "#138f18"   )//green
                                                }

                                                "good" -> { binding.remarks.text =  "Great job! Keep improving steadily."
                                                    UiUtils.textViewTextColor(   binding.remarks,   "#3F8BFB",  null  ) //blue
                                                }

                                                "poor" -> {
                                                    binding.remarks.text = "Keep trying; you’ll get there soon."
                                                    UiUtils.textViewTextColor( binding.remarks,  "#F69300", null) //orange
                                                }

                                                "need_attention" -> {
                                                    binding.remarks.text ="Work harder; success is within reach."
                                                    UiUtils.textViewTextColor(  binding.remarks, "#F69300",  null) //orange
                                                }
                                            }
                                        }else{
                                            binding.remarks.text = "--/--"
                                        }
                                    }

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
                                    binding.uploadLay.visibility = View.VISIBLE
                                    binding.okCancel.visibility = View.VISIBLE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.GONE
                                    binding.doneAssingment.visibility = View.GONE
                                    binding.doneAssingment.text = "Okay"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Kindly upload your Assignment"
                                    binding.status.text = "Not Completed"
                                    binding.resultLay.visibility = View.GONE
                                    binding.points.visibility = View.GONE
                                    UiUtils.textviewCustomDrawable( binding.status,  R.drawable.border_curve_24dp )
                                    UiUtils.textViewBgTint(binding.status, "#fce6e6", null)
                                    UiUtils.textViewTextColor(binding.status, "#EA5455", null)
                                    UiUtils.textViewTextColor(binding.points, "#EA5455", null)
                                    binding.submitOn.text = "Last Date: "
                                    binding.submitdate.text = dueDate
                                    binding.note.text = "This Assingment was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"
                                }
                                else if (status == "pending" && markStatus == "pending") {
                                    binding.uploadLay.visibility = View.VISIBLE
                                    binding.okCancel.visibility = View.VISIBLE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.GONE
                                    binding.doneAssingment.visibility = View.GONE
                                    binding.doneHw.visibility = View.GONE
                                    binding.doneAssingment.text = "Okay"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.remarks.text = "Kindly upload your Assignment "
                                    binding.status.text = "Not Completed"
                                    binding.resultLay.visibility = View.GONE
                                    binding.points.visibility = View.GONE
                                    UiUtils.textviewCustomDrawable( binding.status,R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#fff2d9", null)
                                    UiUtils.textViewTextColor(binding.status, "#F69300", null)
                                    binding.submitOn.text = "Last Date : "
                                    binding.submitdate.text = dueDate
                                    binding.note.text = "Don't forget! You need to complete and submit before the Last date to earn points, if you miss the deadline, you won't receive any points."
                                    UiUtils.linearLayoutBgTint(binding.noteLay, "#FFF2F2", null)
                                }
                                else if (status == "completed" && markStatus == "pending") {
                                    binding.uploadLay.visibility = View.GONE
                                    binding.okCancel.visibility = View.GONE
                                    binding.noteLay.visibility = View.VISIBLE
                                    binding.attachLay.visibility = View.VISIBLE
                                    binding.doneAssingment.visibility = View.VISIBLE
                                    binding.doneAssingment.text = "Okay"
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.points.visibility = View.GONE
                                    binding.remarkLay.visibility = View.VISIBLE
                                    binding.status.text = "Submitted"
                                    binding.remarks.text = "Points Not yet updated"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                    val submittedOn = BaseUtils.getFormattedDate(result[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                    binding.submitOn.text = "Submitted On"
                                    binding.submitdate.text = ":- ${submittedOn}"
                                    binding.givendate.text = ":- ${dueDate}"
                                    binding.note.text = "Good job! Your Assignment has been submitted successfully, You’ve gained points not updated!"
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
                                binding.root.visibility = View.VISIBLE
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
                            val fileInputStream = contentResolver.openInputStream(uri)
                            val mimeType = contentResolver.getType(uri)
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
                        val fileInputStream = contentResolver.openInputStream(uri)
                        val mimeType = contentResolver.getType(uri)
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
                        assignmentAttach.add(url)
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }
}

