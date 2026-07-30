package com.lms.sch.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardCalenderItemBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WeekDayAdapter(
    val mActivity: BaseActivity,
    val dates: ArrayList<Date>,
    val currentDate: Date,
    val list: ArrayList<NoticeBoardResponse.Result>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<WeekDayAdapter.WeekDayViewHolder>() {
    var selectedPos = -1

    private val eventTypeColors = mapOf(
        "meeting" to Color.parseColor("#F5A623"),
        "exam" to Color.parseColor("#E85A5B"),
        "holiday" to Color.parseColor("#6BE9A3"),
        "event" to Color.parseColor("#9013FE")
    )

    inner class WeekDayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardCalenderItemBinding = CardCalenderItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekDayViewHolder {
        val view = LayoutInflater.from(mActivity).inflate(R.layout.card_calender_item, parent, false)
        return WeekDayViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekDayViewHolder, position: Int) {
        val date = dates[position]
        val isCurrentDate = isSameDay(date, currentDate)
        val isSelected = position == selectedPos

        holder.binding.day.text = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
        holder.binding.date.text = SimpleDateFormat("dd", Locale.getDefault()).format(date)

        if (isCurrentDate) {
            UiUtils.textviewCustomDrawable(holder.binding.date, R.drawable.ic_round_primary)
            UiUtils.textViewTextColor(holder.binding.date, null, R.color.white)
            UiUtils.textViewTextColor(holder.binding.day, null, R.color.colorPrimary)
        } else {
            UiUtils.textviewCustomDrawable(holder.binding.date, R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(holder.binding.date, null, R.color.black_varient3)
            UiUtils.textViewTextColor(holder.binding.day, null, R.color.black_varient3)
        }

        if (isSelected) {
            UiUtils.textviewCustomDrawable(holder.binding.date, R.drawable.ic_round_line_3)
            UiUtils.textViewBgTint(holder.binding.date, "#DFEEFF", null)
            UiUtils.textViewTextColor(holder.binding.date, null, R.color.black_varient3)
            UiUtils.textViewTextColor(holder.binding.day, null, R.color.colorPrimary)
        } else if (!isCurrentDate) {
            UiUtils.textviewCustomDrawable(holder.binding.date, R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(holder.binding.date, null, R.color.black_varient3)
            UiUtils.textViewTextColor(holder.binding.day, null, R.color.black_varient3)
        }

        holder.binding.container.removeAllViews()

        val eventsForDate = getEventsForDate(date)

        for (event in eventsForDate) {
            val eventView = View(mActivity)
            val size = 16
            val layoutParams = LinearLayout.LayoutParams(size, size)
            layoutParams.marginStart = 6
            eventView.layoutParams = layoutParams

            val drawable = ContextCompat.getDrawable(mActivity, R.drawable.dot)
            eventView.background = drawable

            val color = eventTypeColors[event.type!!.name] ?: Color.GRAY
            drawable?.setTint(color)

            holder.binding.container.addView(eventView)
        }

        holder.binding.root.setOnClickListener {
            if (selectedPos != position) {
                val previousPos = selectedPos
                selectedPos = position
                notifyItemChanged(previousPos)
                notifyItemChanged(selectedPos)
                onClickListener.onClickItem(position)
            }
        }
    }

    override fun getItemCount(): Int = dates.size

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date1) == format.format(date2)
    }

    private fun getEventsForDate(date: Date): List<NoticeBoardResponse.Result> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateStr = dateFormat.format(date)
        val resultList = mutableListOf<NoticeBoardResponse.Result>()

        for (event in list) {
            try {
                val startDate = dateFormat.parse(event.startDate!!)
                val endDate = dateFormat.parse(event.endDate!!)

                val eventStartStr = dateFormat.format(startDate!!)
                val eventEndStr = dateFormat.format(endDate!!)

                if (dateStr == eventStartStr || dateStr == eventEndStr) {
                    resultList.add(event)
                } else if (date.after(startDate) && date.before(endDate)) {
                    resultList.add(event)
                }
            } catch (e: Exception) {
                continue
            }
        }

        return resultList
    }
}