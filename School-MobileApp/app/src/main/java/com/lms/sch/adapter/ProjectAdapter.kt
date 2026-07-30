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
import com.lms.sch.activity.HomeWorkActivity
import com.lms.sch.activity.ImageViewActivity
import com.lms.sch.activity.PdfViewerActivity
import com.lms.sch.activity.ProjectActivity
import com.lms.sch.databinding.CardAssignmentBinding
import com.lms.sch.databinding.CardProject1Binding
import com.lms.sch.databinding.CardProjectwork360Binding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetTeacherProjectResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ProjectAdapter(
    private val mActivity: BaseActivity,
    val isDiary : Boolean,
    private val list : ArrayList<GetTeacherProjectResponse.Result.Rows>,

):
    RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, type: Int) : RecyclerView.ViewHolder(view) {
        var binding1: CardProject1Binding? = null
        var binding: CardAssignmentBinding? = null

        init {
            if (type == 0) {
                binding = CardAssignmentBinding.bind(view)
            } else {
                binding1 = CardProject1Binding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (isDiary) {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_assignment, parent, false),
                0
            )
        } else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_project1, parent, false),
                1
            )
        }
    }
    /* return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_assignment, parent, false)
        )
    }*/

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isDiary) {
            if (list[position].subject != null) {
                holder.binding!!.subjectName.text = list[position].subject!!.name
            } else {
                holder.binding!!.subjectName.text = "--/--"
            }
            if (list[position].title != null) {
                holder.binding!!.subjectName.text = list[position].title
                holder.binding!!.desc.setContent(list[position].description)
                if (list[position].createdBy != null) {
                    holder.binding!!.teacherName.text =
                        "${list[position].createdBy!!.firstName ?: ""} ${list[position].createdBy!!.lastName}"
                } else {
                    holder.binding!!.teacherName.text = "--/--"
                }
            } else {
                holder.binding!!.subjectName.text = "--/--"
            }
            if (list[position].createdAt != null) {
                holder.binding!!.assGivenDate.text = BaseUtils.getFormattedDate(
                    list[position].createdAt!!,
                    Constants.ApiKeys.TIME_INPUT_FORMAT,
                    Constants.ApiKeys.DATE_FORMAT
                )
            } else {
                holder.binding!!.assGivenDate.text = "--/--"
            }
            if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()) {
                UiUtils.textviewImgDrawable(holder.binding!!.attach, R.drawable.pdf, "end")
            } else {
                holder.binding!!.attach.text = "--None--"
                UiUtils.textviewImgDrawable(holder.binding!!.attach, null, "end")
            }
            holder.binding!!.attachLay.setOnClickListener {
                if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()) {
                    if (list[position].attachment!!.endsWith(".pdf")) {
                        val bundle = Bundle()
                        bundle.putString(Constants.IntentKeys.KEY, list[position].attachment!!)
                        BaseUtils.startActivity(mActivity, PdfViewerActivity(), bundle, false)
                    } else if (list[position].attachment!!.endsWith(".png") || list[position].attachment!!.endsWith(".jpeg") || list[position].attachment!!.endsWith(".jpg")) {
                        val doc = ArrayList<String>()
                        doc.add(list[position].attachment!!)
                        val bundle = Bundle()
                        bundle.putSerializable(Constants.IntentKeys.KEY, doc)
                        BaseUtils.startActivity(mActivity, ImageViewActivity(), bundle, false)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(list[position].attachment!!))
                        mActivity.startActivity(intent)
                    }
                } else {
                    UiUtils.showSnack(
                        "We are unable to fetch the Image",
                        holder.binding!!.root,
                        false
                    )
                }
            }
          /*  holder.binding!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
*/
            /* when(list[position].status){
                "ongoing" ->{
                    holder.binding!!.status.text = "Ongoing"
                    UiUtils.textviewCustomDrawable(holder.binding!!.status, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,null, R.color.light_orange_bg)
                    UiUtils.textViewTextColor(holder.binding!!.status,null, R.color.orange)
                    UiUtils.cardViewBgTint(holder.binding!!.card,"#F69300",null)

                    if (list[position].dueDate != null){
                        holder.binding!!.submitTxt.text = "Last Date"
                        holder.binding!!.assGivenDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding!!.assGivenDate.text = "--/--"
                    }
                }
                "overdue" ->{
                    holder.binding!!.status.text = "Not Completed"
                    UiUtils.textviewCustomDrawable(holder.binding!!.status, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,null, R.color.very_light_red)
                    UiUtils.textViewTextColor(holder.binding!!.status,null, R.color.red_hurry_up)
                    UiUtils.cardViewBgTint(holder.binding!!.card,"#EA5455",null)

                    if (list[position].dueDate != null){
                        holder.binding!!.submitTxt.text = "Last Date"
                        holder.binding!!.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding!!.assUpdatedDate.text = "--/--"
                    }
                }
                "completed" ->{
                    holder.binding!!.status.text = "Completed"
                    UiUtils.textviewCustomDrawable(holder.binding!!.status, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#e6ffe7" ,null)
                    UiUtils.textViewTextColor(holder.binding!!.status,"#32B138",null)
                    UiUtils.cardViewBgTint(holder.binding!!.card,"#32B138",null)

                    if (list[position].status != null){
                        holder.binding!!.submitTxt.text = "Submitted On"
                        holder.binding!!.su.text = BaseUtils.getFormattedDate(list[position].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                    }
                    else{
                        holder.binding!!.submitDate.text = "--/--"
                    }
                }
            }*/
        }
        else {
            if (list[position].subject != null) {
                holder.binding1!!.subject.text = list[position].subject!!.name
                when (list[position].subject!!.name) {
                    "General Knowledge" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#b81f3b", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#b81f3b", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#b81f3b", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#b81f3b", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#b81f3b", null)
                        UiUtils.setTextViewDrawableColor( holder.binding1!!.daysLeft, "#b81f3b", null  )
                    }

                    "English" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#3F8BFB", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#3F8BFB", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#3F8BFB", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#3F8BFB", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#3F8BFB", null)
                        UiUtils.setTextViewDrawableColor(  holder.binding1!!.daysLeft, "#3F8BFB", null)
                    }

                    "Maths" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#45a9d1", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#45a9d1", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#45a9d1", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#45a9d1", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#45a9d1", null)
                        UiUtils.setTextViewDrawableColor( holder.binding1!!.daysLeft, "#45a9d1",  null)
                    }

                    "Science" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#E9E36B", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#E9E36B", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#E9E36B", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#E9E36B", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#E9E36B", null)
                        UiUtils.setTextViewDrawableColor( holder.binding1!!.daysLeft,"#E9E36B", null )
                    }

                    "Hindi" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#A9A8DA", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#A9A8DA", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#A9A8DA", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#A9A8DA", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#A9A8DA", null)
                        UiUtils.setTextViewDrawableColor(  holder.binding1!!.daysLeft,  "#A9A8DA",  null )
                    }

                    "Computer" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#E96B84", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#E96B84", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#E96B84", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#E96B84", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#E96B84", null)
                        UiUtils.setTextViewDrawableColor(  holder.binding1!!.daysLeft, "#E96B84",  null )
                    }

                    "Tamil" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#F6891E", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#F6891E", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#F6891E", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#F6891E", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#F6891E", null)
                        UiUtils.setTextViewDrawableColor( holder.binding1!!.daysLeft,  "#F6891E",null )
                    }

                    "Social Science" -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#6BC5E9", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#6BC5E9", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#6BC5E9", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#6BC5E9", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#6BC5E9", null)
                        UiUtils.setTextViewDrawableColor(
                            holder.binding1!!.daysLeft,
                            "#6BC5E9",
                            null
                        )
                    }

                    else -> {
                        UiUtils.textViewTextColor(holder.binding1!!.subject, "#3F8BFB", null)
                        UiUtils.textViewTextColor(holder.binding1!!.teacher, "#3F8BFB", null)
                        UiUtils.textViewTextColor(holder.binding1!!.viewDoc, "#3F8BFB", null)
                        UiUtils.textViewTextColor(holder.binding1!!.daysLeft, "#3F8BFB", null)
                        UiUtils.setTextViewDrawableColor(holder.binding1!!.subject, "#3F8BFB", null)
                        UiUtils.setTextViewDrawableColor(
                            holder.binding1!!.daysLeft,
                            "#3F8BFB",
                            null
                        )
                    }
                }
            }
            else {
                holder.binding1!!.subject.text = "--/--"
            }
            if (list[position].title != null) {
                holder.binding1!!.title.text = list[position].title
                if (list[position].createdBy != null) {
                    holder.binding1!!.teacher.text =
                        "${list[position].createdBy!!.firstName ?: ""} ${list[position].createdBy!!.lastName}"
                } else {
                    holder.binding1!!.teacher.text = "--/--"
                }
            } else {
                holder.binding1!!.title.text = "--/--"
            }
            if (list[position].createdAt != null && list[position].dueDate != null) {
                val date = BaseUtils.getFormattedDate(
                    list[position].createdAt!!,
                    Constants.ApiKeys.TIME_INPUT_FORMAT,
                    Constants.ApiKeys.DATE_FORMAT
                )
                val due = BaseUtils.getFormattedDate(
                    list[position].dueDate!!,
                    Constants.ApiKeys.TIME_INPUT_FORMAT,
                    Constants.ApiKeys.DATE_FORMAT
                )
                holder.binding1!!.date.text = "$date - $due"
            } else {
                holder.binding1!!.date.text = "--/--"
            }
            if (list[position].dueDate != null) {
                holder.binding1!!.daysLeft.text = BaseUtils.daysLeft(list[position].dueDate!!)
            } else {
                holder.binding1!!.daysLeft.text = "--/--"
            }
            if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()) {
                holder.binding1!!.viewDoc.visibility = View.VISIBLE
            } else {
                holder.binding1!!.viewDoc.visibility = View.GONE
            }
            holder.binding1!!.viewDoc.setOnClickListener {
                if (list[position].attachment != null) {
                    if (list[position].attachment!!.endsWith(".pdf")) {
                        val bundle = Bundle()
                        bundle.putString(Constants.IntentKeys.KEY, list[position].attachment!!)
                        BaseUtils.startActivity(mActivity, PdfViewerActivity(), bundle, false)
                    } else if (list[position].attachment!!.endsWith(".png") || list[position].attachment!!.endsWith(
                            ".jpeg"
                        ) || list[position].attachment!!.endsWith(".jpg")
                    ) {
                        val doc = ArrayList<String>()
                        doc.add(list[position].attachment!!)
                        val bundle = Bundle()
                        bundle.putSerializable(Constants.IntentKeys.KEY, doc)
                        BaseUtils.startActivity(mActivity, ImageViewActivity(), bundle, false)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(list[position].attachment!!))
                        mActivity.startActivity(intent)
                    }
                } else {
                    UiUtils.showSnack(
                        "We are unable to fetch the Image",
                        holder.binding1!!.root,
                        false
                    )
                }
            }
            holder.binding1!!.root.setOnClickListener {
                val intent = Intent(mActivity, ProjectActivity::class.java)
                intent.putExtra("id", list[position]._id)
                mActivity.startActivity(intent)
            }
        }
    }
}


