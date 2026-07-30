package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.SubjectNamesColorBinding
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.utils.UiUtils

class GetSubjectNameColorAdapter(
    private val mActivity: BaseActivity,
    private var list: ArrayList<GetStudentClassTestProgress.Result>,
    private val subjectColorMap: Map<String, String>
) : RecyclerView.Adapter<GetSubjectNameColorAdapter.ViewHolder>() {

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
        else{
            holder.binding.subjectName.text = "--/__"
        }

        val subjectName = list[position].subject?.trim() ?: ""
        val colorHex = subjectColorMap[subjectName] ?: "#607D8B"
        UiUtils.imageViewTint(holder.binding.dot, colorHex, null)
    }
}




/*package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.SubjectNamesColorBinding
import com.lms.sch.utils.UiUtils

class GetSubjectNameColorAdapter(
    private val mActivity: BaseActivity,
    private var list: ArrayList<String>
) : RecyclerView.Adapter<GetSubjectNameColorAdapter.ViewHolder>() {

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
        val subject = list[position]
        holder.binding.subjectName.text = subject ?: "--/--"

        when (subject) {
            "Computer" -> UiUtils.imageViewTint(holder.binding.dot, "#3B3BBF", null)
            "Maths" -> UiUtils.imageViewTint(holder.binding.dot, "#F85F73", null)
            "Science" -> UiUtils.imageViewTint(holder.binding.dot, "#F9B233", null)
            "English" -> UiUtils.imageViewTint(holder.binding.dot, "#9C27B0", null)
            "Tamil" -> UiUtils.imageViewTint(holder.binding.dot, "#4CAF50", null)
            "Social" -> UiUtils.imageViewTint(holder.binding.dot, "#2196F3", null)
            "Telugu" -> UiUtils.imageViewTint(holder.binding.dot, "#E91E63", null)
            "Hindi" -> UiUtils.imageViewTint(holder.binding.dot, "#673AB7", null)
            "GK" -> UiUtils.imageViewTint(holder.binding.dot, "#FF9800", null)
            else -> UiUtils.imageViewTint(holder.binding.dot, "#607D8B", null)
        }
    }
}*/
