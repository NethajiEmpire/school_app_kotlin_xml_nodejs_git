package com.lms.sch.activity

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
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.AssignmentAdapter
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.SpinnerAdapter
//import com.lms.sch.adapter.AssignmentCompletedStudentsAdapter
import com.lms.sch.databinding.ActivityAssignmentBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.response.GetTeacherAssignmentSingleResponse
import com.lms.sch.response.TeacherAssSingleViewResponse
import com.lms.sch.response.TeacherAssignment2Response
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class AssignmentActivity : BaseActivity() {
    lateinit var binding : ActivityAssignmentBinding
    var assId = ""
    var status = ""
    var remark = ""
    var search = ""
    var marksts = ""
    var result: TeacherAssignment2Response.Result? = null
    var result2 = ArrayList<TeacherAssSingleViewResponse.Result>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        assId = intent.getStringExtra("id")!!
        Log.d("ggfgfd", assId )
        status = ""
        getStudentAssignment()
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getAssignmentSingleView2(this,assId).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            result = it.result!!
                            if(result != null){
                                if(result!!.subject != null && result!!.subject!!.name != null){
                                    binding.subject.text = result!!.subject!!.name
                                } else{
                                    binding.subject.text = "--/--"}
                                if (result!!.title != null){
                                    binding.title.text = "Title : ${result!!.title}"
                                }else{
                                    binding.title.text = "--/--"
                                }
                                if (result!!.description != null){
                                    binding.desc.text = result!!.description
                                }else{
                                    binding.desc.text = "--/--"
                                }
                                if (result!!.createdAt != null){
                                    binding.date.text = "Given On : ${BaseUtils.getFormattedDate(result!!.createdAt!!,
                                        Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)}"
                                }else{
                                    binding.date.text = "--/--"
                                }
