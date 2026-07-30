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
import com.lms.sch.databinding.ActivityParentPersonalInfoBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject

class ParentPersonalInfoActivity : BaseActivity() {
    lateinit var binding: ActivityParentPersonalInfoBinding
    var key = ""
    var userId = ""
    var result = ProfileDetailsResponse.ApplicationForm()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityParentPersonalInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (key != null) {
            key = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        }
        binding.pageHeading.text = key
        userId = intent.getStringExtra(Constants.IntentKeys.KEY1)!!

        DialogUtils.showLoader(this)
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

        binding.personalInfoLay.setOnClickListener {
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

        /*
                binding.parentInfoLay.setOnClickListener {
                    if(binding.parentInfoRecycler.visibility == View.GONE){
                        val list = JSONArray()
                        val parentInfo = result.parentInfo
                        val json1 = JSONObject()
                        json1.put("label", "Full Name")
                        json1.put("type","text")
                        json1.put("value", if (parentInfo != null && parentInfo.fatherName!!.isNotEmpty()) parentInfo.fatherName else "--/--")
                        list.put(json1)

                        val json2 = JSONObject()
                        json2.put("label", "Parent ID")
                        json2.put("type","text")
                        json2.put("value", if (parentInfo != null && parentInfo.parentId!!.isNotEmpty()) parentInfo.parentId else "--/--")
                        list.put(json2)

                        val json3 = JSONObject()
                        json3.put("label", "Gender")
                        json3.put("type","text")
                        json3.put("value", if (parentInfo != null && parentInfo.gender!!.isNotEmpty()) parentInfo.gender else "--/--")
                        list.put(json3)

                        val json4 = JSONObject()
                        json4.put("label", "Blood Group")
                        json4.put("type","text")
                        json4.put("value", if (parentInfo != null && parentInfo.bloodGroup!!.isNotEmpty()) parentInfo.bloodGroup else "--/--")
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
                        UiUtils.relativeLayoutBgDrawable(binding.parentInformation, R.drawable.border_curve_top_10dp)
                        UiUtils.relativeLayoutBgTint(binding.parentInformation,"#DAEFFF",null)
                        binding.down2.rotation = 180F
                    }
                    else{
                        binding.parentInfoRecycler.visibility = View.GONE
                        binding.personalInfoRecycler.visibility = View.GONE
                        binding.academicInfoRecycler.visibility = View.GONE
                        binding.documentInfoRecycler.visibility = View.GONE
                        UiUtils.relativeLayoutBgDrawable(binding.parentInformation, R.drawable.border_curve_6dp)
                        binding.down2.rotation = 0F
                    }
                }
        */
    }
    fun loadRecycler(type: String, cList: JSONArray, recyclerView: RecyclerView){
        if (type == "text"){
            var mList = cList
            val linearLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this,mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }
        else {
            var mList = cList
            val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this,mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }
    }
}