package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardLeaveRequestBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.LeaveRequestResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class TeacherLeaveRequestAdapter (
    var mActivity: BaseActivity,
    var list: ArrayList<LeaveRequestResponse.Rows>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TeacherLeaveRequestAdapter.ViewHolder>(){

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
        if (list[position].type != null ){
            holder.binding.reasonLeave.text = list[position].type!!.name!!
        }
        else {
            holder.binding.reasonLeave.text = "--/--"
        }

        if (list[position].createdBy != null){
            if (list[position].createdBy!!._id == mActivity.sharedHelper.id){
                holder.binding.staffName.text = "You(Author)"
            }
            else {
                holder.binding.staffName.text = list[position].createdBy!!.firstName+" "+list[position].createdBy!!.lastName
            }
        }
        else {
            holder.binding.staffName.text = "--/--"
        }
        if (list[position].requestId != null){
            holder.binding.staff.text = list[position].requestId
        }
        else {
            holder.binding.staff.text = "--/--"
        }
        if (list[position].createdBy != null && list[position].createdBy!!.img_url != null){
            Glide.with(mActivity).load(list[position].createdBy!!.img_url).into(holder.binding.img)
        }
        else{
            UiUtils.loadImage(holder.binding.img,R.drawable.ic_user_profile.toString())
        }
//        if (list[position].type != null){
//            holder.binding.leaveType.text = list[position].type!!.name
//        }
//        else {
//            holder.binding.leaveType.text = "--/--"
//        }
        if (list[position].createdAt != null){
            holder.binding.requested.text = BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else {
            holder.binding.requested.text = "--/--"
        }
        if (list[position].startDate != null){
            holder.binding.start.text = BaseUtils.getFormattedDate(list[position].startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else {
            holder.binding.start.text = "--/--"
        }
        if (list[position].endDate != null){
            holder.binding.end.text = BaseUtils.getFormattedDate(list[position].endDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else {
            holder.binding.end.text = "--/--"
        }
        when(list[position].status) {
            "rejected" -> {
                holder.binding.leaveType.text = "Rejected"
                UiUtils.textviewCustomDrawable(holder.binding.leaveType, R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.leaveType, "#FFEBEB", null)
                UiUtils.textViewTextColor(holder.binding.leaveType, "#EA5455", null)
            }
            "approved" -> {
                holder.binding.leaveType.text = "Approved"
                UiUtils.textviewCustomDrawable(holder.binding.leaveType, R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.leaveType, "#CFFFD1", null)
                UiUtils.textViewTextColor(holder.binding.leaveType, "#32B138", null)
            }
            "pending" -> {
                holder.binding.leaveType.text = "Pending"
                UiUtils.textviewCustomDrawable(holder.binding.leaveType, R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.leaveType, "#FFF2DE", null)
                UiUtils.textViewTextColor(holder.binding.leaveType, "#F69300", null)
            }
            else -> {
                holder.binding.leaveType.text = "Pending"
                UiUtils.textviewCustomDrawable(holder.binding.leaveType, R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.leaveType, "#FFF2DE", null)
                UiUtils.textViewTextColor(holder.binding.leaveType, "#F69300", null)
            }

        }
        holder.binding.root.setOnClickListener {
            if (list[position].createdBy != null && list[position].createdBy!!._id != mActivity.sharedHelper.id){
                onClickListener.onClickItem(position)
            }
        }
    }
}