package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardExaminationScheduleBinding
import com.lms.sch.databinding.CardStudentExamresultBinding
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ResultAdapter(
    val mActivity: BaseActivity,
    val list : ArrayList<StudentExamResultResponse.Rows>
    ): RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding : CardStudentExamresultBinding = CardStudentExamresultBinding.bind(view)
    }
    override fun onCreateViewHolder( parent: ViewGroup,  viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_student_examresult,parent,false)
        )
    }
    override fun onBindViewHolder( holder: ViewHolder, position: Int) {
        if (list[position].subject!! != null && list[position].subject!!.name!! != null){
            holder.binding.subject.text = list[position].subject!!.name!!
        }else{
            holder.binding.subject.text = "--/--"
        }
        if (list[position].majorExam != null && list[position].majorExam!!.examType != null &&list[position].majorExam!!.examType!!.name != null){
            holder.binding.examName.text = list[position].majorExam!!.examType!!.name
        }else{
            holder.binding.examName.text = "--/--"
        }
        if (list[position].totalMark != null && list[position].scoredMark != null){
            holder.binding.marks.text = "${list[position].scoredMark} / ${list[position].totalMark}"
        }else{
            holder.binding.marks.text = "--/--"
        }
        if (list[position].examSubject != null && list[position].examSubject!!.date != null){
            holder.binding.date.text = BaseUtils.getFormattedDate(list[position].examSubject!!.date!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }else{
            holder.binding.date.text = "--/--"
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}