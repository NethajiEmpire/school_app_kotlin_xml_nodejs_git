package com.lms.sch.adapter

import android.util.Log
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

class LeaveRequestAdapter (
    var mActivity: BaseActivity,
    var list: ArrayList<LeaveRequestResponse.Rows>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder>(){

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
        if (list[position].title != null){
            holder.binding.reasonLeave.text = list[position].title
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

        if (list[position].type != null){
            holder.binding.leaveType.text = list[position].type!!.name
        }
        else {
            holder.binding.leaveType.text = "--/--"
        }
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
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
            /*val dialog = Dialog(mActivity)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_logout)
            val bind: DialogLogoutBinding = DialogLogoutBinding.inflate(LayoutInflater.from(mActivity))
            dialog.setContentView(bind.root)
//        UiUtils.animation(this, bind.card, R.anim.slide_in_from_bottom, true)
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(mActivity, R.color.transparent))
            )
            var width: Int = (mActivity.resources.displayMetrics.widthPixels * 0.9).roundToInt()
//        var height: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setGravity(Gravity.CENTER)

            bind.logout1.setOnClickListener {
                BaseUtils.logout(mActivity, "")
                dialog.dismiss()
            }
            bind.cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnDismissListener {

            }
            dialog.show()*/
        }
    }
}