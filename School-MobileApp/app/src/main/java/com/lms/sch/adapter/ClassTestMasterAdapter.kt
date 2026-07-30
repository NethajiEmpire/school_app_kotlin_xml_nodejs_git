package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.TeacherClassTestActivity
import com.lms.sch.databinding.CardExamBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetTeacherClassTestResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ClassTestMasterAdapter (
    val mActivity: BaseActivity,
    val list : ArrayList<GetTeacherClassTestResponse.Result.Rows>
): RecyclerView.Adapter<ClassTestMasterAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardExamBinding = CardExamBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_exam, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.yourMarks.visibility = View.GONE

        if(list[position].subject != null && list[position].subject!!.name != null){
            holder.binding.sub.text = list[position].subject!!.name
        } else{
            holder.binding.sub.text = "--/--"
        }
        if (list[position].title != null){
            holder.binding.title.text = list[position].title
        }else{
            holder.binding.title.text = "--/--"
        }

        if (list[position].status != null){
            holder.binding.testStatus.text = list[position].status
        }
        else{
            holder.binding.testStatus.text = "--/--"
        }
        if (list[position].totalMarks != null){
            holder.binding.totalMarks.text = list[position].totalMarks.toString()
        }else{
            holder.binding.totalMarks.text = "--/--"
        }
        if (list[position].scheduledOn != null){
                holder.binding.scheduledOn.text = BaseUtils.getFormattedDate(list[position].scheduledOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }else{
            holder.binding.scheduledOn.text = "--/--"
        }
        holder.binding.root.setOnClickListener {
            if (list[position].status == "completed") {
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY,list[position]._id)
                bundle.putString("status",list[position].status)
                BaseUtils.startActivity(mActivity, TeacherClassTestActivity(), bundle, false)
            }else {
                UiUtils.showSnack("Class Test Not Yet Not completed", holder.binding.root,false)
            }
        }
        when(list[position].status) {
            "today" -> {
                holder.binding.testStatus.text = "Today"
                holder.binding.yourMarks.visibility = View.GONE
                UiUtils.textviewCustomDrawable(holder.binding.testStatus, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding.testStatus,"#FFF2DE",null)
                UiUtils.textViewTextColor(holder.binding.testStatus,"#F69300", null)
            }
            "upcoming" -> {
                holder.binding.yourMarks.visibility = View.GONE
                holder.binding.testStatus.text = "Upcoming"
                UiUtils.textviewCustomDrawable(holder.binding.testStatus, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding.testStatus,"#EDF3FF",null)
                UiUtils.textViewTextColor(holder.binding.testStatus,"#3F8BFB", null)
            }
            "pending" -> {
                holder.binding.yourMarks.visibility = View.GONE
                holder.binding.testStatus.text = "Pending"
                UiUtils.textviewCustomDrawable(holder.binding.testStatus, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding.testStatus,"#FFF3E8",null)
                UiUtils.textViewTextColor(holder.binding.testStatus,"#F39519", null)
            }
            "completed" -> {
                holder.binding.yourMarks.visibility = View.GONE
                holder.binding.testStatus.text = "Completed"
                UiUtils.textviewCustomDrawable(holder.binding.testStatus, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding.testStatus,"#E6FFE2",null)
                UiUtils.textViewTextColor(holder.binding.testStatus,"#348F23", null)
            }
        }
    }
}