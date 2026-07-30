package com.lms.sch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.utils.UiUtils
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardEventBinding
import com.lms.sch.response.NoticeBoardResponse

class EventsPagerAdapter(
    var mActivity: BaseActivity,
    val list : ArrayList<NoticeBoardResponse.Result>
) : RecyclerView.Adapter<EventsPagerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding : CardEventBinding = CardEventBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(
                R.layout.card_event,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.title.text = list[position].title
        holder.binding.desc.text = list[position].description
        if (list[position].type != null){
            when(list[position].type!!.name){
                "event" -> {
                    holder.binding.type.text = "Event"
                    UiUtils.textviewCustomDrawable(holder.binding.type,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.type,"#3F8BFB",null)
                    UiUtils.textViewBgTint(holder.binding.type,"#deebff",null)
                    UiUtils.linearLayoutBgDrawable(holder.binding.card, R.drawable.border_line_curve_10dp_events_blue)
                }
                "announcement" -> {
                    holder.binding.type.text = "Announcement"
                    UiUtils.textviewCustomDrawable(holder.binding.type,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.type,"#F5A623",null)
                    UiUtils.textViewBgTint(holder.binding.type,"#faefdc",null)
                    UiUtils.linearLayoutBgDrawable(holder.binding.card, R.drawable.border_line_curve_10dp_events_yellow)
                }
                "meeting" -> {
                    holder.binding.type.text = "Meeting"
                    UiUtils.textviewCustomDrawable(holder.binding.type,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.type,"#F5A623",null)
                    UiUtils.textViewBgTint(holder.binding.type,"#faefdc",null)
                    UiUtils.linearLayoutBgDrawable(holder.binding.card, R.drawable.border_line_curve_10dp_events_yellow)
                }
                "exam" -> {
                    holder.binding.type.text = "Exam"
                    UiUtils.textviewCustomDrawable(holder.binding.type,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.type,"#E85A5B",null)
                    UiUtils.textViewBgTint(holder.binding.type,"#ffe8e8",null)
                    UiUtils.linearLayoutBgDrawable(holder.binding.card, R.drawable.border_line_curve_10dp_events_red)
                }
                "holiday" -> {
                    holder.binding.type.text = "Holiday"
                    UiUtils.textviewCustomDrawable(holder.binding.type,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.type,"#6BE9A3",null)
                    UiUtils.textViewBgTint(holder.binding.type,"#deffee",null)
                    UiUtils.linearLayoutBgDrawable(holder.binding.card, R.drawable.border_line_curve_10dp_events_green)
                }
                "festival" -> {
                    holder.binding.type.text = "Festival"
                    UiUtils.textviewCustomDrawable(holder.binding.type,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.type,"#5E31B8",null)
                    UiUtils.textViewBgTint(holder.binding.type,"#e1d1ff",null)
                    UiUtils.linearLayoutBgDrawable(holder.binding.card, R.drawable.border_line_curve_10dp_events_purple)
                }
                else -> {
                    holder.binding.type.text = list[position].type!!.name
                    UiUtils.textviewCustomDrawable(holder.binding.type,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.type,"#cccccc",null)
                    UiUtils.textViewBgTint(holder.binding.type,"#f2f2f2",null)
                    UiUtils.linearLayoutBgDrawable(holder.binding.card, R.drawable.border_line_curve_10dp_events_blue)
                }
            }
        }
    }
}