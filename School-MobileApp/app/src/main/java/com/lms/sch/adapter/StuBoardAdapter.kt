package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.AssignmentActivity
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.databinding.ItemTextTabBinding
import com.lms.sch.fragment.FinanceFragment
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StuBoardAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<StudentBoardResponse.Result>,
    val onClickListener: OnClickListener) : RecyclerView.Adapter<StuBoardAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            var binding : ItemTextTabBinding = ItemTextTabBinding.bind(view)
        }
    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_text_tab,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tab1.text = list[position].name
        if (selectedPosition == position){
            UiUtils.textviewCustomDrawable(holder.binding.tab1,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(holder.binding.tab1,null,R.color.colorPrimary)
        }
        else {
            UiUtils.textviewCustomDrawable(holder.binding.tab1,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(holder.binding.tab1,null,R.color.black_varient6)
        }
        holder.binding.root.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            onClickListener.onClickItem(position)
            holder.binding.root.post {
                if (previousPosition >= 0) notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
            }
        }
    }
}