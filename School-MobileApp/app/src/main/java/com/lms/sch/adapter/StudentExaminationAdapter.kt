package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.ExamListActivity
import com.lms.sch.databinding.CardExaminationBinding
import com.lms.sch.databinding.CardExaminationWidth320Binding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetExamResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentExaminationAdapter(
    val mActivity: BaseActivity,
    val isDiary : Boolean,
    val list: ArrayList<GetExamResponse.Row>,
    val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<StudentExaminationAdapter.ViewHolder>() {
        inner class ViewHolder(view : View, type : Int) : RecyclerView.ViewHolder(view){
            var binding1 : CardExaminationBinding? = null
            var binding : CardExaminationWidth320Binding? = null

            init {
                if (type == 0) {
                    binding1 = CardExaminationBinding.bind(view)
                } else {
                    binding = CardExaminationWidth320Binding.bind(view)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (isDiary) {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_examination_width320, parent, false),
                1
            )
        } else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_examination, parent, false),
                0
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isDiary){
            if (list[position].examType != null) {
                holder.binding!!.typeOfExam.text = list[position].examType!!.name
            } else {
                holder.binding!!.typeOfExam.text = "--/--"
            }
            holder.binding!!.subjectCount.text = "${list[position].subjectCount} Subjects"
            holder.binding!!.startDate.text = BaseUtils.getFormattedDate(list[position].startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            holder.binding!!.endDate.text = BaseUtils.getFormattedDate(list[position].endDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT )
            holder.binding!!.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY,list[position]._id)
                BaseUtils.startActivity(mActivity, ExamListActivity(),bundle,false)
            }
            when (list[position].completeStatus) {
                "completed" -> {
                    holder.binding!!.studentStatus.text = "Completed"
                    UiUtils.textviewCustomDrawable( holder.binding!!.studentStatus, R.drawable.border_curve_16dp )
                    UiUtils.linearLayoutBgDrawable( holder.binding!!.parentlay, R.drawable.border_curve_15_light_green)
                    UiUtils.textViewBgTint(holder.binding!!.studentStatus, "#E5F8ED", null)
                    UiUtils.textViewTextColor(holder.binding!!.studentStatus, "#28C76F", null)
                    UiUtils.linearLayoutBgDrawable(holder.binding!!.linearlay,R.drawable.border_curve_15dp_orangeborder)
                    UiUtils.linearLayoutBgTint(holder.binding!!.linearlay, "#E5F8ED", null)
                }
                "upcomming" -> {
                    holder.binding!!.studentStatus.text = "Upcoming"
                    UiUtils.textviewCustomDrawable( holder.binding!!.studentStatus,R.drawable.border_curve_16dp)
                    UiUtils.linearLayoutBgDrawable( holder.binding!!.parentlay,R.drawable.border_curve_15_light_blue)
                    UiUtils.textViewBgTint(holder.binding!!.studentStatus, "#DFEDFD", null)
                    UiUtils.textViewTextColor(holder.binding!!.studentStatus, "#61A4F3", null)
                    UiUtils.linearLayoutBgDrawable( holder.binding!!.linearlay,R.drawable.border_curve_15dp_orangeborder)
                    UiUtils.linearLayoutBgTint(holder.binding!!.linearlay, "#DFEEFF", null)
                }
                "ongoing" -> {
                    holder.binding!!.studentStatus.text = "Ongoing"
                    UiUtils.textviewCustomDrawable(holder.binding!!.studentStatus,R.drawable.border_curve_16dp)
                    UiUtils.linearLayoutBgDrawable( holder.binding!!.parentlay,R.drawable.border_curve_15dp_orange)
                    UiUtils.textViewBgTint(holder.binding!!.studentStatus, "#FFEAD6", null)
                    UiUtils.textViewTextColor(holder.binding!!.studentStatus, "#F39519", null)
                    UiUtils.linearLayoutBgDrawable( holder.binding!!.linearlay, R.drawable.border_curve_15dp_orangeborder )
                    UiUtils.linearLayoutBgTint(holder.binding!!.linearlay, "#FFEAD6", null)
                }
                else -> {
                    UiUtils.textviewCustomDrawable(holder.binding!!.studentStatus,R.drawable.border_curve_16dp)
                    UiUtils.linearLayoutBgDrawable(holder.binding!!.parentlay, R.drawable.border_curve_15dp_orange )
                    UiUtils.textViewBgTint(holder.binding!!.studentStatus, "#FFEAD6", null)
                    UiUtils.textViewTextColor(holder.binding!!.studentStatus, "#F39519", null)
                    UiUtils.linearLayoutBgDrawable(holder.binding!!.linearlay,R.drawable.border_curve_15dp_orangeborder)
                    UiUtils.linearLayoutBgTint(holder.binding!!.linearlay, "#FFEAD6", null)
                }
            }
        }
        else {
            if (list[position].examType != null) {
                holder.binding1!!.typeOfExam.text = list[position].examType!!.name
            } else {
                holder.binding1!!.typeOfExam.text = "--/--"
            }
            holder.binding1!!.subjectCount.text = "${list[position].subjectCount} Subjects"
            holder.binding1!!.startDate.text = BaseUtils.getFormattedDate(list[position].startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            holder.binding1!!.endDate.text = BaseUtils.getFormattedDate(list[position].endDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT )
            holder.binding1!!.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("cmstatus",list[position].completeStatus)
                bundle.putString("status",list[position].status)
                bundle.putString("key",list[position]._id)
                BaseUtils.startActivity(mActivity, ExamListActivity(),bundle,false)
            }
            when (list[position].completeStatus) {
                "completed" -> {
                    holder.binding1!!.studentStatus.text = "Completed"
                    UiUtils.textviewCustomDrawable( holder.binding1!!.studentStatus, R.drawable.border_curve_16dp )
                    UiUtils.linearLayoutBgDrawable( holder.binding1!!.parentlay, R.drawable.border_curve_15_light_green)
                    UiUtils.textViewBgTint(holder.binding1!!.studentStatus, "#E5F8ED", null)
                    UiUtils.textViewTextColor(holder.binding1!!.studentStatus, "#28C76F", null)
                    UiUtils.linearLayoutBgDrawable(holder.binding1!!.linearlay,R.drawable.border_curve_15dp_orangeborder)
                    UiUtils.linearLayoutBgTint(holder.binding1!!.linearlay, "#E5F8ED", null)
                }
                "upcomming" -> {
                    holder.binding1!!.studentStatus.text = "Upcoming"
                    UiUtils.textviewCustomDrawable( holder.binding1!!.studentStatus,R.drawable.border_curve_16dp)
                    UiUtils.linearLayoutBgDrawable( holder.binding1!!.parentlay,R.drawable.border_curve_15_light_blue)
                    UiUtils.textViewBgTint(holder.binding1!!.studentStatus, "#DFEDFD", null)
                    UiUtils.textViewTextColor(holder.binding1!!.studentStatus, "#61A4F3", null)
                    UiUtils.linearLayoutBgDrawable( holder.binding1!!.linearlay,R.drawable.border_curve_15dp_orangeborder)
                    UiUtils.linearLayoutBgTint(holder.binding1!!.linearlay, "#DFEEFF", null)
                }
                "ongoing" -> {
                    holder.binding1!!.studentStatus.text = "Ongoing"
                    UiUtils.textviewCustomDrawable(holder.binding1!!.studentStatus,R.drawable.border_curve_16dp)
                    UiUtils.linearLayoutBgDrawable( holder.binding1!!.parentlay,R.drawable.border_curve_15dp_orange)
                    UiUtils.textViewBgTint(holder.binding1!!.studentStatus, "#FFEAD6", null)
                    UiUtils.textViewTextColor(holder.binding1!!.studentStatus, "#F39519", null)
                    UiUtils.linearLayoutBgDrawable( holder.binding1!!.linearlay, R.drawable.border_curve_15dp_orangeborder )
                    UiUtils.linearLayoutBgTint(holder.binding1!!.linearlay, "#FFEAD6", null)
                }
                else -> {
                    UiUtils.textviewCustomDrawable(holder.binding1!!.studentStatus,R.drawable.border_curve_16dp)
                    UiUtils.linearLayoutBgDrawable(holder.binding1!!.parentlay, R.drawable.border_curve_15dp_orange )
                    UiUtils.textViewBgTint(holder.binding1!!.studentStatus, "#FFEAD6", null)
                    UiUtils.textViewTextColor(holder.binding1!!.studentStatus, "#F39519", null)
                    UiUtils.linearLayoutBgDrawable(holder.binding1!!.linearlay,R.drawable.border_curve_15dp_orangeborder)
                    UiUtils.linearLayoutBgTint(holder.binding1!!.linearlay, "#FFEAD6", null)
                }
            }
        }

    }
}