//                                if (result!!.dueDate != null){
//                                    binding.assUpdatedDate.text = BaseUtils.getFormattedDate(result!!.dueDate!!,
//                                        Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
//                                }else{
//                                    binding.assUpdatedDate.text = "--/--"
//                                }
                            }
                        }
                        else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        binding.backarrow.setOnClickListener{
            onBackPressed()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search.text.toString()
                getStudentAssignment()
            }
        })
        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                getStudentAssignment()
            }
            false
        })
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
            bind.today.visibility = View.GONE
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
            if (status == "today"){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "pending"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "completed"){
                UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
            }
            else {
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            bind.all.setOnClickListener {
                status = ""
                getStudentAssignment()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                status = "pending"
                getStudentAssignment()
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                status = "completed"
                getStudentAssignment()
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
    private fun getStudentAssignment(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getAssignmentSingleView3(this,assId,status,search,"").observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.assignmentRecycler.visibility = View.VISIBLE
                            result2 = it.result!!
                            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                            val adapter = AssignmentAdapter(this,status,it.result!!,object :OnClickListener{
                                override fun onClickItem(pos: Int) {
                                   assUpdMark(pos)
                                }
                            })
                            binding.assignmentRecycler.layoutManager= layoutManager
                            binding.assignmentRecycler.adapter = adapter
                        }
                        else{
                            binding.noData1.root.visibility = View.VISIBLE
                            binding.assignmentRecycler.visibility = View.GONE
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
                        binding.noData1.root.visibility = View.VISIBLE
                        binding.assignmentRecycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun assUpdMark(pos : Int){
        if (result2[pos].status == "pending" ){
            UiUtils.showSnack("the student not submited the Assignment yet ", binding.root, false)
            binding.dialogMarkResult.root.visibility = View.GONE
            binding.dialogMarkUpdate.root.visibility = View.GONE
        }
        else if (result2[pos].markStatus == "pending" && result2[pos].status == "completed" ){
            binding.dialogMarkResult.root.visibility = View.GONE
            binding.dialogMarkUpdate.root.visibility = View.VISIBLE
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
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val clickedValue: String = parent.getItemAtPosition(position) as String
                    /*if(position != -1){

                    }
                    else{

                    }*/
                    when(clickedValue){
                        "Very Good" -> remark = "verygood"
                        "Good" -> remark = "good"
                        "Poor" -> remark = "poor"
                        "Need Attention" -> remark = "need_attention"
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            var student = ""
            var assignment = ""
            if (result2[pos].student != null){
                student = result2[pos].student!!._id!!
                binding.dialogMarkUpdate.stdName.text = "${result2[pos].student!!.firstName} ${result2[pos].student!!.lastName}"
            }
            if (result2[pos].assignment != null){
                assignment = result2[pos].assignment!!._id!!
                binding.dialogMarkUpdate.stdName.text = "${result2[pos].student!!.firstName} ${result2[pos].student!!.lastName}"
                binding.dialogMarkUpdate.tMark.text = result2[pos].assignment!!.totalMarks
            }
            if (result2[pos].attachment!!.isNotEmpty()){
                binding.dialogMarkUpdate.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkUpdate.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, result2[pos].attachment!!)
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
                    val marks = binding.dialogMarkUpdate.mark.text.toString()
                    DialogUtils.showLoader(this)
                    ApiConnection.getInstance().assignmentMarkUpdate(this,assignment,student,remark,marks).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success ->
                                if (success){
                                    UiUtils.showSnack(it.msg, binding.root, true)
                                    getStudentAssignment()
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
            binding.dialogMarkResult.root.visibility = View.GONE
        }
        else if(result2[pos].markStatus == "completed" && result2[pos].status == "completed"){
            binding.dialogMarkResult.root.visibility = View.VISIBLE
            binding.dialogMarkResult.markLay.visibility = View.VISIBLE
            binding.dialogMarkResult.makeasdone.setOnClickListener {
                onBackPressed()
            }
            binding.dialogMarkResult.close2.setOnClickListener {
                onBackPressed()
            }
            binding.dialogMarkResult.txt1.text = "Completed HomeWork"
            binding.dialogMarkResult.urMarktxt.text = "Student Mark"
            binding.dialogMarkResult.stdName.text = "${result2[pos].student!!.firstName} ${result2[pos].student!!.lastName}"
            binding.dialogMarkResult.stdMarks.text = "Roll No : --/--"
            binding.dialogMarkResult.remarks.text = result2[pos].remarks
            binding.dialogMarkResult.marks.text = "${result2[pos].scored_marks}"
            if (result2[pos].attachment!!.isNotEmpty()){
                binding.dialogMarkResult.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkResult.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, result2[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                binding.dialogMarkResult.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkResult.attachRecycler.adapter = adapter
            }
            else {
                binding.dialogMarkResult.attachRecycler.visibility = View.GONE
                binding.dialogMarkResult.txct.visibility = View.GONE
            }
            binding.dialogMarkResult.remarks.text = result2[pos].remarks
        }
        else {
            binding.dialogMarkResult.root.visibility = View.VISIBLE
            binding.dialogMarkUpdate.root.visibility = View.GONE
            binding.dialogMarkResult.txt1.text = "Assingment Result"
            if (result2[pos].student != null){
                binding.dialogMarkResult.stdName.text = "${result2[pos].student!!.firstName} ${result2[pos].student!!.lastName}"
            }
            else{
                binding.dialogMarkResult.stdName.text = "--/--"
            }
            if (result2[pos].attachment!!.isNotEmpty()){
                binding.dialogMarkResult.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkResult.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, result2[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                binding.dialogMarkResult.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkResult.attachRecycler.adapter = adapter
            }
            else {
                binding.dialogMarkResult.attachRecycler.visibility = View.GONE
                binding.dialogMarkResult.txct.visibility = View.GONE
            }
            if (result2[pos].markStatus == "pending"){
                binding.dialogMarkResult.remarks.text = "Not yet updated"
                UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#333333",null) //orange
            }
            else {
//                val marks = result2[pos].scored_marks ?: "--/--"
                val tMarks = "/${result2[pos].assignment!!.totalMarks}"
//                binding.dialogMarkResult.marks.text = marks+tMarks
                when(result2[pos].remarks){
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
                        UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#F69300",null) //orange
                    }
                    "need_attention" -> {
                        binding.dialogMarkResult.remarks.text = "Work harder; success is within reach."
                        UiUtils.textViewTextColor(binding.dialogMarkResult.remarks,"#F69300",null) //orange
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
}

