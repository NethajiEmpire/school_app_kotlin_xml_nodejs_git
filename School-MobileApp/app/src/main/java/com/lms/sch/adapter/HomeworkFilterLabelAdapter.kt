package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardStudentHwCompletedBinding
import com.lms.sch.databinding.CardTextFilterBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.utils.UiUtils

class HomeworkFilterLabelAdapter(
    val mActivity: BaseActivity,
    val list : ArrayList<String>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<HomeworkFilterLabelAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding : CardTextFilterBinding = CardTextFilterBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_text_filter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = list[position]
        /*if (list.size -1 == position){
            holder.binding.v1.visibility = View.GONE
        }
        else {
            holder.binding.v1.visibility = View.VISIBLE
        }*/
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}