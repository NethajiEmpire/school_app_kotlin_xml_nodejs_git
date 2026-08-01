package com.lms.sch.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.databinding.ActivityOtpBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class OtpActivity : BaseActivity() {
    lateinit var binding : ActivityOtpBinding
    var key = -1
    var selected = ""
    private lateinit var page: String
    override lateinit var sharedHelper: SharedHelper
    var timer: CountDownTimer ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        key = intent.getIntExtra(Constants.IntentKeys.KEY,-1)
        var page = intent.getStringExtra(Constants.IntentKeys.KEY1).toString()
        binding.otpSendPage.visibility = View.VISIBLE
        binding.loginPage.visibility = View.GONE
        binding.forgotPage.visibility = View.GONE
        binding.otpVerifyPage.visibility = View.GONE
        binding.back.visibility = View.GONE

        loadOtp()

        if (BuildConfig.DEBUG){
////            binding.edtPhone.setText("7259628931")
//            binding.edtMail.setText("student@lms.com")
//             binding.edtMail.setText("student1@lms.com")
            binding.edtMail.setText("admin@vistaschool.com")
//             binding.edtPassword.setText("1234")
            binding.edtPassword.setText("Password@123")
//            binding.edtMail.setText("parent@lms.com")
//            binding.edtMail.setText("parent@gmail.com")
//            binding.edtPassword.setText("parent@123")
//            binding.edtPassword.setText("1234")
//            binding.edtMail.setText("teacher@lms.com")
//            binding.edtPassword.setText("teacher@123")
//            binding.edtMail.setText("admin@lms.com")
//            binding.edtPassword.setText("admin@123")

//            binding.edtPassword.setText("admin@123")
//              binding.edtMail.setText("teacher5@gmail.com")
//              binding.edtPassword.setText("1234")
        }
        binding.logo.setOnLongClickListener{
            if(BuildConfig.DEBUG){
                ApiDataDialog(this).show(this)
            }
            return@setOnLongClickListener true
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }

        val name = getColoredSpanned("Mobile", "#FF000000")
        val surName = getColoredSpanned("*", "#B32124")
        binding.mobileTxt.text = Html.fromHtml(name+" "+surName,FROM_HTML_MODE_LEGACY)

        val name1 = getColoredSpanned("Email", "#FF000000")
        val surName1 = getColoredSpanned("*", "#B32124")
        binding.emailTxt.text = Html.fromHtml(name1+" "+surName1,FROM_HTML_MODE_LEGACY)

        val name2 = getColoredSpanned("Password", "#FF000000")
        val surName2 = getColoredSpanned("*", "#B32124")
        binding.passTxt.text = Html.fromHtml(name2+" "+surName2,FROM_HTML_MODE_LEGACY)


        binding.loginWithEmail.setOnClickListener {
            binding.otpSendPage.visibility = View.GONE
            binding.loginPage.visibility = View.VISIBLE
            binding.forgotPage.visibility = View.GONE
            binding.otpVerifyPage.visibility = View.GONE
            binding.back.visibility = View.VISIBLE
            binding.edtPhone.setText("")
            binding.sendOtpErr.visibility = View.GONE
            if (timer != null){
                timer?.cancel()
            }
        }

        binding.changeMobile.setOnClickListener {
            if (timer != null){
                timer?.cancel()
            }
            if (selected == "forgotPass"){
                binding.otpSendPage.visibility = View.GONE
                binding.loginPage.visibility = View.GONE
                binding.forgotPage.visibility = View.VISIBLE
                binding.forgotPage1.visibility = View.VISIBLE
                binding.otpVerifyPage.visibility = View.GONE
                binding.back.visibility = View.VISIBLE
                binding.forgotMail.requestFocus()
                binding.forgotMail.setSelection(binding.forgotMail.text.length)
                BaseUtils.showForceKeyboard(binding.forgotMail)
                binding.fpOtp1.setText("")
                binding.fpOtp2.setText("")
                binding.fpOtp3.setText("")
                binding.fpOtp4.setText("")
                binding.fpOtp5.setText("")
                binding.fpOtp6.setText("")
                binding.forgetPage1Err.visibility = View.GONE
            }
            else {
                onBackPressed()
                binding.otpSendPage.visibility = View.VISIBLE
                binding.edtPhone.requestFocus()
                binding.edtPhone.setSelection(binding.edtPhone.text.length)
                BaseUtils.showForceKeyboard(binding.edtPhone)
                binding.otp1.setText("")
                binding.otp2.setText("")
                binding.otp3.setText("")
                binding.otp4.setText("")
                binding.otp5.setText("")
                binding.otp6.setText("")
                binding.verifyOtpErr.visibility = View.GONE
            }

        }

        binding.register.setOnClickListener {
            selected = "otpSend"
            binding.otpSendPage.visibility = View.VISIBLE
            binding.loginPage.visibility = View.GONE
            binding.forgotPage.visibility = View.GONE
            binding.otpVerifyPage.visibility = View.GONE
            binding.back.visibility = View.GONE
            binding.loginMailErr.visibility = View.GONE
            binding.loginErr.visibility = View.GONE
            if (timer != null){
                timer?.cancel()
            }
            binding.edtMail.setText("")
            binding.edtPassword.setText("")
        }

        binding.forgetPass.setOnClickListener {
            selected = "forgotPass"
            binding.otpSendPage.visibility = View.GONE
            binding.loginPage.visibility = View.GONE
            binding.forgotPage.visibility = View.VISIBLE
            binding.forgotPage1.visibility = View.VISIBLE
            binding.otpVerifyPage.visibility = View.GONE
            binding.back.visibility = View.VISIBLE
        }

        if (key == 2 && page == "login"){
            binding.loginWithEmail.performClick()
        }

        binding.resendOtp.setOnClickListener {
            sendOtp()
            if (timer != null){
                timer?.cancel()
            }
            binding.otp1.setText("")
            binding.otp2.setText("")
            binding.otp3.setText("")
            binding.otp4.setText("")
            binding.otp5.setText("")
            binding.otp6.setText("")
        }

        binding.edtPhone.filters = arrayOf(
            InputFilter.LengthFilter(10),
            InputFilter { source, start, end, dest, dstart, dend ->
                for (i in start until end) {
                    val char = source[i]
                    val isFirstPos = dstart == 0
                    if (isFirstPos && (char !in '6'..'9')) {
                        return@InputFilter ""
                    }
                    if (!char.isDigit()) {
                        return@InputFilter ""
                    }
                }
                null
            }
        )

        binding.sendOtp.setOnClickListener {
            val str = binding.edtPhone.text.toString()
            if(str.isEmpty()){
//                UiUtils.showSnack("Please Enter Mobile Number",binding.root,false)
                binding.sendOtpErr.text = "*Please Enter Mobile Number"
                binding.sendOtpErr.visibility = View.VISIBLE
            }
            else if(BaseUtils.isValidPhone(str)){
                if(str.length < 10 || str.length > 10){
//                    UiUtils.showSnack("Please Enter Valid Mobile Number",binding.root,false)
                    binding.sendOtpErr.text = "*Please Enter Valid Mobile Number"
                    binding.sendOtpErr.visibility = View.VISIBLE
                }
                else{
                    binding.sendOtpErr.visibility = View.GONE
                    sendOtp()
                }
            }
        }

        binding.edtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str = s.toString()
                when {
                    str.isEmpty() -> {
                        binding.sendOtpErr.text = "*Please Enter Mobile Number"
                        binding.sendOtpErr.visibility = View.VISIBLE
                    }
                    !BaseUtils.isMobileNumber(str) -> {
                        binding.sendOtpErr.text = "*Invalid Mobile Number Format"
                        binding.sendOtpErr.visibility = View.VISIBLE
                    }
                    str.length != 10 -> {
                        binding.sendOtpErr.text = "*Please Enter a 10-digit Mobile Number"
                        binding.sendOtpErr.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.sendOtpErr.text = ""
                        binding.sendOtpErr.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.verifyOtp.setOnClickListener {
            if(binding.otp1.text.toString().isNotEmpty() && binding.otp2.text.toString().isNotEmpty() && binding.otp3.text.toString().isNotEmpty() && binding.otp4.text.toString().isNotEmpty() && binding.otp5.text.toString().isNotEmpty() && binding.otp6.text.toString().isNotEmpty()){
                val otp = binding.otp1.text.toString()+binding.otp2.text.toString()+binding.otp3.text.toString()+binding.otp4.text.toString()+binding.otp5.text.toString()+binding.otp6.text.toString()
                verifyOtp(otp)
            }
            else{
//                UiUtils.showSnack("Please Enter OTP",binding.root,false)
                binding.verifyOtpErr.text = "*Please enter OTP"
                binding.verifyOtpErr.visibility = View.VISIBLE
            }
        }

        binding.edtMail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                when {
                    email.isEmpty() -> {
                        binding.loginMailErr.text = "*Please Enter Email Id"
                        binding.loginMailErr.visibility = View.VISIBLE
                    }
                    !BaseUtils.isValidEmail(email) -> {
                        binding.loginMailErr.text = "*Please Enter Valid Email Id"
                        binding.loginMailErr.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.loginMailErr.visibility = View.GONE
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                when {
                    password.isEmpty() -> {
                        binding.loginErr.text = "*Please Enter Password"
                        binding.loginErr.visibility = View.VISIBLE
                    }
                    password.length < 3 -> {
                        binding.loginErr.text = "*Please Enter Valid Password"
                        binding.loginErr.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.loginErr.visibility = View.GONE
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.login.setOnClickListener {
            if(binding.edtMail.text.isEmpty()){
                binding.loginMailErr.text = "*Please Enter Email Id"
                binding.loginMailErr.visibility = View.VISIBLE
            }
            else if(!BaseUtils.isValidEmail(binding.edtMail.text.toString())){
                binding.loginMailErr.text = "*Please Enter Valid Email Id"
                binding.loginMailErr.visibility = View.VISIBLE
            }
            else if(binding.edtPassword.text.isEmpty()){
                binding.loginErr.text = "*Please Enter Password"
                binding.loginErr.visibility = View.VISIBLE
                binding.loginMailErr.visibility = View.GONE
            }
//            else if(binding.edtPassword.text.length < 3){
//                binding.loginErr.text = "*Please Enter Valid Password"
//                binding.loginErr.visibility = View.VISIBLE
//                binding.loginMailErr.visibility = View.GONE
//            }
            else{
                binding.verifyOtpErr.visibility = View.GONE
                DialogUtils.showLoader(this)
                ApiConnection.getInstance().login(this,binding.edtMail.text.toString(),binding.edtPassword.text.toString()).observe(this) {
                    DialogUtils.dismissLoader()
                    it?.let {
                        it.success.let { success ->
                            if (success) {
                                if (it.data != null) {
                                    binding.verifyOtpErr.visibility = View.GONE
                                    sharedHelper.loggedIn = true
                                    sharedHelper.token = it.data.accessToken!!
                                    sharedHelper.name = BaseUtils.nullCheckerStr(it.data.user.name)
                                    sharedHelper.id = it.data.user.id!!
                                    sharedHelper.role = it.data.user.role?.uppercase() ?: ""
                                    sharedHelper.email = BaseUtils.nullCheckerStr(it.data.user.email)
                                    sharedHelper.mobileNumber = BaseUtils.nullCheckerStr(it.data.user.mobile)
                                    sharedHelper.imgUrl = ""
                                    moveNext()
                                }
                                else {
                                    binding.loginErr.text = "*${it.msg}"
                                    binding.loginErr.visibility = View.VISIBLE
//                                    UiUtils.showSnack(it.msg, binding.root,false)
                                }
                            }
                            else {
                                binding.loginErr.text = "*${it.msg}"
                                binding.loginErr.visibility = View.VISIBLE
                                UiUtils.showSnack(it.msg, binding.root,false)
                            }
                        }
                    }
                }
            }
        }

        binding.forgotMail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                when {
                    email.isEmpty() -> {
                        binding.forgetPage1Err.text = "*Please Enter Email Id"
                        binding.forgetPage1Err.visibility = View.VISIBLE
                        UiUtils.editTextImgDrawable(binding.forgotMail,null,"end")
                    }
                    !BaseUtils.isValidEmail(email) -> {
                        binding.forgetPage1Err.text = "*Please Enter Valid Email Id"
                        binding.forgetPage1Err.visibility = View.VISIBLE
                        UiUtils.editTextImgDrawable(binding.forgotMail,R.drawable.err,"end")
                    }
                    else -> {
                        binding.forgetPage1Err.visibility = View.GONE
                        UiUtils.editTextImgDrawable(binding.forgotMail,R.drawable.hugeicons_tick,"end")
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.forgotOtpSend.setOnClickListener {
            if(binding.forgotMail.text.isEmpty()){
                binding.forgetPage1Err.text = "*Please Enter Email Id"
                binding.forgetPage1Err.visibility = View.VISIBLE
            }
            else if(!BaseUtils.isValidEmail(binding.forgotMail.text.toString())){
                binding.forgetPage1Err.text = "*Please Enter Valid Email Id"
                binding.forgetPage1Err.visibility = View.VISIBLE
            }
            else{
                binding.forgetPage1Err.visibility = View.GONE
                DialogUtils.showLoader(this)
                ApiConnection.getInstance().forgotPassword(this,binding.forgotMail.text.toString()).observe(this) {
                    DialogUtils.dismissLoader()
                    it?.let {
                        it.success.let { success ->
                            if (success) {
                                UiUtils.showSnack(it.msg, binding.root,true)
                                binding.forgotPage1.visibility = View.GONE
                                binding.otpVerifyPage.visibility = View.VISIBLE
                                timer()
                                binding.pnhText.text = "Check your mail for the OTP which we have sent to ${binding.forgotMail.text} and enter it to verify your account."
                                binding.back.visibility = View.VISIBLE
                                binding.changeMobile.text = "Change Email"
                            }
                            else {
                                Log.d("ggfdhgfjd",it.msg)
                                binding.forgetPage1Err.text = "*${it.msg}"
                                Log.d("ggfdhgfjd",it.msg)

                                binding.forgetPage1Err.visibility = View.VISIBLE
                                Log.d("ggfdhgfjd",it.msg)
                                UiUtils.showSnack(it.msg, binding.root,false)
                                Log.d("ggfdhgfjd",it.msg)
                            }
                        }
                    }
                }
            }
        }

        binding.eye.setOnClickListener {
            binding.eye.isSelected = !binding.eye.isSelected
            if(binding.eye.isSelected){
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
            }
            else{
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
            }
        }

        loadOtp()
    }

    private fun getColoredSpanned(text: String, color: String): String {
        val input = "<font color=$color>$text</font>"
        return input
    }

    private fun moveNext(){
        if (sharedHelper.role == "PARENT"){
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().parentProfile(this).observe(this){
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.data != null && it.data!!.userprofile != null){
                                sharedHelper.childList = it.data!!.userprofile!!.students!!
                                Log.d("child Id", "moveNext: ${"=-----"}")
                                for (items in it.result!!.userprofile!!.students!!){
                                    Log.d("child Id", "moveNext: ${"=-forrr----"}")
                                    if (items.current_user){
                                        Log.d("child Id", "moveNext: ${items.user_id!!._id!!}")
                                        sharedHelper.childId = items.user_id!!._id!!
                                        Log.d("child Id after", "moveNext: ${items.user_id!!._id!!}")
                                        val firstName = items.user_id?.firstName ?: ""
                                        val lastName = items.user_id?.lastName ?: ""
                                        sharedHelper.childName = "${firstName} ${lastName}"
                                        Log.d("childNAmehere", "moveNext: ${sharedHelper.childName}")
                                    }
                                    break
                                }
                                BaseUtils.startActivity(this,DashBoardActivity(),null,true)
                            }
                            else{
                                UiUtils.showSnack(it.msg,binding.root,false)
                            }
                        }else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                }
            }
        }
        else {
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().profile(this).observe(this){
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.data != null){
                                if (it.data!!.userprofile != null && it.data!!.userprofile!!.registrationFee == true){
                                    val bundle = Bundle()
                                    var isBelow5 = false
                                    var std = 0
                                    if (it.data!!.studentPreference != null && it.data!!.studentPreference!!.studentClass != null && it.data!!.studentPreference!!.studentClass!!.name!!.isNotEmpty()){
                                        std = it.data!!.studentPreference!!.studentClass!!.name!!.toInt()
                                        sharedHelper.standard = std.toString()
                                    }
                                    if (std <= 5){
                                        isBelow5 = true
                                    }
                                    if (sharedHelper.role == "GUEST"){
                                        BaseUtils.startActivity(this, GuestActivity(),null,true)
                                    }
                                    else {
                                        bundle.putBoolean("isBelow5",isBelow5)
                                        BaseUtils.startActivity(this,DashBoardActivity(),bundle,true)
                                    }
                                }
                                else{
                                    if (sharedHelper.role == "GUEST"){
                                        BaseUtils.startActivity(this,GuestFeeRegistrationActivity(),null,true)
                                    }
                                    else {
                                        BaseUtils.startActivity(this,DashBoardActivity(),null,true)
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
        }
        /*if(sharedHelper.role == "GUEST") {
            BaseUtils.startActivity(this, GuestFeeRegistrationActivity(), null, true)
        }
        else {
            BaseUtils.startActivity(this,DashBoardActivity(),null,false)
        }*/
    }

    private fun sendOtp(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().sendOTPMobile(this,binding.edtPhone.text.toString()).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        binding.pnhText.text = "Check your phone for the OTP which we have sent to ${binding.edtPhone.text} and enter it to verify your account."
                        timer()
                        binding.otpSendPage.visibility = View.GONE
                        binding.otpVerifyPage.visibility = View.VISIBLE
                        binding.back.visibility = View.VISIBLE
                    }
                    else {
                        binding.sendOtpErr.text = "*${it.msg}"
                        binding.sendOtpErr.visibility = View.VISIBLE
//                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }

    private fun verifyOtp(otp:String){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().verifyOTPMobile(this,binding.edtPhone.text.toString(),otp).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
//                        UiUtils.showSnack(it.msg, binding.root,true)
                        sharedHelper.loggedIn = true
                        sharedHelper.token = it.data!!.accessToken!!
                        sharedHelper.id = it.data!!.user!!.id!!
                        sharedHelper.role = it.data!!.user!!.role?.uppercase() ?: ""
                        sharedHelper.name = it.data!!.user!!.name!!
                        sharedHelper.email = BaseUtils.nullCheckerStr(it.data!!.user!!.email)
                        sharedHelper.mobileNumber = BaseUtils.nullCheckerStr(it.data!!.user!!.mobile)
                        sharedHelper.imgUrl = ""

                        moveNext()
                    }
                    else {
                        binding.verifyOtpErr.text = "*${it.msg}"
                        binding.verifyOtpErr.visibility = View.VISIBLE
//                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }

    private fun timer(){
        binding.resendOtp.visibility = View.VISIBLE
        binding.timer.visibility = View.VISIBLE
        binding.resendOtp.isEnabled = false
        timer = object: CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val text = "${millisUntilFinished / 1000}${"s"}"
                binding.resendOtp.text = "Please wait..."
                UiUtils.textviewImgDrawable(binding.resendOtp,null,"end")
                binding.timer.text = "Resend available in: $text"
            }

            override fun onFinish() {
                UiUtils.textviewImgDrawable(binding.resendOtp,R.drawable.ic_arrow_forward,"end")
                UiUtils.setTextViewDrawableColor(binding.resendOtp,null,R.color.colorPrimary)
                binding.resendOtp.text = "Resend OTP"
                binding.resendOtp.isEnabled = true
                binding.timer.visibility = View.GONE
            }
        }
        timer?.start()
    }

    private fun loadOtp() {
        val otpFields = listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4, binding.otp5, binding.otp6)

        fun checkOtpFields() {
            val areAllFieldsFilled = otpFields.all { it.text.toString().isNotEmpty() }
            if (areAllFieldsFilled) {
                binding.verifyOtpErr.visibility = View.GONE
            } else {
                binding.verifyOtpErr.text = "*Please enter the complete OTP"
                binding.verifyOtpErr.visibility = View.VISIBLE
            }
        }

        otpFields.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                    checkOtpFields()
                }
                override fun afterTextChanged(editable: Editable) {
                    if (editable.isNotEmpty()) {
                        if (index < otpFields.size - 1) {
                            otpFields[index + 1].requestFocus()
                        }
                    }
                }
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (editText.text.toString().isEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus()
                        otpFields[index - 1].setSelection(otpFields[index - 1].text.length)
                    }
                }
                false
            }
        }
    }

    override fun onBackPressed() {
        if (binding.loginPage.visibility == View.VISIBLE){
            binding.otpSendPage.visibility = View.VISIBLE
            binding.loginPage.visibility = View.GONE
            binding.otpVerifyPage.visibility = View.GONE
            binding.forgotPage.visibility = View.GONE
            binding.back.visibility = View.GONE
            if (timer != null){
                timer?.cancel()
            }
        }
        else if (binding.otpVerifyPage.visibility == View.VISIBLE){
            binding.otpSendPage.visibility = View.VISIBLE
            binding.loginPage.visibility = View.GONE
            binding.otpVerifyPage.visibility = View.GONE
            binding.forgotPage.visibility = View.GONE
            binding.back.visibility = View.GONE
            if (timer != null){
                timer?.cancel()
            }
        }
        else if (binding.forgotPage.visibility == View.VISIBLE){
            binding.loginPage.visibility = View.VISIBLE
            binding.otpSendPage.visibility = View.GONE
            binding.otpVerifyPage.visibility = View.GONE
            binding.forgotPage.visibility = View.GONE
            binding.back.visibility = View.VISIBLE
        }
        else {
            super.onBackPressed()
        }
    }
}