package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.ExamResultActivity
import com.lms.sch.databinding.CardAllSessionStudentBinding
import com.lms.sch.databinding.CardExamSubjectListBinding
import com.lms.sch.response.GetExamSubjectResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ExamSubjectAdapter (
    val mActivity: BaseActivity,
    val list: ArrayList<GetExamSubjectResponse.Result.Row>
) : RecyclerView.Adapter<ExamSubjectAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding : CardExamSubjectListBinding = CardExamSubjectListBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_exam_subject_list,parent,false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txt.text = ""+(position+1)
        val colors = ArrayList<String>()
        colors.add("#007BFF")
        colors.add("#FFC107")
        colors.add("#6F42C1")
        colors.add("#116530")
        colors.add("#000c66")
        colors.add("#fa26a0")
        val i = position % colors.size
        UiUtils.linearLayoutBgTint(holder.binding.num, colors[i],null)
        UiUtils.setTextViewDrawableColor(holder.binding.mark,colors[i],null)
        UiUtils.setTextViewDrawableColor(holder.binding.date,colors[i],null)
        if (list[position].subject != null ){
            holder.binding.title.text = list[position].subject!!.name
        }
        else{
            holder.binding.title.text = "--/--"
        }
        if (list[position].incharge != null ){
            holder.binding.code.text = list[position].incharge!!.firstName + " " + list[position].incharge!!.lastName + "(Invigilator)"
        }
        else{
            holder.binding.code.text = "--/--"
        }
        holder.binding.type.text = BaseUtils.convertSeconds(list[position].duration!!)
        holder.binding.day.text = list[position].day!!.toUpperCase()
        holder.binding.mark.text = "${list[position].totalMark} Marks (${list[position].passMark!!} Pass Marks)"
        val date = BaseUtils.getFormattedDate(list[position].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        holder.binding.date.text = "$date - ${BaseUtils.formatTime(list[position].fromTime!!)} - ${BaseUtils.formatTime(list[position].toTime!!)}"
        when(list[position].session){
            "FN" -> {
                UiUtils.textViewTextColor(holder.binding.fn,null,R.color.colorPrimaryVariant)
                UiUtils.textViewTextColor(holder.binding.an,"#E8E8E8",null)
            }
            "AN" -> {
                UiUtils.textViewTextColor(holder.binding.fn,"#E8E8E8",null)
                UiUtils.textViewTextColor(holder.binding.an,null,R.color.colorPrimaryVariant)
            }
            else -> {
                UiUtils.textViewTextColor(holder.binding.fn,"#E8E8E8",null)
                UiUtils.textViewTextColor(holder.binding.an,"#E8E8E8",null)
            }
        }
        if (list.size -1 == position){
            holder.binding.dashView.visibility = View.GONE
        }
        else {
            holder.binding.dashView.visibility = View.VISIBLE
        }

        holder.binding.root.setOnClickListener {
            if (list[position].subject != null && list[position].majorExam != null){
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY,list[position]._id)
                bundle.putString(Constants.IntentKeys.KEY1,list[position].majorExam!!._id)
                BaseUtils.startActivity(mActivity, ExamResultActivity(),bundle,false)
            }
            else {
                UiUtils.showSnack("No subject Id Found",holder.binding.root,false)
            }
        }
    }
}
