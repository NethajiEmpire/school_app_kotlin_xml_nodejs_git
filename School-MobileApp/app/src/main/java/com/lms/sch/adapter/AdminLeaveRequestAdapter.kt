package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardLeaveRequestBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.AdminLeaveRequest
import com.lms.sch.utils.UiUtils

class AdminLeaveRequestAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<AdminLeaveRequest>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<AdminLeaveRequestAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardLeaveRequestBinding = CardLeaveRequestBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_leave_request, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.staffName.text = list[position].staffNames
        holder.binding.staff.text = list[position].staffRole
        holder.binding.reasonLeave.text = list[position].reason

        if (list[position].typeLeave == "person1")
        {
            UiUtils.textViewBgTint(holder.binding.leaveType, "#E8F6FF",null)
            UiUtils.textViewTextColor(holder.binding.leaveType, "#87CEFA",null)
            holder.binding.leaveType.text ="Sick"
        }
        else if (list[position].typeLeave == "person2")
        {
            UiUtils.textViewBgTint(holder.binding.leaveType, "#D3F8E8",null)
            UiUtils.textViewTextColor(holder.binding.leaveType, "#1FB172",null)
            holder.binding.leaveType.text ="Casual "
        }
        else if (list[position].typeLeave == "person3")
        {
            UiUtils.textViewBgTint(holder.binding.leaveType, "#FFEAED",null)
            UiUtils.textViewTextColor(holder.binding.leaveType, "#FF7C90",null)
            holder.binding.leaveType.text ="Maternity"
        }
        else if (list[position].typeLeave == "person4")
        {
            UiUtils.textViewBgTint(holder.binding.leaveType, "#FFEBB7",null)
            UiUtils.textViewTextColor(holder.binding.leaveType, "#DDA50E",null)
            holder.binding.leaveType.text ="Paternity"
        }
        holder.binding!!.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}
