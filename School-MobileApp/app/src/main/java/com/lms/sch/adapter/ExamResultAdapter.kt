package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardExamResultBinding
import com.lms.sch.databinding.CardExamStudentListBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.session.SharedHelper

class ExamResultAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<StudentExamResultResponse.Rows>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<ExamResultAdapter.ViewHolder>() {
    inner class ViewHolder(view : View ): RecyclerView.ViewHolder(view){
        var  binding : CardExamResultBinding = CardExamResultBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_exam_result, parent, false)
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
        if (list[position].status == "pending"){
            holder.binding.markUpdate.visibility = View.GONE
            holder.binding.markPending.visibility = View.VISIBLE
        }
        else{
            holder.binding.markUpdate.visibility = View.VISIBLE
            holder.binding.markPending.visibility = View.GONE
            if (list[position].grade != null){
                holder.binding.grade.text = list[position].grade!!.name
            }
            holder.binding.mark.text = list[position].scoredMark+"/"+list[position].totalMark
        }
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}