package com.lms.sch.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardGuestBinding
import com.lms.sch.databinding.CardGuestListBinding
import com.lms.sch.databinding.CardUmStaffListBinding
import com.lms.sch.databinding.CardUmStudentListBinding
import com.lms.sch.databinding.CardUmTeacherListBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetGuestInfoResponse
import com.lms.sch.response.GetStaffResponse
import com.lms.sch.response.GetStudentResponse
import com.lms.sch.response.GetTeacherResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class UsersAdapter (
    var mActivity: BaseActivity,
    var role: String,
    var list1: ArrayList<GetGuestInfoResponse.Result.Row>,
    var list2: ArrayList<GetStudentResponse.Result.Row>,
    var list3: ArrayList<GetTeacherResponse.Result.Row>,
    var list4: ArrayList<GetStaffResponse.Result.Row>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

    inner class ViewHolder(view: View,type : Int) : RecyclerView.ViewHolder(view) {
        var binding: CardGuestListBinding ?= null
        var binding1: CardUmStudentListBinding ?= null
        var binding2: CardUmTeacherListBinding ?= null
        var binding3: CardUmStaffListBinding ?= null
        init {
            if (type == 0){
                binding = CardGuestListBinding.bind(view)
            }
            else if (type == 1){
                binding1 = CardUmStudentListBinding.bind(view)
            }
            else if (type == 2){
                binding2 = CardUmTeacherListBinding.bind(view)
            }
            else {
                binding3 = CardUmStaffListBinding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (role == "Guest"){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_guest_list, parent, false),
                0
            )
        }
        else if (role == "Student"){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_um_student_list, parent, false),
                1
            )
        }
        else if (role == "Teacher"){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_um_teacher_list, parent, false),
                2
            )
        }
        else{
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_um_staff_list, parent, false),
                3
            )
        }
    }

    override fun getItemCount(): Int {
        if (role == "Guest"){
            return list1.size
        }
        else if (role == "Student"){
            return list2.size
        }
        else if (role == "Teacher"){
            return list3.size
        }
        else {
            return list4.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (role == "Guest"){
            if (list1[position].firstName != null && list1[position].lastName != null){
                holder.binding!!.name.text = list1[position].firstName + " " + list1[position].lastName
            }
            else {
                holder.binding!!.name.text = "Guest"
            }
            if (list1[position].img_url  != null){
                UiUtils.loadImage(holder.binding!!.img,list1[position].img_url.toString())
            }else{
                UiUtils.loadImage(holder.binding!!.img,R.drawable.ic_user_profile.toString())
            }
            holder.binding!!.mobile.text = list1[position].mobile
            if (list1[position].grade_level != null){
                holder.binding!!.grade.text = UiUtils.getOrdinalSuffix(list1[position].grade_level!!.name!!.toInt())
            }
            else {
                holder.binding!!.grade.text = "--/--"
            }
            holder.binding!!.date.text = BaseUtils.getFormattedDate(list1[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            val steps = getColoredSpanned(list1[position].currentStep!!, "#EA5455")
            val tSteps = getColoredSpanned("/${list1[position].totalStep} Steps", "#333333")
            holder.binding!!.steps.text = Html.fromHtml(steps+" "+tSteps,FROM_HTML_MODE_LEGACY)

            when(list1[position].status){
                "informationFormPen" -> {
                    holder.binding!!.status.text = "Student Info Pending"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#FF7C90",null)
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#FFEAED",null)
                }
                "parentInfoPen" -> {
                    holder.binding!!.status.text = "Parent Info Pending"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#FF7C90",null)
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#FFEAED",null)
                }
                "academicInfoPen" -> {
                    holder.binding!!.status.text = "Academic Info Pending"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#FF7C90",null)
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#FFEAED",null)
                }
                "documentInfoPen" -> {
                    holder.binding!!.status.text = "Document Info Pending"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#FF7C90",null)
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#FFEAED",null)
                }
                "paymentInfoPen" -> {
                    holder.binding!!.status.text = "Payment Info Pending"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#FF7C90",null)
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#FFEAED",null)
                }
                "acknowledgementPen" -> {
                    holder.binding!!.status.text = "Acknowledgement Pending"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#FF7C90",null)
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#FFEAED",null)
                }
                "verificationpen" -> {
                    holder.binding!!.status.text = "Verification Pending"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#FF7C90",null)
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#FFEAED",null)
                }
                "verified" -> {
                    holder.binding!!.status.text = "Verified"
                    UiUtils.textViewTextColor(holder.binding!!.status,"#32B138",null)
                    UiUtils.textviewImgDrawable(holder.binding!!.status,null,"start")
                    UiUtils.textviewCustomDrawable(holder.binding!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding!!.status,"#D9FFDB",null)
                }

            }

            holder.binding!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
        else if (role == "Student"){
            if (list2[position] != null ) {
                if (list2[position].student != null) {
                    holder.binding1!!.name.text =
                        list2[position].student!!.firstName + " " + list2[position].student!!.lastName
                    holder.binding1!!.stdId.text = list2[position].student!!.lead_id
                } else {
                    holder.binding1!!.name.text = "--/--"
                    holder.binding1!!.stdId.text = "--/--"
                }
                if (list2[position].student != null && list2[position].student!!.img_url != null) {
                    UiUtils.loadImage(holder.binding1!!.img, list2[position].student!!.img_url.toString()
                    )
                } else {
//                UiUtils.loadImage(holder.binding!!.img,R.drawable.ic_user_profile.toString())
                }
                if (list2[position].studentClass != null && list2[position].studentClass!!.name != null) {
                    holder.binding1!!.std.text =
                        UiUtils.getOrdinalSuffix(list2[position].studentClass!!.name!!.toInt())
                } else {
                    holder.binding1!!.std.text = "--/--"
                }
                holder.binding1!!.date.text = BaseUtils.getFormattedDate(
                    list2[position].createdAt!!,
                    Constants.ApiKeys.TIME_INPUT_FORMAT,
                    Constants.ApiKeys.DATE_FORMAT
                )

                holder.binding1!!.root.setOnClickListener {
                    onClickListener.onClickItem(position)
                }
            }
            else{
                holder.binding1!!.name.text = "--/--"
                holder.binding1!!.stdId.text = "--/--"
            }
        }
        else if (role == "Teacher"){
            if (list3[position].firstName != null && list3[position].lastName != null){
                holder.binding2!!.name.text = list3[position].firstName + " " + list3[position].lastName
            }
            else {
                holder.binding2!!.name.text = "--/--"
            }
            if (list3[position].img_url != null){
                UiUtils.loadImage(holder.binding2!!.img,list3[position].img_url)
            }
            holder.binding2!!.stdId.text = list3[position].lead_id
            if (list3[position].teacherPreference != null){
                holder.binding2!!.qualify.text = list3[position].teacherPreference!!.highestQualification
                if (list3[position].teacherPreference!!.overallExperience!!.toInt() > 1){
                    holder.binding2!!.exp.text = "${list3[position].teacherPreference!!.overallExperience} Years"
                }
                else {
                    holder.binding2!!.exp.text = "${list3[position].teacherPreference!!.overallExperience} Year"
                }
            }
            else {
                holder.binding2!!.qualify.text = "--/--"
                holder.binding2!!.exp.text = "--/--"
            }
            holder.binding2!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
        else {
            if (list4[position].firstName != null && list4[position].lastName != null){
                holder.binding3!!.name.text = list4[position].firstName + " " + list4[position].lastName
            }
            else {
                holder.binding3!!.name.text = "--/--"
            }
            if (list4[position].img_url != null) {
                UiUtils.loadImage(holder.binding3!!.img, list4[position].img_url)
            }
            holder.binding3!!.stdId.text = list4[position].leadId
            if (list4[position].staffType != null){
                holder.binding3!!.role.text = list4[position].staffType!!.name
            }
            else {
                holder.binding3!!.role.text = "--/--"
            }
            holder.binding3!!.joined.text = BaseUtils.getFormattedDate(list4[position].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            when(list4[position].activeStatus){
                "active" -> {
                    holder.binding3!!.status.text = "Active"
                    UiUtils.textViewTextColor(holder.binding3!!.status, "#32B138", null)
                    UiUtils.textviewCustomDrawable(holder.binding3!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding3!!.status, "#D9FFDB", null)
                }
                "inactive" -> {
                    holder.binding3!!.status.text = "Inactive"
                    UiUtils.textViewTextColor(holder.binding3!!.status, "#EA5455", null)
                    UiUtils.textviewCustomDrawable(holder.binding3!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding3!!.status, "#FFDDDD", null)
                }
            }
            holder.binding3!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
    }
    private fun getColoredSpanned(text: String, color: String): String {
        val input = "<font color=$color>$text</font>"
        return input
    }
}