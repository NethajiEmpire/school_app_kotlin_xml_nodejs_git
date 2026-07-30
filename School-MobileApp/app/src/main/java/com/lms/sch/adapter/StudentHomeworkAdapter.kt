package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardHomeworkW360Binding
import com.lms.sch.databinding.CardTaskBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentHomeworkAdapter(
    val mActivity : BaseActivity,
    val isDiary : Boolean,
    private val list: ArrayList<GetHomeworkResponse.Result>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<StudentHomeworkAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, type: Int) : RecyclerView.ViewHolder(view) {

        var binding: CardTaskBinding? = null
        var binding1: CardHomeworkW360Binding? = null

        init {
            if (type == 0) {
                binding = CardTaskBinding.bind(view)
            } else {
                binding1 = CardHomeworkW360Binding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (isDiary) {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_homework_w360, parent, false),
                0
            )
        } else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_task, parent, false),
                1
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isDiary) {
            if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
                holder.binding!!.txtTitle.text = list[position].subject!!.name
            } else {
                holder.binding!!.txtTitle.text = "--/--"
            }
            if (list[position].homework != null) {
                holder.binding!!.topic.text = list[position].homework!!.title
                holder.binding!!.desc.setContent(list[position].homework!!.description)
                if (list[position].homework!!.createdBy != null) {
                    holder.binding!!.txtIncharge.text = "${list[position].homework!!.createdBy?.firstName ?: ""} ${list[position].homework!!.createdBy?.lastName ?: ""}"
                } else {
                    holder.binding!!.txtIncharge.text = "--/--"
                }
            } else {
                holder.binding!!.topic.text = "--/--"
                holder.binding!!.desc.setContent("--/--")
            }
            holder.binding!!.givenDate.text = " Given On : " + BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            holder.binding!!.subDate.text = "Submit On : " + BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            when (list[position].status) {
                "completed" -> {
                    UiUtils.imageviewDrawable( holder.binding!!.checkBoxStatus,R.drawable.green_tick )
                }
                "pending" -> {
                    UiUtils.imageviewDrawable( holder.binding!!.checkBoxStatus,R.drawable.rectangle_checkbox)
                }
                else -> {
                    UiUtils.imageviewDrawable(holder.binding!!.checkBoxStatus,R.drawable.rectangle_checkbox)
                }
            }

            holder.binding!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
        else{
            if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
                holder.binding1!!.txtTitle.text = list[position].subject!!.name
            } else {
                holder.binding1!!.txtTitle.text = "--/--"
            }
            if (list[position].homework != null) {
                holder.binding1!!.topic.text = list[position].homework!!.title
                holder.binding1!!.desc.setContent(list[position].homework!!.description)

                if (list[position].homework!!.createdBy != null) {
                    holder.binding1!!.txtIncharge.text = "${list[position].homework!!.createdBy?.firstName ?: ""} ${list[position].homework!!.createdBy?.lastName ?: ""}"
                } else {
                    holder.binding1!!.txtIncharge.text = "--/--"
                }
            } else {
                holder.binding1!!.topic.text = "--/--"
                holder.binding1!!.desc.setContent("--/--")
            }
            holder.binding1!!.givenDate.text = " Given On : " + BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            when (list[position].status) {
                "completed" -> {
                    holder.binding1!!.subDate.text = "Submitted On : " + BaseUtils.getFormattedDate(list[position].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    UiUtils.imageviewDrawable( holder.binding1!!.checkBoxStatus,R.drawable.green_tick )
                }
                "pending" -> {
                    holder.binding1!!.subDate.text = "Submit On : " + BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    UiUtils.imageviewDrawable( holder.binding1!!.checkBoxStatus,R.drawable.rectangle_checkbox)
                }
                else -> {
                    holder.binding1!!.subDate.text = "Submit On : " + BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    UiUtils.imageviewDrawable(holder.binding1!!.checkBoxStatus,R.drawable.rectangle_checkbox)
                }
            }
            holder.binding1!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }

        }

    }
}