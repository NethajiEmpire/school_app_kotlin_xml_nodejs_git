package com.lms.sch.activity

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.lms.sch.R
import com.lms.sch.databinding.ActivityTeacherProfileBinding
import com.lms.sch.databinding.DialogLogoutBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.TeacherprofileSignleViewResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import kotlin.math.roundToInt

class TeacherProfileActivity : BaseActivity() {
    lateinit var binding : ActivityTeacherProfileBinding
    var result = TeacherprofileSignleViewResponse.Result()
    var teacher_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTeacherProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        getTeacherInfo()
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
       if (sharedHelper.role == "TEACHER" ){
           binding.myDetails.visibility = View.VISIBLE
       }else if (sharedHelper.role == "ADMIN"){
           binding.myDetails.visibility = View.GONE
       }
        binding.myDetails.setOnClickListener {
            if (sharedHelper.role == "TEACHER" ){
                val bundle = Bundle()
                bundle.putString("key","My Details")
                bundle.putString("key1",teacher_id)
                BaseUtils.startActivity(this, TeacherProfileDetailsActivity(),bundle,false)
            }
        }
        binding.professionalDetails.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Professional Details")
            bundle.putString("key1",teacher_id)
            BaseUtils.startActivity(this, TeacherProfileDetailsActivity(),bundle,false)
        }
        binding.notification.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Notification")
            BaseUtils.startActivity(this, NotificationActivity(),bundle,false)
        }
        binding.privacy.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Privacy & policy")
            BaseUtils.startActivity(this, NotificationActivity(),bundle,false)
        }
        binding.complaints.setOnClickListener {
            if (sharedHelper.role == "TEACHER"){
                BaseUtils.startActivity(this, TeacherRaiseComplaintActivity(),null,false)
            }
            else {
                BaseUtils.startActivity(this, ComplaintsActivity(),null,false)
            }
        }
        binding.leaveRequest.setOnClickListener {
            if (sharedHelper.role == "ADMIN"){
                BaseUtils.startActivity(this, LeaveApprovalActivity(),null,false)
            }
            else {
                BaseUtils.startActivity(this, LeaveRequestActivity(),null,false)
            }
        }
        binding.logout.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_logout)
            val bind: DialogLogoutBinding = DialogLogoutBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(bind.root)
//        UiUtils.animation(this, bind.card, R.anim.slide_in_from_bottom, true)
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this, R.color.transparent))
            )
            var width: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//        var height: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setGravity(Gravity.CENTER)

            bind.logout1.setOnClickListener {
                BaseUtils.logout(this, "")
                dialog.dismiss()
            }
            bind.cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnDismissListener {

            }
            dialog.show()
        }
    }
    private fun getTeacherInfo() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().profile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result!!.userprofile != null) {
                                teacher_id = it.result!!.userprofile!!._id.orEmpty()
                                binding.name.text = it.result!!.userprofile!!.firstName + " " + it.result!!.userprofile!!.lastName
                                UiUtils.loadImage(binding.profile,it.result!!.userprofile!!.img_url)
                            } else {
                                binding.name.text = "--/--"
                            }
                            if (it.result!!.userprofile!!.lead_id != null){
                                binding.teacherId.text = it.result!!.userprofile!!.lead_id.orEmpty()
                            }else{
                                binding.teacherId.text = "--/--"
                            }
                            if (it.result!!.userprofile!!.role!!.name != null){
                                binding.role.text = it.result!!.userprofile!!.role!!.name.orEmpty()
                            }else{
                                binding.role.text = "--/--"
                            }
                            if (it.result!!.userprofile!!.mobile != null){
                                binding.mobile.text = "+91 ${it.result!!.userprofile!!.mobile}"
                            }else{
                                binding.mobile.text = "--/--"
                            }
                            if (it.result!!.userprofile!!.email != null){
                                binding.email.text = it.result!!.userprofile!!.email.orEmpty()
                            }else{
                                binding.email.text = "--/--"
                            }

                           /* if (it.result!!.role != null && it.result!!.role!!.name != null) {
                                binding.grade.text = it.result!!.role!!.name
                            } else {
                                binding.grade.text = "--/--"
                            }*/
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
}