package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.StudentListActivity
import com.lms.sch.activity.StudentStatsActivity
import com.lms.sch.databinding.CardStudentReportBinding
import com.lms.sch.databinding.CardSubjectStudentsBinding
import com.lms.sch.response.GetTeacherProgramResponse
import com.lms.sch.response.StudentListAnalyticsResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class SubjectStudentsOverviewAdapter(
    val mActivity: BaseActivity,
    val isHome: Boolean,
    val list : ArrayList<StudentListAnalyticsResponse.Result.Row>
): RecyclerView.Adapter<SubjectStudentsOverviewAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: CardStudentReportBinding = CardStudentReportBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_student_report,parent,false)
        )
    }

    override fun getItemCount(): Int{
//        if (isHome && list.size > 3){
//            return 3
//        }
//        else {
//            return list.size
//        }
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var name = ""
        if (list[position].student != null ) {
            name = list[position].student!!.firstName + " " + list[position].student!!.lastName
            holder.binding.name.text = list[position].student!!.firstName + " " + list[position].student!!.lastName
            UiUtils.loadImage(holder.binding.profile,list[position].student!!.img_url)
        }
        else{
            holder.binding.name.text = "--/--"
            UiUtils.loadImage(holder.binding.profile,R.drawable.ic_user_profile.toString())
        }
        holder.binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,list[position].student!!._id!!)
            BaseUtils.startActivity(mActivity, StudentStatsActivity(),bundle,false)
        }
    }

}