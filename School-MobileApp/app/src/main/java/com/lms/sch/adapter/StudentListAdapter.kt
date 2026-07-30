package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardExamStudentListBinding
import com.lms.sch.response.StudentExamRes
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.utils.UiUtils

class StudentListAdapter  (
    val mActivity: BaseActivity,
    val list: ArrayList<StudentExamRes.Result.Row>
) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {
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
        if (list[position].student!! != null && list[position].student!!.imgUrl != null){
            UiUtils.loadImage(holder.binding.studentImg1,list[position].student!!.imgUrl )
        }else{

        }
        if (list[position].noDueStatus != null){
            if (list[position].noDueStatus == "pending"){
                holder.binding.noDue.text = "On Due"
                holder.binding.status.text = "Not Eligible"
                UiUtils.textViewTextColor(holder.binding.noDue,"#FF6347",null)
            }else{
                holder.binding.noDue.text = "No Due"
                holder.binding.status.text = "Eligible"
                UiUtils.textViewTextColor(holder.binding.noDue,"#008000",null)

            }
        }
        else{
            holder.binding.noDue.text = "No Due"
            holder.binding.status.text = "Eligible"
        }

    }
}