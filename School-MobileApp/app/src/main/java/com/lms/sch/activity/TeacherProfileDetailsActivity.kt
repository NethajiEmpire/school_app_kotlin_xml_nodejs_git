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
import com.lms.sch.databinding.ActivityTeacherProfileDetailsBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.response.TeacherprofileSignleViewResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.Constants.IntentKeys.KEY
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject

class TeacherProfileDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityTeacherProfileDetailsBinding
    var result = TeacherprofileSignleViewResponse.Result()
    var key = ""
    var userId = ""
    var list = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTeacherProfileDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        key = intent.getStringExtra("key")!!
        userId = intent.getStringExtra("key1")!!
        if (key == "My Details") {
            binding.pageHeading.text = key
            binding.myDetails.visibility = View.VISIBLE
            binding.professionalDetails.visibility = View.GONE

            DialogUtils.showLoader(this)
            if (sharedHelper.role == "TEACHER") {
                ApiConnection.getInstance().teacherProfiles(this, userId).observe(this) {
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success ->
                            if (success) {
                                if (it.result != null) {
                                    result = it.result!!
                                    binding.personalInfoLay.performClick()
                                }
                            } else {
                                UiUtils.showSnack(it.msg, binding.root, false)
                            }
                        }
                    }
                }
            } else {
                ApiConnection.getInstance().teacherProfile(this).observe(this) {
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success ->
                            if (success) {
                                if (it.result != null) {
                                    result = it.result!!
                                } else {
                                    UiUtils.showSnack(it.msg, binding.root, false)
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (key == "Professional Details") {
            binding.pageHeading.text = key
            binding.myDetails.visibility = View.GONE
            binding.professionalDetails.visibility = View.VISIBLE
        }
        getTeacherInfo()
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }

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
                UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_top_10dp)
                UiUtils.relativeLayoutBgTint(binding.personalInformation, "#DAEFFF", null)
                binding.down1.rotation = 180F
            } else {
                binding.personalInfoRecycler.visibility = View.GONE
                binding.academicInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_6dp)
                binding.down1.rotation = 0F
            }
        }

        binding.academicInfoLay.setOnClickListener {
            if(binding.academicInfoRecycler.visibility == View.GONE){
                val list = JSONArray()
                val academicInfo = result
                val json1 = JSONObject()
                json1.put("label", "Teacher Type")
                json1.put("type","text")
                json1.put("value", if (academicInfo != null && academicInfo.teacherPreference!!.teacherType!!.isNotEmpty()) academicInfo.teacherPreference!!.teacherType else "--/--")
                list.put(json1)

                val json2 = JSONObject()
                json2.put("label", "Highest Qualification")
                json2.put("type","text")
                json2.put("value", if (academicInfo != null && academicInfo.teacherPreference!!.highestQualification!!.isNotEmpty()) academicInfo.teacherPreference!!.highestQualification else "--/--")
                list.put(json2)

                val json3 = JSONObject()
                json3.put("label", "Experience")
                json3.put("type","text")
                json3.put("value", if (academicInfo != null && academicInfo.teacherPreference!!.overallExperience!!.isNotEmpty()) academicInfo.teacherPreference!!.overallExperience else "--/--")
                list.put(json3)

                val json4 = JSONObject()
                json4.put("label", "Board of Education")
                json4.put("type","text")
                json4.put("value", if (academicInfo != null && academicInfo.teacherPreference!!.board!!.isNotEmpty() && academicInfo.teacherPreference!!.board!!.isNotEmpty()) academicInfo.teacherPreference!!.board!!.map { it.name }.joinToString(", ") else "--/--")
                list.put(json4)

                val json5 = JSONObject()
                json5.put("label", "Major Subjects")
                json5.put("type","text")
                json5.put("value", if (academicInfo != null && academicInfo.teacherPreference!!.majorSubjects!!.isNotEmpty()) academicInfo.teacherPreference!!.majorSubjects!!.map { it.name }.joinToString(", ") else "--/--")
                list.put(json5)

                loadRecycler("text",list,binding.academicInfoRecycler)
                binding.academicInfoRecycler.visibility = View.VISIBLE
                binding.personalInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_top_10dp)
                UiUtils.relativeLayoutBgTint(binding.academicInformation,"#DAEFFF",null)
                binding.down3.rotation = 180F
            }
            else{
                binding.academicInfoRecycler.visibility = View.GONE
                binding.personalInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_6dp)
                binding.down3.rotation = 0F
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

    private fun getTeacherInfo() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().teacherProfiles(this, userId).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            userId = it.result!!._id!!
                            if (it.result!!.firstName != null || it.result!!.lastName != null) {
                                binding.staffName.text =
                                    it.result!!.firstName + " " + it.result!!.lastName
                            } else {
                                binding.staffName.text = "--/--"
                            }
                            if (it.result!!.role != null && it.result!!.role!!.name != null) {
                                binding.role.text = it.result!!.role!!.name
                            } else {
                                binding.role.text = "--/--"
                            }

                            if (it.result!!.mobile != null) {
                                binding.mblNumber.text = it.result!!.mobile
                            } else {
                                binding.mblNumber.text = "--/-- "
                            }
                            if (it.result!!.email != null) {
                                binding.email.text = it.result!!.email
                            } else {
                                binding.email.text = "--/--"
                            }
                            UiUtils.log("dxfgfgh", it.result!!.gender)

                            if (it.result!!.gender != null) {

                                binding.gender.text = it.result!!.gender
                            } else {
                                binding.gender.text = "--/--"
                            }

                            if (it.result!!.address != null) {

                                binding.address.text = "${it.result!!.address ?: ""}"
                            } else {
                                binding.address.text = "--/-- "
                            }
                            if (it.result!!.lead_id != null) {
                                binding.tId.text = "${it.result!!.lead_id ?: ""}"
                            } else {
                                binding.tId.text = "--/-- "
                            }

                            if (it.result!!.blood_group != null) {
                                binding.bloodgroup.text = it.result!!.blood_group
                            } else {
                                binding.bloodgroup.text = "--/-- "
                            }
                            /*
                            if (it.result!!.dob != null) {
                                binding.dateOfBirth.text = BaseUtils.getFormattedDate(it.result!!.dob!!, Constants.ApiKeys.TIME_INPUT_FORMAT1, Constants.ApiKeys.DATE_FORMAT)
                            } else {
                                binding.dateOfBirth.text = "--/-- "
                            }*/
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getAcademicDetails() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().teacherProfiles(this, userId).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            userId = it.result!!._id!!

                            if (it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.teacherType != null) {
                                binding.teacherType.text = it.result!!.teacherPreference!!.teacherType
                            } else {
                                binding.teacherType.text = "--/--"
                            }

                            if (it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.highestQualification != null) {
                                binding.qualification.text = it.result!!.teacherPreference!!.highestQualification
                            } else {
                                binding.qualification.text = "--/--"
                            }
                            if (it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.overallExperience != null) {
                                binding.experience.text = it.result!!.teacherPreference!!.overallExperience
                            } else {
                                binding.experience.text = "--/--"
                            }
                            if (it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.board!![0].name != null) {
                                binding.board.text = it.result!!.teacherPreference!!.board!![0].name!!
                            } else {
                                binding.board.text = "--/--"
                            }
                            if (it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.majorSubjects!![0].name != null) {
                                binding.sub.text = it.result!!.teacherPreference!!.majorSubjects!![0].name!!
                            } else {
                                binding.sub.text = "--/--"
                            }
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
}