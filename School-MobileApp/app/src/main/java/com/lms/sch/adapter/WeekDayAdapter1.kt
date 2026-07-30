package com.lms.sch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.databinding.CardCalenderItemBinding
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.*

class WeekDayAdapter1(
    private val dates: List<Date>,
    private val currentDate: Date,
    private val onDateSelected: (Date) -> Unit
) : RecyclerView.Adapter<WeekDayAdapter1.WeekDayViewHolder>() {

    private var selectedPos = -1

    inner class WeekDayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardCalenderItemBinding = CardCalenderItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekDayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_calender_item, parent, false)
        return WeekDayViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekDayViewHolder, @SuppressLint("RecyclerView") position: Int) {
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
        }
        else if (!isCurrentDate) {
            UiUtils.textviewCustomDrawable(holder.binding.date, R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(holder.binding.date, null, R.color.black_varient3)
            UiUtils.textViewTextColor(holder.binding.day, null, R.color.black_varient3)
        }

        holder.binding.container.visibility = View.GONE

        holder.binding.root.setOnClickListener {
            if (selectedPos != position) {
                val previousPos = selectedPos
                selectedPos = position
                notifyItemChanged(previousPos)
                notifyItemChanged(selectedPos)
                onDateSelected(date)
            }
        }
    }

    override fun getItemCount(): Int = dates.size

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date1) == format.format(date2)
    }
}
