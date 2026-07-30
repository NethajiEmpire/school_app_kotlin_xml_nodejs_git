package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAcademicReport1Binding
import com.lms.sch.databinding.CardAcademicReportBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetOverAllProgressResponse
import com.lms.sch.utils.UiUtils

class OverAllProgressAdapter(
    var mActivity: BaseActivity,
    var type : Int,
    var list: ArrayList<GetOverAllProgressResponse.Result>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<OverAllProgressAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var binding: CardAcademicReportBinding
        lateinit var binding1: CardAcademicReport1Binding

        init {
            if (type == 1){
                binding = CardAcademicReportBinding.bind(view)
            }
            else {
                binding1 = CardAcademicReport1Binding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (type == 1){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_academic_report, parent, false)
            )
        }
        else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_academic_report1, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type == 1){
            if(list[position].type == "assignment"){
                UiUtils.linearLayoutBgDrawable(holder.binding.lin,R.drawable.bg_light_pink_gradient)
                UiUtils.imageviewDrawable(holder.binding.img1,R.drawable.ic_assignment_icon)
                UiUtils.imageviewDrawable(holder.binding.bgImg,R.drawable.layer_clt)
                UiUtils.textViewTextColor(holder.binding.type,"#FF43D8",null)
                UiUtils.textViewTextColor(holder.binding.totalTxt,"#FF43D8",null)
                holder.binding.type.text = "Assignment"
                holder.binding.totalTxt.text = "Submitted"

            }
            else if(list[position].type == "project"){
                UiUtils.linearLayoutBgDrawable(holder.binding.lin,R.drawable.bg_light_blue_gradient)
                UiUtils.imageviewDrawable(holder.binding.img1,R.drawable.ic_assignment_icon)
                UiUtils.imageviewDrawable(holder.binding.bgImg,R.drawable.layer_project)
                UiUtils.textViewTextColor(holder.binding.type,"#3F8BFB",null)
                UiUtils.textViewTextColor(holder.binding.totalTxt,"#3F8BFB",null)
                holder.binding.type.text = "Project"
                holder.binding.totalTxt.text = "Submitted"
            }
            else if(list[position].type == "homework"){
                UiUtils.linearLayoutBgDrawable(holder.binding.lin,R.drawable.bg_light_orange_gradient)
                UiUtils.imageviewDrawable(holder.binding.img1,R.drawable.ic_assignment_icon)
                UiUtils.imageviewDrawable(holder.binding.bgImg,R.drawable.layer_hw)
                UiUtils.textViewTextColor(holder.binding.type,"#FCA133",null)
                UiUtils.textViewTextColor(holder.binding.totalTxt,"#FCA133",null)
                holder.binding.type.text = "Homework"
                holder.binding.totalTxt.text = "Submitted"
            }
            else if(list[position].type == "classtest"){
                UiUtils.linearLayoutBgDrawable(holder.binding.lin,R.drawable.bg_light_green_gradient)
                UiUtils.imageviewDrawable(holder.binding.img1,R.drawable.ic_ct_icon)
                UiUtils.imageviewDrawable(holder.binding.bgImg,R.drawable.layer_clt)
                UiUtils.textViewTextColor(holder.binding.type,"#35C17F",null)
                UiUtils.textViewTextColor(holder.binding.totalTxt,"#35C17F",null)
                holder.binding.type.text = "Class Test"
                holder.binding.totalTxt.text = "Total Test"
            }
            else {
                UiUtils.linearLayoutBgDrawable(holder.binding.lin,R.drawable.bg_light_blue_gradient)
                UiUtils.imageviewDrawable(holder.binding.img1,R.drawable.ic_assignment_icon)
                UiUtils.imageviewDrawable(holder.binding.bgImg,R.drawable.layer_project)
                UiUtils.textViewTextColor(holder.binding.type,"#3F8BFB",null)
                UiUtils.textViewTextColor(holder.binding.totalTxt,"#3F8BFB",null)
                holder.binding.type.text = list[position].type
                holder.binding.totalTxt.text = "Submitted"
            }
            holder.binding.count.text = list[position].completed+"/"+list[position].total
            holder.binding.submitted.text = list[position].total
            holder.binding.percentage.text = list[position].percentage+"%"
            holder.binding.progress.progress = list[position].percentage!!.toInt()
        }
        else {
            if(list[position].type == "homework"){
                UiUtils.linearLayoutBgDrawable(holder.binding1.lin,R.drawable.border_line_gradient_curve_14dp_yellow)
                UiUtils.imageviewDrawable(holder.binding1.bgImg,R.drawable.stats1)
                holder.binding1.type.text = "Homework"
            }
            else if(list[position].type == "project"){
                UiUtils.linearLayoutBgDrawable(holder.binding1.lin,R.drawable.border_line_gradient_curve_14dp_red)
                UiUtils.imageviewDrawable(holder.binding1.bgImg,R.drawable.stats3)
                holder.binding1.type.text = "Project"
            }
            else if(list[position].type == "assignment"){
                UiUtils.linearLayoutBgDrawable(holder.binding1.lin,R.drawable.border_line_gradient_curve_14dp_blue)
                UiUtils.imageviewDrawable(holder.binding1.bgImg,R.drawable.stats4)
                holder.binding1.type.text = "Assignment"
            }
            else if(list[position].type == "classtest"){
                UiUtils.linearLayoutBgDrawable(holder.binding1.lin,R.drawable.border_line_gradient_curve_14dp_green)
                UiUtils.imageviewDrawable(holder.binding1.bgImg,R.drawable.stat2)
                holder.binding1.type.text = "Class Test"
            }
            else {
                UiUtils.linearLayoutBgDrawable(holder.binding1.lin,R.drawable.border_line_gradient_curve_14dp_red)
                UiUtils.imageviewDrawable(holder.binding1.bgImg,R.drawable.stats3)
                holder.binding1.type.text = list[position].type
            }
            holder.binding1.count.text = list[position].completed+"/"+list[position].total
            holder.binding1.percentage.text = list[position].percentage+"%"
            holder.binding1.progress.progress = list[position].percentage!!.toInt()
            holder.binding1.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
    }
}