package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardClassTimetableWeeklyBinding
import com.lms.sch.models.TimeTableSlots
import com.lms.sch.utils.UiUtils

class WeeklyTimeTableAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<TimeTableSlots>,
): RecyclerView.Adapter<WeeklyTimeTableAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardClassTimetableWeeklyBinding = CardClassTimetableWeeklyBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_class_timetable_weekly, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].isWeekDay){
            if (position == 0){
                UiUtils.textViewTextColor(holder.binding.weekDay,"#333333",null)
                UiUtils.textviewCustomDrawable(holder.binding.weekDay, R.drawable.border_curve_6dp)
            }
            holder.binding.mainLay.visibility = View.GONE
            holder.binding.subLay.visibility = View.GONE
            holder.binding.weekLay.visibility = View.GONE
            holder.binding.weekDay.visibility = View.VISIBLE
            holder.binding.weekDay.text = list[position].subject
        }
        else {
            holder.binding.weekDay.visibility = View.GONE
            holder.binding.mainLay.visibility = View.GONE
            holder.binding.subLay.visibility = View.GONE
            holder.binding.weekLay.visibility = View.GONE
        }
        if (list[position].time.isNotEmpty()){
            holder.binding.mainLay.visibility = View.VISIBLE
            holder.binding.subLay.visibility = View.GONE
            holder.binding.dayRecycler.visibility = View.VISIBLE
            holder.binding.num.text = list[position].time
            holder.binding.no.text = list[position].periodNumber
        }
        else {
            holder.binding.mainLay.visibility = View.GONE
            holder.binding.subLay.visibility = View.GONE
            holder.binding.dayRecycler.visibility = View.GONE
        }
        if (list[position].isBreak) {
            holder.binding.subLay.visibility = View.GONE
            holder.binding.mainLay.visibility = View.GONE
            holder.binding.breakLay.visibility = View.VISIBLE
            holder.binding.dayRecycler.visibility = View.GONE
            holder.binding.weekLay.visibility = View.VISIBLE
            holder.binding.breakTxt.text = list[position].subject
            holder.binding.day.text = list[position].time
        } else if (list[position].time.isEmpty() && !list[position].isWeekDay){
            holder.binding.subLay.visibility = View.VISIBLE
            holder.binding.breakLay.visibility = View.GONE
            holder.binding.weekLay.visibility = View.GONE
            holder.binding.subject.text = list[position].subject
            holder.binding.teacher.text = list[position].teacher

            UiUtils.cardViewBgColor(holder.binding.card, list[position].bg_card,null)
            UiUtils.linearLayoutBgTint(holder.binding.linBg, list[position].bg_lay,null)
        }
    }
}