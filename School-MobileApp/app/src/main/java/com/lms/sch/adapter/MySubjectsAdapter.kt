package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardMyClassBinding
import com.lms.sch.databinding.CardMySubjectsBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetTeacherProgramResponse
import com.lms.sch.utils.UiUtils

class MySubjectsAdapter(
    val mActivity: BaseActivity,
    val list : ArrayList<GetTeacherProgramResponse.Result>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<MySubjectsAdapter.ViewHolder>()  {
    var selectedPosition = -1
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: CardMyClassBinding = CardMyClassBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_my_class,parent,false)
        )
    }

    override fun getItemCount(): Int{
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].myClass == true) {
            holder.binding.myclass.visibility  = View.VISIBLE

        } else {
            holder.binding.myclass.visibility  = View.INVISIBLE
        }
        if (list[position].subject != null && list[position].subject!!.subjectId != null){
            when(list[position].standard.toString()){
                "1" -> {
                    holder.binding.totalStd.text = "${list[position].standard}st -${list[position].section} Sec / ${list[position].subject!!.subjectId!!.name}"
                }
                "2" -> {
                    holder.binding.totalStd.text = "${list[position].standard}nd -${list[position].section} Sec / ${list[position].subject!!.subjectId!!.name}"
                }
                "3" -> {
                    holder.binding.totalStd.text = "${list[position].standard}rd -${list[position].section} Sec / ${list[position].subject!!.subjectId!!.name}"
                }
                else -> {
                    holder.binding.totalStd.text = "${list[position].standard}th -${list[position].section} Sec / ${list[position].subject!!.subjectId!!.name}"
                }
            }
        }
        else {
            holder.binding.totalStd.text = "--.--"
        }
        if (selectedPosition == position){
            UiUtils.linearLayoutBgDrawable(holder.binding.mysub,R.drawable.border_line_curve_4dp_primary)
        }
        else {
            UiUtils.linearLayoutBgDrawable(holder.binding.mysub,R.drawable.border_line_curve_4dp_gray)
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
        if (position == 0 && selectedPosition == -1) {
            holder.binding.root.post {
                holder.binding.root.performClick()
            }
        }
    }
}