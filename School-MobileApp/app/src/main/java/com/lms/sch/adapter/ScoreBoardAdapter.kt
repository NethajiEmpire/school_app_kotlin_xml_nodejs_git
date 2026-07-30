package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardRankBinding
import com.lms.sch.response.GetScoreboardResponse
import com.lms.sch.utils.UiUtils

class ScoreBoardAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<GetScoreboardResponse.Result>
) : RecyclerView.Adapter<ScoreBoardAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding: CardRankBinding = CardRankBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.card_rank,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            UiUtils.constraintLayoutBgDrawable(holder.binding.drb,R.drawable.rank_one_drb)
            if (list[position].firstName != null && list[position].lastName != null) {
                holder.binding.stdName.text ="${list[position].firstName} ${list[position].lastName}"
            } else {
                holder.binding.stdName.text = "--/--"
            }
            if (list[position].points != null) {
                holder.binding.stdMarks.text = list[position].points.toString()
            } else {
                holder.binding.stdMarks.text = "--/--"
            }
            if (list[position].rank != null) {
                holder.binding.rank.text = "${list[position].rank.toString()} Rank"
            } else {
                holder.binding.rank.text = "--/--"
            }
            if (list[position].img_url != null && list[position].img_url!!.isNotEmpty()) {
                UiUtils.loadImage(holder.binding.profile, list[position].img_url.toString())
            }else{
                "--/--"
            }

            /*if (list[position].imgUrl != null) {
                UiUtils.textviewImgDrawable( holder.binding.studentImg, list[position].imgUrl!!.toInt()!!,"")
            } else {
                UiUtils.textviewImgDrawable(holder.binding.studentImg, R.drawable.ic_user_profile,"")
            }*/

        /*else if (list[position].rank == 2) {
            UiUtils.constraintLayoutBgDrawable(holder.binding.drb,R.drawable.rank_two_student)
            if (list[position].firstName != null && list[position].lastName != null) {
                holder.binding.stdName.text =
                    "${list[position].firstName} ${list[position].lastName}"
            } else {
                holder.binding.stdName.text = "--/--"
            }
            if (list[position].points != null) {
                holder.binding.stdMarks.text = list[position].points.toString()
            } else {
                holder.binding.stdMarks.text = "--/--"
            }
            if (list[position].rank != null) {
                holder.binding.rank.text = "${list[position].rank.toString()} Rank"
            } else {
                holder.binding.rank.text = "--/--"
            }
            if (list[position].imgUrl != null) {
                UiUtils.imageviewDrawable( holder.binding.studentImg, list[position].imgUrl!!.toInt() )
            } else {
                UiUtils.imageviewDrawable(holder.binding.studentImg, R.drawable.ic_user_profile)
            }
        }*/
       /* else{
            UiUtils.constraintLayoutBgDrawable(holder.binding.drb,R.drawable.border_curve_8dp)
            UiUtils.constraintLayoutBgColor(holder.binding.drb,"#F0F6FF",null)
            if (list[position].firstName != null && list[position].lastName != null) {
                holder.binding.stdName.text =
                    "${list[position].firstName} ${list[position].lastName}"
            } else {
                holder.binding.stdName.text = "--/--"
            }
            if (list[position].points != null) {
                holder.binding.stdMarks.text = list[position].points.toString()
            } else {
                holder.binding.stdMarks.text = "--/--"
            }
            if (list[position].rank != null) {
                holder.binding.rank.text = "${list[position].rank.toString()} Rank"
            } else {
                holder.binding.rank.text = "--/--"
            }
            *//*if (list[position].imgUrl != null) {
                UiUtils.imageviewDrawable(holder.binding.studentImg,list[position].imgUrl!!.toInt())
            } else {
                UiUtils.imageviewDrawable(holder.binding.studentImg, R.drawable.ic_user_profile)
            }*//*

        }*/
    }
}