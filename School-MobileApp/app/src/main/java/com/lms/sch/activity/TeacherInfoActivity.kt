package com.lms.sch.activity

import android.os.Bundle
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
import com.lms.sch.databinding.ActivityTeacherInfoBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.TeacherprofileSignleViewResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import com.lms.sch.utils.UiUtils.log
import org.json.JSONArray
import org.json.JSONObject

class TeacherInfoActivity : BaseActivity() {
    lateinit var binding: ActivityTeacherInfoBinding
    var result = TeacherprofileSignleViewResponse.Result()
    var userId = ""
    var title = ""
    var role = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTeacherInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        userId = intent.getStringExtra(Constants.IntentKeys.KEY1)?: ""
        title = intent.getStringExtra(Constants.IntentKeys.KEY) ?: ""
        role = intent.getStringExtra("role") ?: ""

        if (title == "Teacher Info"){
            binding.topHeader.text = "Teacher Info"
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().teacherProfiles(this, userId).observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null) {
                                result = it.result!!
                                binding.name.text = it.result!!.firstName+" "+it.result!!.lastName
                                binding.email.text = it.result!!.email
                                binding.mobile.text = "+91 ${it.result!!.mobile}"
                                binding.stdId.text = it.result!!.lead_id!!
//                                UiUtils.imageviewDrawable(binding.tprofile,it.result!!.img_url!!.toInt())
                                binding.personalInfoLay.performClick()
                            }
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
        }else{
            binding.topHeader.text = "Staff Info"
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().staffProfile(this, userId).observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null) {
                                result = it.result!!
                                binding.name.text = it.result!!.firstName+" "+it.result!!.lastName
                                binding.email.text = it.result!!.email
                                binding.mobile.text = "+91 ${it.result!!.mobile}"
                                binding.stdId.text = it.result!!.lead_id!!
//                                UiUtils.imageviewDrawable(binding.tprofile,it.result!!.img_url!!.toInt())
                                binding.personalInfoLay.performClick()
                            }
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
        }
        if (role == "STAFF"){
            binding.personalInfoLay.setOnClickListener {
                binding.academicInfoLay.visibility = View.GONE
                if (binding.personalInfoRecycler.visibility == View.GONE) {
                    val list = JSONArray()
                    val teacherInfo = result
                    val json1 = JSONObject()
                    json1.put("label", "Address")
                    json1.put("type", "text")
                    json1.put("value", if (teacherInfo != null && teacherInfo.address!!.isNotEmpty()) teacherInfo.address else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Gender")
                    json2.put("type", "text")
                    json2.put("value", if (teacherInfo != null && teacherInfo.gender!!.isNotEmpty()) teacherInfo.gender else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "lead_id")
                    json3.put("type", "text")
                    json3.put("value", if (teacherInfo != null && teacherInfo.lead_id!!.isNotEmpty()) teacherInfo.lead_id else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Date of Birth")
                    json4.put("type", "text")
                    json4.put("value", if (teacherInfo != null && teacherInfo.dob!!.isNotEmpty()) BaseUtils.getFormattedDate(teacherInfo.dob!!,
                        Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT) else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "State")
                    json5.put("type", "text")
                    json5.put("value", if (teacherInfo != null && teacherInfo.state!!.isNotEmpty()) teacherInfo.state else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "City")
                    json6.put("type", "text")
                    json6.put("value", if (teacherInfo != null && teacherInfo.city!!.isNotEmpty()) teacherInfo.city else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Pincode")
                    json7.put("type", "text")
                    json7.put("value", if (teacherInfo != null && teacherInfo.pincode!!.isNotEmpty()) teacherInfo.pincode else "--/--")
                    list.put(json7)

                    val json8 = JSONObject()
                    json8.put("label", "Country")
                    json8.put("type", "text")
                    json8.put("value", if (teacherInfo != null && teacherInfo.country!!.isNotEmpty()) teacherInfo.country else "--/--")
                    list.put(json8)

                    val json9 = JSONObject()
                    json9.put("label", "Aadhar Number")
                    json9.put("type", "text")
                    json9.put("value", if (teacherInfo != null && teacherInfo.aadhar_number!!.isNotEmpty()) teacherInfo.aadhar_number else "--/--")
                    list.put(json9)

                    val json10 = JSONObject()
                    json10.put("label", "Nationality")
                    json10.put("type", "text")
                    json10.put("value", if (teacherInfo != null && teacherInfo.nationality!!.isNotEmpty()) teacherInfo.nationality else "--/--")
                    list.put(json10)

                    val json11 = JSONObject()
                    json11.put("label", "Religion")
                    json11.put("type", "text")
                    json11.put("value", if (teacherInfo != null && teacherInfo.religion!!.isNotEmpty()) teacherInfo.religion else "--/--")
                    list.put(json11)

                    val json12 = JSONObject()
                    json12.put("label", "Category")
                    json12.put("type", "text")
                    json12.put("value", if (teacherInfo != null && teacherInfo.category!!.isNotEmpty()) teacherInfo.category else "--/--")
                    list.put(json12)

                    val json13 = JSONObject()
                    json13.put("label", "Blood Group")
                    json13.put("type", "text")
                    json13.put("value", if (teacherInfo != null && teacherInfo.blood_group!!.isNotEmpty()) teacherInfo.blood_group else "--/--")
                    list.put(json13)


                    loadRecycler("text", list, binding.personalInfoRecycler)
                    binding.personalInfoRecycler.visibility = View.VISIBLE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.personalInformation, "#DAEFFF", null)
                    binding.down1.rotation = 180F
                } else {
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_6dp)
                    binding.down1.rotation = 0F
                }
            }

            binding.BankDetails.setOnClickListener {
                if(binding.Bankrecycle.visibility == View.GONE){
                    val list = JSONArray()
                    val academicInfo = result!!.teacherPreference
                    val json1 = JSONObject()
                    json1.put("label", "Acc Holder Name")
                    json1.put("type","text")
//                json1.put("value", if (academicInfo != null) academicInfo.teacherType else "--/--")
                    json1.put("value","--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Bank Name")
                    json2.put("type","text")
//                json2.put("value", if (academicInfo != null) academicInfo.highestQualification!! else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Branch")
                    json3.put("type","text")
//                json3.put("value", if (academicInfo != null ) academicInfo.overallExperience!! else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "IFSC Code")
                    json4.put("type","text")
//                json4.put("value", if (academicInfo != null && academicInfo.board!!.isNotEmpty() ) academicInfo.board!! else "--/--")
                    json4.put("value","--/--")
                    list.put(json4)

                    loadRecycler("text",list,binding.Bankrecycle)
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.VISIBLE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.parentInformation,"#DAEFFF",null)
                    binding.down3.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_6dp)
                    binding.down3.rotation = 0F
                }
            }

            binding.documentInfoLay.setOnClickListener {
                if(binding.documentInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val documentInfo = result
                    val json1 = JSONObject()
                    json1.put("label", "Accounting For Managers")
                    json1.put("type","document")
//                json1.put("value", if (documentInfo != null && documentInfo.birthCertificate != null && documentInfo.birthCertificate!!.url!!.isNotEmpty()) documentInfo.birthCertificate!!.url!! else "--/--")
                    json1.put("value","--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Aadhaar Card")
                    json2.put("type","document")
//                json2.put("value", if (documentInfo != null && documentInfo.aadharCard != null && documentInfo.aadharCard!!.url!!.isNotEmpty()) documentInfo.aadharCard!!.url!! else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "PAN Card")
                    json3.put("type","document")
//                json3.put("value", if (documentInfo != null && documentInfo.studentPhoto != null && documentInfo.studentPhoto!!.url!!.isNotEmpty()) documentInfo.studentPhoto!!.url!! else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Qualification Certificate")
                    json4.put("type","document")
//                json4.put("value", if (documentInfo != null && documentInfo.previousSchoolMarksheet != null && documentInfo.previousSchoolMarksheet!!.url!!.isNotEmpty()) documentInfo.previousSchoolMarksheet!!.url!! else "--/--")
                    json4.put("value","--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", " Experience Certificate")
                    json5.put("type","document")
//                json5.put("value", if (documentInfo != null && documentInfo.transferCertificate != null && documentInfo.transferCertificate!!.url!!.isNotEmpty()) documentInfo.transferCertificate!!.url!! else "--/--")
                    json5.put("value","--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "Photo & Signature")
                    json6.put("type","document")
//                json6.put("value", if (documentInfo != null && documentInfo.parentIdProof != null && documentInfo.parentIdProof!!.url!!.isNotEmpty()) documentInfo.parentIdProof!!.url!! else "--/--")
                    json6.put("value","--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Address Proof")
                    json7.put("type","document")
//                json7.put("value", if (documentInfo != null && documentInfo.addressProof != null && documentInfo.addressProof!!.url!!.isNotEmpty()) documentInfo.addressProof!!.url!! else "--/--")
                    json7.put("value","--/--")
                    list.put(json7)

                    loadRecycler("document",list,binding.documentInfoRecycler)
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.VISIBLE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.documentInformation,"#DAEFFF",null)
                    binding.down2.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_6dp)
                    binding.down2.rotation = 0F
                }
            }

            binding.LeaveAttendance.setOnClickListener {
                if(binding.leaverecycle.visibility == View.GONE){
                    val list = JSONArray()
                    val academicInfo = result!!.teacherPreference
                    val json1 = JSONObject()
                    json1.put("label", "Leaves Taken (This Year)")
                    json1.put("type","text")
//                json1.put("value", if (academicInfo != null) academicInfo.teacherType else "--/--")
                    json1.put("value","--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Remaining Casual Leaves")
                    json2.put("type","text")
//                json2.put("value", if (academicInfo != null) academicInfo.highestQualification!! else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Last Leave Date")
                    json3.put("type","text")
//                json3.put("value", if (academicInfo != null ) academicInfo.overallExperience!! else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Attendance Rate")
                    json4.put("type","text")
//                json4.put("value", if (academicInfo != null && academicInfo.board!!.isNotEmpty() ) academicInfo.board!! else "--/--")
                    json4.put("value","--/--")
                    list.put(json4)

                    loadRecycler("text",list,binding.leaverecycle)
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.VISIBLE
                    UiUtils.relativeLayoutBgDrawable(binding.LeaveAttendanceinfo,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.LeaveAttendanceinfo,"#DAEFFF",null)
                    binding.down3.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.LeaveAttendanceinfo,R.drawable.border_curve_6dp)
                    binding.down3.rotation = 0F
                }
            }

        }else{

            binding.personalInfoLay.setOnClickListener {
                if (binding.personalInfoRecycler.visibility == View.GONE) {
                    val list = JSONArray()
                    val teacherInfo = result
                    val json1 = JSONObject()
                    json1.put("label", "Address")
                    json1.put("type", "text")
                    json1.put("value", if (teacherInfo != null && teacherInfo.address!!.isNotEmpty()) teacherInfo.address else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Gender")
                    json2.put("type", "text")
                    json2.put("value", if (teacherInfo != null && teacherInfo.gender!!.isNotEmpty()) teacherInfo.gender else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "lead_id")
                    json3.put("type", "text")
                    json3.put("value", if (teacherInfo != null && teacherInfo.lead_id!!.isNotEmpty()) teacherInfo.lead_id else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Date of Birth")
                    json4.put("type", "text")
                    json4.put("value", if (teacherInfo != null && teacherInfo.dob!!.isNotEmpty()) BaseUtils.getFormattedDate(teacherInfo.dob!!,
                        Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT) else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "State")
                    json5.put("type", "text")
                    json5.put("value", if (teacherInfo != null && teacherInfo.state!!.isNotEmpty()) teacherInfo.state else "--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "City")
                    json6.put("type", "text")
                    json6.put("value", if (teacherInfo != null && teacherInfo.city!!.isNotEmpty()) teacherInfo.city else "--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Pincode")
                    json7.put("type", "text")
                    json7.put("value", if (teacherInfo != null && teacherInfo.pincode!!.isNotEmpty()) teacherInfo.pincode else "--/--")
                    list.put(json7)

                    val json8 = JSONObject()
                    json8.put("label", "Country")
                    json8.put("type", "text")
                    json8.put("value", if (teacherInfo != null && teacherInfo.country!!.isNotEmpty()) teacherInfo.country else "--/--")
                    list.put(json8)

                    val json9 = JSONObject()
                    json9.put("label", "Aadhar Number")
                    json9.put("type", "text")
                    json9.put("value", if (teacherInfo != null && teacherInfo.aadhar_number!!.isNotEmpty()) teacherInfo.aadhar_number else "--/--")
                    list.put(json9)

                    val json10 = JSONObject()
                    json10.put("label", "Nationality")
                    json10.put("type", "text")
                    json10.put("value", if (teacherInfo != null && teacherInfo.nationality!!.isNotEmpty()) teacherInfo.nationality else "--/--")
                    list.put(json10)

                    val json11 = JSONObject()
                    json11.put("label", "Religion")
                    json11.put("type", "text")
                    json11.put("value", if (teacherInfo != null && teacherInfo.religion!!.isNotEmpty()) teacherInfo.religion else "--/--")
                    list.put(json11)

                    val json12 = JSONObject()
                    json12.put("label", "Category")
                    json12.put("type", "text")
                    json12.put("value", if (teacherInfo != null && teacherInfo.category!!.isNotEmpty()) teacherInfo.category else "--/--")
                    list.put(json12)

                    val json13 = JSONObject()
                    json13.put("label", "Blood Group")
                    json13.put("type", "text")
                    json13.put("value", if (teacherInfo != null && teacherInfo.blood_group!!.isNotEmpty()) teacherInfo.blood_group else "--/--")
                    list.put(json13)


                    loadRecycler("text", list, binding.personalInfoRecycler)
                    binding.personalInfoRecycler.visibility = View.VISIBLE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.personalInformation, "#DAEFFF", null)
                    binding.down1.rotation = 180F
                } else {
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_6dp)
                    binding.down1.rotation = 0F
                }
            }

            binding.academicInfoLay.setOnClickListener {
                if(binding.academicInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val academicInfo = result!!.teacherPreference
                    val json1 = JSONObject()
                    json1.put("label", "Teacher Type")
                    json1.put("type","text")
                    json1.put("value", if (academicInfo != null) academicInfo.teacherType else "--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Highest Qualification")
                    json2.put("type","text")
                    json2.put("value", if (academicInfo != null) academicInfo.highestQualification!! else "--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Overall Experience")
                    json3.put("type","text")
                    json3.put("value", if (academicInfo != null ) academicInfo.overallExperience!! else "--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Board")
                    json4.put("type","text")
                    json4.put("value", if (academicInfo != null && academicInfo.board!!.isNotEmpty() ) academicInfo.board!! else "--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", "Subject")
                    json5.put("type","text")
                    json5.put("value", if (academicInfo != null && academicInfo.majorSubjects != null ) academicInfo.majorSubjects!![0].name!! else "--/--")
                    list.put(json5)

                    loadRecycler("text",list,binding.academicInfoRecycler)
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.VISIBLE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.academicInformation,"#DAEFFF",null)
                    binding.down3.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_6dp)
                    binding.down3.rotation = 0F
                }
            }

            binding.BankDetails.setOnClickListener {
                if(binding.Bankrecycle.visibility == View.GONE){
                    val list = JSONArray()
                    val academicInfo = result!!.teacherPreference
                    val json1 = JSONObject()
                    json1.put("label", "Acc Holder Name")
                    json1.put("type","text")
//                json1.put("value", if (academicInfo != null) academicInfo.teacherType else "--/--")
                    json1.put("value","--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Bank Name")
                    json2.put("type","text")
//                json2.put("value", if (academicInfo != null) academicInfo.highestQualification!! else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Branch")
                    json3.put("type","text")
//                json3.put("value", if (academicInfo != null ) academicInfo.overallExperience!! else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "IFSC Code")
                    json4.put("type","text")
//                json4.put("value", if (academicInfo != null && academicInfo.board!!.isNotEmpty() ) academicInfo.board!! else "--/--")
                    json4.put("value","--/--")
                    list.put(json4)

                    loadRecycler("text",list,binding.Bankrecycle)
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.VISIBLE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.parentInformation,"#DAEFFF",null)
                    binding.down3.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_6dp)
                    binding.down3.rotation = 0F
                }
            }

            binding.documentInfoLay.setOnClickListener {
                if(binding.documentInfoRecycler.visibility == View.GONE){
                    val list = JSONArray()
                    val documentInfo = result
                    val json1 = JSONObject()
                    json1.put("label", "Accounting For Managers")
                    json1.put("type","document")
//                json1.put("value", if (documentInfo != null && documentInfo.birthCertificate != null && documentInfo.birthCertificate!!.url!!.isNotEmpty()) documentInfo.birthCertificate!!.url!! else "--/--")
                    json1.put("value","--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Aadhaar Card")
                    json2.put("type","document")
//                json2.put("value", if (documentInfo != null && documentInfo.aadharCard != null && documentInfo.aadharCard!!.url!!.isNotEmpty()) documentInfo.aadharCard!!.url!! else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "PAN Card")
                    json3.put("type","document")
//                json3.put("value", if (documentInfo != null && documentInfo.studentPhoto != null && documentInfo.studentPhoto!!.url!!.isNotEmpty()) documentInfo.studentPhoto!!.url!! else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Qualification Certificate")
                    json4.put("type","document")
//                json4.put("value", if (documentInfo != null && documentInfo.previousSchoolMarksheet != null && documentInfo.previousSchoolMarksheet!!.url!!.isNotEmpty()) documentInfo.previousSchoolMarksheet!!.url!! else "--/--")
                    json4.put("value","--/--")
                    list.put(json4)

                    val json5 = JSONObject()
                    json5.put("label", " Experience Certificate")
                    json5.put("type","document")
//                json5.put("value", if (documentInfo != null && documentInfo.transferCertificate != null && documentInfo.transferCertificate!!.url!!.isNotEmpty()) documentInfo.transferCertificate!!.url!! else "--/--")
                    json5.put("value","--/--")
                    list.put(json5)

                    val json6 = JSONObject()
                    json6.put("label", "Photo & Signature")
                    json6.put("type","document")
//                json6.put("value", if (documentInfo != null && documentInfo.parentIdProof != null && documentInfo.parentIdProof!!.url!!.isNotEmpty()) documentInfo.parentIdProof!!.url!! else "--/--")
                    json6.put("value","--/--")
                    list.put(json6)

                    val json7 = JSONObject()
                    json7.put("label", "Address Proof")
                    json7.put("type","document")
//                json7.put("value", if (documentInfo != null && documentInfo.addressProof != null && documentInfo.addressProof!!.url!!.isNotEmpty()) documentInfo.addressProof!!.url!! else "--/--")
                    json7.put("value","--/--")
                    list.put(json7)

                    loadRecycler("document",list,binding.documentInfoRecycler)
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.VISIBLE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.documentInformation,"#DAEFFF",null)
                    binding.down2.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_6dp)
                    binding.down2.rotation = 0F
                }
            }

            binding.LeaveAttendance.setOnClickListener {
                if(binding.leaverecycle.visibility == View.GONE){
                    val list = JSONArray()
                    val academicInfo = result!!.teacherPreference
                    val json1 = JSONObject()
                    json1.put("label", "Leaves Taken (This Year)")
                    json1.put("type","text")
//                json1.put("value", if (academicInfo != null) academicInfo.teacherType else "--/--")
                    json1.put("value","--/--")
                    list.put(json1)

                    val json2 = JSONObject()
                    json2.put("label", "Remaining Casual Leaves")
                    json2.put("type","text")
//                json2.put("value", if (academicInfo != null) academicInfo.highestQualification!! else "--/--")
                    json2.put("value","--/--")
                    list.put(json2)

                    val json3 = JSONObject()
                    json3.put("label", "Last Leave Date")
                    json3.put("type","text")
//                json3.put("value", if (academicInfo != null ) academicInfo.overallExperience!! else "--/--")
                    json3.put("value","--/--")
                    list.put(json3)

                    val json4 = JSONObject()
                    json4.put("label", "Attendance Rate")
                    json4.put("type","text")
//                json4.put("value", if (academicInfo != null && academicInfo.board!!.isNotEmpty() ) academicInfo.board!! else "--/--")
                    json4.put("value","--/--")
                    list.put(json4)

                    loadRecycler("text",list,binding.leaverecycle)
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.VISIBLE
                    UiUtils.relativeLayoutBgDrawable(binding.LeaveAttendanceinfo,R.drawable.border_curve_top_10dp)
                    UiUtils.relativeLayoutBgTint(binding.LeaveAttendanceinfo,"#DAEFFF",null)
                    binding.down3.rotation = 180F
                }
                else{
                    binding.personalInfoRecycler.visibility = View.GONE
                    binding.academicInfoRecycler.visibility = View.GONE
                    binding.Bankrecycle.visibility = View.GONE
                    binding.documentInfoRecycler.visibility = View.GONE
                    binding.leaverecycle.visibility = View.GONE
                    UiUtils.relativeLayoutBgDrawable(binding.LeaveAttendanceinfo,R.drawable.border_curve_6dp)
                    binding.down3.rotation = 0F
                }
            }

        }


    }
    fun loadRecycler(type: String, cList: JSONArray, recyclerView: RecyclerView) {
        if (type == "text") {
            var mList = cList
            val linearLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this, mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        } else {
            var mList = cList
            val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this, mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }
    }

}