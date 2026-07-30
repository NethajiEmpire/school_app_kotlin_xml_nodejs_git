package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.AdminTimeTableActivity
import com.lms.sch.activity.LeaderBoardActivity
import com.lms.sch.databinding.CardTextFilterBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.BatchDropdownResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.utils.UiUtils

class LeaderboardFilterAdapter(
    var mActivity: LeaderBoardActivity,
    var examList: ArrayList<String>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<LeaderboardFilterAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardTextFilterBinding = CardTextFilterBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_text_filter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return examList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        UiUtils.log("iugf",""+examList[position])
        if (position >= 0 && position < examList.size) {
            UiUtils.log("iugf",""+examList[position])
            holder.binding.name.text = examList[position]
        } else {
            holder.binding.name.text = "Invalid Data"
        }
        if (mActivity.selectedPos == position && mActivity.examName == examList[position]){
            UiUtils.textviewImgDrawable(holder.binding.name,R.drawable.hugeicons_tick,"start")
        }
        else {
            UiUtils.textviewImgDrawable(holder.binding.name,null,"start")
        }

        if (position == examList.size - 1){
            holder.binding.v1.visibility = View.GONE
        }
        else {
            holder.binding.v1.visibility = View.VISIBLE
        }

        holder.binding.root.setOnClickListener {
            val previousPos = mActivity.selectedPos
            if (previousPos != RecyclerView.NO_POSITION && previousPos >= 0 && previousPos < examList.size) {
                notifyItemChanged(previousPos)
            }
            mActivity.selectedPos = position
            notifyItemChanged(mActivity.selectedPos)
            onClickListener.onClickItem(position)
        }


    }
}