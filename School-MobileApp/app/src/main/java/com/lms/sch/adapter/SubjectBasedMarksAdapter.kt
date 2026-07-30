package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardClassTestReportBinding
import com.lms.sch.databinding.CardMySubjectsBinding
import com.lms.sch.databinding.CardProjectwork360Binding
import com.lms.sch.databinding.CardProjectworkBinding
import com.lms.sch.response.GetClassTestResponse
import com.lms.sch.response.StudentProjectResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils

class SubjectBasedMarksAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<GetClassTestResponse.Result>
): RecyclerView.Adapter<SubjectBasedMarksAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: CardClassTestReportBinding = CardClassTestReportBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_class_test_report,parent,false)
        )
    }

    override fun getItemCount(): Int{
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position].subject != null){
            holder.binding.subName.text = list[position].subject!!.name
        }
        else{
            holder.binding.subName.text = "--/--"
        }
        if (list[position].scored_marks != null && list[position].classTest!!.totalMarks != null){
            holder.binding.marks.text = list[position].scored_marks!! + "/" + list[position].classTest!!.totalMarks
        }

    }

}