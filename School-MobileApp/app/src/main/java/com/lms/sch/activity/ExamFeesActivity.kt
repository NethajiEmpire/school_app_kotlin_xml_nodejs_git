package com.lms.sch.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.ExamSubjectAdapter
import com.lms.sch.databinding.ActivityExamFeesBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetExamSingleViewResponse
import com.lms.sch.response.GetExamSubjectResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class ExamFeesActivity : BaseActivity() {
    lateinit var binding : ActivityExamFeesBinding
    var result: GetExamSingleViewResponse.Result? = null
    var result1 = ArrayList<GetExamSubjectResponse.Result.Row>()
    var examId = ""
    var search = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityExamFeesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        examId = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search.text.toString()
                examSubject()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                examSubject()
            }
            false
        })
        examSubject()
        binding.students.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,examId)
            BaseUtils.startActivity(this, ExamStudentListActivity(),bundle,false)
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
    }

    fun examSubject(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getExamList(this,search,examId,"").observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            result1 = it.result!!.rows!!
                            binding.noData.root.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                            val adapter = ExamSubjectAdapter(this,it.result!!.rows!!)
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                            binding.recycler.layoutManager = layoutManager
                            binding.recycler.adapter = adapter
                        }
                        else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                    else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
}