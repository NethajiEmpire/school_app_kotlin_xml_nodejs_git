package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardExamStudentListBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.StudentExamResponse
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.utils.UiUtils

class StudentList2Adapter (
    val mActivity: BaseActivity,
    val list: ArrayList<StudentExamResultResponse.Rows>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<StudentList2Adapter.ViewHolder>() {
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
        } else {
            holder.binding.name.text = "--/--"
        }
        if (list[position].student!! != null && list[position].student!!.img_Url != null){
            UiUtils.loadImage(holder.binding.studentImg1,list[position].student!!.img_Url )
        }else{

        }
        if (list[position].status == "pass"){
            holder.binding.noDue.text = "Pass"
            UiUtils.textViewTextColor(holder.binding.noDue,null, R.color.green)
            holder.binding.status.text = "${list[position].scoredMark}/${list[position].totalMark}"
        }
        else if (list[position].status == "fail"){
            holder.binding.noDue.text = "Fail"
            UiUtils.textViewTextColor(holder.binding.noDue,null, R.color.red2)
            holder.binding.status.text = "${list[position].scoredMark}/${list[position].totalMark}"
        }
        else{
            holder.binding.noDue.text = "Update Mark"
            UiUtils.textViewTextColor(holder.binding.noDue,null, R.color.green)
            holder.binding.status.text = "${list[position].scoredMark}/${list[position].totalMark}"
        }
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}