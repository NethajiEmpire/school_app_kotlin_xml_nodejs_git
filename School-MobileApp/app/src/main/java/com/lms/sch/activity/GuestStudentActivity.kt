package com.lms.sch.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.UserDetailsAdapter
import com.lms.sch.databinding.ActivityGuestStudentBinding
import com.lms.sch.databinding.ActivityStudentInfoBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.StudentProfileResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject

class GuestStudentActivity : BaseActivity() {
    lateinit var binding : ActivityGuestStudentBinding
    var result = StudentProfileResponse.ApplicationForm()
    var resultacademic = StudentProfileResponse.Result()
    var serId = ""
    var currentSection = "stdInfo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGuestStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        serId = intent.getStringExtra(Constants.IntentKeys.KEY1)?: "--/--"
        getStudent()
        binding.stdInfo.setOnClickListener {
            if(currentSection == "stdInfo"){
                binding.stdInfoRecycle.visibility == View.GONE
                currentSection = ""
            }
            else{
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

                loadRecycler("text",list,binding.stdInfoRecycle)
                binding.stdInfoRecycle.visibility = View.VISIBLE
                currentSection = "stdInfo"
            }
        }
        binding.parentInfo.setOnClickListener {
            if(currentSection == "parentInfo"){
                binding.stdInfoRecycle.visibility == View.GONE
                currentSection = ""
            }
            else{
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

                loadRecycler("text",list,binding.stdInfoRecycle)
                binding.stdInfoRecycle.visibility = View.VISIBLE
                currentSection = "parentInfo"
            }
        }
        binding.academicInfo.setOnClickListener {
            if(currentSection == "academicInfo") {
                binding.stdInfoRecycle.visibility == View.GONE
                currentSection = ""

            }
            else{
                val list = JSONArray()
                val academicInfo = result.academicInfo
                val json1 = JSONObject()
                json1.put("label", "Previous School Name")
                json1.put("type", "text")
                json1.put(
                    "value",
                    if (academicInfo != null && academicInfo.previousSchoolName!!.isNotEmpty()) academicInfo.previousSchoolName else "--/--"
                )
                list.put(json1)

//                val json2 = JSONObject()
//                json2.put("label", "Class Applying For")
//                json2.put("type","text")
//                json2.put("value", if (academicInfo != null && academicInfo.classApplying != null && academicInfo.classApplying!!.name!!.isNotEmpty()) academicInfo.classApplying!!.name!! else "--/--")
//                list.put(json2)

//                val json3 = JSONObject()
//                json3.put("label", "Board of Education")
//                json3.put("type","text")
//                json3.put("value", if (academicInfo != null && academicInfo.boardOfEducation != null && academicInfo.boardOfEducation!!.name!!.isNotEmpty()) academicInfo.boardOfEducation!!.name else "--/--")
//                list.put(json3)

                loadRecycler("text", list, binding.stdInfoRecycle)
                binding.stdInfoRecycle.visibility = View.VISIBLE
                currentSection = "academicInfo"
            }
        }
        binding.transactionInfo.setOnClickListener {
            if(currentSection == "document"){
                binding.stdInfoRecycle.visibility == View.GONE
                currentSection = ""
            }
            else{
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

                loadRecycler("document",list,binding.stdInfoRecycle)
                binding.stdInfoRecycle.visibility = View.VISIBLE
                currentSection = "document"
            }
        }
        binding.stdInfo.performClick()
    }
    fun loadRecycler(type: String, cList: JSONArray , recyclerView: RecyclerView){
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
    private fun getStudent(){
        Log.d("ghghgout",serId)
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentprofile(this,serId).observe(this) {
            Log.d("ghghgout",serId)
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            result = it.result!!.applicationForm!!
                            resultacademic = it.result!!
                            if (it.result!!.student != null){
                                binding.name.text = it.result!!.student!!.firstName+" "+it.result!!.student!!.lastName
                                binding.email.text = it.result!!.student!!.email
                                binding.mobile.text = "+91 ${it.result!!.student!!.mobile}"
                                binding.stdId.text = it.result!!.student!!.lead_id!!
                                UiUtils.loadImage(binding.profileimg,it.result!!.student!!.imgUrl!!)
                            }
                            else {
                                UiUtils.showSnack(it.msg, binding.root,false)
                            }
//                            binding.personalInfoLay.performClick()
//                            binding.attendanceTap.performClick()
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
    }
}