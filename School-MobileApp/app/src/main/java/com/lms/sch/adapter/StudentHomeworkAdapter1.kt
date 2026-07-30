package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.HomeWorkResultActivity
import com.lms.sch.activity.KidsExamActivity
import com.lms.sch.databinding.CardHomeWorkBinding
import com.lms.sch.databinding.CardHomeworkW360Binding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentHomeworkAdapter1(
    val mActivity : BaseActivity,
    val isDiary : Boolean,
    private val list: ArrayList<GetHomeworkResponse.Result>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<StudentHomeworkAdapter1.ViewHolder>() {
    inner class ViewHolder(view: View, type: Int) : RecyclerView.ViewHolder(view) {
        var binding1: CardHomeworkW360Binding? = null
        var binding: CardHomeWorkBinding? = null
        init {
            if (type == 0) {
                binding1 = CardHomeworkW360Binding.bind(view)
            } else {
                binding = CardHomeWorkBinding.bind(view)
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
                LayoutInflater.from(mActivity).inflate(R.layout.card_home_work, parent, false),
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
        else{
            if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
                holder.binding!!.subject.text = list[position].subject!!.name
            } else {
                holder.binding!!.subject.text = "--/--"
            }

            if (list[position].homework != null) {
                holder.binding!!.title.text = list[position].homework!!.title
                holder.binding!!.desc.text = list[position].homework!!.description
                if (list[position].homework!!.createdBy != null) {
                    holder.binding!!.teacher.text = "${list[position].homework!!.createdBy?.firstName ?: ""} ${list[position].homework!!.createdBy?.lastName ?: ""}"
                } else {
                    holder.binding!!.teacher.text = "--/--"
                }
            } else {
                holder.binding!!.title.text = "--/--"
                holder.binding!!.desc.text = "--/--"
            }
            val given = BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            val due = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            holder.binding!!.date.text = "$given - $due"

            when (list[position].status) {
                "completed" -> {
                    UiUtils.imageviewDrawable( holder.binding!!.checkBoxStatus,R.drawable.green_tick )
                    holder.binding!!.txt.text = "Done"
                }
                "pending" -> {
                    UiUtils.imageviewDrawable( holder.binding!!.checkBoxStatus,R.drawable.rectangle_checkbox)
                    holder.binding!!.txt.text = "Mark as done"
                }
                else -> {
                    UiUtils.imageviewDrawable(holder.binding!!.checkBoxStatus,R.drawable.rectangle_checkbox)
                    holder.binding!!.txt.text = "Mark as done"
                }
            }

            holder.binding!!.root.setOnClickListener {
//                onClickListener.onClickItem(position)
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY,list[position].homework!!._id!!)
                bundle.putString("status", list[position]!!.markStatus)
                BaseUtils.startActivity(mActivity, HomeWorkResultActivity(),bundle,false)
            }
            if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
                when(list[position].subject?.name){
                    "General Knowledge" -> {
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#b81f3b",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#b81f3b",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#ffe6ea",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#b81f3b",null)
                    }
                    "English" ->{
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#3F8BFB",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#F0F6FF",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#3F8BFB",null)
                    }
                    "Maths" ->{
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#348f23",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#348f23",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#d8fad2",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#348f23",null)
                    }
                    "Science" ->{
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#E9E36B",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#E9E36B",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#FFFDD4",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#E9E36B",null)
                    }
                    "Hindi" ->{
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#A9A8DA",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#A9A8DA",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#F9F9F9",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#A9A8DA",null)
                    }
                    "Computer" ->{
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#E96B84",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#E96B84",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#FFEEF1",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#E96B84",null)
                    }
                    "Tamil" ->{
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#F6891E",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#F6891E",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#FFF5EB",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#F6891E",null)
                    }
                    "Social Science" ->{
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#45a9d1",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#45a9d1",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#ebf9ff",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#45a9d1",null)
                    }
                    else -> {
                        UiUtils.textViewTextColor(holder.binding!!.subject,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding!!.teacher,"#3F8BFB",null)
                        UiUtils.textViewBgTint(holder.binding!!.subject,"#F0F6FF",null)
                        UiUtils.setTextViewDrawableColor(holder.binding!!.subject,"#3F8BFB",null)
                    }
                }
            }

        }

    }
}