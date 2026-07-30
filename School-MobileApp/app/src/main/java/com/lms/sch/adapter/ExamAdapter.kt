package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.ExamActivity
import com.lms.sch.activity.ProjectActivity
import com.lms.sch.databinding.CardExam2Binding
import com.lms.sch.databinding.CardExamBinding
import com.lms.sch.databinding.CardExaminationBinding
import com.lms.sch.databinding.CardProjectworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.ProjectModelClass
import com.lms.sch.response.GetExamResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ExamAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<GetExamResponse.Row>,
    var onClickListener: OnClickListener
) : RecyclerView.Adapter<ExamAdapter.ViewHolder>() {
    inner class ViewHolder(view : View ): RecyclerView.ViewHolder(view){
        var  binding : CardExaminationBinding = CardExaminationBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_examination, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].examType != null) {
            holder.binding.typeOfExam.text = list[position].examType!!.name
        } else {
            holder.binding.typeOfExam.text = "--/--"
        }
        holder.binding.subjectCount.text = "${list[position].subjectCount} Subjects"
        holder.binding.startDate.text = BaseUtils.getFormattedDate(list[position].startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        holder.binding.endDate.text = BaseUtils.getFormattedDate(list[position].endDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT )
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
        when (list[position].completeStatus) {
            "completed" -> {
                holder.binding.studentStatus.text = "Completed"
                UiUtils.textviewCustomDrawable( holder.binding.studentStatus, R.drawable.border_curve_16dp )
                UiUtils.linearLayoutBgDrawable( holder.binding.parentlay, R.drawable.border_curve_15_light_green)
                UiUtils.textViewBgTint(holder.binding.studentStatus, "#E5F8ED", null)
                UiUtils.textViewTextColor(holder.binding.studentStatus, "#28C76F", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.linearlay, R.drawable.border_curve_15dp_orangeborder)
                UiUtils.linearLayoutBgTint(holder.binding.linearlay, "#E5F8ED", null)
            }
            "upcomming" -> {
                holder.binding.studentStatus.text = "Upcoming"
                UiUtils.textviewCustomDrawable( holder.binding.studentStatus, R.drawable.border_curve_16dp)
                UiUtils.linearLayoutBgDrawable( holder.binding.parentlay, R.drawable.border_curve_15_light_blue)
                UiUtils.textViewBgTint(holder.binding.studentStatus, "#DFEDFD", null)
                UiUtils.textViewTextColor(holder.binding.studentStatus, "#61A4F3", null)
                UiUtils.linearLayoutBgDrawable( holder.binding.linearlay, R.drawable.border_curve_15dp_orangeborder)
                UiUtils.linearLayoutBgTint(holder.binding.linearlay, "#DFEEFF", null)
            }
            "ongoing" -> {
                holder.binding.studentStatus.text = "Ongoing"
                UiUtils.textviewCustomDrawable(holder.binding.studentStatus, R.drawable.border_curve_16dp)
                UiUtils.linearLayoutBgDrawable( holder.binding.parentlay, R.drawable.border_curve_15dp_orange)
                UiUtils.textViewBgTint(holder.binding.studentStatus, "#FFEAD6", null)
                UiUtils.textViewTextColor(holder.binding.studentStatus, "#F39519", null)
                UiUtils.linearLayoutBgDrawable( holder.binding.linearlay, R.drawable.border_curve_15dp_orangeborder )
                UiUtils.linearLayoutBgTint(holder.binding.linearlay, "#FFEAD6", null)
            }
            else -> {
                UiUtils.textviewCustomDrawable(holder.binding.studentStatus, R.drawable.border_curve_16dp)
                UiUtils.linearLayoutBgDrawable(holder.binding.parentlay, R.drawable.border_curve_15dp_orange )
                UiUtils.textViewBgTint(holder.binding.studentStatus, "#FFEAD6", null)
                UiUtils.textViewTextColor(holder.binding.studentStatus, "#F39519", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.linearlay, R.drawable.border_curve_15dp_orangeborder)
                UiUtils.linearLayoutBgTint(holder.binding.linearlay, "#FFEAD6", null)
            }
        }

    }
}