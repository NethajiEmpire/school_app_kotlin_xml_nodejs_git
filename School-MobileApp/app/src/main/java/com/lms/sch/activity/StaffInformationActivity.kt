package com.lms.sch.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.UserDetailsAdapter
import com.lms.sch.databinding.ActivityStaffInformationBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject

class StaffInformationActivity : BaseActivity() {
    lateinit var binding: ActivityStaffInformationBinding
    var key = ""
    var userId = ""
    var result = ProfileDetailsResponse.ApplicationForm()
    var list = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStaffInformationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (key != null ){
            key = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        }
        binding.pageHeading.text = key
        userId = intent.getStringExtra(Constants.IntentKeys.KEY1)!!

        DialogUtils.showLoader(this)
        if (sharedHelper.role == "ADMIN"){
            ApiConnection.getInstance().profile(this,userId).observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.result != null && it.result!!.applicationForm!= null){
                                result = it.result!!.applicationForm!!
                                if (result.studentInfo != null){
                                    binding.staffName.text = result.studentInfo!!.firstName+" "+result.studentInfo!!.lastName
                                    binding.email.text = result.studentInfo!!.email
                                    binding.mobile.text = result.studentInfo!!.mobile
                                    binding.role.text = "--/--"
                                    binding.personalInfoLay.performClick()
                                }
                            }
                        }
                        else {
//                            UiUtils.showSnack(it.msg, binding.root,false)
                        }
                    }
                }
            }
        }
        else {
            ApiConnection.getInstance().profile(this).observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    Log.d("poiuytrdxcv", it.toString())
                    it.success.let { success->
                        Log.d("poiuytrdxcv", "Success: $success")
                        if (success){
                            if (it.result!!.userprofile != null){
                                Log.d("poiuytrdxcv", "UserProfile present: ${it.result!!.userprofile}")
                                if (it.result!!.userprofile!!.firstName != null && it.result!!.userprofile!!.lastName != null){
                                    binding.staffName.text = it.result!!.userprofile!!.firstName+" "+it.result!!.userprofile!!.lastName
                                }
                                binding.email.text = it.result!!.userprofile!!.email
                                binding.mobile.text = it.result!!.userprofile!!.mobile
                                if (it.result!!.userprofile!!.role != null){
                                    binding.role.text = it.result!!.userprofile!!.role!!.name
                                }
                            }
                            else {
                                Log.e("poiuytrdxcv", "UserProfile is null")
                                binding.staffName.text = "--/--"
                                binding.email.text = "--/--"
                                binding.mobile.text = "--/--"
                                binding.role.text = "--/--"
                            }
                            binding.personalInfoLay.performClick()
                        }
                        else {
                            Log.e("poiuytrdxcv", "API success is false: ${it.msg}")
                            UiUtils.showSnack(it.msg, binding.root,false)
                        }
                    }
                }
            }
        }

        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        val roles = sharedHelper.role
        if (roles == "PARENT") {
            binding.persName.text = "Child Information"
            binding.parentName.text = "My Information"
            binding.academicName.text = "Academic Details"
            binding.documentInfoLay.visibility = View.GONE
        }

        binding.personalInfoLay.setOnClickListener {
            val role = sharedHelper.role
            if (role == "PARENT"){
                binding.persName.text = "Child Information"
                if(binding.personalInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val studentInfo = result.studentInfo
                    val json1 = JSONObject()
                    json1.put("label", "Date of Birth")
                    json1.put("type","text")
                    json1.put("value", if (studentInfo != null && studentInfo.dob!!.isNotEmpty()) studentInfo.dob else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Gender")
                    json2.put("type","text")
                    json2.put("value", if (studentInfo != null && studentInfo.gender!!.isNotEmpty()) studentInfo.gender else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Blood Group")
                    json3.put("type","text")
                    json3.put("value", if (studentInfo != null && studentInfo.blood_group!!.isNotEmpty()) studentInfo.blood_group else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Address")
                    json4.put("type","text")
                    json4.put("value", if (studentInfo != null && studentInfo.address!!.isNotEmpty()) studentInfo.address else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "State")
                    json5.put("type","text")
                    json5.put("value", if (studentInfo != null && studentInfo.state!!.isNotEmpty()) studentInfo.state else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "City")
                    json6.put("type","text")
                    json6.put("value", if (studentInfo != null && studentInfo.city!!.isNotEmpty()) studentInfo.city else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Pincode")
                    json7.put("type","text")
                    json7.put("value", if (studentInfo != null && studentInfo.pincode!!.isNotEmpty()) studentInfo.pincode else "--/--")
                    list.put(json7)

                    val json8 = JSONObject()
                    json8.put("label", "Country")
                    json8.put("type","text")
                    json8.put("value", if (studentInfo != null && studentInfo.country!!.isNotEmpty()) studentInfo.country else "--/--")
                    list.put(json8)

                    val json9 = JSONObject()
                    json9.put("label", "Aadhar Number")
                    json9.put("type","text")
                    json9.put("value", if (studentInfo != null && studentInfo.aadhar_number!!.isNotEmpty()) studentInfo.aadhar_number else "--/--")
                    list.put(json9)

                    val json10 = JSONObject()
                    json10.put("label", "Nationality")
                    json10.put("type","text")
                    json10.put("value", if (studentInfo != null && studentInfo.nationality!!.isNotEmpty()) studentInfo.nationality else "--/--")
                    list.put(json10)

                    val json11 = JSONObject()
                    json11.put("label", "Religion")
                    json11.put("type","text")
                    json11.put("value", if (studentInfo != null && studentInfo.religion!!.isNotEmpty()) studentInfo.religion else "--/--")
                    list.put(json11)

                    val json12 = JSONObject()
                    json12.put("label", "Category")
                    json12.put("type","text")
                    json12.put("value", if (studentInfo != null && studentInfo.category!!.isNotEmpty()) studentInfo.category else "--/--")
                    list.put(json12)

                    loadRecycler("text",list,binding.personalInfoRecycler)
                    binding.personalInfoRecycler.visibility = View.VISIBLE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.personalInformation,"#DAEFFF",null)
                    binding.down1.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_6dp)
                    binding.down1.rotation = 0F
                }
            }
            else{
                if(binding.personalInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val studentInfo = result.studentInfo
                    val json1 = JSONObject()
                    json1.put("label", "Date of Birth")
                    json1.put("type","text")
                    json1.put("value", if (studentInfo != null && studentInfo.dob!!.isNotEmpty()) studentInfo.dob else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Gender")
                    json2.put("type","text")
                    json2.put("value", if (studentInfo != null && studentInfo.gender!!.isNotEmpty()) studentInfo.gender else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Blood Group")
                    json3.put("type","text")
                    json3.put("value", if (studentInfo != null && studentInfo.blood_group!!.isNotEmpty()) studentInfo.blood_group else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Address")
                    json4.put("type","text")
                    json4.put("value", if (studentInfo != null && studentInfo.address!!.isNotEmpty()) studentInfo.address else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "State")
                    json5.put("type","text")
                    json5.put("value", if (studentInfo != null && studentInfo.state!!.isNotEmpty()) studentInfo.state else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "City")
                    json6.put("type","text")
                    json6.put("value", if (studentInfo != null && studentInfo.city!!.isNotEmpty()) studentInfo.city else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Pincode")
                    json7.put("type","text")
                    json7.put("value", if (studentInfo != null && studentInfo.pincode!!.isNotEmpty()) studentInfo.pincode else "--/--")
                    list.put(json7)

                    val json8 = JSONObject()
                    json8.put("label", "Country")
                    json8.put("type","text")
                    json8.put("value", if (studentInfo != null && studentInfo.country!!.isNotEmpty()) studentInfo.country else "--/--")
                    list.put(json8)

                    val json9 = JSONObject()
                    json9.put("label", "Aadhar Number")
                    json9.put("type","text")
                    json9.put("value", if (studentInfo != null && studentInfo.aadhar_number!!.isNotEmpty()) studentInfo.aadhar_number else "--/--")
                    list.put(json9)

                    val json10 = JSONObject()
                    json10.put("label", "Nationality")
                    json10.put("type","text")
                    json10.put("value", if (studentInfo != null && studentInfo.nationality!!.isNotEmpty()) studentInfo.nationality else "--/--")
                    list.put(json10)

                    val json11 = JSONObject()
                    json11.put("label", "Religion")
                    json11.put("type","text")
                    json11.put("value", if (studentInfo != null && studentInfo.religion!!.isNotEmpty()) studentInfo.religion else "--/--")
                    list.put(json11)

                    val json12 = JSONObject()
                    json12.put("label", "Category")
                    json12.put("type","text")
                    json12.put("value", if (studentInfo != null && studentInfo.category!!.isNotEmpty()) studentInfo.category else "--/--")
                    list.put(json12)

                    loadRecycler("text",list,binding.personalInfoRecycler)
                    binding.personalInfoRecycler.visibility = View.VISIBLE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.personalInformation,"#DAEFFF",null)
                    binding.down1.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_6dp)
                    binding.down1.rotation = 0F
                }
            }
        }

        binding.parentInfoLay.setOnClickListener {
            val role = sharedHelper.role
            if (role == "PARENT"){
                binding.parentName.text = "My Information"
                if(binding.parentInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val parentInfo = result.parentInfo
                    val json1 = JSONObject()
                    json1.put("label", "Full Name")
                    json1.put("type","text")
                    json1.put("value", if (parentInfo != null && parentInfo.fatherName!!.isNotEmpty()) parentInfo.fatherName else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Mother's Name")
                    json2.put("type","text")
                    json2.put("value", if (parentInfo != null && parentInfo.motherName!!.isNotEmpty()) parentInfo.motherName else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Guardian's Name")
                    json3.put("type","text")
                    json3.put("value", if (parentInfo != null && parentInfo.guardianName!!.isNotEmpty()) parentInfo.guardianName else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Father’s Occupation")
                    json4.put("type","text")
                    json4.put("value", if (parentInfo != null && parentInfo.fatherOccupation!!.isNotEmpty()) parentInfo.fatherOccupation else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "Mother’s Occupation")
                    json5.put("type","text")
                    json5.put("value", if (parentInfo != null && parentInfo.motherOccupation!!.isNotEmpty()) parentInfo.motherOccupation else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "Parent’s Contact Number")
                    json6.put("type","text")
                    json6.put("value", if (parentInfo != null && parentInfo.parentsMobile!!.isNotEmpty()) parentInfo.parentsMobile else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Emergency Contact Number")
                    json7.put("type","text")
                    json7.put("value", if (parentInfo != null && parentInfo.emergencyMobile!!.isNotEmpty()) parentInfo.emergencyMobile else "--/--")
                    list.put(json7)

                    val json8 = JSONObject()
                    json8.put("label", "Parent’s Email Address")
                    json8.put("type","text")
                    json8.put("value", if (parentInfo != null && parentInfo.parentsEmail!!.isNotEmpty()) parentInfo.parentsEmail else "--/--")
                    list.put(json8)

                    val json9 = JSONObject()
                    json9.put("label", "Parent’s Address")
                    json9.put("type","text")
                    json9.put("value", if (parentInfo != null && parentInfo.parentsAddress!!.isNotEmpty()) parentInfo.parentsAddress else "--/--")
                    list.put(json9)

                    loadRecycler("text",list,binding.parentInfoRecycler)
                    binding.parentInfoRecycler.visibility = View.VISIBLE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.parentInformation,"#DAEFFF",null)
                    binding.down2.rotation = 180F
                }
                else{
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_6dp)
                    binding.down2.rotation = 0F
                }
            }
            else{
                if(binding.parentInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val parentInfo = result.parentInfo
                    val json1 = JSONObject()
                    json1.put("label", "Father's Name")
                    json1.put("type","text")
                    json1.put("value", if (parentInfo != null && parentInfo.fatherName!!.isNotEmpty()) parentInfo.fatherName else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Mother's Name")
                    json2.put("type","text")
                    json2.put("value", if (parentInfo != null && parentInfo.motherName!!.isNotEmpty()) parentInfo.motherName else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Guardian's Name")
                    json3.put("type","text")
                    json3.put("value", if (parentInfo != null && parentInfo.guardianName!!.isNotEmpty()) parentInfo.guardianName else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Father’s Occupation")
                    json4.put("type","text")
                    json4.put("value", if (parentInfo != null && parentInfo.fatherOccupation!!.isNotEmpty()) parentInfo.fatherOccupation else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "Mother’s Occupation")
                    json5.put("type","text")
                    json5.put("value", if (parentInfo != null && parentInfo.motherOccupation!!.isNotEmpty()) parentInfo.motherOccupation else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "Parent’s Contact Number")
                    json6.put("type","text")
                    json6.put("value", if (parentInfo != null && parentInfo.parentsMobile!!.isNotEmpty()) parentInfo.parentsMobile else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Emergency Contact Number")
                    json7.put("type","text")
                    json7.put("value", if (parentInfo != null && parentInfo.emergencyMobile!!.isNotEmpty()) parentInfo.emergencyMobile else "--/--")
                    list.put(json7)

                    val json8 = JSONObject()
                    json8.put("label", "Parent’s Email Address")
                    json8.put("type","text")
                    json8.put("value", if (parentInfo != null && parentInfo.parentsEmail!!.isNotEmpty()) parentInfo.parentsEmail else "--/--")
                    list.put(json8)

                    val json9 = JSONObject()
                    json9.put("label", "Parent’s Address")
                    json9.put("type","text")
                    json9.put("value", if (parentInfo != null && parentInfo.parentsAddress!!.isNotEmpty()) parentInfo.parentsAddress else "--/--")
                    list.put(json9)

                    loadRecycler("text",list,binding.parentInfoRecycler)
                    binding.parentInfoRecycler.visibility = View.VISIBLE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.parentInformation,"#DAEFFF",null)
                    binding.down2.rotation = 180F
                }
                else{
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_6dp)
                    binding.down2.rotation = 0F
                }
            }
        }

        binding.academicInfoLay.setOnClickListener {
            val role = sharedHelper.role
            if (role == "PARENT"){
                if(binding.academicInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val academicInfo = result.academicInfo
                    val json1 = JSONObject()
                    json1.put("label", "Previous School Name")
                    json1.put("type","text")
                    json1.put("value", if (academicInfo != null && academicInfo.previousSchoolName!!.isNotEmpty()) academicInfo.previousSchoolName else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Class Applying For")
                    json2.put("type","text")
//                json2.put("value", if (academicInfo != null && academicInfo.classApplying != null && academicInfo.classApplying!!.name!!.isNotEmpty()) UiUtils.getOrdinalSuffix(academicInfo.classApplying!!.name!!.toInt()) else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Board of Education")
                    json3.put("type","text")
//                json3.put("value", if (academicInfo != null && academicInfo.boardOfEducation != null && academicInfo.boardOfEducation!!.name!!.isNotEmpty()) academicInfo.boardOfEducation!!.name else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    loadRecycler("text",list,binding.academicInfoRecycler)
                    binding.academicInfoRecycler.visibility = View.VISIBLE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.academicInformation,"#DAEFFF",null)
                    binding.down3.rotation = 180F
                }
                else{
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_6dp)
                    binding.down3.rotation = 0F
                }
            }
            else{
                if(binding.academicInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val academicInfo = result.academicInfo
                    val json1 = JSONObject()
                    json1.put("label", "Previous School Name")
                    json1.put("type","text")
                    json1.put("value", if (academicInfo != null && academicInfo.previousSchoolName!!.isNotEmpty()) academicInfo.previousSchoolName else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Class Applying For")
                    json2.put("type","text")
//                json2.put("value", if (academicInfo != null && academicInfo.classApplying != null && academicInfo.classApplying!!.name!!.isNotEmpty()) UiUtils.getOrdinalSuffix(academicInfo.classApplying!!.name!!.toInt()) else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Board of Education")
                    json3.put("type","text")
//                json3.put("value", if (academicInfo != null && academicInfo.boardOfEducation != null && academicInfo.boardOfEducation!!.name!!.isNotEmpty()) academicInfo.boardOfEducation!!.name else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    loadRecycler("text",list,binding.academicInfoRecycler)
                    binding.academicInfoRecycler.visibility = View.VISIBLE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.academicInformation,"#DAEFFF",null)
                    binding.down3.rotation = 180F
                }
                else{
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_6dp)
                    binding.down3.rotation = 0F
                }
            }
        }

        binding.documentInfoLay.setOnClickListener {
            val role = sharedHelper.role
            if (role == "PARENT"){
                binding.documentInfoLay.visibility = View.GONE
                if(binding.documentInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val documentInfo = result.documentInfo
                    val json1 = JSONObject()
                    json1.put("label", "Birth Certificate")
                    json1.put("type","document")
                    json1.put("value", if (documentInfo != null && documentInfo.birthCertificate != null && documentInfo.birthCertificate!!.url!!.isNotEmpty()) documentInfo.birthCertificate!!.url!! else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Aadhaar Card")
                    json2.put("type","document")
                    json2.put("value", if (documentInfo != null && documentInfo.aadharCard != null && documentInfo.aadharCard!!.url!!.isNotEmpty()) documentInfo.aadharCard!!.url!! else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Student Photo")
                    json3.put("type","document")
                    json3.put("value", if (documentInfo != null && documentInfo.studentPhoto != null && documentInfo.studentPhoto!!.url!!.isNotEmpty()) documentInfo.studentPhoto!!.url!! else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Previous School Marksheet")
                    json4.put("type","document")
                    json4.put("value", if (documentInfo != null && documentInfo.previousSchoolMarksheet != null && documentInfo.previousSchoolMarksheet!!.url!!.isNotEmpty()) documentInfo.previousSchoolMarksheet!!.url!! else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "Transfer Certificate")
                    json5.put("type","document")
                    json5.put("value", if (documentInfo != null && documentInfo.transferCertificate != null && documentInfo.transferCertificate!!.url!!.isNotEmpty()) documentInfo.transferCertificate!!.url!! else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "Parent’s ID Proof")
                    json6.put("type","document")
                    json6.put("value", if (documentInfo != null && documentInfo.parentIdProof != null && documentInfo.parentIdProof!!.url!!.isNotEmpty()) documentInfo.parentIdProof!!.url!! else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Address Proof")
                    json7.put("type","document")
                    json7.put("value", if (documentInfo != null && documentInfo.addressProof != null && documentInfo.addressProof!!.url!!.isNotEmpty()) documentInfo.addressProof!!.url!! else "--/--")
                    list.put(json7)

                    loadRecycler("document",list,binding.documentInfoRecycler)
                    binding.documentInfoRecycler.visibility = View.VISIBLE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.documentInformation,"#DAEFFF",null)
                    binding.down2.rotation = 180F
                }
                else{
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_6dp)
                    binding.down2.rotation = 0F
                }
            }
            else{
                if(binding.documentInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val documentInfo = result.documentInfo
                    val json1 = JSONObject()
                    json1.put("label", "Birth Certificate")
                    json1.put("type","document")
                    json1.put("value", if (documentInfo != null && documentInfo.birthCertificate != null && documentInfo.birthCertificate!!.url!!.isNotEmpty()) documentInfo.birthCertificate!!.url!! else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Aadhaar Card")
                    json2.put("type","document")
                    json2.put("value", if (documentInfo != null && documentInfo.aadharCard != null && documentInfo.aadharCard!!.url!!.isNotEmpty()) documentInfo.aadharCard!!.url!! else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Student Photo")
                    json3.put("type","document")
                    json3.put("value", if (documentInfo != null && documentInfo.studentPhoto != null && documentInfo.studentPhoto!!.url!!.isNotEmpty()) documentInfo.studentPhoto!!.url!! else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Previous School Marksheet")
                    json4.put("type","document")
                    json4.put("value", if (documentInfo != null && documentInfo.previousSchoolMarksheet != null && documentInfo.previousSchoolMarksheet!!.url!!.isNotEmpty()) documentInfo.previousSchoolMarksheet!!.url!! else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "Transfer Certificate")
                    json5.put("type","document")
                    json5.put("value", if (documentInfo != null && documentInfo.transferCertificate != null && documentInfo.transferCertificate!!.url!!.isNotEmpty()) documentInfo.transferCertificate!!.url!! else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "Parent’s ID Proof")
                    json6.put("type","document")
                    json6.put("value", if (documentInfo != null && documentInfo.parentIdProof != null && documentInfo.parentIdProof!!.url!!.isNotEmpty()) documentInfo.parentIdProof!!.url!! else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Address Proof")
                    json7.put("type","document")
                    json7.put("value", if (documentInfo != null && documentInfo.addressProof != null && documentInfo.addressProof!!.url!!.isNotEmpty()) documentInfo.addressProof!!.url!! else "--/--")
                    list.put(json7)

                    loadRecycler("document",list,binding.documentInfoRecycler)
                    binding.documentInfoRecycler.visibility = View.VISIBLE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.documentInformation,"#DAEFFF",null)
                    binding.down2.rotation = 180F
                }
                else{
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.parentInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_6dp)
                    binding.down2.rotation = 0F
                }
            }
        }

        /*binding.personalInformation.setOnClickListener {

            val isVisible = binding.personalShowDetails.visibility == View.VISIBLE
            if (isVisible) {
                binding.personalShowDetails.visibility = View.GONE
//                binding.upArrowPersonalInfo.visibility = View.GONE
//                binding.downArrowPersonalInfo.visibility = View.VISIBLE
                UiUtils.relativeLayoutBgDrawable(binding.personalInformation, R.drawable.border_curve_8dp_white_bg)
            } else {
                binding.personalShowDetails.visibility = View.VISIBLE
//                binding.upArrowPersonalInfo.visibility = View.VISIBLE
//                binding.downArrowPersonalInfo.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.personalInformation, R.drawable.top_side_curve_gray)
            }
        }*/

        /*  binding.parentInformation.setOnClickListener {
              val isVisible = binding.parentShowDetails.visibility == View.VISIBLE

              if (isVisible) {
                  binding.parentShowDetails.visibility = View.GONE
                  binding.pupArrowPersonalInfo.visibility = View.GONE
                  binding.pdownArrowPersonalInfo.visibility = View.VISIBLE
                  UiUtils.relativeLayoutBgDrawable(binding.parentInformation, R.drawable.border_curve_8dp_white_bg)
              } else {
                  binding.parentShowDetails.visibility = View.VISIBLE
                  binding.pupArrowPersonalInfo.visibility = View.VISIBLE
                  binding.pdownArrowPersonalInfo.visibility = View.GONE
                  UiUtils.relativeLayoutBgDrawable(binding.parentInformation, R.drawable.top_side_curve_gray)
              }
          }*/

        binding.academicDetails1.setOnClickListener {
            val isVisible = binding.showAcademicDetails1.visibility == View.VISIBLE
            if (isVisible) {
                binding.showAcademicDetails1.visibility = View.GONE
                binding.downArrowAcademicDetails1.visibility = View.VISIBLE
                binding.upArrowAcademicDetails1.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.academicDetails1, R.drawable.border_curve_8dp_white_bg)
            } else {
                binding.showAcademicDetails1.visibility = View.VISIBLE
                binding.downArrowAcademicDetails1.visibility = View.GONE
                binding.upArrowAcademicDetails1.visibility = View.VISIBLE
                UiUtils.relativeLayoutBgDrawable(binding.academicDetails1, R.drawable.top_side_curve_gray)
            }
        }
        if (key == "My Information"){
            binding.bankInfo.visibility = View.GONE
        }
        else{
            val role = sharedHelper.role
            if (role == "PARENT"){
                binding.bankInfo.visibility = View.VISIBLE

                binding.bankDetails.setOnClickListener {
                    val isVisible = binding.showBankDetails.visibility == View.VISIBLE
                    if (isVisible) {
                        binding.showBankDetails.visibility = View.GONE
                        binding.down5.visibility = View.VISIBLE
                        binding.v5.visibility = View.VISIBLE
                        UiUtils.relativeLayoutBgDrawable(binding.bankDetails,R.drawable.border_curve_6dp)
                        binding.down5.rotation = 0F
                    } else {
                        binding.showBankDetails.visibility = View.VISIBLE
                        binding.down5.visibility = View.VISIBLE
                        binding.v5.visibility = View.GONE
                        UiUtils.relativeLayoutBgDrawable(binding.bankDetails,R.drawable.border_curve_top_10dp)
                        UiUtils.relativeLayoutBgTint(binding.bankDetails,"#DAEFFF",null)
                        binding.down5.rotation = 180F
                    }
                }
            }
            else{
                binding.bankInfo.visibility = View.VISIBLE

                binding.bankDetails.setOnClickListener {
                    val isVisible = binding.showBankDetails.visibility == View.VISIBLE
                    if (isVisible) {
                        binding.showBankDetails.visibility = View.GONE
                        binding.down5.visibility = View.VISIBLE
                        UiUtils.relativeLayoutBgDrawable(binding.bankDetails,R.drawable.border_curve_top_10dp)
                        UiUtils.relativeLayoutBgTint(binding.bankDetails,"#DAEFFF",null)
                        binding.down5.rotation = 180F
                    } else {
                        binding.showBankDetails.visibility = View.VISIBLE
                        binding.down5.visibility = View.VISIBLE
                        UiUtils.relativeLayoutBgDrawable(binding.bankDetails,R.drawable.border_curve_6dp)
                        binding.down5.rotation = 0F
                    }
                }
            }
        }
        binding.documents.setOnClickListener {
            val isVisible = binding.showDocument.visibility == View.VISIBLE
            if (isVisible) {
                binding.showDocument.visibility = View.GONE
                binding.dUpArrow.visibility = View.GONE
                binding.dDownArrow.visibility = View.VISIBLE
                UiUtils.relativeLayoutBgDrawable(binding.documents, R.drawable.border_curve_8dp_white_bg)
            } else {
                // Expand
                binding.showDocument.visibility = View.VISIBLE
                binding.dUpArrow.visibility = View.VISIBLE
                binding.dDownArrow.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.documents, R.drawable.top_side_curve_gray)
            }
            binding.studentInfoDocumnet.afr.text = "Birth Certificate"
            binding.studentInfoDocumnet.afr1.text = "AadharCard"
            binding.studentInfoDocumnet1.afr.text = "Student Photo"
            binding.studentInfoDocumnet1.afr1.text = "Previous School Marksheet"
            binding.studentInfoDocumnet2.afr.text = "Transfer Certificate"
            binding.studentInfoDocumnet2.afr1.text = "Parent Id Proof"
            binding.studentInfoDocumnet3.afr.text = "Address Proof"
            binding.studentInfoDocumnet3.afr1.text = "--/--"
            getDocumnet()
        }
        if (key == "My Information"){
            binding.leaveAndAttendance.visibility = View.GONE
        }
        else{
            binding.leaveAndAttendance.setOnClickListener {
                val isVisible = binding.showLeaveDetails.visibility == View.VISIBLE
                if (isVisible) {
                    // Collapse
                    binding.showLeaveDetails.visibility = View.GONE
                    binding.lupArraw.visibility = View.GONE
                    binding.lDowArrow.visibility = View.VISIBLE
                    UiUtils.relativeLayoutBgDrawable(binding.leaveAndAttendance, R.drawable.border_curve_8dp_white_bg)
                } else {
                    // Expand
                    binding.showLeaveDetails.visibility = View.VISIBLE
                    binding.lupArraw.visibility = View.VISIBLE
                    binding.lDowArrow.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.leaveAndAttendance, R.drawable.top_side_curve_gray)
                }
            }
        }

    }
    fun loadRecycler(type: String, cList: JSONArray, recyclerView: RecyclerView){
        if (type == "text"){
            var mList = cList
            val linearLayoutManager = GridLayoutManager(this, 2,RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this,mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }
        else {
            var mList = cList
            val linearLayoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this,mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }
    }

    private fun getDocumnet(){
        ApiConnection.getInstance().profile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.applicationForm!= null){

                            if (it.result!!.applicationForm!!.documentInfo!!.birthCertificate!!.status!= null){
                                binding.studentInfoDocumnet.urls.text = it.result!!.applicationForm!!.documentInfo!!.birthCertificate!!.status
                                binding.studentInfoDocumnet.urls.text = "URL"
                                UiUtils.setTextViewDrawableColor(binding.studentInfoDocumnet.urls, "0000FF", null)

                            }
                            else{
                                binding.studentInfoDocumnet.urls.text = "--/-- "
                            }
                            if (it.result!!.applicationForm!!.documentInfo!!.aadharCard!!.status!= null){
                                binding.studentInfoDocumnet.urls1.text = it.result!!.applicationForm!!.documentInfo!!.aadharCard!!.status
                                binding.studentInfoDocumnet.urls1.text = "URL"
                                UiUtils.setTextViewDrawableColor(binding.studentInfoDocumnet.urls1, "0000FF", null)

                            }
                            else{
                                binding.studentInfoDocumnet.urls1.text = "--/-- "
                            }
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }

   /* private fun getAcademicDetails(){
        ApiConnection.getInstance().profile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.applicationForm!= null){

                            if (it.result!!.applicationForm!!.academicInfo!!.previousSchoolName!= null){
                                binding.prevSchoolName.text = it.result!!.applicationForm!!.academicInfo!!.previousSchoolName
                            }
                            else{
                                binding.prevSchoolName.text = "--/-- "
                            }
                            if (it.result!!.applicationForm!!.academicInfo!!.previousClassName!!.name!= null){
                                binding.prevSchoolName.text = it.result!!.applicationForm!!.academicInfo!!.previousClassName!!.name
                            }
                            else{
                                binding.prevSchoolName.text = "--/-- "
                            }
                            if (it.result!!.applicationForm!!.academicInfo!!.classApplying!!.name!= null){
                                binding.classApply.text = it.result!!.applicationForm!!.academicInfo!!.classApplying!!.name
                            }
                            else{
                                binding.classApply.text = "--/-- "
                            }
                            if (it.result!!.applicationForm!!.academicInfo!!.boardOfEducation!!.name!= null){
                                binding.boe.text = it.result!!.applicationForm!!.academicInfo!!.boardOfEducation!!.name
                            }
                            else{
                                binding.boe.text = "--/-- "
                            }
                            if (it.result!!.applicationForm!!.academicInfo!!.batch!= null){
                                binding.batch.text = it.result!!.applicationForm!!.academicInfo!!.batch
                            }
                            else{
                                binding.batch.text = "--/-- "
                            }
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }
*/

}