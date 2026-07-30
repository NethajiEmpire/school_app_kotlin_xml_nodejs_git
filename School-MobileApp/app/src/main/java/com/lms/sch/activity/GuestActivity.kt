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
import com.lms.sch.R
import com.lms.sch.databinding.ActivityGuestBinding
import com.lms.sch.databinding.DialogLogoutBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedPref
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import kotlin.math.roundToInt

class GuestActivity : BaseActivity() {
    lateinit var binding: ActivityGuestBinding
    var result = ProfileDetailsResponse.Result()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            finish()
        }
        binding.tutorName.text= "S. Geetha Lakshmi"
        binding.tutorDesignation1.text = "Mentor"

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

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().profile(this).observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null) {
                                /*if (it.result?.currentStep == 1 && it.result?.userProfile?.status == ""){

                                }*/
                                result = it.result!!
                                loadStep(it.result!!)
                            }
                        }
                    }
                }
            }
        }

        binding.includeConvert.login.setOnClickListener {
            TempSingleton.clearAllValues()
            SharedPref(this).clearAll()
            val bundle = Bundle()
            bundle.putInt(Constants.IntentKeys.KEY,2)
            bundle.putString(Constants.IntentKeys.KEY1,"login")
            BaseUtils.startActivity(this, OtpActivity(), bundle, true)
        }

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().profile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            /*if (it.result?.currentStep == 1 && it.result?.userProfile?.status == ""){

                            }*/
                            result = it.result!!
                            loadStep(it.result!!)
                        }
                    }
                }
            }
        }
    }

    private fun loadStep(data: ProfileDetailsResponse.Result) {
        // val value = 2
        var status = data.userprofile?.status
        val value =  data.userprofile?.currentStep!!
        UiUtils.log("uygf", "c step " + value)
        UiUtils.log("uygf", "status " + status)
        if (value == 1) {
            var bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY, status)
            BaseUtils.startActivity(this, ApplicationActivity(), null, false)
        }

        if (value == 0) {
            val progress = 10
            binding.includeGuest.txtProgress.text = "$progress%"
            binding.includeGuest.bar.progress = progress

            UiUtils.textviewCustomDrawable(binding.includeGuest.txt1, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt1, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt1, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt2, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt2, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt3, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt3, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt4, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt4, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt5, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt5, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt6, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt6, null, R.color.black)
            binding.includeGuest.txt1Value.text = "On process"
            UiUtils.textViewTextColor(binding.includeGuest.txt1Value, "#D9981B", null)
            binding.includeGuest.txt2Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt2Value, "#A3A3A3", null)
            binding.includeGuest.txt3Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt3Value, "#A3A3A3", null)
            binding.includeGuest.txt3Value.visibility = View.VISIBLE
            binding.includeGuest.docImg.visibility = View.INVISIBLE
            binding.includeGuest.txt4Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt4Value, "#A3A3A3", null)
            binding.includeGuest.txt5Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt5Value, "#A3A3A3", null)
            binding.includeGuest.txt6Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt6Value, "#A3A3A3", null)
            UiUtils.imageviewDrawable(binding.includeGuest.img1, R.drawable.ic_progress)
            UiUtils.imageviewDrawable(binding.includeGuest.img2, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img3, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img4, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img5, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img6, R.drawable.ic_pending)
            UiUtils.viewBgColor(binding.includeGuest.view1, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view2, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view3, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view4, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view5, "#A3A3A3", null)
            binding.includeGuest.constraint1.setOnClickListener {
                var bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY, status)
                BaseUtils.startActivity(this, ApplicationActivity(), null, false)
            }
        }
        else if (value == 1 || value == 2 || value == 3) {
            when(value) {
                1 -> {
                    val progress = 20
                    binding.includeGuest.txtProgress.text = "$progress%"
                    binding.includeGuest.bar.progress = progress
                }
                2 -> {
                    val progress = 30
                    binding.includeGuest.txtProgress.text = "$progress%"
                    binding.includeGuest.bar.progress = progress
                }
                3 -> {
                    val progress = 40
                    binding.includeGuest.txtProgress.text = "$progress%"
                    binding.includeGuest.bar.progress = progress
                }
            }

            UiUtils.textviewCustomDrawable(binding.includeGuest.txt1, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt1, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt1, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt2, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt2, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt2, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt3, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt3, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt4, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt4, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt5, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt5, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt6, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt6, null, R.color.black)
            binding.includeGuest.txt1Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt1Value, "#28C76F", null)
            binding.includeGuest.txt2Value.text = "On process"
            UiUtils.textViewTextColor(binding.includeGuest.txt2Value, "#D9981B", null)
            binding.includeGuest.txt3Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt3Value, "#A3A3A3", null)
            binding.includeGuest.txt3Value.visibility = View.VISIBLE
            binding.includeGuest.docImg.visibility = View.INVISIBLE
            binding.includeGuest.txt4Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt4Value, "#A3A3A3", null)
            binding.includeGuest.txt5Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt5Value, "#A3A3A3", null)
            binding.includeGuest.txt6Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt6Value, "#A3A3A3", null)
            UiUtils.imageviewDrawable(binding.includeGuest.img1, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img2, R.drawable.ic_progress)
            UiUtils.imageviewDrawable(binding.includeGuest.img3, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img4, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img5, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img6, R.drawable.ic_pending)
            UiUtils.viewBgColor(binding.includeGuest.view1, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view2, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view3, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view4, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view5, "#A3A3A3", null)
            binding.includeGuest.constraint2.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY, data.userprofile?.status)
                BaseUtils.startActivity(this, ApplicationActivity(), bundle, false)
            }
        }
        else if (value == 4) {
            val progress = 50
            binding.includeGuest.txtProgress.text = "$progress%"
            binding.includeGuest.bar.progress = progress

            UiUtils.textviewCustomDrawable(binding.includeGuest.txt1, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt1, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt1, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt2, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt2, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt2, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt3, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt3, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt3, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt4, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt4, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt5, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt5, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt6, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt6, null, R.color.black)
            binding.includeGuest.txt1Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt1Value, "#28C76F", null)
            binding.includeGuest.txt2Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt2Value, "#28C76F", null)
            binding.includeGuest.txt3Value.text = "On process"
            UiUtils.textViewTextColor(binding.includeGuest.txt3Value, "#D9981B", null)
            binding.includeGuest.txt3Value.visibility = View.VISIBLE
            binding.includeGuest.docImg.visibility = View.INVISIBLE
            binding.includeGuest.txt4Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt4Value, "#A3A3A3", null)
            binding.includeGuest.txt5Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt5Value, "#A3A3A3", null)
            binding.includeGuest.txt6Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt6Value, "#A3A3A3", null)
            UiUtils.imageviewDrawable(binding.includeGuest.img1, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img2, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img3, R.drawable.ic_progress)
            UiUtils.imageviewDrawable(binding.includeGuest.img4, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img5, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img6, R.drawable.ic_pending)
            UiUtils.viewBgColor(binding.includeGuest.view1, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view2, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view3, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view4, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view5, "#A3A3A3", null)
            binding.includeGuest.constraint3.setOnClickListener {
                val bundle = Bundle()
                val status = data.userprofile?.status
                bundle.putString(Constants.IntentKeys.KEY, status)
                BaseUtils.startActivity(this, ApplicationActivity(), bundle, false)
                /*if (status != null && status != "application_form_pending" && status != "academic_info_pending") {

                }*/
            }
        }
        else if (value == 5) {
            val progress = 70
            binding.includeGuest.txtProgress.text = "$progress%"
            binding.includeGuest.bar.progress = progress

            UiUtils.textviewCustomDrawable(binding.includeGuest.txt1, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt1, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt1, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt2, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt2, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt2, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt3, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt3, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt3, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt4, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt4, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt4, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt5, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt5, null, R.color.black)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt6, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt6, null, R.color.black)
            binding.includeGuest.txt1Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt1Value, "#28C76F", null)
            binding.includeGuest.txt2Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt2Value, "#28C76F", null)
            binding.includeGuest.txt3Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt3Value, "#28C76F", null)
            binding.includeGuest.docImg.visibility = View.GONE
            binding.includeGuest.txt3Value.visibility = View.VISIBLE
            binding.includeGuest.txt4Value.text = "On process"
            //  binding.includeGuest.txt4Value.visibility = View.GONE
            UiUtils.textViewTextColor(binding.includeGuest.txt4Value, "#D9981B", null)
            binding.includeGuest.txt5Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt5Value, "#A3A3A3", null)
            binding.includeGuest.txt6Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt6Value, "#A3A3A3", null)
            UiUtils.imageviewDrawable(binding.includeGuest.img1, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img2, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img3, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img4, R.drawable.ic_progress)
            UiUtils.imageviewDrawable(binding.includeGuest.img5, R.drawable.ic_pending)
            UiUtils.imageviewDrawable(binding.includeGuest.img6, R.drawable.ic_pending)
            UiUtils.viewBgColor(binding.includeGuest.view1, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view2, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view3, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view4, "#A3A3A3", null)
            UiUtils.viewBgColor(binding.includeGuest.view5, "#A3A3A3", null)
            binding.includeGuest.constraint4.setOnClickListener {
                val bundle = Bundle()
                val status = data.userprofile?.status
                if (status != null && status == "paymentInfoPen"){
                    bundle.putString(Constants.IntentKeys.KEY,"payment")
                    BaseUtils.startActivity(this, ApplicationActivity(),bundle,false)
                }
//                bundle.putString(Constants.IntentKeys.KEY, "payment")
//                bundle.putString(Constants.IntentKeys.KEY1, "")
//                BaseUtils.startActivity(this, ApplicationActivity(), bundle, false)
            }
        }
        else if (value == 6 && status == "acknowledgementPen") {
            val progress = 90
            binding.includeGuest.txtProgress.text = "$progress%"
            binding.includeGuest.bar.progress = progress

            UiUtils.textviewCustomDrawable(binding.includeGuest.txt1, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt1, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt1, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt2, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt2, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt2, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt3, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt3, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt3, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt4, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt4, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt4, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt5, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt5, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt5, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt6, R.drawable.ic_round_line)
            UiUtils.textViewTextColor(binding.includeGuest.txt6, null, R.color.black)
            binding.includeGuest.txt1Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt1Value, "#28C76F", null)
            binding.includeGuest.txt2Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt2Value, "#28C76F", null)
            binding.includeGuest.txt3Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt3Value, "#28C76F", null)
            //   binding.includeGuest.txt4Value.visibility = View.GONE
            binding.includeGuest.txt3Value.visibility = View.VISIBLE
            binding.includeGuest.docImg.visibility = View.GONE
            binding.includeGuest.txt4Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt4Value, "#28C76F", null)
            binding.includeGuest.txt5Value.text = "On process"
            UiUtils.textViewTextColor(binding.includeGuest.txt5Value, "#D9981B", null)
            binding.includeGuest.txt6Value.text = "Pending"
            UiUtils.textViewTextColor(binding.includeGuest.txt6Value, "#A3A3A3", null)
            UiUtils.imageviewDrawable(binding.includeGuest.img1, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img2, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img3, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img4, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img5, R.drawable.ic_progress)
            UiUtils.imageviewDrawable(binding.includeGuest.img6, R.drawable.ic_pending)
            UiUtils.viewBgColor(binding.includeGuest.view1, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view2, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view3, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view4, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view5, "#A3A3A3", null)
            binding.includeGuest.constraint5.setOnClickListener {
                val bundle = Bundle()
                val status = data.userprofile?.status
                /*if (status != null && status != "approval_pending"){

                }*/
                bundle.putString(Constants.IntentKeys.KEY, status)
                BaseUtils.startActivity(this, ApplicationActivity(), bundle, false)
            }
        }
        else if (value == 7 && status == "verificationpen") {
            val progress = 100
            binding.includeGuest.txtProgress.text = "$progress%"
            binding.includeGuest.bar.progress = progress

            UiUtils.textviewCustomDrawable(binding.includeGuest.txt1, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt1, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt1, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt2, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt2, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt2, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt3, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt3, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt3, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt4, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt4, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt4, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt5, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt5, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt5, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt6, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt6, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt6, null, R.color.white)
            binding.includeGuest.txt1Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt1Value, "#28C76F", null)
            binding.includeGuest.txt2Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt2Value, "#28C76F", null)
            binding.includeGuest.txt3Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt3Value, "#28C76F", null)
            //  binding.includeGuest.txt4Value.visibility = View.GONE
            binding.includeGuest.txt3Value.visibility = View.VISIBLE
            binding.includeGuest.docImg.visibility = View.GONE
            binding.includeGuest.txt4Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt4Value, "#28C76F", null)
            binding.includeGuest.txt5Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt5Value, "#28C76F", null)
            binding.includeGuest.txt6Value.text = "On process"
            UiUtils.textViewTextColor(binding.includeGuest.txt6Value, "#D9981B", null)
            UiUtils.imageviewDrawable(binding.includeGuest.img1, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img2, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img3, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img4, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img5, R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img6, R.drawable.ic_progress)
            UiUtils.viewBgColor(binding.includeGuest.view1, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view2, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view3, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view4, "#28C76F", null)
            UiUtils.viewBgColor(binding.includeGuest.view5, "#28C76F", null)
            binding.includeGuest.constraint6.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY, "verificationPen")
                BaseUtils.startActivity(this, ApplicationActivity(), bundle, false)
            }
        }
        else if (value == 7 && status == "verified") {
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt1, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt1, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt1, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt2, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt2, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt2, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt3, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt3, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt3, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt4, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt4, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt4, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt5, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt5, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt5, null, R.color.white)
            UiUtils.textviewCustomDrawable(binding.includeGuest.txt6, R.drawable.ic_round)
            UiUtils.textViewBgTint(binding.includeGuest.txt6, "#333333", null)
            UiUtils.textViewTextColor(binding.includeGuest.txt6, null, R.color.white)
            binding.includeGuest.txt1Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt1Value, "#28C76F", null)
            binding.includeGuest.txt2Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt2Value, "#28C76F", null)
            binding.includeGuest.txt3Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt3Value, "#28C76F", null)
            binding.includeGuest.txt4Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt4Value, "#28C76F", null)
            binding.includeGuest.txt5Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt5Value, "#28C76F", null)
            binding.includeGuest.txt6Value.text = "Done"
            UiUtils.textViewTextColor(binding.includeGuest.txt6Value,"#28C76F",null)
            UiUtils.imageviewDrawable(binding.includeGuest.img1,R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img2,R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img3,R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img4,R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img5,R.drawable.ic_complete)
            UiUtils.imageviewDrawable(binding.includeGuest.img6,R.drawable.ic_complete)
            UiUtils.viewBgColor(binding.includeGuest.view1,"#28C76F",null)
            UiUtils.viewBgColor(binding.includeGuest.view2,"#28C76F",null)
            UiUtils.viewBgColor(binding.includeGuest.view3,"#28C76F",null)
            UiUtils.viewBgColor(binding.includeGuest.view4,"#28C76F",null)
            UiUtils.viewBgColor(binding.includeGuest.view5,"#28C76F",null)
//            ApiConnection.getInstance().signOut(this)
            binding.includeConvert.root.visibility = View.VISIBLE

        }
    }
    override fun onResume() {
        super.onResume()
        if (TempSingleton.getInstance().isFormComplete) {
            loadStep(result)
        }
    }
}