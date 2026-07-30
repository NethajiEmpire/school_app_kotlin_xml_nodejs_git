package com.lms.sch.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lms.sch.R
import com.lms.sch.databinding.ActivityStudentProfileBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetStudentResponse
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.response.StudentSingleVIewResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class StudentProfileActivity : BaseActivity() {
    lateinit var binding : ActivityStudentProfileBinding
    var  result = ProfileDetailsResponse.Result()
    var stdId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stdId = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        getStudentProfile()
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }
    fun getStudentProfile() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().student(this,stdId).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                if (it.success) {
                    if (it.result != null) {
                        result = it.result!!
                        binding.rollNbr.text = result.rollNo!!
                        if (result.userprofile != null){
                            binding.name.text = "${result!!.userprofile!!.firstName} ${result!!.userprofile!!.lastName!!}"

                            binding.stdName.text = "${result!!.userprofile!!.firstName} ${result!!.userprofile!!.lastName!!}"
                        }else{
                            binding.name.text = "--/--"
                        }
                        if (result.studentPreference != null && result.studentPreference!!.studentClass != null && result.studentPreference!!.section != null){
                            binding.grade.text = "${UiUtils.getOrdinalSuffix(result.studentPreference!!.studentClass!!.name!!.toInt())} | ${result.studentPreference!!.rollNo}"
                            binding.studentstd.text = "${UiUtils.getOrdinalSuffix(result.studentPreference!!.studentClass!!.name!!.toInt())} - ${result.studentPreference!!.section!!.name} - Sec"
                        }else{
                            binding.grade.text = "--/--"
                            binding.studentstd.text = "--/--"
                        }
                        if (result.applicationForm!!.studentInfo != null){
                            binding.gender.text = result.applicationForm!!.studentInfo!!.gender!!
                            binding.dob.text = result.applicationForm!!.studentInfo!!.dob!!
                            binding.bg.text = result.applicationForm!!.studentInfo!!.blood_group!!
                            binding.adress.text = "${result.applicationForm!!.studentInfo!!.address},${result.applicationForm!!.studentInfo!!.city},${result.applicationForm!!.studentInfo!!.state},${result.applicationForm!!.studentInfo!!.country}"
                        }else{
                            binding.adress.text = "--/--"
                        }
                        if (result.applicationForm!!.parentInfo != null){
                            binding.fatherName.text = result.applicationForm!!.parentInfo!!.fatherName!!
                            binding.fatherMail.text = result.applicationForm!!.parentInfo!!.parentsEmail!!
                            binding.motherName.text = result.applicationForm!!.parentInfo!!.motherName!!
                            binding.matherEmail.text = result.applicationForm!!.parentInfo!!.parentsEmail!!
                        }else{
                            binding.matherEmail.text = "--/--"
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                } else {
                    UiUtils.showSnack(it.msg, binding.root, false)
                }
            }
        }
    }
}
