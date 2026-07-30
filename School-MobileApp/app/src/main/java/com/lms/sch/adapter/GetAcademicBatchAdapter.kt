package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAcademicBatchBinding
import com.lms.sch.databinding.CardAcademicBoardBinding
import com.lms.sch.response.GetAcademicBatchResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class GetAcademicBatchAdapter (
    val mActivity: BaseActivity,
    var list: ArrayList<GetAcademicBatchResponse.Result.Rows>,
) : RecyclerView.Adapter<GetAcademicBatchAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardAcademicBatchBinding = CardAcademicBatchBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_academic_batch, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].batchId != null) {
            holder.binding.batchId.text = list[position].batchId
        } else {
            holder.binding.batchId.text = "--/--"
        }
        if (list[position].name != null) {
            holder.binding.batch.text = list[position].name
        } else {
            holder.binding.batch.text = "--/--"
        }
        if (list[position].teacherCount != null) {
            holder.binding.teacherCount.text = list[position].teacherCount!!.toString()
        } else {
            holder.binding.teacherCount.text = "--/--"
        }
        if (list[position].start_date != null && list[position].end_date != null) {
            val formattedStartDate = BaseUtils.getFormattedDate(list[position].start_date!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            val formattedEndDate = BaseUtils.getFormattedDate(list[position].end_date!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            holder.binding.startEndDate.text = "$formattedStartDate - $formattedEndDate"
        } else {
            holder.binding.startEndDate.text = "--/--"
        }

        if (list[position].studentCount != null) {
            holder.binding.studentCounts.text = list[position].studentCount!!.toString()
        } else {
            holder.binding.studentCounts.text = "--/--"
        }
        when (list[position].status) {
            "active" -> {
                holder.binding.status.text = "Active"
                UiUtils.textviewCustomDrawable(holder.binding.status, R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#E5F8ED", null)
                UiUtils.textViewTextColor(holder.binding.status, "#28C76F", null)
            }
            "inactive" -> {
                holder.binding.status.text = "Inactive"
                UiUtils.textviewCustomDrawable(holder.binding.status, R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#FFF2DE", null)
                UiUtils.textViewTextColor(holder.binding.status, "#F39519", null)
            }
        }
        if (list[position].admission_now!!){
            UiUtils.imageviewDrawable(holder.binding.addOpen,R.drawable.add_open)
        }
        else {
            UiUtils.imageviewDrawable(holder.binding.addOpen,R.drawable.add_close)
        }
    }
}