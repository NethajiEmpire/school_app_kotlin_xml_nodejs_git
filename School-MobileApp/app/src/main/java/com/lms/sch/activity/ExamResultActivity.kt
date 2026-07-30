package com.lms.sch.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.ExamResultAdapter
import com.lms.sch.databinding.ActivityExamResultBinding
import com.lms.sch.databinding.FilterExamResultStudentBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetExamResponse
import com.lms.sch.response.GetExamSingleViewResponse
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class ExamResultActivity : BaseActivity() {
    lateinit var binding : ActivityExamResultBinding
    var subjectId = ""
    var examId = ""
    var ansSheet = ""
    var noDue = ""
    var examStatus = ""
    var search = ""
    var mStatus = ""
    var status = ""
    var attendance = ""
    lateinit var  studentResult : ArrayList<GetExamResponse.Row>
    var result: GetExamSingleViewResponse.Result? = null
    lateinit var examResult : ArrayList<StudentExamResultResponse.Rows>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExamResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subjectId = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        examId = intent.getStringExtra(Constants.IntentKeys.KEY1)!!
        binding.backarrow.setOnClickListener {
            finish()
        }
        binding.search3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search3.text.toString()
                getExam()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search3.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search3.text.toString()
                getExam()
            }
            false
        })
        getExam()
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getExamSingle(this, examId).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            result = it.result!!
                            if (result!!.examType != null && result!!.examType!!.name != null) {
                                binding.midTerm.text = result!!.examType!!.name
                                binding.topHeader.text = result!!.examType!!.name
                            } else {
                                binding.midTerm.text = "--/--"
                                binding.topHeader.text = "Exam"
                            }
                            if (result!!.standard != null && result!!.standard!!.name != null) {
                                binding.std.text = UiUtils.getOrdinalSuffix(result!!.standard!!.name!!.toInt())
                            } else {
                                binding.std.text = "--/--"
                            }
                            if (result!!.batch != null && result!!.batch!!.name != null) {
                                binding.batch.text = result!!.batch!!.name
                            } else {
                                binding.batch.text = "--/--"
                            }
                            if (result!!.startDate != null) {
                                binding.startDate.text = BaseUtils.getFormattedDate(result!!.startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                            } else {
                                binding.startDate.text = "--/--"
                            }
                            if (result!!.endDate != null) {
                                binding.endDate.text = BaseUtils.getFormattedDate(result!!.endDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                            } else {
                                binding.endDate.text = "--/--"
                            }
                            status = result!!.completeStatus!!
                            when (result!!.completeStatus) {
                                "ongoing" -> {
                                    binding.status.text = "Ongoing"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#FFF2DE", null)
                                    UiUtils.textViewTextColor(binding.status, "#F69300", null)
                                }

                                "upcomming" -> {
                                    binding.status.text = "Upcoming"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#EDF3FF", null)
                                    UiUtils.textViewTextColor(binding.status, "#3F8BFB", null)
                                }

                                "completed" -> {
                                    binding.status.text = "Completed"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
                                    UiUtils.textViewTextColor(binding.status, "#32B138", null)
                                }
                            }
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        var isClicked = false
        binding.down.setOnClickListener {
            isClicked = !isClicked
            if (isClicked) {
                binding.allDetails.visibility = View.VISIBLE
                binding.down.rotation = 180f
            } else {
                binding.allDetails.visibility = View.GONE
                binding.down.rotation = 0f
            }
        }
        binding.filter3.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterExamResultStudentBinding = FilterExamResultStudentBinding.inflate(inflater)
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
            if (mStatus == "pending"){
                attendance = ""
                UiUtils.textviewImgDrawable(bind.mPending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pass,null,"start")
                UiUtils.textviewImgDrawable(bind.fail,null,"start")
                UiUtils.textviewImgDrawable(bind.absent,null,"start")
                UiUtils.textviewImgDrawable(bind.present,null,"start")
            }
            else if (mStatus == "pass"){
                attendance = ""
                UiUtils.textviewImgDrawable(bind.pass,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.mPending,null,"start")
                UiUtils.textviewImgDrawable(bind.fail,null,"start")
                UiUtils.textviewImgDrawable(bind.absent,null,"start")
                UiUtils.textviewImgDrawable(bind.present,null,"start")
            }
            else if (mStatus == "fail"){
                attendance = ""
                UiUtils.textviewImgDrawable(bind.fail,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pass,null,"start")
                UiUtils.textviewImgDrawable(bind.mPending,null,"start")
                UiUtils.textviewImgDrawable(bind.absent,null,"start")
                UiUtils.textviewImgDrawable(bind.present,null,"start")
            }
            else if (attendance == "absent"){
                mStatus = ""
                UiUtils.textviewImgDrawable(bind.absent,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pass,null,"start")
                UiUtils.textviewImgDrawable(bind.mPending,null,"start")
                UiUtils.textviewImgDrawable(bind.fail,null,"start")
                UiUtils.textviewImgDrawable(bind.present,null,"start")
            }
            else if (attendance == "present"){
                mStatus = ""
                UiUtils.textviewImgDrawable(bind.present,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pass,null,"start")
                UiUtils.textviewImgDrawable(bind.mPending,null,"start")
                UiUtils.textviewImgDrawable(bind.absent,null,"start")
                UiUtils.textviewImgDrawable(bind.fail,null,"start")
            }
            else {
                mStatus = ""
                attendance = ""
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pass,null,"start")
                UiUtils.textviewImgDrawable(bind.fail,null,"start")
                UiUtils.textviewImgDrawable(bind.mPending,null,"start")
                UiUtils.textviewImgDrawable(bind.absent,null,"start")
                UiUtils.textviewImgDrawable(bind.present,null,"start")
            }

            bind.all.setOnClickListener {
                mStatus = ""
                attendance = ""
                getExam()
                popupWindow.dismiss()
            }
            bind.mPending.setOnClickListener {
                mStatus = "pending"
                attendance = ""
                getExam()
                popupWindow.dismiss()
            }
            bind.fail.setOnClickListener {
                mStatus = "fail"
                attendance = ""
                getExam()
                popupWindow.dismiss()
            }
            bind.pass.setOnClickListener {
                mStatus = "pass"
                attendance = ""
                getExam()
                popupWindow.dismiss()
            }
            bind.absent.setOnClickListener {
                attendance = "absent"
                mStatus = ""
                getExam()
                popupWindow.dismiss()
            }
            bind.pass.setOnClickListener {
                attendance = "present"
                mStatus = ""
                getExam()
                popupWindow.dismiss()
            }
            val anchorView = binding.filter3
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

    }

    fun getExam(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().examResult(this,search,subjectId,noDue,mStatus,attendance).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            examResult = it.result!!.rows!!
                            binding.studentRecycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            val adapter = ExamResultAdapter(this,it.result!!.rows!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    getExamResult(pos)
                                }
                            })
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                            binding.studentRecycler.layoutManager = layoutManager
                            binding.studentRecycler.adapter = adapter
                        }
                        else {
                            binding.studentRecycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
                        }
                    }
                    else {
                        binding.studentRecycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    fun getExamResult(pos : Int){
        if (SharedHelper(this).role == "TEACHER" && status =="completed"){
            if (examResult[pos].status == "pending"){
                binding.dialogMarkResult.resultLay.visibility = View.GONE
            }
            else {
                binding.dialogMarkResult.resultLay.visibility = View.VISIBLE
            }
            binding.dialogMarkResult.root.visibility = View.VISIBLE
            
            binding.dialogMarkResult.cancel.setOnClickListener {
                binding.dialogMarkResult.root.visibility = View.GONE
            }
            binding.dialogMarkResult.close.setOnClickListener {
                binding.dialogMarkResult.root.visibility = View.GONE
            }
            var student = ""
            var exam = ""
            if (examResult[pos].student != null && (examResult[pos].student!!.firstName != null || examResult[pos].student!!.lastName != null)){
                student = examResult[pos]._id!!
                binding.dialogMarkResult.stdName.text = "${examResult[pos].student!!.firstName} ${examResult[pos].student!!.lastName} "
            }else{
                binding.dialogMarkResult.stdName.text = "--/--"
            }
            if (examResult[pos].majorExam != null && examResult[pos].majorExam!!._id != null && examResult[pos].majorExam!!.examType!!.name != null){
                exam = examResult[pos].majorExam!!._id!!
                binding.dialogMarkResult.txt1.text = examResult[pos].majorExam!!.examType!!.name
            }else{
                binding.dialogMarkResult.txt1.text = "--/--"
            }
            if (examResult[pos].totalScore != null && examResult[pos].totalScore!!.isNotEmpty()){
                binding.dialogMarkResult.sMarks.text = examResult[pos].totalScore
            }else{
                binding.dialogMarkResult.sMarks.text = "0"
            }
            if (examResult[pos].examSubject != null && examResult[pos].examSubject!!.practicalMark!!.isNotEmpty()){
                binding.dialogMarkResult.tPracMark.text = examResult[pos].examSubject!!.practicalMark!!
            }else{
                binding.dialogMarkResult.tPracMark.text = "0"
            }
            if (examResult[pos].totalMark != null && examResult[pos].examSubject != null){
                val tm = examResult[pos].totalMark!!.toInt() - examResult[pos].examSubject!!.practicalMark!!.toInt()
                binding.dialogMarkResult.tMark.text = tm.toString()
                binding.dialogMarkResult.tMarks.text = "/ ${examResult[pos].totalMark}"
            }else{
                binding.dialogMarkResult.tMark.text = "0"
                binding.dialogMarkResult.tMarks.text = "0"
            }
            binding.dialogMarkResult.attach.setOnClickListener {
                openDocList()
            }
            binding.dialogMarkResult.done.setOnClickListener {
                if (student.isNotEmpty()){
                    val marks = binding.dialogMarkResult.mark.text.toString()
                    val attendance = "present"
                    val practicalMarks = binding.dialogMarkResult.practicalmark.text.toString()
                    val tPracMarks = binding.dialogMarkResult.tPracMark.text.toString()
                    val tMarks = binding.dialogMarkResult.tMark.text.toString()
                    val tScore = binding.dialogMarkResult.tMark.text.toString().toInt() + binding.dialogMarkResult.tPracMark.text.toString().toInt()
                    if (marks.isEmpty()){
                        UiUtils.showSnack("Please enter marks",binding.root,false)
                    }
                    else if (practicalMarks.isEmpty()){
                        UiUtils.showSnack("Please enter practical marks",binding.root,false)
                    }
                    else if (practicalMarks.toInt() > tPracMarks.toInt()){
                        UiUtils.showSnack("Practical marks cannot be greater than total practical marks",binding.root,false)
                    }
                    else if (marks.toInt() > tMarks.toInt()){
                        UiUtils.showSnack("Marks cannot be greater than total marks",binding.root,false)
                    }
                    else if (marks.toInt() < 0){
                        UiUtils.showSnack("Marks cannot be less than 0",binding.root,false)
                    }
                    else if (practicalMarks.toInt() < 0){
                        UiUtils.showSnack("Practical marks cannot be less than 0",binding.root,false)
                    }
                    else if (tMarks.toInt() < 0){
                        UiUtils.showSnack("Total marks cannot be less than 0",binding.root,false)
                    }
                    else if (marks.toInt() > tScore){
                        UiUtils.showSnack("Marks cannot be greater than total score",binding.root,false)
                    }

                    else if (ansSheet.isEmpty()){
                        UiUtils.showSnack("Please upload answer sheet",binding.root,false)
                    }
                    else{
                        DialogUtils.showLoader(this)
                        ApiConnection.getInstance().examMarkUpd(this,student,attendance,practicalMarks,marks,ansSheet).observe(this){
                            it.let {
                                DialogUtils.dismissLoader()
                                it.success.let { success ->
                                    if (success){
                                        UiUtils.showSnack(it.msg, binding.root, true)
                                        ansSheet = ""
                                        binding.dialogMarkResult.root.visibility = View.GONE
                                        getExam()
                                        binding.dialogMarkResult.attach.text = ""
                                        binding.dialogMarkResult.mark.setText("")
                                        binding.dialogMarkResult.practicalmark.setText("")
                                        binding.dialogMarkResult.tMark.setText("")
                                    }
                                    else {
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
        }
        else{
            binding.dialogMarkResult.root.visibility = View.GONE
            binding.noDuelay.visibility = View.VISIBLE
            getExam()
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
                                binding.dialogMarkResult.attach.text = documentFile?.name
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
                        ansSheet = url
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }
    
}