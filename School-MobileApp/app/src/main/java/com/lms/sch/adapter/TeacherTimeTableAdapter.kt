package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardClassTimetableBinding
import com.lms.sch.databinding.CardNewtimetableBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.TeacherTimeTableResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils
import kotlin.text.capitalize

class TeacherTimeTableAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<TeacherTimeTableResponse.Result.Periods>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TeacherTimeTableAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding : CardClassTimetableBinding = CardClassTimetableBinding.bind(view)
    }
    override fun onCreateViewHolder(  parent: ViewGroup,    viewType: Int  ): ViewHolder {
        return ViewHolder(
        LayoutInflater.from(mActivity).inflate(R.layout.card_class_timetable, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val isSubjectOrFree = (item.subject != null && item.subject!!.name!!.isNotEmpty()) || item.type == "free"

        if (isSubjectOrFree) {
            holder.binding.weekLay.visibility = View.GONE
            holder.binding.timetableLay.visibility = View.VISIBLE

            val periodNumber = getPeriodNumber(position)
            holder.binding.no.text = "$periodNumber"

            if (item.type == "free") {
                holder.binding.subject.text = "Free"
                holder.binding.teacher.text = "Period"
                UiUtils.linearLayoutBgTint(holder.binding.linBg, "#f7f7f7", null)
                UiUtils.cardViewBgTint(holder.binding.card, "#e0e0e0", null)
            } else {
                holder.binding.subject.text =
                    "${UiUtils.getOrdinalSuffix(item.studentClass!!.toInt())} - ${item.section} Sec"
                holder.binding.teacher.text = item.subject?.name

                when (item.subject?.name) {
                    "General Knowledge" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#FCE8FF", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#DF3FFB", null)
                    }
                    "English" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#F0F6FF", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#3F8BFB", null)
                    }
                    "Maths" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#E0FFEE", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#6BE9A3", null)
                    }
                    "Science" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#FFFDD4", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#E9E36B", null)
                    }
                    "Hindi" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#F9F9F9", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#A9A8DA", null)
                    }
                    "Computer" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#FFEEF1", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#E96B84", null)
                    }
                    "Tamil" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#FFF5EB", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#F6891E", null)
                    }
                    "Social Science" -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#DCF5FF", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#6BC5E9", null)
                    }
                    else -> {
                        UiUtils.linearLayoutBgTint(holder.binding.linBg, "#DCF5FF", null)
                        UiUtils.cardViewBgTint(holder.binding.card, "#6BC5E9", null)
                    }
                }
            }

        } else {
            holder.binding.weekLay.visibility = View.VISIBLE
            holder.binding.timetableLay.visibility = View.GONE
            holder.binding.breakTxt.text = item.type!!.capitalize()
        }
        if (item.startTime != null) {
            val formatted = BaseUtils.formatTime(item.startTime!!)
            holder.binding.startTime.text = formatted
            holder.binding.startTime1.text = formatted
        } else {
            holder.binding.startTime.text = ""
            holder.binding.startTime1.text = ""
        }

        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
    private fun getPeriodNumber(position: Int): Int {
        var count = 1
        for (i in 0 until position) {
            val item = list[i]
            if ((item.subject != null && item.subject!!.name!!.isNotEmpty()) || item.type == "free") {
                count++
            }
        }
        return count
    }
    override fun getItemCount(): Int {
       return list.size
    }
}