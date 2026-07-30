package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardMonthPickerBinding
import com.lms.sch.databinding.CardYearMonthPickerBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.utils.UiUtils
import java.util.Calendar

class MonthsAdapter(
    var mActivity: BaseActivity,
    var list : ArrayList<String>,
    var selectedYear : Int,
    var selectedMonth : Int,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<MonthsAdapter.ViewHolder>() {
    var selectedPos = -1
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardMonthPickerBinding = CardMonthPickerBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_month_picker, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val month = list[position]
        holder.binding.month.text = month

        val isSelected = position == selectedPos
        val isCurrentMonth = isCurrentMonth(selectedYear, position)

        if (isCurrentMonth) {
            UiUtils.textviewCustomDrawable(holder.binding.month, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(holder.binding.month, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(holder.binding.month, null, R.color.white)
        } else {
            UiUtils.textviewCustomDrawable(holder.binding.month, R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(holder.binding.month, null, R.color.black_varient6)
        }

        if (isSelected) {
            UiUtils.textviewCustomDrawable(holder.binding.month, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(holder.binding.month, "#DFEEFF", R.color.colorPrimary)
            UiUtils.textViewTextColor(holder.binding.month, null, R.color.black_varient6)
        } else if (!isCurrentMonth) {
            UiUtils.textviewCustomDrawable(holder.binding.month, R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(holder.binding.month, null, R.color.black_varient6)
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

    private fun isCurrentMonth(selectedYear: Int, selectedMonth: Int): Boolean {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val isCurrentMonth = selectedYear == currentYear && selectedMonth == currentMonth
        if (isCurrentMonth) {
            UiUtils.log("isCurrentMonth", "Selected Year: $selectedYear, Selected Month: $selectedMonth matches current year and month")
        }

        return isCurrentMonth
    }

}