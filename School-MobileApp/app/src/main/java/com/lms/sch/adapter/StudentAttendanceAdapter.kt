package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAttendanceTextBinding
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentAttendanceAdapter(
    private val mActivity: BaseActivity,
    private val list: ArrayList<GetStudentAttenDanceResponse.Result.Attendance>
) : RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardAttendanceTextBinding = CardAttendanceTextBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_attendance_text, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val txt = BaseUtils.getFormattedDate(list[position].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.E)
        holder.binding.txt.text = txt.first().toString()

        when (list[position].status?.lowercase()) {
            "present" -> {
                UiUtils.textViewTextColor(holder.binding.txt, "#FFFFFF", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.lin, R.drawable.border_curve_8dp)
                UiUtils.linearLayoutBgTint(holder.binding.lin, "#32B138", null) // Green for present
            }
            "absent" -> {
                UiUtils.textViewTextColor(holder.binding.txt, "#FFFFFF", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.lin, R.drawable.border_curve_8dp)
                UiUtils.linearLayoutBgTint(holder.binding.lin, "#EA5455", null) // Red for absent
            }
            "halfday" -> {
                UiUtils.textViewTextColor(holder.binding.txt, "#FFFFFF", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.lin, R.drawable.border_curve_8dp)
                UiUtils.linearLayoutBgTint(holder.binding.lin, "#5D82ED", null) // Blue for half-day
            }
            "pending" -> {
                UiUtils.textViewTextColor(holder.binding.txt, "#FFFFFF", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.lin, R.drawable.border_curve_8dp)
                UiUtils.linearLayoutBgTint(holder.binding.lin, "#FFA500", null) // Orange for pending
            }
            "holiday" -> {
                UiUtils.textViewTextColor(holder.binding.txt, "#FFFFFF", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.lin, R.drawable.border_curve_8dp)
                UiUtils.linearLayoutBgTint(holder.binding.lin, "#808080", null) // Gray for holiday
            }
            else -> {
                UiUtils.textViewTextColor(holder.binding.txt, "#000000", null)
                UiUtils.linearLayoutBgDrawable(holder.binding.lin, R.drawable.border_curve_8dp)
                UiUtils.linearLayoutBgTint(holder.binding.lin, "#D3D3D3", null) // Light gray for unknown
            }
        }
    }
}