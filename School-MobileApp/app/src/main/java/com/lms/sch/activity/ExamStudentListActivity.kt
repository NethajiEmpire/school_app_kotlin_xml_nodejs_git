package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.ExamStudentListAdapter
import com.lms.sch.databinding.ActivityExamStudentListBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetExamSingleViewResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class ExamStudentListActivity : BaseActivity() {
    lateinit var binding : ActivityExamStudentListBinding
    var examId = ""
    var result: GetExamSingleViewResponse.Result? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExamStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        examId = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        binding.backarrow.setOnClickListener {
            finish()
        }
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
                                binding.std.text = result!!.standard!!.name
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

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentExamination(this,examId).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.studentRecycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            val adapter = ExamStudentListAdapter(this,it.result!!.rows!!)
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
}