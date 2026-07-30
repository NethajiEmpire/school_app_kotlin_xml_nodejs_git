package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAdminTimeTableBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.AdminTimeTable
import com.lms.sch.response.GetTeacherProgramResponse
import com.lms.sch.response.GetTimeTableResponse
import com.lms.sch.response.ProgramResponse
import com.lms.sch.utils.UiUtils

class AdminTimeTableAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<ProgramResponse.Rows>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<AdminTimeTableAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardAdminTimeTableBinding = CardAdminTimeTableBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_admin_time_table, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].studentClass != null && list[position].section != null){
            holder.binding.standard.text = "${UiUtils.getOrdinalSuffix(list[position].studentClass!!.name!!.toInt())} - ${list[position].section!!.name} Sec"
        }
        else {
            holder.binding.standard.text = "--/--"
        }
        if (list[position].batch != null){
            holder.binding.batch.text = list[position].batch!!.name
        }
        else {
            holder.binding.batch.text = "--/--"
        }
        if (list[position].programUniqueId != null){
            holder.binding.program.text = list[position].programUniqueId
        }
        else {
            holder.binding.program.text = "--/--"
        }
        if (list[position].classTeacher != null){
            holder.binding.incharge.text = list[position].classTeacher!!.firstName + " " + list[position].classTeacher!!.lastName
        }
        else {
            holder.binding.incharge.text = "--/--"
        }
        if (list[position].subjects != null && list[position].subjects!!.isNotEmpty()){
            holder.binding.subjects.text = "${list[position].subjects!!.size} Subjects"
        }
        else {
            holder.binding.incharge.text = "0 Subjects"
        }

        val repeatedPosition = position % 12
        when(repeatedPosition) {
            0 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#E7EFF4", null) // Light Blue
                UiUtils.cardViewBgTint(holder.binding.card, "#00355C", null)     // Dark Blue
            }
            1 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#FEF5E9", null) // Light Orange
                UiUtils.cardViewBgTint(holder.binding.card, "#FFA93C", null)    // Dark Orange
            }
            2 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#E5ECFF", null) // Light Lavender
                UiUtils.cardViewBgTint(holder.binding.card, "#799BFF", null)    // Dark Lavender
            }
            3 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#E4EEE5", null) // Light Green
                UiUtils.cardViewBgTint(holder.binding.card, "#109F24", null)    // Dark Green
            }
            4 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#FDECF9", null) // Light Pink
                UiUtils.cardViewBgTint(holder.binding.card, "#EE73CF", null)    // Dark Pink
            }
            5 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#E9EFFD", null) // Light Navy
                UiUtils.cardViewBgTint(holder.binding.card, "#002EB4", null)    // Dark Navy
            }
            6 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#EAF4F4", null) // Light Teal
                UiUtils.cardViewBgTint(holder.binding.card, "#0097A3", null)    // Dark Teal
            }
            7 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#F1EEFC", null) // Light Purple
                UiUtils.cardViewBgTint(holder.binding.card, "#9588D1", null)    // Dark Purple
            }
            8 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#FFF7E6", null) // Light Yellow
                UiUtils.cardViewBgTint(holder.binding.card, "#FFD700", null)    // Dark Yellow
            }
            9 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#F4E9F9", null) // Light Mauve
                UiUtils.cardViewBgTint(holder.binding.card, "#8A2BE2", null)    // Dark Mauve
            }
            10 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#F3F7EB", null) // Light Olive
                UiUtils.cardViewBgTint(holder.binding.card, "#556B2F", null)    // Dark Olive
            }
            11 -> {
                UiUtils.textViewBgTint(holder.binding.subjects, "#FDF5F4", null) // Light Coral
                UiUtils.cardViewBgTint(holder.binding.card, "#FF4500", null)    // Dark Coral
            }
        }



        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }

    }
}