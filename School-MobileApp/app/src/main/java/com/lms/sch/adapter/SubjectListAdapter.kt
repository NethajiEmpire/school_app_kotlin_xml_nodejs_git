package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.databinding.ItemTextTabBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.DropdownResponse
import com.lms.sch.utils.UiUtils

class SubjectListAdapter(
    val mActivity: SubjectWiseProgressActivity,
    val list: ArrayList<DropdownResponse.Result>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<SubjectListAdapter.ViewHolder>()  {
    var selectedPosition = -1
    fun clearSelection() {
        val oldPosition = selectedPosition
        selectedPosition = -1
        if (oldPosition >= 0) notifyItemChanged(oldPosition)
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: ItemTextTabBinding = ItemTextTabBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.item_text_tab,parent,false)
        )
    }
    override fun getItemCount(): Int{
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tab1.text = list[position].label
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
            UiUtils.textviewCustomDrawable(mActivity.binding.tab1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(mActivity.binding.tab1, null, R.color.black_varient6)
            onClickListener.onClickItem(position)
            holder.binding.root.post {
                if (previousPosition >= 0) notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
            }
        }
    }

}