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
import com.lms.sch.databinding.CardAssignmentBinding
import com.lms.sch.databinding.CardAssignmentW320Binding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetStudentAssignmentRes
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentAssignmentAdapter (
    val mActivity: BaseActivity,
    val isDiary : Boolean,
    val list : ArrayList<GetStudentAssignmentRes.Result>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<StudentAssignmentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View,type: Int):RecyclerView.ViewHolder(view){
        var binding1: CardAssignmentW320Binding ?= null
        var binding: CardAssignmentBinding ?= null
        init {
            if (type == 0){
                binding1 = CardAssignmentW320Binding.bind(view)
            }
            else {
                binding = CardAssignmentBinding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         if (isDiary){
             return ViewHolder(
                 LayoutInflater.from(mActivity).inflate(R.layout.card_assignment_w320,parent,false),
                 0
             )
         }
         else {
             return ViewHolder(
                 LayoutInflater.from(mActivity).inflate(R.layout.card_assignment,parent,false),
                 1
             )
         }
    }

     override fun getItemCount(): Int {
        return list.size
     }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         if (isDiary){
             if (list[position].subject != null){
                 holder.binding1!!.subjectName.text = list[position].subject!!.name
             }
             else {
                 holder.binding1!!.subjectName.text = "--/--"
             }
             if (list[position].assignment != null && list[position].assignment!!.createdBy != null){
                 holder.binding1!!.teacherName.text = "${list[position].assignment!!.createdBy!!.firstName ?: ""} ${list[position].assignment!!.createdBy!!.lastName ?: ""}"
             }
             else {
                 holder.binding1!!.teacherName.text = "--/--"
             }
             if (list[position].assignment != null && list[position].assignment!!.title!!.isNotEmpty()){
                 holder.binding1!!.assignmentTitle.text = list[position].assignment!!.title
             }
             else {
                 holder.binding1!!.assignmentTitle.text = "--/--"
             }
             if (list[position].assignment != null && list[position].assignment!!.description!!.isNotEmpty()){
                 holder.binding1!!.desc.setContent(list[position].assignment!!.description)
             }
             else{
                 holder.binding1!!.desc.text = "--/--"
             }
             if (list[position].createdAt != null){
                 holder.binding1!!.assGivenDate.text = BaseUtils.getFormattedDate(list[position].createdAt!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
             }
             else{
                 holder.binding1!!.assGivenDate.text = "--/--"
             }
             if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
                 holder.binding1!!.attach.text = "+${list[position].attachment!!.size}"
                 UiUtils.textviewImgDrawable(holder.binding1!!.attach,R.drawable.pdf,"end")
             }
             else{
                 holder.binding1!!.attach.text = "--None--"
                 UiUtils.textviewImgDrawable(holder.binding1!!.attach,null,"end")
             }
             holder.binding1!!.attachLay.setOnClickListener {
                 if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
                     if (list[position].attachment!![0].endsWith(".pdf")){
                         val bundle = Bundle()
                         bundle.putString(Constants.IntentKeys.KEY, list[position].attachment!![0])
                         BaseUtils.startActivity(mActivity, PdfViewerActivity(),bundle,false)
                     }
                     else if (list[position].attachment!![0].endsWith(".png") || list[position].attachment!![0].endsWith(".jpeg") || list[position].attachment!![0].endsWith(".jpg")){
                         val doc = ArrayList<String>()
                         for (items in list[position].attachment!!){
                             if (items.endsWith(".jpg") || items.endsWith(".jpeg") || items.endsWith(".png")){
                                 doc.add(items)
                             }
                         }
                         val bundle = Bundle()
                         bundle.putSerializable(Constants.IntentKeys.KEY,doc)
                         BaseUtils.startActivity(mActivity, ImageViewActivity(),bundle,false)
                     }
                     else {
                         val intent = Intent(Intent.ACTION_VIEW)
                         intent.setData(Uri.parse(list[position].attachment!![0]))
                         mActivity.startActivity(intent)
                     }
                 }
                 else {
                     UiUtils.showSnack("We are unable to fetch the Image",holder.binding1!!.root,false)
                 }
             }
             when(list[position].status){
                 "overdue" -> {
                     holder.binding1!!.status.text = "Not Completed"
                     UiUtils.textviewCustomDrawable(holder.binding1!!.status,R.drawable.border_curve_24dp)
                     UiUtils.textViewBgTint(holder.binding1!!.status,"#fce6e6" ,null)
                     UiUtils.textViewTextColor(holder.binding1!!.status,"#EA5455",null)
                     UiUtils.cardViewBgTint(holder.binding1!!.card,"#EA5455",null)
                     holder.binding1!!.submitTxt.text = "Last Date"
                     if (list[position].dueDate != null){
                         holder.binding1!!.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                     }
                     else{
                         holder.binding1!!.assUpdatedDate.text = "--/--"
                     }
                 }
                 "completed" ->{
                     holder.binding1!!.status.text = "Submitted"
                     UiUtils.textviewCustomDrawable(holder.binding1!!.status,R.drawable.border_curve_24dp)
                     UiUtils.textViewBgTint(holder.binding1!!.status,"#e6ffe7" ,null)
                     UiUtils.textViewTextColor(holder.binding1!!.status,"#32B138",null)
                     UiUtils.cardViewBgTint(holder.binding1!!.card,"#32B138",null)
                     holder.binding1!!.submitTxt.text = "Submitted On"
                     if (list[position].submittedOn != null){
                         holder.binding1!!.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].submittedOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                     }
                     else{
                         holder.binding1!!.assUpdatedDate.text = "--/--"
                     }
                 }
                 "pending" ->{
                     holder.binding1!!.status.text = "Ongoing"
                     UiUtils.textviewCustomDrawable(holder.binding1!!.status,R.drawable.border_curve_24dp)
                     UiUtils.textViewBgTint(holder.binding1!!.status,"#fff2d9" ,null)
                     UiUtils.textViewTextColor(holder.binding1!!.status,"#F69300",null)
                     UiUtils.cardViewBgColor(holder.binding1!!.card,"#F69300",null)
                     holder.binding1!!.submitTxt.text = "Last Date"
                     if (list[position].dueDate != null){
                         holder.binding1!!.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                     }
                     else{
                         holder.binding1!!.assUpdatedDate.text = "--/--"
                     }
                 }
             }
             holder.binding1!!.root.setOnClickListener {
                 onClickListener.onClickItem(position)
             }
         }
         else {
             if (list[position].subject != null){
                 holder.binding!!.subjectName.text = list[position].subject!!.name
             }
             else {
                 holder.binding!!.subjectName.text = "--/--"
             }
             if (list[position].assignment != null && list[position].assignment!!.createdBy != null){
                 holder.binding!!.teacherName.text = "${list[position].assignment!!.createdBy!!.firstName ?: ""} ${list[position].assignment!!.createdBy!!.lastName ?: ""}"
             }
             else {
                 holder.binding!!.teacherName.text = "--/--"
             }
             if (list[position].assignment != null && list[position].assignment!!.title!!.isNotEmpty()){
                 holder.binding!!.assignmentTitle.text = list[position].assignment!!.title
             }
             else {
                 holder.binding!!.assignmentTitle.text = "--/--"
             }
             if (list[position].assignment != null && list[position].assignment!!.description!!.isNotEmpty()){
                 holder.binding!!.desc.setContent(list[position].assignment!!.description)
             }
             else{
                 holder.binding!!.desc.text = "--/--"
             }
             if (list[position].createdAt != null){
                 holder.binding!!.assGivenDate.text = BaseUtils.getFormattedDate(list[position].createdAt!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
             }
             else{
                 holder.binding!!.assGivenDate.text = "--/--"
             }
             if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
                 holder.binding!!.attach.text = "+${list[position].attachment!!.size}"
                 UiUtils.textviewImgDrawable(holder.binding!!.attach,R.drawable.pdf,"end")
             }
             else{
                 holder.binding!!.attach.text = "--None--"
                 UiUtils.textviewImgDrawable(holder.binding!!.attach,null,"end")
             }
             holder.binding!!.attachLay.setOnClickListener {
                 if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
                     if (list[position].attachment!![0].endsWith(".pdf")){
                         val bundle = Bundle()
                         bundle.putString(Constants.IntentKeys.KEY, list[position].attachment!![0])
                         BaseUtils.startActivity(mActivity, PdfViewerActivity(),bundle,false)
                     }
                     else if (list[position].attachment!![0].endsWith(".png") || list[position].attachment!![0].endsWith(".jpeg") || list[position].attachment!![0].endsWith(".jpg")){
                         val doc = ArrayList<String>()
                         for (items in list[position].attachment!!){
                             if (items.endsWith(".jpg") || items.endsWith(".jpeg") || items.endsWith(".png")){
                                 doc.add(items)
                             }
                         }
                         val bundle = Bundle()
                         bundle.putSerializable(Constants.IntentKeys.KEY,doc)
                         BaseUtils.startActivity(mActivity, ImageViewActivity(),bundle,false)
                     }
                     else {
                         if (list[position].attachment != null && list[position].attachment!![0].isNotEmpty()) {
                             val intent = Intent(Intent.ACTION_VIEW)
                             intent.setData(Uri.parse(list[position].attachment!![0]))
                             mActivity.startActivity(intent)
                         }
                         else {
                             UiUtils.showSnack("We are unable to fetch the Image",holder.binding!!.root,false)
                         }
                     }
                 }
             }
             when(list[position].status){
                 "overdue" -> {
                     holder.binding!!.status.text = "Not Completed"
                     UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                     UiUtils.textViewBgTint(holder.binding!!.status,"#fce6e6" ,null)
                     UiUtils.textViewTextColor(holder.binding!!.status,"#EA5455",null)
                     UiUtils.cardViewBgTint(holder.binding!!.card,"#EA5455",null)
                     holder.binding!!.submitTxt.text = "Last Date"
                     if (list[position].dueDate != null){
                         holder.binding!!.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                     }
                     else{
                         holder.binding!!.assUpdatedDate.text = "--/--"
                     }
                 }
                 "completed" ->{
                     holder.binding!!.status.text = "Submitted"
                     UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                     UiUtils.textViewBgTint(holder.binding!!.status,"#e6ffe7" ,null)
                     UiUtils.textViewTextColor(holder.binding!!.status,"#32B138",null)
                     UiUtils.cardViewBgTint(holder.binding!!.card,"#32B138",null)
                     holder.binding!!.submitTxt.text = "Submitted On"
                     if (list[position].submittedOn != null){
                         holder.binding!!.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].submittedOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                     }
                     else{
                         holder.binding!!.assUpdatedDate.text = "--/--"
                     }
                 }
                 "pending" ->{
                     holder.binding!!.status.text = "Ongoing"
                     UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                     UiUtils.textViewBgTint(holder.binding!!.status,"#fff2d9" ,null)
                     UiUtils.textViewTextColor(holder.binding!!.status,"#F69300",null)
                     UiUtils.cardViewBgColor(holder.binding!!.card,"#F69300",null)
                     holder.binding!!.submitTxt.text = "Last Date"
                     if (list[position].dueDate != null){
                         holder.binding!!.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                     }
                     else{
                         holder.binding!!.assUpdatedDate.text = "--/--"
                     }
                 }
             }
             holder.binding!!.root.setOnClickListener {
                 onClickListener.onClickItem(position)
             }
         }
     }
}
