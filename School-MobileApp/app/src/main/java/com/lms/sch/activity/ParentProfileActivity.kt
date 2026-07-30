package com.lms.sch.activity

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.lms.sch.R
import com.lms.sch.databinding.ActivityParentProfileBinding
import com.lms.sch.databinding.DialogLogoutBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import kotlin.math.roundToInt

class ParentProfileActivity : BaseActivity() {
    lateinit var binding : ActivityParentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityParentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.version.text = "Version " + BaseUtils.getVersionName(this)

        binding.back.setOnClickListener {
            finish()
        }
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().ParentProfile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.userprofile != null){

                            if (it.result!!.userprofile!!.img_url != null && it.result!!.userprofile!!.img_url!!.isNotEmpty()){
                                UiUtils.loadImage(binding.img,it.result!!.userprofile!!.img_url)
                            }
                            else{
                                UiUtils.imageviewDrawable(binding.img, R.drawable.ic_user_profile)
                            }
                            if (it.result!!.userprofile!!.firstName != null && it.result!!.userprofile!!.lastName != null){
                                binding.parentname.text = it.result!!.userprofile!!.firstName + " " + it.result!!.userprofile!!.lastName
                            }else{
                                binding.parentname.text = "--/--"
                            }
                            if (it.result!!.userprofile != null && it.result!!.userprofile!!.role != null && it.result!!.userprofile!!.role!!.name != null){
                                binding.role.text = it.result!!.userprofile!!.role!!.name
                            }
                            else{
                                binding.role.text = "--/--"
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg, binding.root,false)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().profile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.userprofile != null){
                            if (it.result!!.userprofile!!.firstName != null && it.result!!.userprofile!!.lastName != null){
                                binding.StdName.text = it.result!!.userprofile!!.firstName + " " + it.result!!.userprofile!!.lastName
                            }else{
                                binding.StdName.text = "--/--"
                            }
                            if (it.result!!.studentPreference != null && it.result!!.studentPreference!!.studentClass != null){
                                SharedHelper(this).standard  = it.result!!.studentPreference!!.studentClass!!.name!!
                                binding.rollNo.text = "Roll No: ${it.result!!.userprofile!!.rollNo}"
                            }else{
                                binding.rollNo.text = "Roll No: --/-- "
                            }
                            if (it.result!!.studentPreference != null && it.result!!.studentPreference!!.studentClass != null){
                                SharedHelper(this).standard  = it.result!!.studentPreference!!.studentClass!!.name!!
                                binding.stdStand.text = UiUtils.getOrdinalSuffix(it.result!!.studentPreference!!.studentClass!!.name!!.toInt()) + " Std - 'B' Sec"
                            }
                            else{
                                binding.stdStand.text = "-- Std - 'B' Sec"
                            }
                            if (it.result!!.userprofile != null && it.result!!.userprofile!!.lead_id != null){
                                binding.admissionId.text = it.result!!.userprofile!!.lead_id
                            }else{
                                binding.admissionId.text = "--/--"
                            }
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
//        binding.status.text = "Active"
//        UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
//        UiUtils.textViewBgTint(binding.status, "#e6ffe7", null)
//        UiUtils.textViewTextColor(binding.status, "#32B138", null)

        binding.status.text = "Female"
        UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
        UiUtils.textViewBgTint(binding.status, "#F0E9FF", null)
        UiUtils.textViewTextColor(binding.status, "#5E31B8", null)

        binding.studentInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Child Information")
            bundle.putString("key1",sharedHelper.childId)
            BaseUtils.startActivity(this, StaffInformationActivity(),bundle,false)
        }
        binding.complaints.setOnClickListener {
            BaseUtils.startActivity(this, TeacherRaiseComplaintActivity(), null, false)
        }
        binding.settings.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("key","Notification")
            BaseUtils.startActivity(this, NotificationActivity(),bundle,false)
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
}