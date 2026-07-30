package com.lms.sch.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.ClassTestAdapter
import com.lms.sch.adapter.StudentExaminationAdapter
import com.lms.sch.databinding.ActivityAdminExamBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.ClassTestResponse
import com.lms.sch.response.GetExamResponse
import com.lms.sch.response.StudentClassTestResponse
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class AdminExamActivity : BaseActivity() {
    lateinit var binding: ActivityAdminExamBinding
    var examstatus = ""
    var result = ArrayList<GetExamResponse.Row>()
    var result1 = ArrayList<ClassTestResponse.Result>()
    var clsTestRes = ArrayList<StudentClassTestResponse.Result>()
    var status = ""
    var search = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminExamBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        getStudentExamination()
        binding.search3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search3.text.toString()
//                if (testMenu == "classTest"){
//                    getClassTest()
//                } else{
//                    getExam()
//                }
            }
        })
        binding.tabclassTestToday.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabclassTestToday,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.todayClassTestId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.ClassTestupcomingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.classTestcompletedId,null, R.color.black_varient6)
            getClassTest()
        }
        binding.tabClassTestUpcoming.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestUpcoming,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabclassTestToday,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.ClassTestupcomingId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.todayClassTestId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.classTestcompletedId,null, R.color.black_varient6)
            getClassTest()
        }
        binding.tabClassTestCompleted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestCompleted,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabclassTestToday,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.classTestcompletedId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.todayClassTestId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.ClassTestupcomingId,null, R.color.black_varient6)
            getClassTest()
        }
        binding.classExam.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classExam, null,R.color.white)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.black_varient3)
            binding.pageExam.visibility = View.VISIBLE
            binding.recycler.visibility = View.VISIBLE
            binding.classExam.visibility = View.VISIBLE
            binding.classTestMenu.visibility = View.GONE
            binding.examMenu.visibility = View.VISIBLE
            examstatus = "ongoing"
            getStudentExamination()
        }
        binding.classTest.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_6dp )
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_4dp )
            UiUtils.textViewBgTint(binding.classTest, null,R.color.white)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.black_varient3)
            binding.pageExam.visibility = View.VISIBLE
            binding.recycler.visibility = View.VISIBLE
            binding.classExam.visibility = View.VISIBLE
            binding.classTestMenu.visibility = View.GONE
            binding.examMenu.visibility = View.VISIBLE
            examstatus = "ongoing"
            getStudentExamination()
        }
        binding.tabExamOngoing.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExamOngoing,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabExamCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.completedId,null, R.color.black_varient6)
            examstatus = "ongoing"
            getStudentExamination()
        }
        binding.tabExamUpcoming.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExamUpcoming,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamOngoing,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabExamCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.completedId,null, R.color.black_varient6)
            examstatus = "notcompleted"
            getStudentExamination()
        }
        binding.tabExamCompleted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExamCompleted,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamOngoing,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabExamUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.completedId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.black_varient6)
            examstatus = "completed"
            getStudentExamination()
        }
    }
    private fun getClassTest(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getClassTest(this,search, status).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noData3.root.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                            result1 = it.result!!
                            val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                            val adapter = ClassTestAdapter(this,it.result!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
//                                    getClsTestDialog(pos)
                                }
                            })
                            binding.recycler.layoutManager = layoutManager
                            binding.recycler.adapter = adapter
                        }else{
                            UiUtils.showSnack(it.msg, binding.root,false)
                            binding.noData3.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData3.root.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getStudentExamination(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getExam(this,search, examstatus).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.noData3.root.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                            result = it.result!!.rows!!
                            val layoutManager = LinearLayoutManager(this,
                                RecyclerView.VERTICAL,false)
                            val adapter = StudentExaminationAdapter(this,false,result!!,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    /*getExam(pos)*/
                                }
                            })
                            binding.recycler.layoutManager = layoutManager
                            binding.recycler.adapter = adapter
                        }  else{
                            binding.noData3.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData3.root.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                    }
                }
            }
        }
    }
    /*fun getClsTestDialog(pos: Int){
        if(result1[pos].subject != null && result1[pos].subject!!.name != null){
            binding.examDialog.subName.text = result1[pos].subject!!.name
        }
        else {
            binding.examDialog.subName.text = "--/--"
        }
        if (result1[pos].classTest != null && result1[pos].classTest!!.title != null){
            binding.examDialog.testTitle.text = result1[pos].classTest!!.title
        }
        else{
            binding.examDialog.testTitle.text = "--/--"
        }
        if (result1[pos].classTest != null && result1[pos].classTest!!.description != null){
            binding.examDialog.description.text = " * ${result1[pos].classTest!!.description}"
        }
        else{
            binding.examDialog.description.text = "--/--"
        }
        if (result1[pos].testStatus != null ){
            binding.examDialog.examStatus.text = result1[pos].testStatus
        }
        else{
            binding.examDialog.examStatus.text = "--/--"
        }
        if (result1[pos].totalMarks != null){
            binding.examDialog.totalMarks.text = result1[pos].totalMarks
        }
        else{
            binding.examDialog.totalMarks.text = "--/--"
        }
        if (result1[pos].student!!.firstName != null  || result1[pos].student!!.lastName != null){

            binding.examDialog.studentName.text = "${result1[pos].student!!.firstName} ${result1[pos].student!!.lastName}"
        }
        else{
            binding.examDialog.studentName.text = "--/--"
        }
        if (result1[pos].chapter != null && result1[pos].chapter!!.chapterNumber != null && result1[pos].chapter!!.name !=null){
            binding.examDialog.chapter.text = "Chapter ${result1[pos].chapter!!.chapterNumber}: ${result1[pos].chapter!!.name}"
        }
        else{
            binding.examDialog.chapter.text = "--/--"
        }

        if (result1[pos].testStatus == "completed"){
            if (result1[pos].scoredMarks != null){
                binding.examDialog.yourMarks.visibility = View.VISIBLE
                binding.examDialog.scoredMarks.text = result1[pos].scoredMarks
            }
            else{
                binding.examDialog.scoredMarks.text = "--/--"
            }}
        else{
            binding.examDialog.yourMarks.visibility = View.GONE
        }
        if (result1[pos].scheduledOn != null){
            binding.examDialog.date.text = BaseUtils.getFormattedDate(result1[pos].scheduledOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            binding.examDialog.date.text = "--/--"
        }
        if (result1[pos].remarks != null){
            binding.examDialog.teacherRemarkContent.text = result1[pos].remarks
        }
        else{
            binding.examDialog.teacherRemarkContent.text = "--/--"
        }
        if (result1[pos].testStatus == "completed"){
            binding.examDialog.examStatus.text = "Completed"
            binding.examDialog.yourMarks.visibility = View.VISIBLE
            binding.examDialog.next.text = "Okay"
            binding.examDialog.answerSheet.visibility = View.VISIBLE
            binding.examDialog.tRemarks.visibility = View.VISIBLE
            binding.examDialog.credits.text = "+${result1[pos].credits} Points"
            UiUtils.textViewTextColor(binding.examDialog.credits, "#348F23",null)
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_16dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus, "#E6FFE2",null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#348F23",null)
        }
        else if(result1[pos].testStatus == "ongoing"){
            binding.examDialog.examStatus.text = "On Going"
            binding.examDialog.yourMarks.visibility = View.GONE
            binding.examDialog.next.text = "Okay , i’ll Prepare for it"
            binding.examDialog.answerSheet.visibility = View.GONE
            binding.examDialog.tRemarks.visibility = View.GONE
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_16dp)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#61A4F3", null)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#DFEDFD", null)
        }
        else if(result1[pos].testStatus == "upcoming"){
            binding.examDialog.examStatus.text = "Upcoming"
            binding.examDialog.yourMarks.visibility = View.GONE
            binding.examDialog.next.text = "Okay"
            binding.examDialog.answerSheet.visibility = View.GONE
            binding.examDialog.tRemarks.visibility = View.GONE
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_16dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#FFF8EB", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#F39519", null)
        }
        binding.examDialog.root.visibility = View.VISIBLE

    }*/
}