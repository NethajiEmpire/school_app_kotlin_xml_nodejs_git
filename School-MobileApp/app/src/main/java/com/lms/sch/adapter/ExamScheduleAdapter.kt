package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardExamScheduleBinding
import com.lms.sch.databinding.CardExaminationScheduleBinding
import com.lms.sch.response.GetExamSubjectResponse
import com.lms.sch.response.StudentExamResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ExamScheduleAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<GetExamSubjectResponse.Result.Row>
) : RecyclerView.Adapter<ExamScheduleAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding : CardExaminationScheduleBinding = CardExaminationScheduleBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_examination_schedule,parent,false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].subject != null ){
            holder.binding.subject.text = list[position].subject!!.name
        }
        else{
            holder.binding.subject.text = "--/--"
        }
        if (list[position].date != null){
            holder.binding.date.text = BaseUtils.getFormattedDate(list[position].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            holder.binding.date.text = "--/--"
        }
        if (list[position].majorExam != null && list[position].majorExam!!.examType != null){
            holder.binding.examName.text = list[position].majorExam!!.examType!!.name
        }
        else{
            holder.binding.examName.text = "--/--"
        }
        if (list[position].date != null){
            val date = BaseUtils.getFormattedDate(list[position].date!!,Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            val duration = BaseUtils.convertSeconds(list[position].duration!!)
            holder.binding.date.text = "$date | ${list[position].fromTime} ${list[position].session} - ${list[position].toTime} ${list[position].session} | $duration"
        }
        else{
            holder.binding.date.text = "--/--"
        }
        if (list[position].day != null){
            holder.binding.day.text = list[position].day
        }
        else{
            holder.binding.day.text = "--/--"
        }
        when(list[position].session){
            "FN" ->{
                UiUtils.textViewTextColor(holder.binding.fn, null,R.color.colorPrimaryVariant)
                UiUtils.textViewTextColor(holder.binding.an, "#E9EDF4",null)
            }
            "AN" -> {
                UiUtils.textViewTextColor(holder.binding.fn, "#E9EDF4",null)
                UiUtils.textViewTextColor(holder.binding.an, null,R.color.colorPrimaryVariant)
            }
        }
        when (list[position].completeStatus) {
            "completed" -> {
                holder.binding.status.text = "Completed"
                UiUtils.textviewCustomDrawable( holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#deffdf", null)
                UiUtils.textViewTextColor(holder.binding.status, "#32B138", null)
            }
            "upcomming" -> {
                holder.binding.status.text = "Upcoming"
                UiUtils.textviewCustomDrawable( holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#DAE7FF", null)
                UiUtils.textViewTextColor(holder.binding.status, "#3F8BFB", null)
            }
            "ongoing" -> {
                holder.binding.status.text = "Ongoing"
                UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#FFEAD6", null)
                UiUtils.textViewTextColor(holder.binding.status, "#F39519", null)
            }
            else -> {
                holder.binding.status.text = list[position].completeStatus
                UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#FFEAD6", null)
                UiUtils.textViewTextColor(holder.binding.status, "#F39519", null)
            }
        }
    }
}
