package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.SubjectNamesColorBinding
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.SubjectWiseClassExamProResponse
import com.lms.sch.utils.UiUtils

class SubjectsNameAdapter  (
    private val mActivity: BaseActivity,
    private var list: ArrayList<GetStudentClassTestProgress.Result>,
    private val colors: List<Int>
) : RecyclerView.Adapter<SubjectsNameAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: SubjectNamesColorBinding = SubjectNamesColorBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.subject_names_color, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].subject != null){
            holder.binding.subjectName.text = list[position].subject!!

        }
        else {
            holder.binding.subjectName.text = "--/__"
        }

        val colorHex = colors.getOrNull(position)?.let { String.format("#%08X", it) }
        UiUtils.imageViewTint(holder.binding.dot, colorHex, null)
    }
    override fun getItemCount(): Int = list.size

}