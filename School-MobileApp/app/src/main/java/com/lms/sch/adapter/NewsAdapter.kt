package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.databinding.CardMonthlyAnouncementBinding
import com.lms.sch.models.NewsModel
import com.lms.sch.utils.UiUtils

class NewsAdapter(
    private val list: ArrayList<NewsModel>,
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding : CardMonthlyAnouncementBinding = CardMonthlyAnouncementBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_monthly_anouncement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].title.isEmpty()){
            holder.binding.card.visibility = View.INVISIBLE
        }
        else {
            holder.binding.card.visibility = View.VISIBLE
        }
        holder.binding.date.text = list[position].date
        holder.binding.day.text = list[position].day
        holder.binding.txtTitle.text = list[position].title
        holder.binding.desc.text = list[position].desc
        holder.binding.status.text = list[position].event

        when (list[position].event){
            "Assembly" -> {
                UiUtils.textViewBgTint(holder.binding.status, "#DAE7FF",null)
                UiUtils.textViewTextColor(holder.binding.status, "#3F8BFB",null)
                UiUtils.constraintLayoutBgDrawable(holder.binding.card,R.drawable.border_line_curve_10dp_events_blue)
            }
            "Holiday" -> {
                UiUtils.textViewBgTint(holder.binding.status, "#CFFFD1",null)
                UiUtils.textViewTextColor(holder.binding.status, "#32B138",null)
                UiUtils.constraintLayoutBgDrawable(holder.binding.card,R.drawable.border_line_curve_10dp_events_green)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}