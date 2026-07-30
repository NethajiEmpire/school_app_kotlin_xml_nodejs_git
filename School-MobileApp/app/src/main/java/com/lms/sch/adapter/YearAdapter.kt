package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardYearMonthPickerBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.utils.UiUtils
import java.util.Calendar

class YearAdapter(
    var mActivity: BaseActivity,
    var list : ArrayList<String>,
    var selectedYear : Int,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<YearAdapter.ViewHolder>() {
    var selectedPos = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardYearMonthPickerBinding = CardYearMonthPickerBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_year_month_picker, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val year = list[position]
        holder.binding.year.text = year

        val sYear = year.toInt()
        val isCurrentYear = isCurrentYear(sYear)
        val isSelected = position == selectedPos

        if (isCurrentYear) {
            UiUtils.textviewCustomDrawable(holder.binding.year, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(holder.binding.year, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(holder.binding.year, null, R.color.white)
        } else {
            UiUtils.textviewCustomDrawable(holder.binding.year, R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(holder.binding.year, null, R.color.black_varient6)
        }

        if (isSelected) {
            UiUtils.textviewCustomDrawable(holder.binding.year, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(holder.binding.year, "#DFEEFF", R.color.colorPrimary)
            UiUtils.textViewTextColor(holder.binding.year, null, R.color.black_varient6)
        } else if (!isCurrentYear) {
            UiUtils.textviewCustomDrawable(holder.binding.year, R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(holder.binding.year, null, R.color.black_varient6)
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

    private fun isCurrentYear(selectedYear: Int): Boolean {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        if (selectedYear == currentYear) {
            UiUtils.log("isCurrentYear", "Current year matched: $selectedYear")
        }

        return selectedYear == currentYear
    }

}