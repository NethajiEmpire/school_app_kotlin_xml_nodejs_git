package com.lms.sch.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.ExamScheduleAdapter
import com.lms.sch.adapter.ExamStudentListAdapter
import com.lms.sch.adapter.ResultAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.StudentList2Adapter
import com.lms.sch.adapter.StudentListAdapter
import com.lms.sch.databinding.ActivityExamListBinding
import com.lms.sch.databinding.FilterAssignmentBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetExamSubjectResponse
import com.lms.sch.response.StudentExamRes
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class ExamListActivity : BaseActivity() {
    lateinit var binding: ActivityExamListBinding
    var result = ArrayList<GetExamSubjectResponse.Result.Row>()
    var result1 = ArrayList<StudentExamResultResponse.Rows>()
    var examAttach = ""
    var examResId = ""
    var completeStatus = ""
    var count = 0
    var key = ""
    var search = ""
    var status = ""
    var subId = ""
    var noDue = ""
    var isClick = false
    val  practicalMarks = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExamListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        var id = sharedHelper.childId

        completeStatus = intent.getStringExtra("cmstatus") ?: ""
        status = intent.getStringExtra("status") ?: ""
        key = intent.getStringExtra("key") ?: ""
        subId = intent.getStringExtra("subjectId") ?: ""
        Log.d("subjectId", subId)
        Log.d("completeStatus", completeStatus)
        Log.d("status", status)
        Log.d("key", key)
        status =  ""
        if (completeStatus == "completed") {
            isClick = false
            status =  ""
            binding.filter.visibility = View.GONE
            binding.filter1.visibility = View.VISIBLE
            /*binding.filter1.setOnClickListener {
                val inflater = LayoutInflater.from(this)
                val bind: FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
                val popupView: View = bind.root
                bind.completed.text = "Update Mark"
                bind.today.text = "Pass"
                bind.pending.text = "Fail"
                val widthInDp = 120
                val density = resources.displayMetrics.density
                val widthInPx = (widthInDp * density).toInt()
                val popupWindow =
                    PopupWindow(popupView, widthInPx, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                popupWindow.isOutsideTouchable = true
                popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                popupWindow.elevation = 8f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(8f)
                }
                if (status == "") {
                    UiUtils.textviewImgDrawable(bind.all, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.today, null, "start")
                    UiUtils.textviewImgDrawable(bind.pending, null, "start")
                    UiUtils.textviewImgDrawable(bind.completed, null, "start")
                } else if (status == "pass") {
                    UiUtils.textviewImgDrawable(bind.today, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.all, null, "start")
                    UiUtils.textviewImgDrawable(bind.pending, null, "start")
                    UiUtils.textviewImgDrawable(bind.completed, null, "start")
                } else if (status == "fail") {
                    UiUtils.textviewImgDrawable(bind.pending, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.all, null, "start")
                    UiUtils.textviewImgDrawable(bind.today, null, "start")
                    UiUtils.textviewImgDrawable(bind.completed, null, "start")
                } else {
                    UiUtils.textviewImgDrawable(bind.completed, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.pending, null, "start")
                    UiUtils.textviewImgDrawable(bind.today, null, "start")
                    UiUtils.textviewImgDrawable(bind.all, null, "start")
                }
                bind.all.setOnClickListener {
                    status = ""
                    getExamResult1()
                    popupWindow.dismiss()
                }
                bind.today.setOnClickListener {
                    status = "pass"
                    getExamResult1()
                    popupWindow.dismiss()
                }
                bind.pending.setOnClickListener {
                    status = "fail"
                    getExamResult1()
                    popupWindow.dismiss()
                }
                bind.completed.setOnClickListener {
                    status = "pending"
                    getExamResult1()
                    popupWindow.dismiss()
                }

                val anchorView = binding.filter1
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
            }*/
//            getExamResult()
        }
        else if (completeStatus == "pending" && status == "active") {
        }
        else if (completeStatus == "ongoing" || completeStatus == "upcomming") {
            isClick = true
            binding.filter.visibility = View.VISIBLE
            binding.filter1.visibility = View.GONE
            getsubjects()
            binding.filter.setOnClickListener {
                val inflater = LayoutInflater.from(this)
                val bind: FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
                val popupView: View = bind.root
                bind.completed.visibility = View.GONE
                bind.today.text = "Approved"
                val widthInDp = 120
                val density = resources.displayMetrics.density
                val widthInPx = (widthInDp * density).toInt()
                val popupWindow =
                    PopupWindow(popupView, widthInPx, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                popupWindow.isOutsideTouchable = true
                popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                popupWindow.elevation = 8f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(8f)
                }
                if (noDue == "") {
                    UiUtils.textviewImgDrawable(bind.all, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.today, null, "start")
                    UiUtils.textviewImgDrawable(bind.pending, null, "start")
                    UiUtils.textviewImgDrawable(bind.completed, null, "start")
                } else if (noDue == "pending") {
                    UiUtils.textviewImgDrawable(bind.today, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.all, null, "start")
                    UiUtils.textviewImgDrawable(bind.pending, null, "start")
                    UiUtils.textviewImgDrawable(bind.completed, null, "start")
                } else if (noDue == "uploaded") {
                    UiUtils.textviewImgDrawable(bind.pending, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.all, null, "start")
                    UiUtils.textviewImgDrawable(bind.today, null, "start")
                    UiUtils.textviewImgDrawable(bind.completed, null, "start")
                } else {
                    UiUtils.textviewImgDrawable(bind.completed, R.drawable.hugeicons_tick, "start")
                    UiUtils.textviewImgDrawable(bind.all, null, "start")
                    UiUtils.textviewImgDrawable(bind.today, null, "start")
                    UiUtils.textviewImgDrawable(bind.pending, null, "start")
                }
                bind.all.setOnClickListener {
                    noDue = ""
                    studentStatus()
                    popupWindow.dismiss()
                }
                bind.today.setOnClickListener {
                    noDue = "approved"
                    studentStatus()
                    popupWindow.dismiss()
                }
                bind.pending.setOnClickListener {
                    noDue = "pending"
                    studentStatus()
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
        }
        else {
            UiUtils.showSnack("Not Done ", binding.root, false)
        }

        binding.search3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search3.text.toString()
                getsubjects()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        binding.search3.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search3.text.toString()
                getsubjects()
            }
            false
        })
        binding.tabclassTestToday.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabclassTestToday,  R.drawable.border_line_curve_24dp_primary  )
            UiUtils.textViewTextColor(binding.todayClassTestId, "#232B68", null)
            UiUtils.linearLayoutBgDrawable( binding.tabClassTestUpcoming, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.ClassTestupcomingId, "#666666", null)
            UiUtils.linearLayoutBgDrawable( binding.tabClassTestCompleted, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.classTestcompletedId, "#666666", null)
            UiUtils.textviewCustomDrawable(binding.count1, R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.count2, R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.count3, R.drawable.ic_round_line_3)
        }
        binding.tabClassTestUpcoming.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(
                binding.tabClassTestUpcoming,
                R.drawable.border_line_curve_24dp_primary
            )
            UiUtils.textViewTextColor(binding.ClassTestupcomingId, "#232B68", null)
            UiUtils.linearLayoutBgDrawable(
                binding.tabclassTestToday,
                R.drawable.border_line_curve_24dp_grey
            )
            UiUtils.textViewTextColor(binding.todayClassTestId, "#666666", null)
            UiUtils.linearLayoutBgDrawable(
                binding.tabClassTestCompleted,
                R.drawable.border_line_curve_24dp_grey
            )
            UiUtils.textViewTextColor(binding.classTestcompletedId, "#666666", null)
            UiUtils.textviewCustomDrawable(binding.count1, R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.count2, R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.count3, R.drawable.ic_round_line_3)
        }
        binding.tabClassTestCompleted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(
                binding.tabClassTestCompleted,
                R.drawable.border_line_curve_24dp_primary
            )
            UiUtils.textViewTextColor(binding.classTestcompletedId, "#232B68", null)
            UiUtils.linearLayoutBgDrawable(
                binding.tabClassTestUpcoming,
                R.drawable.border_line_curve_24dp_grey
            )
            UiUtils.textViewTextColor(binding.ClassTestupcomingId, "#666666", null)
            UiUtils.linearLayoutBgDrawable(
                binding.tabclassTestToday,
                R.drawable.border_line_curve_24dp_grey
            )
            UiUtils.textViewTextColor(binding.todayClassTestId, "#666666", null)
            UiUtils.textviewCustomDrawable(binding.count1, R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.count2, R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.count3, R.drawable.ic_round_line2)
        }
    }
    fun getsubjects() {
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getExamList(this, search, key, "").observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            result = it.result!!.rows!!
                            binding.noData.root.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                            val adapter = ExamScheduleAdapter(this, it.result!!.rows!!)
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                            binding.recycler.layoutManager = layoutManager
                            binding.recycler.adapter = adapter
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    } else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun studentStatus() {
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getExamStdList(this, search, key, noDue).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.noData.root.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                            val adapter = StudentListAdapter(this, it.result!!.rows!!)
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                            binding.recycler.layoutManager = layoutManager
                            binding.recycler.adapter = adapter
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    } else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun getExamResult() {
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().examResultall(this, completeStatus, key, search, subId)
            .observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                                result1 = it.result!!.rows!!
                                binding.noData.root.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE
                                val adapter = StudentList2Adapter( this, it.result!!.rows!!,object : OnClickListener {
                                        override fun onClickItem(pos: Int) {
                                            examResId = it.result!!.rows!![pos]._id!!
                                            updMarkResult(pos)
                                        }
                                    })
                                val layoutManager =
                                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                binding.recycler.layoutManager = layoutManager
                                binding.recycler.adapter = adapter
                            } else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.recycler.visibility = View.GONE
                            }
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
    }
