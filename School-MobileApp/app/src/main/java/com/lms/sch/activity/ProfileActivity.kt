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
import com.lms.sch.databinding.ActivityProfileBinding
import com.lms.sch.databinding.DialogLogoutBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import kotlin.math.roundToInt

class ProfileActivity : BaseActivity() {
    lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.version.text = "Version " + BaseUtils.getVersionName(this)
        binding.back.setOnClickListener {
            finish()
        }

        binding.scoreBoardLay.setOnClickListener{
            BaseUtils.startActivity(this, ActivityScoreBoard(),null,false)
        }
        binding.settings.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("key","Notification")
            BaseUtils.startActivity(this, NotificationActivity(),bundle,false)
        }

        binding.leaderBoardLay.setOnClickListener{
            BaseUtils.startActivity(this, LeaderBoardActivity(),null,false)
        }
        binding.studentInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","My Information")
            bundle.putString("key1",sharedHelper.id)
            BaseUtils.startActivity(this, StaffInformationActivity(),bundle,false)
        }
        binding.myTeacherInfo.setOnClickListener {
            BaseUtils.startActivity(this, MyTeacherActivity(),null,false)
        }

        binding.complaints.setOnClickListener {
            BaseUtils.startActivity(this, TeacherRaiseComplaintActivity(), null, false)
        }
        binding.leaveRequeststd.setOnClickListener {
            BaseUtils.startActivity(this, StudentLeaveActivity(), null, false)
        }
        binding.settings.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Settings")
            BaseUtils.startActivity(this, NotificationActivity(),bundle,false)
        }

        ApiConnection.getInstance().profile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.userprofile != null){
                            if (it.result!!.userprofile!!.firstName != null && it.result!!.userprofile!!.lastName != null){
                                binding.name.text = it.result!!.userprofile!!.firstName + " " + it.result!!.userprofile!!.lastName
                            }else{
                                binding.name.text = "--/--"
                            }
                            if (it.result!!.studentPreference != null && it.result!!.studentPreference!!.studentClass != null){
                                SharedHelper(this).standard  = it.result!!.studentPreference!!.studentClass!!.name!!
                                binding.grade.text = UiUtils.getOrdinalSuffix(it.result!!.studentPreference!!.studentClass!!.name!!.toInt()) + " Standard | Roll No: ${it.result!!.userprofile!!.rollNo}"
                            }
                            else{
                                binding.grade.text = "--/-- Standard | Roll No: 29"
                            }
                            if (it.result!!.userprofile!!.img_url != null && it.result!!.userprofile!!.img_url!!.isNotEmpty()){
                                UiUtils.loadImage(binding.img,it.result!!.userprofile!!.img_url)
                            }
                            else{
                                UiUtils.imageviewDrawable(binding.img,R.drawable.ic_user_profile)
                            }

                        }                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getStatsProgressPoints(this).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){

                            if (it.result!!.rank != null) {
                                binding.rank.text = "${it.result!!.rank.toString()} th"
                            } else {
                                binding.rank.text = "0 th"
                            }

                            if (it.result!!.score != null){
                                binding.points.text = it.result!!.score!!.toString()
                            }
                            else{
                                binding.points.text = "0"
                            }

                            if (it.result!!.rank != null) {
                                binding.place.text = "${it.result!!.rank.toString()} th Place"
                            } else {
                                binding.place.text = "0 th Place"
                            }
                        }
                        else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
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
}