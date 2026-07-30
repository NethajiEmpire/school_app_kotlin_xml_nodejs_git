package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAttendanceBinding
import com.lms.sch.databinding.CardKidsAttendanceBinding
import com.lms.sch.fragment.MyClassFragment
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetAttendanceResponse
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import java.util.Locale

class KidsAttendanceAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<GetStudentAttenDanceResponse.Result.Attendance>
) : RecyclerView.Adapter<KidsAttendanceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardKidsAttendanceBinding = CardKidsAttendanceBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_kids_attendance, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = BaseUtils.getFormattedDate(list[position].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DD)
        val mon = BaseUtils.getFormattedDate(list[position].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.MMM)
        val day = BaseUtils.getFormattedDate(list[position].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DDN)
        val date1 = BaseUtils.getFormattedDate(list[position].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT3)
        holder.binding.no.text = date
        holder.binding.mon.text = mon.uppercase(Locale.ROOT)
        holder.binding.day.text = day.uppercase(Locale.ROOT)
        holder.binding.date1.text = date1

        when(list[position].status){
            "present" -> {
                holder.binding.status.text = "Present"
                UiUtils.textViewTextColor(holder.binding.status,"#FFC107",null)
                UiUtils.textViewBgTint(holder.binding.status,"#fcf5de",null)
                UiUtils.setTextViewDrawableColor(holder.binding.status,"#FFC107",null)
                UiUtils.textViewBgTint(holder.binding.no,"#FFC107",null)
                UiUtils.textViewTextColor(holder.binding.no,"#FFFFFF",null)
                UiUtils.textViewTextColor(holder.binding.mon,"#FFC107",null)
                UiUtils.viewBgTint(holder.binding.verticalLine,"#FFC107",null)
                UiUtils.viewBgTint(holder.binding.view,"#FFC107",null)
            }
            "absent" -> {
                holder.binding.status.text = "Absent"
                UiUtils.textViewTextColor(holder.binding.status,"#AB47BC",null)
                UiUtils.textViewBgTint(holder.binding.status,"#f6d2fc",null)
                UiUtils.setTextViewDrawableColor(holder.binding.status,"#AB47BC",null)
                UiUtils.textViewBgTint(holder.binding.no,"#AB47BC",null)
                UiUtils.textViewTextColor(holder.binding.no,"#FFFFFF",null)
                UiUtils.textViewTextColor(holder.binding.mon,"#AB47BC",null)
                UiUtils.viewBgTint(holder.binding.verticalLine,"#AB47BC",null)
                UiUtils.viewBgTint(holder.binding.view,"#AB47BC",null)
            }
            "halfDay" -> {
                holder.binding.status.text = "Half Day"
                UiUtils.textViewTextColor(holder.binding.status,"#FF4081",null)
                UiUtils.textViewBgTint(holder.binding.status,"#ffe0eb",null)
                UiUtils.setTextViewDrawableColor(holder.binding.status,"#FF4081",null)
                UiUtils.textViewBgTint(holder.binding.no,"#FF4081",null)
                UiUtils.textViewTextColor(holder.binding.no,"#FFFFFF",null)
                UiUtils.textViewTextColor(holder.binding.mon,"#FF4081",null)
                UiUtils.viewBgTint(holder.binding.verticalLine,"#FF4081",null)
                UiUtils.viewBgTint(holder.binding.view,"#FF4081",null)
            }
            else -> {
                holder.binding.status.text = list[position].status
                UiUtils.textViewTextColor(holder.binding.status,"#323a61",null)
                UiUtils.textViewBgTint(holder.binding.status,"#e8ecff",null)
                UiUtils.setTextViewDrawableColor(holder.binding.status,"#323a61",null)
                UiUtils.textViewBgTint(holder.binding.no,"#323a61",null)
                UiUtils.textViewTextColor(holder.binding.no,"#FFFFFF",null)
                UiUtils.textViewTextColor(holder.binding.mon,"#323a61",null)
                UiUtils.viewBgTint(holder.binding.verticalLine,"#e8ecff",null)
                UiUtils.viewBgTint(holder.binding.view,"#e8ecff",null)
            }
        }
    }

 }