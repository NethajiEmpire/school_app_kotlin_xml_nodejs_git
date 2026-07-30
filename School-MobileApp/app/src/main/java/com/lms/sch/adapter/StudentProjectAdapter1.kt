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
import com.lms.sch.activity.HomeWorkResultActivity
import com.lms.sch.activity.ImageViewActivity
import com.lms.sch.activity.PdfViewerActivity
import com.lms.sch.activity.ProjectResActivity
import com.lms.sch.databinding.CardProject1Binding
import com.lms.sch.databinding.CardProjectwork360Binding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.StudentProjectResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentProjectAdapter1(
    val mActivity: BaseActivity,
    val isDiary : Boolean,
    val list : ArrayList<StudentProjectResponse.Result>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<StudentProjectAdapter1.ViewHolder>()  {

    inner class ViewHolder(view: View,type : Int):RecyclerView.ViewHolder(view){
        var binding1 : CardProject1Binding ?= null
        var binding: CardProjectwork360Binding ?= null
        init {
            if (type == 0){
                binding = CardProjectwork360Binding.bind(view)
            }
            else {
                binding1 = CardProject1Binding.bind(view)
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
                LayoutInflater.from(mActivity).inflate(R.layout.card_project1,parent,false),
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
                UiUtils.textviewImgDrawable(holder.binding!!.attach,R.drawable.pdf,"end")
            }
            else{
                holder.binding!!.attach.text = "--None--"
                UiUtils.textviewImgDrawable(holder.binding!!.attach,null,"end")
            }
            holder.binding!!.attachLay.setOnClickListener {
                if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
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
                holder.binding1!!.subject.text = list[position].subject!!.name
                when(list[position].subject!!.name){
                    "General Knowledge" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#b81f3b",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#b81f3b",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#b81f3b",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#b81f3b",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#b81f3b",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#b81f3b",null)
                    }
                    "English" ->{
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#3F8BFB",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#3F8BFB",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#3F8BFB",null)
                    }
                    "Maths" ->{
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#45a9d1",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#45a9d1",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#45a9d1",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#45a9d1",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#45a9d1",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#45a9d1",null)
                    }
                    "Science" ->{
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#E9E36B",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#E9E36B",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#E9E36B",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#E9E36B",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#E9E36B",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#E9E36B",null)
                    }
                    "Hindi" ->{
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#A9A8DA",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#A9A8DA",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#A9A8DA",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#A9A8DA",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#A9A8DA",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#A9A8DA",null)
                    }
                    "Computer" ->{
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#E96B84",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#E96B84",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#E96B84",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#E96B84",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#E96B84",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#E96B84",null)
                    }
                    "Tamil" ->{
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#F6891E",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#F6891E",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#F6891E",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#F6891E",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#F6891E",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#F6891E",null)
                    }
                    "Social Science" ->{
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#6BC5E9",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#6BC5E9",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#6BC5E9",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#6BC5E9",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#6BC5E9",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#6BC5E9",null)
                    }
                    else -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc,"#3F8BFB",null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft,"#3F8BFB",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject,"#3F8BFB",null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.daysLeft,"#3F8BFB",null)
                    }
                }
            }
            else {
                holder.binding1!!.subject.text = "--/--"
            }
            if (list[position].project != null){
                holder.binding1!!.title.text = list[position].project!!.title
                if (list[position].project!!.createdBy != null){
                    holder.binding1!!.teacher.text = "${list[position].project!!.createdBy!!.firstName ?: ""} ${list[position].project!!.createdBy!!.lastName}"
                }
                else {
                    holder.binding1!!.teacher.text = "--/--"
                }
            }
            else {
                holder.binding1!!.title.text = "--/--"
            }
            if (list[position].createdAt != null && list[position].dueDate != null){
                val date = BaseUtils.getFormattedDate(list[position].createdAt!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                val due = BaseUtils.getFormattedDate(list[position].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                holder.binding1!!.date.text = "$date - $due"
            }
            else{
                holder.binding1!!.date.text = "--/--"
            }
            if (list[position].dueDate != null){
                holder.binding1!!.daysLeft.text = BaseUtils.daysLeft(list[position].dueDate!!)
            }
            else{
                holder.binding1!!.daysLeft.text = "--/--"
            }
            if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
                holder.binding1!!.viewDoc.visibility = View.VISIBLE
            }
            else{
                holder.binding1!!.viewDoc.visibility = View.GONE
            }
            holder.binding1!!.viewDoc.setOnClickListener {
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
//                onClickListener.onClickItem(position)
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY,list[position].project!!._id!!)
                BaseUtils.startActivity(mActivity, ProjectResActivity(),bundle,false)
            }

        }

    }


}