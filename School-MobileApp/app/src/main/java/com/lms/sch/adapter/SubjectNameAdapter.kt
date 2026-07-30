package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.SubjectNamesColorBinding
import com.lms.sch.response.SubjectWiseClassExamProResponse
import com.lms.sch.utils.UiUtils

class SubjectNameAdapter (
    private val mActivity: BaseActivity,
    private var list: ArrayList<SubjectWiseClassExamProResponse.Result>,
    private val colors: List<Int>
) : RecyclerView.Adapter<SubjectNameAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: SubjectNamesColorBinding = SubjectNamesColorBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.subject_names_color, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].subject != null){
            holder.binding.subjectName.text = list[position].subject!!

        }
        else {
            holder.binding.subjectName.text = "--/__"
        }

        val colorHex = colors.getOrNull(position)?.let { String.format("#%08X", it) }
        UiUtils.imageViewTint(holder.binding.dot, colorHex, null)
//        when (list[position].subject.toString()) {
//            "Computer" -> UiUtils.imageViewTint(holder.binding.dot, "#AEB8FE", null)
//            "Maths" -> UiUtils.imageViewTint(holder.binding.dot, "#FFB3BA", null)
//            "Science" -> UiUtils.imageViewTint(holder.binding.dot, "#FFE0A3", null)
//            "English" -> UiUtils.imageViewTint(holder.binding.dot, "#D1B3FF", null)
//            "Tamil" -> UiUtils.imageViewTint(holder.binding.dot, "#B2F2BB", null)
//            "Social" -> UiUtils.imageViewTint(holder.binding.dot, "#B3E5FC", null)
//            "Telugu" -> UiUtils.imageViewTint(holder.binding.dot, "#FFD180", null)
//            "Hindi" -> UiUtils.imageViewTint(holder.binding.dot, "#F8BBD0", null)
//            "GK" -> UiUtils.imageViewTint(holder.binding.dot, "#D1C4E9", null)
//            else -> UiUtils.imageViewTint(holder.binding.dot, "#CFD8DC", null)
//        }
    }
}