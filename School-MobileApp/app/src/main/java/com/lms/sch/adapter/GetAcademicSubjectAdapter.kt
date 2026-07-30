package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAcademicSubjectBinding
import com.lms.sch.response.GetAcademicSubjectResponse
import com.lms.sch.utils.UiUtils

class GetAcademicSubjectAdapter(
    val mActivity: BaseActivity,
    var list: ArrayList<GetAcademicSubjectResponse.Result>,
) : RecyclerView.Adapter<GetAcademicSubjectAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardAcademicSubjectBinding = CardAcademicSubjectBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_academic_subject, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].subjectId != null) {
            holder.binding.subId.text = "Sub ID\n"+list[position].subjectId
        } else {
            holder.binding.subId.text = "--/--"
        }
        if (list[position].name != null) {
            holder.binding.subName.text = list[position].name
        } else {
            holder.binding.subName.text = "--/--"
        }
        if (list[position].teachers != null) {
            holder.binding.teacherCnt.text = ""+list[position].teachers!!
        } else {
            holder.binding.teacherCnt.text = "--/--"
        }
        if (list[position].description != null) {
            holder.binding.desc.setContent(list[position].description)
        } else {
            holder.binding.desc.setContent("--/--")
        }
        if (list[position].img_url != null && list[position].img_url!!.isNotEmpty()) {
            Glide.with(mActivity).load(list[position].img_url).into(holder.binding.img)
        }
        when (list[position].status) {
            "active" -> {
                holder.binding.status.text = "Active"
                UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#E5F8ED", null)
                UiUtils.textViewTextColor(holder.binding.status, "#28C76F", null)
            }
            "inactive" -> {
                holder.binding.status.text = "Inactive"
                UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status, "#FFF2DE", null)
                UiUtils.textViewTextColor(holder.binding.status, "#F39519", null)
            }

        }
    }

}
