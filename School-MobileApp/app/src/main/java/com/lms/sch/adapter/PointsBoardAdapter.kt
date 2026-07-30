package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardPointsHistoryBinding
import com.lms.sch.databinding.CardRankBinding
import com.lms.sch.response.GetScoreboardResponse
import com.lms.sch.response.PointsHistoryResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class PointsBoardAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<PointsHistoryResponse.Result.FinalList>
): RecyclerView.Adapter<PointsBoardAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding: CardPointsHistoryBinding = CardPointsHistoryBinding.bind(view)
    }
    override fun onCreateViewHolder(  parent: ViewGroup, viewType: Int ): ViewHolder {
        return ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.card_points_history,parent,false))
    }
    override fun onBindViewHolder(  holder: ViewHolder,  position: Int ) {
        if (list[position] != null){
            holder.binding.TypeOfWork.text = list[position].module ?: "--"
            holder.binding.givenDate.text = BaseUtils.getFormattedDate(list[position].Date?: "", Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            holder.binding.points.text = "+${list[position].credits?.toString()}" ?: "--"
            holder.binding.report.text = list[position].name ?: "--"
            when (list[position].module) {
                "assignment", "attendance","classTest" -> {
                    UiUtils.textviewCustomDrawable(holder.binding.TypeOfWork, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding.TypeOfWork, "#ECF3FF", null)
                    UiUtils.textViewTextColor(holder.binding.TypeOfWork, "#3F8BFB", null)
                }
                "project" -> {
                    UiUtils.textviewCustomDrawable(holder.binding.TypeOfWork, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding.TypeOfWork, "#FFF1E5", null)
                    UiUtils.textViewTextColor(holder.binding.TypeOfWork, "#FF7700", null)
                }
                "homework" -> {
                    UiUtils.textviewCustomDrawable(holder.binding.TypeOfWork, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding.TypeOfWork, "#FFF9E8", null)
                    UiUtils.textViewTextColor(holder.binding.TypeOfWork, "#FFBF00", null)
                }
            }
            when (list[position].credits.toString()) {
                "0" -> {
                    UiUtils.textViewTextColor(holder.binding.points, "#EA5455", null)
                    UiUtils.imageviewDrawable(holder.binding.greenDot, R.drawable.green_dot)
                    UiUtils.imageViewTint(holder.binding.greenDot,"#EA5455",null)
                }
                "5" -> {
                    UiUtils.textViewTextColor(holder.binding.points, "#32B138", null)
                    UiUtils.imageviewDrawable(holder.binding.greenDot, R.drawable.green_dot)
                }
                else -> {
                    UiUtils.textViewTextColor(holder.binding.points, "#32B138", null)
                    UiUtils.imageviewDrawable(holder.binding.greenDot, R.drawable.green_dot)
                }
            }
        }else{
            holder.binding.TypeOfWork.text = "--/--"
            holder.binding.givenDate.text = "--/--"
            holder.binding.points.text = "--/--"
            holder.binding.report.text = "--/--"
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}