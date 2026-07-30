package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.StudentAttendanceActivity
import com.lms.sch.databinding.CardAttendanceBinding
import com.lms.sch.fragment.MyClassFragment
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetAttendanceResponse
import com.lms.sch.response.GetStudentResponse
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import com.lms.sch.utils.UiUtils.getTodayDate

class AttendanceAdapter(
    val mActivity: StudentAttendanceActivity,
    val list: ArrayList<GetAttendanceResponse.Result.Row>
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardAttendanceBinding = CardAttendanceBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(mActivity).inflate(R.layout.card_attendance, parent, false))
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].student != null){
            holder.binding.stdName.text= list[position].student!!.firstName +" "+ list[position].student!!.lastName
            UiUtils.loadImage(holder.binding.profile,list[position].student!!.img_url!!)
        }
        else {
            holder.binding.stdName.text = "--/--"
        }
        var standard = ""
        if (list[position].standard != null){
            standard = UiUtils.getOrdinalSuffix(list[position].standard!!.toInt())
        }
        var rollNo = ""
//        if (list[position].rollNo != null && list[position].rollNo!!.isNotEmpty()){
//            rollNo = list[position].rollNo!!
//        }
//        else {
//            rollNo = "--/--"
//        }
        holder.binding.rollNo.text = "${standard} - ${list[position].section} Sec | $rollNo"
//        val pro = arrayListOf(70,65,56,87,90)
//        val ran = pro.random()
//        holder.binding.circularProgressBar1.progress = ran
//        holder.binding.progressTxt.text = "${ran}%"

        when (list[position].status) {
            "present" -> {
                holder.binding.status.text = "Present"
                UiUtils.textViewTextColor(holder.binding.status,"#32B138",null)
                UiUtils.textViewBgTint(holder.binding.status,"#DBFFDD",null)
                UiUtils.textViewTextColor(holder.binding.present, "#32B138", null)
                UiUtils.textviewImgDrawable(holder.binding.present, R.drawable.greenbtn,"start")
            }
            "absent" -> {
                holder.binding.status.text = "Absent"
                UiUtils.textViewTextColor(holder.binding.status,"#EA5455",null)
                UiUtils.textViewBgTint(holder.binding.status,"#FFDDDD",null)
                UiUtils.textViewTextColor(holder.binding.absent, "#E85A5B", null)
                UiUtils.textviewImgDrawable(holder.binding.absent, R.drawable.redbtn,"start")
            }
            "halfDay" -> {
                holder.binding.status.text = "HalfDay"
                UiUtils.textViewTextColor(holder.binding.status,"#3F8BFB",null)
                UiUtils.textViewBgTint(holder.binding.status,"#E8F6FF",null)
                UiUtils.textViewTextColor(holder.binding.halfDay, "#1170E4", null)
                UiUtils.textviewImgDrawable(holder.binding.halfDay, R.drawable.bluebtn,"start")
            }
            else -> {
                UiUtils.textViewTextColor(holder.binding.halfDay, "#868686", null)
                UiUtils.textviewImgDrawable(holder.binding.halfDay, R.drawable.radio_empty,"start")
            }
        }
        holder.binding.present.setOnClickListener {
            if (list[position].student != null){
                updateAttendance("present",holder,position)
            }
            else {
                UiUtils.showSnack("Student not found",holder.binding.root,false)
            }
        }
        holder.binding.absent.setOnClickListener {
            if (list[position].student != null){
                updateAttendance("absent",holder,position)
            }
            else {
                UiUtils.showSnack("Student not found",holder.binding.root,false)
            }
        }
        holder.binding.halfDay.setOnClickListener {
            if (list[position].student != null){
                updateAttendance("halfDay",holder,position)
            }
            else {
                UiUtils.showSnack("Student not found",holder.binding.root,false)
            }
        }
    }

    fun updateAttendance(status: String,holder: ViewHolder,position: Int){
        val student = list[position].student!!._id
        val date = getTodayDate()
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().attendanceUpdate(mActivity,student!!,date,status).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        UiUtils.showSnack(it.msg,holder.binding.root,true)
                        mActivity.getAttendance()
                    }
                    else {
                        UiUtils.showSnack(it.msg,holder.binding.root,false)
                    }
                }
            }
        }
    }
 }