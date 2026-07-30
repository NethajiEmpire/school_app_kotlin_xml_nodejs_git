package com.lms.sch.adapter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.ImageViewActivity
import com.lms.sch.activity.PdfViewerActivity
import com.lms.sch.databinding.CardProjectwork360Binding
import com.lms.sch.databinding.CardProjectworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.StudentProjectResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentProjectAdapter(
    val mActivity: BaseActivity,
    val isDiary : Boolean,
    val list : ArrayList<StudentProjectResponse.Result>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<StudentProjectAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View,type : Int):RecyclerView.ViewHolder(view){
        var binding1 : CardProjectworkBinding ?= null
        var binding: CardProjectwork360Binding ?= null
        init {
            if (type == 0){
                binding = CardProjectwork360Binding.bind(view)
            }
            else {
                binding1 = CardProjectworkBinding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (isDiary){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_projectwork_360,parent,false),
                0
            )
        }
        else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_projectwork,parent,false),
                1
            )
        }

    }

    override fun getItemCount(): Int{
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isDiary){
            if (list[position].subject != null){
                holder.binding!!.subjectName.text = list[position].subject!!.name
            }
            else {
                holder.binding!!.subjectName.text = "--/--"
            }
            if (list[position].project != null){
                holder.binding!!.projectName.text = list[position].project!!.title
                holder.binding!!.desc.setContent(list[position].project!!.description)
                if (list[position].project!!.createdBy != null){
                    holder.binding!!.tname.text = "${list[position].project!!.createdBy!!.firstName ?: ""} ${list[position].project!!.createdBy!!.lastName}"
                }
                else {
                    holder.binding!!.tname.text = "--/--"
                }
            }
            else {
                holder.binding!!.subjectName.text = "--/--"
            }
            if (list[position].createdAt != null){
                holder.binding!!.givenDate.text = BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            }
            else{
                holder.binding!!.givenDate.text = "--/--"
            }
            if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
//                holder.binding!!.attach.text = "+${list[position].attachment!!.size}"
                UiUtils.textviewImgDrawable(holder.binding!!.attach,R.drawable.pdf,"end")
            }
            else{
                holder.binding!!.attach.text = "--None--"
                UiUtils.textviewImgDrawable(holder.binding!!.attach,null,"end")
            }
            holder.binding!!.attachLay.setOnClickListener {
                if (list[position].project != null && list[position].project!!.attachment != null && list[position].project!!.attachment!!.isNotEmpty()){
                    if (list[position].project!!.attachment!!.endsWith(".pdf")){
                        val bundle = Bundle()
                        bundle.putString(Constants.IntentKeys.KEY, list[position].project!!.attachment!!)
                        BaseUtils.startActivity(mActivity, PdfViewerActivity(),bundle,false)
                    }
                    else if (list[position].project!!.attachment!!.endsWith(".png") || list[position].project!!.attachment!!.endsWith(".jpeg") || list[position].project!!.attachment!!.endsWith(".jpg")){
                        val doc = ArrayList<String>()
                        doc.add(list[position].project!!.attachment!!)
                        val bundle = Bundle()
                        bundle.putSerializable(Constants.IntentKeys.KEY,doc)
                        BaseUtils.startActivity(mActivity, ImageViewActivity(),bundle,false)
                    }
                    else {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(list[position].project!!.attachment!!))
                        mActivity.startActivity(intent)
                    }
                }
                else {
                    UiUtils.showSnack("We are unable to fetch the Image",holder.binding!!.root,false)
                }
            }
            holder.binding!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }

            when(list[position].status){
                "ongoing" ->{
                    holder.binding!!.projectStatus.text = "Ongoing"
                    UiUtils.textviewCustomDrawable(holder.binding!!.projectStatus, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.projectStatus,null, R.color.light_orange_bg)
                    UiUtils.textViewTextColor(holder.binding!!.projectStatus,null, R.color.orange)
                    UiUtils.cardViewBgTint(holder.binding!!.card,"#F69300",null)

                    if (list[position].dueDate != null){
                        holder.binding!!.submitTxt.text = "Last Date"
                        holder.binding!!.submitDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding!!.submitDate.text = "--/--"
                    }
                }
                "overdue" ->{
                    holder.binding!!.projectStatus.text = "Not Completed"
                    UiUtils.textviewCustomDrawable(holder.binding!!.projectStatus, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.projectStatus,null, R.color.very_light_red)
                    UiUtils.textViewTextColor(holder.binding!!.projectStatus,null, R.color.red_hurry_up)
                    UiUtils.cardViewBgTint(holder.binding!!.card,"#EA5455",null)

                    if (list[position].dueDate != null){
                        holder.binding!!.submitTxt.text = "Last Date"
                        holder.binding!!.submitDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding!!.submitDate.text = "--/--"
                    }
                }
                "completed" ->{
                    holder.binding!!.projectStatus.text = "Completed"
                    UiUtils.textviewCustomDrawable(holder.binding!!.projectStatus, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.projectStatus,"#e6ffe7" ,null)
                    UiUtils.textViewTextColor(holder.binding!!.projectStatus,"#32B138",null)
                    UiUtils.cardViewBgTint(holder.binding!!.card,"#32B138",null)

                    if (list[position].submittedOn != null){
                        holder.binding!!.submitTxt.text = "Submitted On"
                        holder.binding!!.submitDate.text = BaseUtils.getFormattedDate(list[position].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding!!.submitDate.text = "--/--"
                    }
                }
            }
        }
        else {
            if (list[position].subject != null){
                holder.binding1!!.subjectName.text = list[position].subject!!.name
            }
            else {
                holder.binding1!!.subjectName.text = "--/--"
            }
            if (list[position].project != null){
                holder.binding1!!.projectName.text = list[position].project!!.title
                holder.binding1!!.desc.setContent(list[position].project!!.description)
                if (list[position].project!!.createdBy != null){
                    holder.binding1!!.tname.text = "${list[position].project!!.createdBy!!.firstName ?: ""} ${list[position].project!!.createdBy!!.lastName}"
                }
                else {
                    holder.binding1!!.tname.text = "--/--"
                }
            }
            else {
                holder.binding1!!.subjectName.text = "--/--"
            }
            if (list[position].createdAt != null){
                holder.binding1!!.givenDate.text = BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            }
            else{
                holder.binding1!!.givenDate.text = "--/--"
            }
            if (list[position].project!!.attachment != null && list[position].project!!.attachment!!.isNotEmpty()){
//                holder.binding1!!.attach.text = "+${list[position].project!!.attachment!!.size}"
                UiUtils.textviewImgDrawable(holder.binding1!!.attach,R.drawable.pdf,"end")
            }
            else{
                holder.binding1!!.attach.text = "--None--"
                UiUtils.textviewImgDrawable(holder.binding1!!.attach,null,"end")
            }
            holder.binding1!!.attachLay.setOnClickListener {
                if (list[position].project != null && list[position].project!!.attachment != null && list[position].project!!.attachment!!.isNotEmpty()){
                    if (list[position].project!!.attachment!!.endsWith(".pdf")){
                        val bundle = Bundle()
                        bundle.putString(Constants.IntentKeys.KEY, list[position].project!!.attachment!!)
                        BaseUtils.startActivity(mActivity, PdfViewerActivity(),bundle,false)
                    }
                    else if (list[position].project!!.attachment!!.endsWith(".png") || list[position].project!!.attachment!!.endsWith(".jpeg") || list[position].project!!.attachment!!.endsWith(".jpg")){
                        val doc = ArrayList<String>()
                        doc.add(list[position].project!!.attachment!!)
                        val bundle = Bundle()
                        bundle.putSerializable(Constants.IntentKeys.KEY,doc)
                        BaseUtils.startActivity(mActivity, ImageViewActivity(),bundle,false)
                    }
                    else {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(list[position].project!!.attachment!!))
                        mActivity.startActivity(intent)
                    }
                }
                else {
                    UiUtils.showSnack("We are unable to fetch the Image",holder.binding1!!.root,false)
                }
            }
            holder.binding1!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }

            when(list[position].status){
                "pending" ->{
                    holder.binding1!!.projectStatus.text = "Ongoing"
                    UiUtils.textviewCustomDrawable(holder.binding1!!.projectStatus, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding1!!.projectStatus,null, R.color.light_orange_bg)
                    UiUtils.textViewTextColor(holder.binding1!!.projectStatus,null, R.color.orange)
                    UiUtils.cardViewBgTint(holder.binding1!!.card,"#F69300",null)

                    if (list[position].dueDate != null){
                        holder.binding1!!.submitTxt.text = "Last Date"
                        holder.binding1!!.submitDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding1!!.submitDate.text = "--/--"
                    }
                }
                "overdue" ->{
                    holder.binding1!!.projectStatus.text = "Not Completed"
                    UiUtils.textviewCustomDrawable(holder.binding1!!.projectStatus, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding1!!.projectStatus,null, R.color.very_light_red)
                    UiUtils.textViewTextColor(holder.binding1!!.projectStatus,null, R.color.red_hurry_up)
                    UiUtils.cardViewBgTint(holder.binding1!!.card,"#EA5455",null)

                    if (list[position].dueDate != null){
                        holder.binding1!!.submitTxt.text = "Last Date"
                        holder.binding1!!.submitDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding1!!.submitDate.text = "--/--"
                    }
                }
                "completed" ->{
                    holder.binding1!!.projectStatus.text = "Completed"
                    UiUtils.textviewCustomDrawable(holder.binding1!!.projectStatus, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding1!!.projectStatus,"#e6ffe7" ,null)
                    UiUtils.textViewTextColor(holder.binding1!!.projectStatus,"#32B138",null)
                    UiUtils.cardViewBgTint(holder.binding1!!.card,"#32B138",null)

                    if (list[position].submittedOn != null){
                        holder.binding1!!.submitTxt.text = "Submitted On"
                        holder.binding1!!.submitDate.text = BaseUtils.getFormattedDate(list[position].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding1!!.submitDate.text = "--/--"
                    }
                }
            }
        }

    }

}