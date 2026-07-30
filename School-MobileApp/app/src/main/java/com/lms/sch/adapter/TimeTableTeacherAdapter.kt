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
import com.lms.sch.response.GetTeacherScheduleResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class TimeTableTeacherAdapter (
    var mActivity: BaseActivity,
    var isDiary: Boolean,
    var list: ArrayList<GetTeacherScheduleResponse.PeriodDetails>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TimeTableTeacherAdapter.ViewHolder>(){

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
                holder.binding!!.subject.text  = "${UiUtils.getOrdinalSuffix(list[position].studentClass!!.toInt())} - ${list[position].section} Sec"
                holder.binding!!.teacher.text  = list[position].subject?.name
                holder.binding!!.no.text = getPeriodNumber(position).toString()
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
                if (list[position].startTime != null){
                    holder.binding!!.startTime.text = BaseUtils.formatTime(list[position].startTime!!)
                }
                else{
                    holder.binding!!.startTime.text = ""
                }
            }
            else if (list[position].type == "free"){
                holder.binding!!.subject.text  = "Free"
                holder.binding!!.teacher.text  = "Period"
//                holder.binding!!.no.text  = ""+period
//                period++
                holder.binding!!.no.text = getPeriodNumber(position).toString()
                UiUtils.linearLayoutBgTint(holder.binding!!.linBg,"#f7f7f7",null)
                UiUtils.cardViewBgTint(holder.binding!!.card,"#e0e0e0",null)
            }

            holder.binding!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
        else {
            if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
                holder.binding1!!.weekLay.visibility = View.GONE
                holder.binding1!!.timetableLay.visibility = View.VISIBLE
                holder.binding1!!.subject.text  = "${UiUtils.getOrdinalSuffix(list[position].studentClass!!.toInt())} - ${list[position].section} Sec"
                holder.binding1!!.teacher.text  = list[position].subject?.name
                holder.binding1!!.no.text = getPeriodNumber(position).toString()
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
                        UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#DCF5FF",null)
                        UiUtils.cardViewBgTint(holder.binding1!!.card,"#6BC5E9",null)
                    }
                }
            }
            else if (list[position].type == "free"){
                holder.binding1!!.weekLay.visibility = View.GONE
                holder.binding1!!.timetableLay.visibility = View.VISIBLE
                holder.binding1!!.subject.text  = "Free"
                holder.binding1!!.teacher.text  = "Period"
//                holder.binding1!!.no.text  = ""+period
//                period++
                holder.binding1!!.no.text = getPeriodNumber(position).toString()
                UiUtils.linearLayoutBgTint(holder.binding1!!.linBg,"#f7f7f7",null)
                UiUtils.cardViewBgTint(holder.binding1!!.card,"#e0e0e0",null)
            }
            else{
                holder.binding1!!.weekLay.visibility = View.VISIBLE
                holder.binding1!!.timetableLay.visibility = View.GONE
                holder.binding1!!.breakTxt.text = list[position].type!!.capitalize()
            }
            if (list[position].startTime != null){
                holder.binding1!!.startTime.text = BaseUtils.formatTime(list[position].startTime!!)
                holder.binding1!!.startTime1.text = BaseUtils.formatTime(list[position].startTime!!)
            }
            else{
                holder.binding1!!.startTime.text = ""
                holder.binding1!!.startTime1.text = ""
            }
            holder.binding1!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
    }
    private fun getPeriodNumber(position: Int): Int {
        var count = 1
        for (i in 0 until position) {
            val item = list[i]
            if ((item.subject != null && item.subject!!.name!!.isNotEmpty()) || item.type == "free") {
                count++
            }
        }
        return count
    }
}