package com.lms.sch.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.StdClsTestResAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.databinding.ActivityTeacherClassTestBinding
import com.lms.sch.databinding.FilterClasstestMarkUpdateBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetTeacherClsTestSingleViewResponse
import com.lms.sch.response.GetTeacherStdClsTestResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class TeacherClassTestActivity : BaseActivity() {
    lateinit var binding: ActivityTeacherClassTestBinding
    private var singleIdResult = GetTeacherClsTestSingleViewResponse.Result()
    private var clsTestResult = ArrayList<GetTeacherStdClsTestResponse.Result>()
    var classTestId = ""
    var clsTestRes = ""
    var status = ""
    var remark = ""
    var mark = ""
    var markStatus = ""
    var count = 0
    var assignmentAttach = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTeacherClassTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        classTestId = intent.getStringExtra(Constants.IntentKeys.KEY)!!

        getStudentClsTstDetails()

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getClsTestSingleView(this, classTestId).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()

                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            singleIdResult = it.result!!
                            UiUtils.textviewCustomDrawable( binding.status,  R.drawable.border_curve_24dp )
                            UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                            UiUtils.textViewTextColor(binding.status, "#32B138", null)
                            if (singleIdResult!!.subject != null && singleIdResult!!.subject!!.name != null) {
                                binding.txtTitle.text = singleIdResult!!.title
                            } else {
                                binding.txtTitle.text = "--/--"
                            }
                            binding.desc.text = singleIdResult.description
                            if (singleIdResult!!.subject != null && singleIdResult!!.subject!!.name != null) {
                                binding.txtTopic.text = singleIdResult!!.subject!!.name
                            } else {
                                binding.txtTopic.text = "--/--"
                            }
                            if (singleIdResult!!.status != null) {
                                binding.status.text = markStatus
                            } else {
                                binding.status.text = "--/--"
                            }
                            if (singleIdResult!!.totalMarks != null) {
                                binding.marks.text = singleIdResult!!.totalMarks.toString()
                            } else {
                                binding.marks.text = "--/--"
                            }
                            if (singleIdResult!!.scheduledOn != null) {
                                binding.date.text = BaseUtils.getFormattedDate( singleIdResult!!.scheduledOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT )
                            } else {
                                binding.date.text = "--/--"
                            }
                            when (singleIdResult!!.status) {
                                /*"today" -> {
                                    binding.status.text = "Today"
                                    UiUtils.textviewCustomDrawable(  binding.status,  R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#FFF2DE", null)
                                    UiUtils.textViewTextColor(binding.status, "#F69300", null)
                                }

                                "upcoming" -> {
                                    binding.status.text = "Upcoming"
                                    UiUtils.textviewCustomDrawable(  binding.status,  R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#EDF3FF", null)
                                    UiUtils.textViewTextColor(binding.status, "#3F8BFB", null)
                                }*/

                                "completed" -> {
                                    binding.status.text = "Completed"
                                    UiUtils.textviewCustomDrawable( binding.status,  R.drawable.border_curve_24dp )
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
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

        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterClasstestMarkUpdateBinding = FilterClasstestMarkUpdateBinding.inflate(inflater)
            val popupView : View = bind.root

            val widthInDp = 120
            val density = resources.displayMetrics.density
            val widthInPx = (widthInDp * density).toInt()

            val popupWindow = PopupWindow(popupView,widthInPx,ViewGroup.LayoutParams.WRAP_CONTENT,true)
            popupWindow.isOutsideTouchable = true
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow.elevation = 8f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(8f)
            }
            
            if (markStatus == "pending"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (markStatus == "completed"){
                UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
            }
            else {
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }


            bind.all.setOnClickListener {
                markStatus = ""
                getStudentClsTstDetails()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                markStatus = "pending"
                getStudentClsTstDetails()
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                markStatus = "completed"
                getStudentClsTstDetails()
                popupWindow.dismiss()
            }
            val anchorView = binding.filter
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)

            val endGapDp = 8
            val topGapDp = 8
            val endGapPx = (endGapDp * density).toInt()
            val topGapPx = (topGapDp * density).toInt()
            val xPos = location[0] + anchorView.width - widthInPx - endGapPx
            val yPos = location[1] + anchorView.height + topGapPx

            popupWindow.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                xPos,
                yPos
            )
        }

        var isClicked = false
        binding.down.setOnClickListener {
            isClicked = !isClicked
            if (isClicked){
                binding.allDetails.visibility = View.VISIBLE
                binding.down.rotation = 180f
            }
            else {
                binding.allDetails.visibility = View.GONE
                binding.down.rotation = 0f
            }
        }
    }

    fun getStudentClsTstDetails() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentClsTestRes(this,classTestId,clsTestRes,markStatus).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            clsTestResult = it.result!!
                            val adapter = StdClsTestResAdapter(this, false, it.result!!, object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        updMarkResult(pos)
                                    }
                                })
                            val layoutManager =LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                            binding.completedrecycle.layoutManager = layoutManager
                            binding.completedrecycle.adapter = adapter
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.completedrecycle.visibility = View.GONE
                        }
                    } else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.completedrecycle.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }

        }
    }

    fun updMarkResult(pos : Int) {
        if (clsTestResult[pos].markStatus == "pending") {
            binding.dialogMarkResult.root.visibility = View.GONE
            binding.dialogMarkUpdate.root.visibility = View.VISIBLE
            binding.dialogMarkUpdate.uploadLay.visibility = View.VISIBLE
            binding.dialogMarkUpdate.uploadLay.setOnClickListener {
                openDocList()
            }
            var spin = ArrayList<String>()
            spin.add("Select..")
            spin.add("Very Good")
            spin.add("Good")
            spin.add("Need Attention")
            spin.add("Poor")
            val adapter = SpinnerAdapter(this, spin)
            binding.dialogMarkUpdate.spinner.adapter = adapter
            binding.dialogMarkUpdate.spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val clickedValue: String = parent.getItemAtPosition(position) as String
                        when (clickedValue) {
                            "Very Good" -> remark = "verygood"
                            "Good" -> remark = "good"
                            "Poor" -> remark = "poor"
                            "Need Attention" -> remark = "need_attention"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            var student = ""
            var classTest = ""
            if (clsTestResult[pos].student != null) {
                student = clsTestResult[pos].student!!._id!!
                binding.dialogMarkUpdate.stdName.text = "${clsTestResult[pos].student!!.firstName} ${clsTestResult[pos].student!!.lastName}"
            }
            if (clsTestResult[pos].student != null) {
                classTest = clsTestResult[pos].classTest!!._id!!
                binding.dialogMarkUpdate.stdName.text =
                    "${clsTestResult[pos].student!!.firstName} ${clsTestResult[pos].student!!.lastName}"
                binding.dialogMarkUpdate.tMark.text =
                    clsTestResult[pos].classTest!!.totalMarks.toString()!!
            }

            if (clsTestResult[pos].attachment!!.isNotEmpty()) {
                binding.dialogMarkUpdate.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkUpdate.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, clsTestResult[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.dialogMarkUpdate.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkUpdate.attachRecycler.adapter = adapter
            } else {
                binding.dialogMarkUpdate.attachRecycler.visibility = View.GONE
                binding.dialogMarkUpdate.txct.visibility = View.GONE
            }
            binding.dialogMarkUpdate.cancel.setOnClickListener {
                binding.dialogMarkUpdate.root.visibility = View.GONE
            }
            binding.dialogMarkUpdate.close.setOnClickListener {
                binding.dialogMarkUpdate.root.visibility = View.GONE
            }
            binding.dialogMarkUpdate.done.setOnClickListener {
                if (student.isNotEmpty()) {
                    val marks = binding.dialogMarkUpdate.mark.text.toString()
                    if (marks.isEmpty()) {
                        UiUtils.showSnack("Update the marks", binding.root, false)
                    } else if (remark.isEmpty()) {
                        UiUtils.showSnack("Select the remarks", binding.root, false)
                    } else {
                        DialogUtils.showLoader(this)
                        ApiConnection.getInstance().clsTestMarkUpd(this, classTest,assignmentAttach, student, marks, remark).observe(this) {
                            it.let {
                                DialogUtils.dismissLoader()
                                it.success.let { success ->
                                    if (success) {
                                        UiUtils.showSnack(it.msg, binding.root, true)
                                        getStudentClsTstDetails()
                                        binding.dialogMarkUpdate.root.visibility = View.GONE
                                    } else {
                                        UiUtils.showSnack(it.msg, binding.root, false)
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Student id is not present", binding.root, false)
                }
            }
            UiUtils.animation(this,  binding.dialogMarkUpdate.topLay,
                R.anim.slide_in_from_bottom,
                true
            )
            binding.dialogMarkUpdate.root.visibility = View.VISIBLE
            binding.dialogMarkResult.root.visibility = View.GONE
        }
        else {
            binding.dialogMarkResult.root.visibility = View.VISIBLE
            binding.dialogMarkUpdate.root.visibility = View.GONE
            binding.dialogMarkUpdate.uploadLay.visibility = View.VISIBLE
            binding.dialogMarkResult.txt1.text = "Class Test Result"
            binding.dialogMarkResult.urMarktxt.text = "Student Mark"


            if (clsTestResult[pos].student != null) {
                binding.dialogMarkResult.stdName.text =  "${clsTestResult[pos].student!!.firstName} ${clsTestResult[pos].student!!.lastName}"
            } else {
                binding.dialogMarkResult.stdName.text = "--/--"
            }
            if (clsTestResult[pos].attachment!!.isNotEmpty()) {
                binding.dialogMarkResult.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkResult.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, clsTestResult[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.dialogMarkResult.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkResult.attachRecycler.adapter = adapter
            } else {
                binding.dialogMarkResult.attachRecycler.visibility = View.GONE
                binding.dialogMarkResult.txct.visibility = View.GONE
            }
            if (clsTestResult[pos].scored_marks != null) {
                binding.dialogMarkResult.marks.text =
                    "${clsTestResult[pos].scored_marks}/${clsTestResult[pos].classTest!!.totalMarks}"
            } else {
                binding.dialogMarkResult.marks.text = "--/--"
            }
            if (clsTestResult[pos].markStatus == "pending"){
                binding.dialogMarkResult.remarks.text = "Not yet updated"
                UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#333333",null) //orange
            }
            else {
                when(clsTestResult[pos].remarks){
                    "verygood" -> {
                        binding.dialogMarkResult.remarks.text = "Outstanding performance! You’re doing great."
//                        UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#32B138",null)//green
                        UiUtils.textViewGradient(binding.dialogMarkResult.remarks,"#32B138","#138f18")//green
                    }
                    "good" -> {
                        binding.dialogMarkResult.remarks.text = "Great job! Keep improving steadily."
                        UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#3F8BFB",null) //blue
                    }
                    "poor" -> {
                        binding.dialogMarkResult.remarks.text = "Keep trying; you’ll get there soon."
                        UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#F39519",null) //orange
                    }
                    "need_attention" -> {
                        binding.dialogMarkResult.remarks.text = "Work harder; success is within reach."
                        UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#F39519",null) //orange
                    }
                }
            }

            binding.dialogMarkResult.cancel.setOnClickListener {
                binding.dialogMarkResult.root.visibility = View.GONE
            }
            binding.dialogMarkResult.close2.setOnClickListener {
                binding.dialogMarkResult.root.visibility = View.GONE
            }
            binding.dialogMarkResult.makeasdone.setOnClickListener {
                binding.dialogMarkResult.root.visibility = View.GONE
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
                                binding.dialogMarkUpdate.attach.text = documentFile?.name
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

   /* fun updMarkResult(pos: Int) {
        var spin = ArrayList<String>()
        spin.add("Select..")
        spin.add("Very Good")
        spin.add("Good")
        spin.add("Average")
        spin.add("Need Attention")
        spin.add("Poor")
        val adapter = SpinnerAdapter(this, spin)
        binding.dialogMarkUpdate.spinner.adapter = adapter
        binding.dialogMarkUpdate.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View?,position: Int,id: Long) {
                val clickedValue: String = parent.getItemAtPosition(position) as String
                when(clickedValue){
                    "Very Good" -> remark = "verygood"
                    "Good" -> remark = "good"
                    "Poor" -> remark = "poor"
                    "Need Attention" -> remark = "need_attention"
                    else -> remark = ""
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        var student = ""
        var homework = ""
        if (clsTestResult[pos].student != null){
            student = clsTestResult[pos].student!!._id!!
            binding.dialogMarkUpdate.stdName.text = "${clsTestResult[pos].student!!.firstName} ${clsTestResult[pos].student!!.lastName}"
        }
        if (clsTestResult[pos].classTest != null){
            homework = clsTestResult[pos].classTest!!._id!!
            binding.dialogMarkUpdate.tMark.text = clsTestResult[pos].classTest!!.title
        }
        if (clsTestResult[pos].attachment!!.isNotEmpty()){
            binding.dialogMarkUpdate.attachRecycler.visibility = View.VISIBLE
            binding.dialogMarkUpdate.txct.visibility = View.VISIBLE
            val adapter = AttachAdapter(this, clsTestResult[pos].attachment!!)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            binding.dialogMarkUpdate.attachRecycler.layoutManager = layoutManager
            binding.dialogMarkUpdate.attachRecycler.adapter = adapter
        }
        else {
            binding.dialogMarkUpdate.attachRecycler.visibility = View.GONE
            binding.dialogMarkUpdate.txct.visibility = View.GONE
        }
        binding.dialogMarkUpdate.cancel.setOnClickListener {
            binding.dialogMarkUpdate.root.visibility = View.GONE
        }
        binding.dialogMarkUpdate.close.setOnClickListener {
            binding.dialogMarkUpdate.root.visibility = View.GONE
        }
        binding.dialogMarkUpdate.done.setOnClickListener {
            if (student.isNotEmpty()){
                DialogUtils.showLoader(this)
                ApiConnection.getInstance().clsTestMarkUpd(this,homework,student,remark).observe(this){
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success ->
                            if (success){
                                UiUtils.showSnack(it.msg, binding.root, true)
                                getStudentClsTstDetails(true)
                                binding.dialogMarkUpdate.root.visibility = View.GONE
                            }
                            else {
                                UiUtils.showSnack(it.msg, binding.root, false)
                            }
                        }
                    }
                }
            }
            else {
                UiUtils.showSnack("Student id is not present", binding.root, false)
            }
        }
        UiUtils.animation(this,binding.dialogMarkUpdate.topLay, R.anim.slide_in_from_bottom,true)
        binding.dialogMarkUpdate.root.visibility = View.VISIBLE
    }*/

}