/* if (list[position].subject != null && list[position].subject!!.name!!.isNotEmpty()) {
     holder.binding.subjectName.text = list[position].subject!!.name
 } else {
     holder.binding.subjectName.text = "--/--"
 }
 if (list[position].teacher != null && list[position].teacher!!.firstName != null && list[position].teacher!!.lastName != null) {
     holder.binding.teacherName.text = "${list[position].teacher?.firstName} ${list[position].teacher?.lastName}"
 } else {
     holder.binding.teacherName.text = "--/--"
 }

 if (list[position].title != null && list[position].title!!.isNotEmpty()) {
     holder.binding.assignmentTitle.text = list[position].title
 } else {
     holder.binding.assignmentTitle.text = "--/--"
 }

 if (list[position].description != null && list[position].description!!.isNotEmpty()) {
     holder.binding.desc.text = list[position].description
 } else {
     holder.binding.desc.text = "--/--"
 }

 if (list[position].createdAt != null) {
     holder.binding.assGivenDate.text = BaseUtils.getFormattedDate(list[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
 } else {
     holder.binding.assGivenDate.text = "--/--"
 }
 if (list[position].dueDate != null) {
     holder.binding.assUpdatedDate.text = BaseUtils.getFormattedDate(list[position].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
 } else {
     holder.binding.assUpdatedDate.text = "--/--"
 }
 holder.binding.root.setOnClickListener {
     if (list[position].status == "completed"){
         val bundle = Bundle()
         bundle.putString(Constants.IntentKeys.KEY, list[position]._id)
         bundle.putString("status",list[position].status)
         BaseUtils.startActivity(mActivity, ProjectActivity(), bundle, false)
     }else{
         UiUtils.showSnack("Project Not Yet Not completed", holder.binding.root,false)
     }

 }



when (list[position].status) {
 "pending" -> {
     holder.binding.status.text = "Pending"
     UiUtils.textviewCustomDrawable(holder.binding.status, R.drawable.border_curve_24dp)
     UiUtils.textViewBgTint(holder.binding.status, "#fff2d9", null)
     UiUtils.textViewTextColor(holder.binding.status, "#F69300", null)
     UiUtils.cardViewBgTint(holder.binding.card, "#F69300", null)
 }

 "completed" -> {
     holder.binding.status.text = "Completed"
     UiUtils.textviewCustomDrawable(holder.binding.status, R.drawable.border_curve_24dp)
     UiUtils.textViewBgTint(holder.binding.status, "#e6ffe7", null)
     UiUtils.textViewTextColor(holder.binding.status, "#32B138", null)
     UiUtils.cardViewBgTint(holder.binding.card, "#32B138", null)
 }
 else -> {
     holder.binding.status.text = list[position].status
     UiUtils.textviewCustomDrawable(holder.binding.status, R.drawable.border_curve_24dp)
     UiUtils.textViewBgTint(holder.binding.status, "#fff2d9", null)
     UiUtils.textViewTextColor(holder.binding.status, "#F69300", null)
     UiUtils.cardViewBgTint(holder.binding.card, "#F69300", null)
 }
}
}
}
*/