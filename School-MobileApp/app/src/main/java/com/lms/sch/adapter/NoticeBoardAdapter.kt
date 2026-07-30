package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAnnouncementBinding
import com.lms.sch.databinding.CardMonthlyAnouncementBinding
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class NoticeBoardAdapter(
    val mActivity: BaseActivity,
    val status : String,
    val list : ArrayList<NoticeBoardResponse.Result>
) : RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder>() {
        inner class ViewHolder(view: View,type: Int) : RecyclerView.ViewHolder(view){
            var binding :  CardAnnouncementBinding ?= null
            var binding1 : CardMonthlyAnouncementBinding ?= null
            init {
                if (type == 0){
                   binding = CardAnnouncementBinding.bind(view)
                }
                else {
                    binding1 = CardMonthlyAnouncementBinding.bind(view)
                }
            }

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (status == "day"){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_announcement,parent,false),
                0
            )
        }
        else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_monthly_anouncement,parent,false),
                1
            )
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (status == "day"){
            holder.binding!!.txtTitle.text = list[position].title
            if (list[position].type != null){
                when(list[position].type!!.name) {
                    "announcement" -> {
                        holder.binding!!.Assemblytype.text = "Assembly"
                        UiUtils.linearLayoutBgDrawable(holder.binding!!.maidAnnouncement, R.drawable.border_line_curve_10dp_events_red)
                        UiUtils.textViewTextColor(holder.binding!!.Assemblytype, null, R.color.red2)
                        UiUtils.textviewCustomDrawable(holder.binding!!.Assemblytype, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding!!.Assemblytype, null, R.color.light_red_bg)
                    }
                    "holiday" -> {
                        holder.binding!!.Assemblytype.text = "Holiday"
                        UiUtils.linearLayoutBgDrawable(holder.binding!!.maidAnnouncement, R.drawable.border_line_curve_10dp_events_green)
                        UiUtils.textViewTextColor(holder.binding!!.Assemblytype, null, R.color.green)
                        UiUtils.textviewCustomDrawable(holder.binding!!.Assemblytype, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding!!.Assemblytype, null, R.color.light_green)
                    }
                    "event" -> {
                        holder.binding!!.Assemblytype.text = "Event"
                        UiUtils.linearLayoutBgDrawable(holder.binding!!.maidAnnouncement, R.drawable.border_line_curve_10dp_events_blue)
                        UiUtils.textViewTextColor(holder.binding!!.Assemblytype, null, R.color.blue)
                        UiUtils.textviewCustomDrawable(holder.binding!!.Assemblytype, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding!!.Assemblytype, "#D8E1F4", null)
                    }
                    else -> {
                        holder.binding!!.Assemblytype.text = list[position].type!!.name
                        UiUtils.linearLayoutBgDrawable(holder.binding!!.maidAnnouncement, R.drawable.border_line_curve_10dp_events_yellow)
                        UiUtils.textViewTextColor(holder.binding!!.Assemblytype,"#f2c90f", null)
                        UiUtils.textviewCustomDrawable(holder.binding!!.Assemblytype, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding!!.Assemblytype, "#FFDFA9", null)
                    }
                }
            }
            holder.binding!!.description.setContent(list[position].description)
            if (list[position].startDate != null){
                holder.binding!!.announcedOn.text = BaseUtils.getFormattedDate(list[position].startDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            }
            else {
                holder.binding!!.announcedOn.text = "--/--"
            }
            if (list[position].endDate != null){
                holder.binding!!.happeningOn.text=BaseUtils.getFormattedDate(list[position].endDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            }
            else {
                holder.binding!!.happeningOn.text = "--/--"
            }

        }
        else {
            holder.binding1!!.txtTitle.text = list[position].title
            if (list[position].type != null){
                holder.binding1!!.status.text = list[position].type!!.name
                when(list[position].type!!.name) {
                    "announcement" -> {
                        holder.binding1!!.status.text = "Assembly"
                        UiUtils.constraintLayoutBgDrawable(holder.binding1!!.card, R.drawable.border_line_curve_10dp_events_red)
                        UiUtils.textViewTextColor(holder.binding1!!.status, null, R.color.red2)
                        UiUtils.textviewCustomDrawable(holder.binding1!!.status, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding1!!.status, null, R.color.light_red_bg)
                    }
                    "holiday" ->{
                        holder.binding1!!.status.text = "Holiday"
                        UiUtils.constraintLayoutBgDrawable(holder.binding1!!.card, R.drawable.border_line_curve_10dp_events_green)
                        UiUtils.textViewTextColor(holder.binding1!!.status, null, R.color.green)
                        UiUtils.textviewCustomDrawable(holder.binding1!!.status, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding1!!.status, null, R.color.light_green)
                    }
                    "event" -> {
                        holder.binding1!!.status.text = "Event"
                        UiUtils.constraintLayoutBgDrawable(holder.binding1!!.card, R.drawable.border_line_curve_10dp_events_blue)
                        UiUtils.textViewTextColor(holder.binding1!!.status, null, R.color.blue)
                        UiUtils.textviewCustomDrawable(holder.binding1!!.status, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding1!!.status, null, R.color.light_blue_bg)
                    }
                    else -> {
                        holder.binding1!!.status.text = list[position].type!!.name
                        UiUtils.constraintLayoutBgDrawable(holder.binding1!!.card, R.drawable.border_line_curve_10dp_events_blue)
                        UiUtils.textViewTextColor(holder.binding1!!.status, null, R.color.red_hurry_up)
                        UiUtils.textviewCustomDrawable(holder.binding1!!.status, R.drawable.border_curve_24dp)
                        UiUtils.textViewBgTint(holder.binding1!!.status, null, R.color.light_red_bg)
                    }
                }
            }
            holder.binding1!!.desc.text = list[position].description
            holder.binding1!!.day.text = BaseUtils.getFormattedDate(list[position].createdAt!!,
                Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DN)
            holder.binding1!!.date.text = BaseUtils.getFormattedDate(list[position].createdAt!!,
                Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DD)

        }
    }
}