//    fun getExamResult1() {
//        if (search.isEmpty()) {
//            DialogUtils.showLoader(this)
//        }
//        ApiConnection.getInstance().examResultall1(this, status, key, search, subId)
//            .observe(this) {
//                it.let {
//                    DialogUtils.dismissLoader()
//                    it.success.let { success ->
//                        if (success) {
//                            if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
////                                result1 = it.result!!.rows!!
//                                binding.noData.root.visibility = View.GONE
//                                binding.recycler.visibility = View.VISIBLE
//                                val adapter = StudentList2Adapter( this, it.result!!.rows!!,object : OnClickListener {
//                                    override fun onClickItem(pos: Int) {
////                                        examResId = it.result!!.rows!![pos]._id!!
////                                        updMarkResult(pos)
//                                    }
//                                })
//                                val layoutManager =
//                                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//                                binding.recycler.layoutManager = layoutManager
//                                binding.recycler.adapter = adapter
//                            } else {
////                                UiUtils.showSnack(it.msg, binding.root, false)
//                                binding.noData.root.visibility = View.VISIBLE
//                            }
//                        } else {
//                            binding.noData.root.visibility = View.VISIBLE
////                            UiUtils.showSnack(it.msg, binding.root, false)
//
//                        }
//                    }
//                }
//            }
//    }
    fun updMarkResult(pos : Int) {
        if (result1[pos].status == "pending") {
            binding.dialogMarkResult.root.visibility = View.GONE
            binding.dialogMarkUpdate.root.visibility = View.VISIBLE
            binding.dialogMarkUpdate.uploadLay.visibility = View.VISIBLE
            binding.dialogMarkUpdate.uploadLay.setOnClickListener {
                openDocList()
            }
            var student = ""
//            var classTest = ""
            if (result1[pos].student != null) {
                student = result1[pos].student!!._id!!
                binding.dialogMarkUpdate.stdName.text = "${result1[pos].student!!.firstName} ${result1[pos].student!!.lastName}"
            }
            if (result1[pos].student != null) {
//                classTest = result1[pos].classTest!!._id!!
                binding.dialogMarkUpdate.stdName.text =  "${result1[pos].student!!.firstName} ${result1[pos].student!!.lastName}"
                binding.dialogMarkUpdate.tMark.text = result1[pos].totalMark.toString()!!
            }
            binding.dialogMarkUpdate.remarks.visibility = View.GONE
            binding.dialogMarkUpdate.spinner.visibility = View.GONE


            val answerSheet = result1[pos].answerSheet

            var answerSheetUrl = ""
            if (answerSheet != null && !answerSheet.url.isNullOrEmpty()) {
                answerSheetUrl = answerSheet.url!!
            }else{
                UiUtils.showSnack("Update the marks Sheet", binding.root, false)
            }

            binding.dialogMarkUpdate.cancel.setOnClickListener {
                binding.dialogMarkUpdate.root.visibility = View.GONE
            }
            binding.dialogMarkUpdate.close.setOnClickListener {
                binding.dialogMarkUpdate.root.visibility = View.GONE
            }
            binding.dialogMarkUpdate.done.setOnClickListener {
                if (examAttach.isNotEmpty()) {
                    val marks = binding.dialogMarkUpdate.mark.text.toString()

                    if (marks.isEmpty()) {
                        UiUtils.showSnack("Update the marks", binding.root, false)
                    } else {
                        DialogUtils.showLoader(this)
                        ApiConnection.getInstance().ExamMarkUpd(this, "present", marks,practicalMarks,examAttach,examResId).observe(this) {
                            it.let {
                                DialogUtils.dismissLoader()
                                it.success.let { success ->
                                    if (success) {
                                        UiUtils.showSnack(it.msg, binding.root, true)
                                        getExamResult()
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
            binding.dialogMarkResult.txt1.text = "Exam Result"
            binding.dialogMarkResult.urMarktxt.text = "Student Mark"

            if (result1[pos].student != null) {
                binding.dialogMarkResult.stdName.text =  "${result1[pos].student!!.firstName} ${result1[pos].student!!.lastName}"
            } else {
                binding.dialogMarkResult.stdName.text = "--/--"
            }
            val attachmentUrl = result1[pos].answerSheet?.url
            if (!attachmentUrl.isNullOrEmpty()) {
                binding.dialogMarkResult.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkResult.txct.visibility = View.VISIBLE
                binding.dialogMarkResult.txct.text = "Student Attachement"
                val attachmentList = arrayListOf(attachmentUrl)
                val adapter = AttachAdapter(this, attachmentList)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.dialogMarkResult.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkResult.attachRecycler.adapter = adapter
            } else {
                binding.dialogMarkResult.txct.text = "Student Attachement Pending"
                binding.dialogMarkResult.attachRecycler.visibility = View.GONE
                binding.dialogMarkResult.txct.visibility = View.VISIBLE
            }
            if (result1[pos].scoredMark != null) {
                binding.dialogMarkResult.marks.text = "${result1[pos].scoredMark}/${result1[pos].totalMark!!}"
            } else {
                binding.dialogMarkResult.marks.text = "--/--"
            }
            binding.dialogMarkResult.remark.visibility = View.GONE
            binding.dialogMarkResult.remarks.visibility = View.GONE
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
    fun upload(filepart: MultipartBody.Part) {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().uploadFile(this, filepart).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root, true)
                        val url = it.result[0].location!!
                        examAttach = url
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
            if (data != null) {
                if (data.clipData != null) {
                    var docs = data.clipData
                    for (items in 0 until docs!!.itemCount) {
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
                                val fileBody =
                                    RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                                filePart = MultipartBody.Part.createFormData(
                                    "file",
                                    documentFile?.name,
                                    fileBody
                                )
                                val size = BaseUtils.convertBytes(fileSize.toLong())
                                Log.d("dv1", "" + documentFile?.name)
                                Log.d("dv0", "" + uri.path)
                                Log.d("dv3", "" + size)
                                Log.d("dv4", "" + mimeType)
                                if (fileSizeInMB <= 5) {
                                    val json = JSONObject()
                                    json.put("name", documentFile?.name)
                                    json.put("size", size)
                                    json.put("type", mimeType)
//                                    urlName.add(json)
                                    upload(filePart)
                                } else {
                                    UiUtils.showSnack(
                                        "File size exceeds 50 MB",
                                        binding.root,
                                        false
                                    )
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
                            filePart = MultipartBody.Part.createFormData(
                                "file",
                                documentFile?.name,
                                fileBody
                            )
                            val size = BaseUtils.convertBytes(fileSize.toLong())
                            Log.d("dv1", "" + documentFile?.name)
                            Log.d("dv0", "" + uri.path)
                            Log.d("dv3", "" + size)
                            Log.d("dv4", "" + mimeType)
                            if (fileSizeInMB <= 5) {
                                val json = JSONObject()
                                json.put("name", documentFile?.name)
                                json.put("size", size)
                                json.put("type", mimeType)
                                binding.dialogMarkUpdate.attach.text = documentFile?.name
                                upload(filePart)
                            } else {
                                UiUtils.showSnack("File size exceeds 5 MB", binding.root, false)
                            }
                        }
                    }
                }
            }
        }
    }
}





