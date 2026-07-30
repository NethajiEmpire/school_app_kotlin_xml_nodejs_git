package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardStudentHwCompletedBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetTeacherStdClsTestResponse
import com.lms.sch.utils.UiUtils

class StdClsTestResAdapter (
    val mActivity: BaseActivity,
    val isDairy : Boolean,
    val list : ArrayList<GetTeacherStdClsTestResponse.Result>,
    val onClickListener: OnClickListener
):
    RecyclerView.Adapter<StdClsTestResAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardStudentHwCompletedBinding = CardStudentHwCompletedBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_student_hw_completed, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].markStatus == "completed"){
            holder.binding.page1.visibility = View.VISIBLE
            holder.binding.page2.visibility = View.GONE
            if (list[position].student != null){
                holder.binding.stdName.text = list[position].student!!.firstName +" "+ list[position].student!!.lastName
            }
            else {
                holder.binding.stdName.text = "--/--"
            }
            if (list[position].student != null && list[position].student!!.img_url != null && list[position].student!!.img_url!!.isNotEmpty()){
                UiUtils.loadImage(holder.binding.studentImg,list[position].student!!.img_url)
            }
            when(list[position].status){
                "pending" -> {
                    holder.binding.status.text = "Pending"
                    UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.status,"#E85A5B",null)
                    UiUtils.textViewBgTint(holder.binding.status,"#FFEBEB",null)
                }
                "completed" -> {
                    holder.binding.status.text = "Completed"
                    UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewTextColor(holder.binding.status,"#28C76F",null)
                    UiUtils.textViewBgTint(holder.binding.status,"#EBFBF0",null)
                }
            }
        }
        else {
            holder.binding.page1.visibility = View.GONE
            holder.binding.page2.visibility = View.VISIBLE

            if (list[position].student != null){
                holder.binding.stdName1.text = list[position].student!!.firstName +" "+ list[position].student!!.lastName
            }
            else {
                holder.binding.stdName1.text = "--/--"
            }
            if (list[position].student != null && list[position].student!!.img_url != null && list[position].student!!.img_url!!.isNotEmpty()){
                UiUtils.loadImage(holder.binding.studentImg1,list[position].student!!.img_url)
            }
            when(list[position].status){
                "pending" -> {
                    UiUtils.textviewImgDrawable(holder.binding.pending,R.drawable.redbtn,"start")
                    UiUtils.textviewImgDrawable(holder.binding.completed,R.drawable.checkbox_white,"start")
                }
                "completed" -> {
                    UiUtils.textviewImgDrawable(holder.binding.pending,R.drawable.checkbox_white,"start")
                    UiUtils.textviewImgDrawable(holder.binding.completed,R.drawable.greenbtn,"start")
                }
            }
        }
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}