package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardExamBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.ClassTestResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ClassTestAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<ClassTestResponse.Result>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<ClassTestAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardExamBinding = CardExamBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_exam, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].subject != null && list[position].subject!!.name != null){
            holder.binding.sub.text = list[position].subject!!.name
        }
        else{
            holder.binding.sub.text = "--/--"
        }
        if (list[position].classTest != null && list[position].classTest!!.title != null){
            holder.binding.title.text = list[position].classTest!!.title
        }
        else{
            holder.binding.title.text = "--/--"
        }
        if (list[position].status != null){
            holder.binding.testStatus.text = list[position].status
        }
        else{
            holder.binding.testStatus.text = "--/--"
        }
        if (list[position].classTest != null && list[position].classTest!!.totalMarks != null){
            holder.binding.totalMarks.text = list[position].classTest!!.totalMarks!!.toString()
        }
        else{
            holder.binding.totalMarks.text = "--/--"
        }
        if (list[position].scheduledOn != null){
            holder.binding.scheduledOn.text = BaseUtils.getFormattedDate(list[position].scheduledOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            holder.binding.scheduledOn.text = "--/--"
        }
        holder.binding!!.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
        when(list[position].status){
            "completed" ->{
                holder.binding.testStatus.text = "Completed"
                holder.binding.yourMarks.visibility = View.VISIBLE
                if (list[position].scored_marks != null ){
                    holder.binding.scoredMarks.text = list[position].scored_marks.toString()
                }
                else{
                    holder.binding.scoredMarks.text = "--/--"
                }
                UiUtils.textviewCustomDrawable(holder.binding.testStatus, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding.testStatus,"#EFFFF0",null)
                UiUtils.textViewTextColor(holder.binding.testStatus,"#348F23", null)
            }
            "today" ->{
                holder.binding.testStatus.text = "Today"
                holder.binding.yourMarks.visibility = View.GONE
                UiUtils.textviewCustomDrawable(holder.binding.testStatus, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding.testStatus,"#DFEDFD",null)
                UiUtils.textViewTextColor(holder.binding.testStatus,"#61A4F3", null)
            }
            "pending" ->{
                holder.binding.testStatus.text = "Pending"
                holder.binding.yourMarks.visibility = View.GONE
                UiUtils.textviewCustomDrawable(holder.binding.testStatus, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding.testStatus,"#FFF2DE", null)
                UiUtils.textViewTextColor(holder.binding.testStatus,"#F39519", null)
            }
        }
    }
}
