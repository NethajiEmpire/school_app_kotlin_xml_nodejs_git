package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardClassTimetableBinding
import com.lms.sch.databinding.CardTimeTableBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetClassTimeTableResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class TimeTableAdapter (
    var mActivity: BaseActivity,
    var isDiary: Boolean,
    var list: ArrayList<GetClassTimeTableResponse.Period>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TimeTableAdapter.ViewHolder>(){
    var period = 1
    inner class ViewHolder(view: View,type: Int) : RecyclerView.ViewHolder(view) {
        var binding: CardTimeTableBinding ?= null
        var binding1: CardClassTimetableBinding ?= null
        init {
            if (type == 0){
                binding = CardTimeTableBinding.bind(view)
            }
            else {
                binding1 = CardClassTimetableBinding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (isDiary){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_time_table, parent, false),
                0
            )
        }
        else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_class_timetable, parent, false),
                1
            )
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isDiary){
            if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
                holder.binding!!.subject.text  = list[position].subject?.name
                if (list[position].teacher != null){
                    holder.binding!!.teacher.text  = list[position].teacher!!.firstName + " " + list[position].teacher!!.lastName
                }
                else {
                    holder.binding!!.teacher.text  = "--/--"
                }
                holder.binding!!.no.text  = ""+period
                period++
                when(list[position].subject?.name){
                    "General Knowledge" -> {
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#FCE8FF",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#DF3FFB",null)
                    }
                    "English" ->{
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#F0F6FF",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#3F8BFB",null)
                    }
                    "Maths" ->{
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#E0FFEE",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#6BE9A3",null)
                    }
                    "Science" ->{
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#FFFDD4",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#E9E36B",null)
                    }
                    "Hindi" ->{
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#F9F9F9",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#A9A8DA",null)
                    }
                    "Computer" ->{
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#FFEEF1",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#E96B84",null)
                    }
                    "Tamil" ->{
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#FFF5EB",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#F6891E",null)
                    }
                    "Social Science" ->{
                        UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#DCF5FF",null)
                        UiUtils.cardViewBgTint(holder.binding!!.card,"#6BC5E9",null)
                    }
                }
                if (list[position].timePeriod != null && list[position].timePeriod!!.startTime != null){
                    holder.binding!!.startTime.text = BaseUtils.formatTime(list[position].timePeriod!!.startTime!!)
                }
                else{
                    holder.binding!!.startTime.text = ""
                }
                holder.binding!!.root.setOnClickListener {
                    onClickListener.onClickItem(position)
                }
            }
        }
        else {
            if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
                holder.binding1!!.weekLay.visibility = View.GONE
                holder.binding1!!.timetableLay.visibility = View.VISIBLE
                holder.binding1!!.subject.text  = list[position].subject?.name
                holder.binding1!!.teacher.text  = list[position].teacher!!.firstName + " " + list[position].teacher!!.lastName
                holder.binding1!!.no.text  = ""+period
                period++
                when(list[position].subject?.name){
                    "General Knowledge" -> {
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#FCE8FF",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#DF3FFB",null)
                    }
                    "English" ->{
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#F0F6FF",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#3F8BFB",null)
                    }
                    "Maths" ->{
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#E0FFEE",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#6BE9A3",null)
                    }
                    "Science" ->{
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#FFFDD4",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#E9E36B",null)
                    }
                    "Hindi" ->{
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#F9F9F9",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#A9A8DA",null)
                    }
                    "Computer" ->{
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#FFEEF1",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#E96B84",null)
                    }
                    "Tamil" ->{
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#FFF5EB",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#F6891E",null)
                    }
                    "Social Science" ->{
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#DCF5FF",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#6BC5E9",null)
                    }
                    else -> {
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#FFEEF1",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#E96B84",null)
                    }
                }
            }
            else{
                holder.binding1!!.weekLay.visibility = View.VISIBLE
                holder.binding1!!.timetableLay.visibility = View.GONE
                holder.binding1!!.breakTxt.text = list[position].type!!.capitalize()
            }
            if (list[position].timePeriod != null && list[position].timePeriod!!.startTime != null){
                holder.binding1!!.startTime.text = BaseUtils.formatTime(list[position].timePeriod!!.startTime!!)
                holder.binding1!!.startTime1.text = BaseUtils.formatTime(list[position].timePeriod!!.startTime!!)
            }
            else{
                holder.binding1!!.startTime.text = ""
            }
            holder.binding1!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
    }
}