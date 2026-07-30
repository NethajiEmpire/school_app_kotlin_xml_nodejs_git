package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.ExamFeesActivity
import com.lms.sch.databinding.CardExamStudentListBinding
import com.lms.sch.databinding.CardExaminationBinding
import com.lms.sch.response.GetExamResponse
import com.lms.sch.response.StudentExamResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ExamStudentListAdapter (
    val mActivity: BaseActivity,
    val list: ArrayList<StudentExamResponse.Row>
) : RecyclerView.Adapter<ExamStudentListAdapter.ViewHolder>() {
    inner class ViewHolder(view : View ): RecyclerView.ViewHolder(view){
        var  binding : CardExamStudentListBinding = CardExamStudentListBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_exam_student_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].student != null) {
            holder.binding.name.text = list[position].student!!.firstName+" "+list[position].student!!.lastName
            UiUtils.loadImage(holder.binding.studentImg1,list[position].student!!.img_url)
        } else {
            holder.binding.name.text = "--/--"
        }
    }
}