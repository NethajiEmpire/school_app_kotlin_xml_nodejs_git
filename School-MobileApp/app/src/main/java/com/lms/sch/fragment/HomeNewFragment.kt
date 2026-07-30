package com.lms.sch.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.activity.AdminCircularActivity
import com.lms.sch.activity.KidsAssignmentActivity
import com.lms.sch.activity.KidsAttendanceActivity
import com.lms.sch.activity.KidsExamActivity
import com.lms.sch.activity.KidsHomeworkActivity
import com.lms.sch.activity.KidsProjectActivity
import com.lms.sch.activity.KidsTimeTableActivity
import com.lms.sch.activity.MyTeacherActivity
import com.lms.sch.activity.ProfileActivity
import com.lms.sch.adapter.MyTeachersAdapter
import com.lms.sch.databinding.FragmentHomeNewBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class HomeNewFragment : BaseFragment() {
    lateinit var binding : FragmentHomeNewBinding
    var resultAtt = GetStudentAttenDanceResponse.Result()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentHomeNewBinding.inflate(inflater,container,false)
        val view = binding.root

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().profile(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.userprofile != null){
                            if (it.result!!.userprofile!!.firstName != null && it.result!!.userprofile!!.lastName != null){
                                binding.name.text = it.result!!.userprofile!!.firstName + " " + it.result!!.userprofile!!.lastName
                                binding.welcomeName.text = it.result!!.userprofile!!.firstName + " " + it.result!!.userprofile!!.lastName
                                UiUtils.loadImage(binding.profile,it.result!!.userprofile!!.img_url!!)
                            }
                            else{
                                binding.name.text = "--/--"
                                binding.welcomeName.text = "--/--"
                            }
                            if (it.result!!.userprofile!!.rollNo != null && it.result!!.userprofile!!.rollNo!!.isNotEmpty()){
                                binding.rollNo.text = "Roll No : ${it.result!!.userprofile!!.rollNo}"
                            }
                            else {
                                binding.rollNo.text = "--/--"
                            }
                            if (it.result!!.userprofile!!.lead_id != null && it.result!!.userprofile!!.lead_id!!.isNotEmpty()){
                                binding.addNo.text = it.result!!.userprofile!!.lead_id                            }
                            else {
                                binding.addNo.text = "--/--"
                            }
                            if (it.result!!.userprofile!!.img_url != null && it.result!!.userprofile!!.img_url!!.isNotEmpty()){
                                UiUtils.loadImage(binding.img,it.result!!.userprofile!!.img_url!!)
                                UiUtils.loadImage(binding.profile,it.result!!.userprofile!!.img_url!!)
                            }
                            var std = ""
                            if (it.result!!.studentPreference != null && it.result!!.studentPreference!!.studentClass != null){
                                std = UiUtils.getOrdinalSuffix(it.result!!.studentPreference!!.studentClass!!.name!!.toInt())
                                binding.std.text = "$std - ${it.result!!.studentPreference!!.section!!.name} - Sec"
                                mActivity.sharedHelper.stdClassSec = "$std - ${it.result!!.studentPreference!!.section!!.name} - Sec"
                            }
                            else {
                                binding.std.text = "--/--"
                            }
                        }
                        else{
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getMyTeachers(mActivity).observe(mActivity){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            if (it.result!!.classTeacher != null) {
                                binding.incharge.text = it.result!!.classTeacher!!.firstName + " " + it.result!!.classTeacher!!.lastName
                            }
                            else {
                                binding.incharge.text = "--/--"
                            }

                        }
                    }
                }
            }
        }
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getStudentAttendance(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        resultAtt = it.result!!
                        if (it.result != null ) {

                            if (resultAtt.progress?.presentPercentage != null) {
                                val percValue = resultAtt.progress!!.presentPercentage
                                binding.perc.text = "$percValue %"
                                binding.progressBar.progress = percValue ?: 0
                            } else {
                                binding.perc.text = "--/--"
                                binding.progressBar.progress = 0
                            }


                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)

                    }
                }
            }
        }

        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, ProfileActivity(),null,false)
        }
        binding.img.setOnClickListener {
            BaseUtils.startActivity(mActivity, ProfileActivity(),null,false)
        }
        binding.timeTable.setOnClickListener {
            BaseUtils.startActivity(mActivity, KidsTimeTableActivity(),null,false)
        }
        binding.homework.setOnClickListener {
            BaseUtils.startActivity(mActivity, KidsHomeworkActivity(),null,false)
        }
        binding.assignment.setOnClickListener {
            BaseUtils.startActivity(mActivity, KidsAssignmentActivity(),null,false)
        }
        binding.project.setOnClickListener {
            BaseUtils.startActivity(mActivity, KidsProjectActivity(),null,false)
        }
        binding.attendance.setOnClickListener {
            BaseUtils.startActivity(mActivity, KidsAttendanceActivity(),null,false)
        }
        binding.myTeachers.setOnClickListener {
            BaseUtils.startActivity(mActivity, MyTeacherActivity(),null,false)
        }
        binding.events.setOnClickListener {
            BaseUtils.startActivity(mActivity, AdminCircularActivity(),null,false)
        }
        binding.fees.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_fees)
        }
        binding.progress.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_progress)
        }
        binding.chat.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_chat)
        }
        binding.exam.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,"exam")
            BaseUtils.startActivity(mActivity, KidsExamActivity(),bundle,false)
        }
        binding.classTest.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,"classTest")
            BaseUtils.startActivity(mActivity, KidsExamActivity(),bundle,false)
        }

        return view
    }

}