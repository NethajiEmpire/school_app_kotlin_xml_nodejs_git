package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAdminComplaintsAllBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetComplaintResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class GetComplaintsAdapter  (
    val mActivity: BaseActivity,
    var list: ArrayList<GetComplaintResponse.Result.Rows>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<GetComplaintsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardAdminComplaintsAllBinding = CardAdminComplaintsAllBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_admin_complaints_all, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].title != null) {
            holder.binding.cmpName.text = list[position].title
        } else {
            holder.binding.cmpName.text = "--/--"
        }

        val userRole = mActivity.sharedHelper.role
        val logInUserId = mActivity.sharedHelper.id

       /* if (list[position].createdBy != null) {
            if (list[position].createdBy!!._id == logInUserId) {
                var isValidRole = false
                val validRoles = arrayListOf("STUDENT", "PARENT", "TEACHER")
                for (role in validRoles) {
                    if (userRole == role) {
                        isValidRole = true
                        break
                    }
                }
                if (isValidRole) {
                    holder.binding.name.text = "You"
                } else {
                    holder.binding.name.text = "${list[position].createdBy!!.firstName ?: ""} ${list[position].createdBy!!.lastName ?: ""}"
                }
            } else {
                holder.binding.name.text = "${list[position].createdBy!!.firstName ?: ""} ${list[position].createdBy!!.lastName ?: ""}"
            }
        }
        else {
            holder.binding.name.text = "--/--"
        }*/
        if (list[position].createdBy != null) {
            if (list[position].createdBy!!._id == logInUserId) {
                if (userRole in arrayListOf("STUDENT", "PARENT", "TEACHER")) {
                    holder.binding.name.text = "You"
                } else {
                    holder.binding.name.text = "${list[position].createdBy!!.firstName ?: ""} ${list[position].createdBy!!.lastName ?: ""}"
                }
            } else {
                holder.binding.name.text = "${list[position].createdBy!!.firstName ?: ""} ${list[position].createdBy!!.lastName ?: ""}"
            }
        } else {
            holder.binding.name.text = "--/--"
        }

        if (list[position].createdBy != null && list[position].createdBy!!.email != null) {
            holder.binding.txtEmail1.text = list[position].createdBy!!.email
        } else {
            holder.binding.txtEmail1.text = "--/--"
        }
        if (list[position].studentDetails != null && list[position].studentDetails!!.standard != null && list[position].studentDetails!!.standard!!.name != null) {
            holder.binding.std.text = UiUtils.getOrdinalSuffix(list[position].studentDetails!!.standard!!.name!!.toInt()) + " Standard | Roll No: 29"

        } else {
            holder.binding.std.text = "--/--"
        }

        if (list[position].createdAt != null){
            holder.binding!!.createAt.text = BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            holder.binding!!.createAt.text = "--/--"
        }
        if (list[position].toWhom != null && list[position].toWhom!!.name != null) {
            holder.binding.whomName.text = list[position].toWhom!!.name!!
        } else {
            holder.binding.whomName.text = "--/--"
        }
        when (list[position].status) {
            "solved" -> {
                holder.binding!!.status.text = "Solved"
                UiUtils.textviewCustomDrawable(holder.binding!!.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding!!.status, "#E5F8ED", null)
                UiUtils.textViewTextColor(holder.binding!!.status, "#32B138", null)
                // UiUtils.viewBgTint(holder.binding!!.dashView, "#50FB32", null)
                // UiUtils.linearLayoutBgDrawable(holder.binding!!.complaintLay, R.drawable.border_line_curve_8dp_green)
            }
            "unsolved" -> {
                holder.binding!!.status.text = "Unsolved"
                UiUtils.textviewCustomDrawable(holder.binding!!.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(holder.binding!!.status, "#FFEBEB", null)
                UiUtils.textViewTextColor(holder.binding!!.status, "#EA5455", null)
                //   UiUtils.viewBgTint(holder.binding!!.dashView, "#F39519", null)
                // UiUtils.linearLayoutBgDrawable(holder.binding!!.complaintLay, R.drawable.border_line_curve_8dp_orange)
            }
        }
        holder.binding!!.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY, list[position]._id)
            onClickListener.onClickItem(position)
        }
    }
}
