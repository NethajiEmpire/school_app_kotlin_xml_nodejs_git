package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.AdminTimeTableActivity
import com.lms.sch.databinding.CardTextFilterBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.BatchDropdownResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.utils.UiUtils

class TimetableFilterAdapter (
    var mActivity: AdminTimeTableActivity,
    var selected : String,
    var batchList: ArrayList<BatchDropdownResponse.Result>,
    var classList: ArrayList<DropdownResponse.Result>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TimetableFilterAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardTextFilterBinding = CardTextFilterBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_text_filter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (selected == "batch"){
            batchList.size
        }
        else if (selected == "class") {
            classList.size
        }
        else {
            batchList.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (selected == "batch"){
            if (position >= 0 && position < batchList.size) {
                holder.binding.name.text = batchList[position].label
            } else {
                holder.binding.name.text = "Invalid Data"
            }
            if (mActivity.selectedPos == position && mActivity.batchName == batchList[position].label){
                UiUtils.textviewImgDrawable(holder.binding.name,R.drawable.hugeicons_tick,"start")
            }
            else {
                UiUtils.textviewImgDrawable(holder.binding.name,null,"start")
            }
        }
        else if (selected == "classes"){
            if (position >= 0 && position < classList.size) {
                holder.binding.name.text = UiUtils.getOrdinalSuffix(classList[position].label!!.toInt())
            } else {
                holder.binding.name.text = "Invalid Data"
            }
            val res = UiUtils.getOrdinalSuffix(classList[position].label!!.toInt())
            if (mActivity.selectedPos1 == position && mActivity.classname == res){
                UiUtils.textviewImgDrawable(holder.binding.name,R.drawable.hugeicons_tick,"start")
            }
            else {
                UiUtils.textviewImgDrawable(holder.binding.name,null,"start")
            }
        }

        if (position == batchList.size - 1){
            holder.binding.v1.visibility = View.GONE
        }
        else {
            holder.binding.v1.visibility = View.VISIBLE
        }

        holder.binding.root.setOnClickListener {
            when (selected) {
                "batch" -> {
                    val previousPos = mActivity.selectedPos
                    if (previousPos != RecyclerView.NO_POSITION && previousPos >= 0 && previousPos < batchList.size) {
                        notifyItemChanged(previousPos)
                    }

                    mActivity.selectedPos = position
                    notifyItemChanged(mActivity.selectedPos)
                    onClickListener.onClickItem(position)
                }
                "classes" -> {
                    val previousPos = mActivity.selectedPos1
                    if (previousPos != RecyclerView.NO_POSITION && previousPos >= 0 && previousPos < classList.size) {
                        notifyItemChanged(previousPos)
                    }

                    mActivity.selectedPos1 = position
                    notifyItemChanged(mActivity.selectedPos1)
                    onClickListener.onClickItem(position)
                }
            }
        }


    }
}