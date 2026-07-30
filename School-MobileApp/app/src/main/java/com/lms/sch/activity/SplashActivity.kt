package com.lms.sch.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.lms.sch.R
import com.lms.sch.databinding.ActivitySplashBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    lateinit var sharedHelper: SharedHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        UiUtils. notificationBar(this,null, R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedHelper = SharedHelper(this)
        checkLogin()
    }

    fun checkLogin(){
        if(sharedHelper.loggedIn){
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().profile(this).observe(this){
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.result != null){
                                if (it.result!!.userprofile != null && it.result!!.userprofile!!.registrationFee == true){
                                    val bundle = Bundle()
                                    var isBelow5 = false
                                    var std = 0
                                    if (it.result!!.studentPreference != null && it.result!!.studentPreference!!.studentClass != null && it.result!!.studentPreference!!.studentClass!!.name!!.isNotEmpty()){
                                        std = it.result!!.studentPreference!!.studentClass!!.name!!.toInt()
                                        sharedHelper.standard = std.toString()
                                    }
                                    if (std <= 5){
                                        isBelow5 = true
                                    }
                                    if (sharedHelper.role == "GUEST"){
                                        BaseUtils.startActivity(this@SplashActivity, GuestActivity(),null,true)
                                    }
                                    else {
                                        bundle.putBoolean("isBelow5",isBelow5)
                                        BaseUtils.startActivity(this@SplashActivity,DashBoardActivity(),bundle,true)
                                    }
                                }
                                else {
                                    if (sharedHelper.role == "GUEST"){
                                        BaseUtils.startActivity(this@SplashActivity,GuestFeeRegistrationActivity(),null,true)
                                    }
                                    else {
                                        BaseUtils.startActivity(this@SplashActivity,DashBoardActivity(),null,true)
                                    }
                                }
                            }
                       }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                }
            }
            UiUtils.log("SDF",""+sharedHelper.role)

        }
        else{
            if (sharedHelper.isGuestLandingOpen){
                BaseUtils.startActivity(this@SplashActivity,OtpActivity(),null,true)
            }
            else {
                BaseUtils.startActivity(this@SplashActivity, GuestLandingActivity(), null, true)
            }
        }
    }
}