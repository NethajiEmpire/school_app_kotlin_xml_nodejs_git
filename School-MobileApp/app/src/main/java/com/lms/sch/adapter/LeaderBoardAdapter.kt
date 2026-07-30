package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardLeaderBoard1Binding
import com.lms.sch.response.GetLeaderboardResponse
import com.lms.sch.utils.UiUtils

class LeaderBoardAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<GetLeaderboardResponse.Result.Rows>
): RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardLeaderBoard1Binding = CardLeaderBoard1Binding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_leader_board1, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].rank != null){
            holder.binding.grade.text = "${list[position].grade} Grade"
        }
        else{
            holder.binding.grade.text = "--/--"
        }
        if (list[position].name != null) {
            holder.binding.name.text = list[position].name
        }
        else {
            holder.binding.name.text = "--/--"
        }
        if (list[position].scoredMark != null && list[position].totalMark != null) {
            holder.binding.marks.text = "${list[position].scoredMark} / ${list[position].totalMark} Marks"
        }
        else {
            holder.binding.marks.text = "--/--"
        }
        if (list[position].img_url != null && list[position].img_url!!.isNotEmpty()) {
            UiUtils.loadImage(holder.binding.profile, list[position].img_url)
        }
        when(list[position].grade){
            "A","A+" -> {
                UiUtils.constraintLayoutBgTint(holder.binding.top,"#F8FFF8",null)
                UiUtils.textViewTextColor(holder.binding.grade,"#32B138",null)
            }
            "B","B+" -> {
                UiUtils.constraintLayoutBgTint(holder.binding.top,"#F0F6FF",null)
                UiUtils.textViewTextColor(holder.binding.grade,"#3F8BFB",null)
            }
            "O" -> {
                UiUtils.constraintLayoutBgTint(holder.binding.top,"#FFF9ED",null)
                UiUtils.textViewTextColor(holder.binding.grade,"#FFAA00",null)
            }
            else -> {
                UiUtils.constraintLayoutBgColor(holder.binding.top,"#F0F6FF",null)
                UiUtils.textViewTextColor(holder.binding.grade,"#3F8BFB",null)
            }
        }
    }
}