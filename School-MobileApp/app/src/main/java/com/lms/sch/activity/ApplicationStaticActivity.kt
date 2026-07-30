package com.lms.sch.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.CardsAdapter
import com.lms.sch.adapter.GuestAdapter
import com.lms.sch.adapter.GuestStaticAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.databinding.ActivityApplicationBinding
import com.lms.sch.databinding.ActivityApplicationStaticBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

class ApplicationStaticActivity : BaseActivity() {

    lateinit var binding : ActivityApplicationStaticBinding
    var titlepos = 0
    var adapter: GuestStaticAdapter? = null
    var genderlist: ArrayList<String> = ArrayList()
    var bloodGroupList: ArrayList<String> = ArrayList()
    var nationalityList: ArrayList<String> = ArrayList()
    var religoiusList: ArrayList<String> = ArrayList()
    var categoryList: ArrayList<String> = ArrayList()
    var titles = ArrayList<String>()
    var whomPaid = ""
    var modeofPay = ""
    var isStudentCreated = false
    var isApplicationSubmit = false
    var feePage = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityApplicationStaticBinding.inflate(layoutInflater)
        setContentView(binding.root)
        titles.clear()
        titles.add("Student Information")
        titles.add("Parent / Guardian Info")
        titles.add("Academic Information")
        titles.add("Document Upload")
        titles.add("Fee Payment")
        titles.add("Waiting for Admin Approval")
        titles.add("Acknowledgment")

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.steps.text = "01 of 0" +titles.size
        loadRecycler(titles[titlepos])
        binding.title.text = titles[titlepos]

        /*binding.next.setOnClickListener {
            */
        /*if (isValidFields()){

            }*/

        /*
            UiUtils.log("jhgf","next")
            saveData()
            if (titles[titlepos] == "Fee Payment") {
                if (feePage == 1) {
                    titlepos++
                    binding.steps.text = "0${titlepos+1} of 0"+titles.size
                    binding.title.text = titles[titlepos]
                    binding.includePayment.root.visibility = View.VISIBLE
                    binding.includePayment.page1.visibility = View.GONE
                    binding.includePayment.page2.visibility = View.VISIBLE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    loadProgress(titlepos+1)
                } else {
                    feePage = 0
                }
            }
            titlepos++
            if(titlepos < titles.size){
                binding.steps.text = "0${titlepos+1} of 0"+titles.size
                binding.title.text = titles[titlepos]
                if (titles[titlepos] == "Fee Payment" && feePage == 0){
                    binding.includePayment.root.visibility = View.VISIBLE
                    binding.includePayment.page1.visibility = View.VISIBLE
                    binding.includePayment.page2.visibility = View.GONE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    loadProgress(titlepos+1)
                }
                else if (titles[titlepos] == "Fee Payment" && feePage == 1){
                    binding.includePayment.root.visibility = View.VISIBLE
                    binding.includePayment.page1.visibility = View.GONE
                    binding.includePayment.page2.visibility = View.VISIBLE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                }
                else if (titles[titlepos] == "Document Upload" ){
                    binding.includePayment.root.visibility = View.GONE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                    loadDocumentPage()
                }
                else if (titles[titlepos] == "Waiting for Admin Approval") {
                    binding.includePayment.root.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    binding.linDocVerify.visibility = View.VISIBLE
                    UiUtils.log("jhgf","next")
                    loadProgress(titlepos+1)

                }

                else {
                    */

        /*if (titles[titlepos] == "Student Information" && !isStudentCreated){
                        isStudentCreated = false
//                        studentCreate()
                    }
                    else if (titles[titlepos] == "Academic Information" && !isApplicationSubmit){
                        isApplicationSubmit = false
//                        applicationForm()
                    }*/
        /*
                    binding.includePayment.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                    binding.noData.root.visibility = View.GONE
                    loadRecycler(titles[titlepos])
                }
            }
            else{
//                titlepos = 0
//                binding.title.text = titles[titlepos]
//                loadRecycler(titles[titlepos])
            }
        }*/

        binding.next.setOnClickListener {
            UiUtils.log("jhgf", "next")
            saveData()

            if (titlepos == 6){
                TempSingleton.getInstance().isFormComplete = true
                finish()
            }

            if (titles[titlepos] == "Fee Payment") {
                if (feePage == 0) {
                    feePage = 1
                    binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
                    binding.title.text = titles[titlepos]
                    binding.includePayment.root.visibility = View.VISIBLE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    binding.includeAcknow.root.visibility = View.GONE
                    loadProgress(titlepos + 1)
                    return@setOnClickListener
                } else {
                    feePage = 0
                }
            }

            titlepos++
            if (titlepos < titles.size) {
                binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
                binding.title.text = titles[titlepos]

                if (titles[titlepos] == "Fee Payment" && feePage == 0) {
                    binding.includePayment.root.visibility = View.VISIBLE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    loadProgress(titlepos + 1)
                } else if (titles[titlepos] == "Fee Payment" && feePage == 1) {
                    binding.includePayment.root.visibility = View.VISIBLE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                } else if (titles[titlepos] == "Document Upload") {
                    binding.includePayment.root.visibility = View.GONE
                    binding.noData.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                    loadDocumentPage()
                } else if (titles[titlepos] == "Waiting for Admin Approval") {
                    binding.includePayment.root.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    binding.linDocVerify.visibility = View.VISIBLE
                    UiUtils.log("jhgf", "next")
                    loadProgress(titlepos + 1)
                }
                 else if (titles[titlepos] == "Acknowledgment") {
                    binding.includePayment.root.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.noData.root.visibility = View.GONE
                    binding.includeAcknow.root.visibility = View.VISIBLE
                    UiUtils.log("jhgf", "Acknowledgment Page Shown")
                    loadProgress(titlepos + 1)
                } else {
                    binding.includePayment.root.visibility = View.GONE
                    binding.linDocVerify.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                    binding.noData.root.visibility = View.GONE
                    UiUtils.log("jhgf", "loading recycler")
                    loadRecycler(titles[titlepos])

                }
            }
        }




        binding.spinnerList.close.setOnClickListener {
            binding.spinnerList.search.setText("")
            binding.spinnerList.root.visibility = View.GONE
        }
        binding.root.setOnRefreshListener {
            binding.root.isRefreshing = false
        }

        /*binding.includePayment.chkOnline.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.includePayment.chkOffline.isChecked = false
              //  binding.includePayment.linOfffline.visibility = View.GONE
                binding.includePayment.linOnline.visibility = View.VISIBLE
                binding.includePayment.chkOnline.visibility = View.VISIBLE
                binding.includePayment.onlinePay.root.visibility=View.VISIBLE
//                loadOnlinePay()
            }
        }
        binding.includePayment.chkOffline.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.includePayment.chkOnline.isChecked = false
                binding.includePayment.linOnline.visibility = View.GONE
               // binding.includePayment.linOfffline.visibility = View.VISIBLE
                binding.includePayment.chkOnline.visibility = View.VISIBLE
                loadspin()
            }
        }*/

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {}
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        if (binding.spinnerList.root.visibility == View.VISIBLE){
                            binding.spinnerList.root.visibility = View.GONE
//                            binding.spinnerList.search.setText("")
                        }
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {}
                }
            }
        })


    }

    fun loadDocumentPage(){
        if(sharedHelper.documentUpload.length() == 0){
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().getDocumentMaster(this).observe(this) {
                DialogUtils.dismissLoader()
                it?.let {
                    it.success.let { success ->
                        if (success) {
                            if(it.result != null && it.result!!.size > 0){
                                val jsonArray:JSONArray = JSONArray()
                                for(items in it.result!!){
                                    val jsonObject1 = JSONObject()
                                    jsonObject1.put("type","textView")
                                    jsonObject1.put("hint",items.DOC_CODE)
                                    if(items.required!!){
                                        jsonObject1.put("title",items.DOC_TYPE+" *")
                                        UiUtils.log("jhgf",""+items.DOC_TYPE)
                                        jsonObject1.put("showValue","Choose File")
                                    }
                                    else{
                                        jsonObject1.put("title",items.DOC_TYPE)
                                        jsonObject1.put("showValue","Choose File")
                                    }
                                    jsonObject1.put("value","")
                                    jsonObject1.put("status","")
                                    jsonObject1.put("statusValue","")
                                    jsonArray.put(jsonObject1)
                                }
                                sharedHelper.documentUpload = jsonArray
                                loadRecycler("Document Upload")
                            }
                            else{
                                UiUtils.showSnack(it.msg, binding.root,false)
                            }
                            /*else{
                                val jsonObject1 = JSONObject()
                                jsonObject1.put("type","textView")
                                jsonObject1.put("title","All Semester Marksheet *")
                                jsonObject1.put("hint","")
                                jsonObject1.put("showValue","Choose File")
                                jsonObject1.put("value","")
                                docJsonArray.put(jsonObject1)

                                val jsonObject2 = JSONObject()
                                jsonObject2.put("type","textView")
                                jsonObject2.put("title","10th Marksheet *")
                                jsonObject2.put("hint","")
                                jsonObject2.put("showValue","Choose File")
                                jsonObject2.put("value","")
                                docJsonArray.put(jsonObject2)

                                val jsonObject3 = JSONObject()
                                jsonObject3.put("type","textView")
                                jsonObject3.put("title","Degree or Professional certificate *")
                                jsonObject3.put("hint","")
                                jsonObject3.put("showValue","Choose File")
                                jsonObject3.put("value","")
                                docJsonArray.put(jsonObject3)

                                val jsonObject4 = JSONObject()
                                jsonObject4.put("type","textView")
                                jsonObject4.put("title","Photo ID Proof - Adhar Card *")
                                jsonObject4.put("hint","")
                                jsonObject4.put("showValue","Choose File")
                                jsonObject4.put("value","")
                                docJsonArray.put(jsonObject4)

                                val jsonObject5 = JSONObject()
                                jsonObject5.put("type","textView")
                                jsonObject5.put("title","Passport Size Photo *")
                                jsonObject5.put("hint","")
                                jsonObject5.put("showValue","Choose File")
                                jsonObject5.put("value","")
                                docJsonArray.put(jsonObject5)

                                val jsonObject6 = JSONObject()
                                jsonObject6.put("type","textView")
                                jsonObject6.put("title","Community certificate *")
                                jsonObject6.put("hint","")
                                jsonObject6.put("showValue","Choose File")
                                jsonObject6.put("value","")
                                docJsonArray.put(jsonObject6)

                                val jsonObject7 = JSONObject()
                                jsonObject7.put("type","textView")
                                jsonObject7.put("title","Transfer certificate *")
                                jsonObject7.put("hint","")
                                jsonObject7.put("showValue","Choose File")
                                jsonObject7.put("value","")
                                docJsonArray.put(jsonObject7)

                                val jsonObject8 = JSONObject()
                                jsonObject8.put("type","textView")
                                jsonObject8.put("title","Birth certificate *")
                                jsonObject8.put("hint","")
                                jsonObject8.put("showValue","Choose File")
                                jsonObject8.put("value","")
                                docJsonArray.put(jsonObject8)
                            }*/
                        }
                        else {
                            UiUtils.showSnack(it.msg, binding.root,false)
                        }
                    }
                }
            }
        }
        else{
            var requireCount1 = 0
            for(i in 0 until  sharedHelper.documentUpload.length()){
                if(sharedHelper.documentUpload.getJSONObject(i).optString("title").contains("*")){
                    if(sharedHelper.documentUpload.getJSONObject(i).optString("value").isEmpty()){
                        requireCount1++
                    }
                }
                /*else{
                    if(sharedHelper.documentUpload.getJSONObject(i).optString("value").isEmpty()){
                        requireCount1++
                    }
                }*/
            }

            if(requireCount1 != 0){
                // titlepos = 5
                loadRecycler("Document Upload")
                if(TempSingleton.getInstance().docDetails.isNotEmpty()){
                    var isRejected = false
                    val jsonArray = sharedHelper.documentUpload
                    for(items in TempSingleton.getInstance().docDetails){
                        Log.d("xfgnx",items.DOC_STATUS.toString())
                        val doc_id = items.DOC_CODE.toString()
                        if(items.DOC_STATUS.equals("rejected")){
                            isRejected = true
//                            val remarks = items.Remarks[0]
                            for (i in 0 until  jsonArray.length()){
                                if(jsonArray.getJSONObject(i).optString("hint") == doc_id){
                                    jsonArray.getJSONObject(i).put("value", "")
                                    jsonArray.getJSONObject(i).put("status", items.DOC_STATUS)
//                                    jsonArray.getJSONObject(i).put("statusValue", items.Remarks.last())
                                }
                            }
                        }
                        else{
                            for (i in 0 until  jsonArray.length()){
                                if(jsonArray.getJSONObject(i).optString("hint") == doc_id){
                                    jsonArray.getJSONObject(i).put("value", items.DOC_URL)
                                    jsonArray.getJSONObject(i).put("status", items.DOC_STATUS)
                                }
                            }
                        }
                    }

                    if(isRejected){
                        sharedHelper.documentUpload = jsonArray
                        loadRecycler("Document Upload")
                    }
                }
            }
            else{
                binding.recycler.visibility = View.GONE
                binding.noData.root.visibility = View.GONE
                binding.includePayment.root.visibility = View.GONE
                binding.next.visibility = View.GONE
                binding.linDocVerify.visibility = View.VISIBLE
                if(TempSingleton.getInstance().docDetails.isNotEmpty()){
                    var isRejected = false
                    val jsonArray = sharedHelper.documentUpload
                    for(items in TempSingleton.getInstance().docDetails){
                        val doc_id = items.DOC_CODE.toString()
                        if(items.DOC_STATUS.equals("rejected")){
                            isRejected = true
//                            val remarks = items.Remarks[0]
                            for (i in 0 until  jsonArray.length()){
                                if(jsonArray.getJSONObject(i).optString("hint") == doc_id){
                                    jsonArray.getJSONObject(i).put("value", "")
                                    jsonArray.getJSONObject(i).put("status", items.DOC_STATUS)
//                                    jsonArray.getJSONObject(i).put("statusValue", remarks)
                                }
                            }
                        }
                        else{
                            for (i in 0 until  jsonArray.length()){
                                if(jsonArray.getJSONObject(i).optString("hint") == doc_id){
                                    jsonArray.getJSONObject(i).put("value", items.DOC_URL)
                                    jsonArray.getJSONObject(i).put("status", items.DOC_STATUS)
                                }
                            }
                        }
                    }

                    if(isRejected){
                        sharedHelper.documentUpload = jsonArray
                        loadRecycler("Document Upload")
                    }
                }
            }
        }
    }

    fun isValidFields(): Boolean {
        if (titlepos == 0) {
            for (i in 0 until adapter!!.list.length()) {
                val title = adapter!!.list.getJSONObject(i).optString("title")
                val showValue = adapter!!.list.getJSONObject(i).optString("showValue")
                val type = adapter!!.list.getJSONObject(i).optString("type")
                val isRequired = title.contains("*")

                if (isRequired && showValue.isEmpty()) {
                    val cleanTitle = title.replace("*", "").trim()
                    if (cleanTitle == "Student Phone Number") {
                        if (showValue.isNotEmpty() && BaseUtils.isValidPhone(showValue)){
                            UiUtils.showSnack("Please enter a valid Mobile Number.", binding.root, false)
                            return false
                        }
                        else {
                            UiUtils.showSnack("Please enter $cleanTitle", binding.root, false)
                            return false
                        }
                    }
                    else if (title == "Student Email *") {
                        if (showValue.isNotEmpty() && BaseUtils.isValidEmailId(showValue)){
                            UiUtils.showSnack("Please enter a valid Email ID.", binding.root, false)
                            return false
                        }
                        else {
                            UiUtils.showSnack("Please enter $cleanTitle", binding.root, false)
                            return false
                        }
                    }
                    else {
                        if (type == "spinner") {
                            UiUtils.showSnack("Please select $cleanTitle", binding.root, false)
                            return false
                        } else {
                            UiUtils.showSnack("Please enter $cleanTitle", binding.root, false)
                            return false
                        }
                    }
                }
            }
            return true
        }
        else if (titlepos == 1) {
            for (i in 0 until adapter!!.list.length()) {
                val title = adapter!!.list.getJSONObject(i).optString("title")
                val showValue = adapter!!.list.getJSONObject(i).optString("showValue")
                val type = adapter!!.list.getJSONObject(i).optString("type")
                val isRequired = title.contains("*")

                if (isRequired && showValue.isEmpty()) {
                    val cleanTitle = title.replace("*", "").trim()
                    if (type == "spinner") {
                        UiUtils.showSnack("Please select $cleanTitle", binding.root, false)
                        return false
                    } else {
                        UiUtils.showSnack("Please enter $cleanTitle", binding.root, false)
                        return false
                    }
                }
            }
            saveData()
            return true
        }
        else if (titlepos == 2) {
            for (i in 0 until adapter!!.list.length()) {
                val title = adapter!!.list.getJSONObject(i).optString("title")
                val showValue = adapter!!.list.getJSONObject(i).optString("showValue")
                val type = adapter!!.list.getJSONObject(i).optString("type")
                val isRequired = title.contains("*")

                if (isRequired && showValue.isEmpty()) {
                    val cleanTitle = title.replace("*", "").trim()
                    if (type == "spinner") {
                        UiUtils.showSnack("Please select $cleanTitle", binding.root, false)
                        return false
                    } else {
                        UiUtils.showSnack("Please enter $cleanTitle", binding.root, false)
                        return false
                    }
                }
            }
            saveData()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if (binding.spinnerList.root.visibility == View.VISIBLE) {
            binding.spinnerList.root.visibility = View.GONE
        }

        /*else if (binding.includePayment.linOnline.visibility == View.VISIBLE) {
            binding.includePayment.chkOnline.isChecked = false
            binding.includePayment.chkOffline.isChecked = true
            binding.includePayment.linOnline.visibility = View.GONE
            binding.includePayment.linOfffline.visibility = View.VISIBLE
            loadspin()
        }
        else if (binding.includePayment.linOfffline.visibility == View.VISIBLE) {
            binding.includePayment.chkOnline.isChecked = true
            binding.includePayment.chkOffline.isChecked = false
            binding.includePayment.linOnline.visibility = View.VISIBLE
            binding.includePayment.linOfffline.visibility = View.GONE
        }*/

        else if (titlepos > 0 && titles[titlepos] == "Parent / Guardian Info") {
            titlepos--
            loadRecycler(titles[titlepos])
            binding.steps.text = "0${titlepos+1} of 0"+titles.size
            binding.title.text = titles[titlepos]
        }
        else if (titlepos > 0 && titles[titlepos] == "Academic Information") {
            titlepos--
            loadRecycler(titles[titlepos])
            binding.steps.text = "0${titlepos+1} of 0"+titles.size
            binding.title.text = titles[titlepos]
        }
        else if (titlepos > 0 && titles[titlepos] == "Document Upload") {
            titlepos--
            loadRecycler(titles[titlepos])
            binding.steps.text = "0${titlepos+1} of 0"+titles.size
            binding.title.text = titles[titlepos]
        }

        else if (titlepos > 0 && titles[titlepos] == "Fee Payment") {
            titlepos--
            loadRecycler(titles[titlepos])
            binding.steps.text = "0${titlepos + 1} of 0" + titles.size
            binding.title.text = titles[titlepos]

        }
        else if (titlepos > 0 && titles[titlepos] == "Waiting for Admin Approval") {
            titlepos--
            loadRecycler(titles[titlepos])
            binding.steps.text = "0${titlepos+1} of 0"+titles.size
            binding.title.text = titles[titlepos]
            binding.includePayment.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
        }

        else if (titles[titlepos] == "Student Information") {
            finish()
        }
        else {
            super.onBackPressed()
        }
    }



    private fun initiatePayment() {
        DialogUtils.showLoader(this)

        try {
            val options = JSONObject()
            options.put("name", "Your School Name")
            options.put("description", "School Fees Payment")
            options.put("currency", "INR")
            options.put("amount", "10000") // Amount in paise

            // Prefill email if available
            sharedHelper.studentInfo.optJSONObject(9)?.let { emailObj ->
                options.put("prefill", JSONObject().apply {
                    put("email", emailObj.optString("showValue"))
                    put("contact", sharedHelper.mobileNumber)
                })
            }

        } catch (e: Exception) {
            DialogUtils.dismissLoader()
            Toast.makeText(this, "Payment failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadspin(){
        val spinlist1: ArrayList<String> = ArrayList()
        val spinlist2: ArrayList<String> = ArrayList()
        // spinlist1.add("Select")
        spinlist1.add("Accounts")
        spinlist1.add("To Mentor")
        spinlist1.add("Others")
        // spinlist2.add("Select")
        spinlist2.add("Cash")
        spinlist2.add("Cheque")
        spinlist2.add("DD")
        val adapter1 = SpinnerAdapter(this, spinlist1)
        binding.includePayment.spinnerWhomPaid.adapter = adapter1
        binding.includePayment.spinnerWhomPaid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val clickedValue: String = parent.getItemAtPosition(position) as String
                if(position != 0){
                    whomPaid = clickedValue
                }
                else{
                    // whomPaid = ""
                    whomPaid = clickedValue
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val adapter = SpinnerAdapter(this, spinlist2)
        binding.includePayment.spinnerModeOfPay.adapter = adapter
        binding.includePayment.spinnerModeOfPay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val clickedValue: String = parent.getItemAtPosition(position) as String
                if(position != 0){
                    modeofPay = clickedValue
                }
                else{
                    // modeofPay = ""
                    modeofPay = clickedValue
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    fun loadRecycler(value: String){
        adapter = GuestStaticAdapter(this, getJsonArray(value))
        binding.recycler.adapter = adapter
        binding.recycler.scrollToPosition(titlepos)
    }

    fun studentCreate(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentForm(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        isStudentCreated = true
                        UiUtils.showSnack(it.msg,binding.root,true)
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    fun applicationForm(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().applicationForm(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        isApplicationSubmit = true
                        UiUtils.showSnack(it.msg,binding.root,true)
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    fun saveData(){
        if(binding.title.text == "Document Upload"){
            sharedHelper.documentUpload = adapter!!.list
        }
        else if(titlepos == 0){
            sharedHelper.studentInfo = adapter!!.list
        }
        else if(titlepos == 1){
            sharedHelper.guardianInfo = adapter!!.list
        }
        else if(titlepos == 2){
            sharedHelper.academicInfo = adapter!!.list
        }
    }

    /*fun searchList(str:String){
        if(str.isNotEmpty()){
            var slist: ArrayList<String> = ArrayList()
            for(items in customerList){
                if(items.name!!.contains(str,true)){
                    slist.add(items)
                }
            }
            loadCusRecycler(slist)
        }
        else{
            binding.userCreate.card.visibility = View.GONE
            binding.userCreate.close.visibility = View.GONE
            loadCusRecycler(customerList)
        }
    }*/

    fun loadSpinnerRecycler(data: ArrayList<String>, position: Int) {
        binding.spinnerList.root.visibility = View.VISIBLE
        if (data.isNotEmpty()) {
            val adapter = CardsAdapter(this, data, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    val jsonObject = adapter!!.list.getJSONObject(position)
                    jsonObject.put("value", data[pos])
                    jsonObject.put("showValue", data[pos])
                    adapter!!.notifyItemChanged(position)
                    binding.spinnerList.root.visibility = View.GONE
                }
            })
            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.spinnerList.recycler.layoutManager = linearLayoutManager
            binding.spinnerList.recycler.adapter = adapter
        }
    }

    fun getJsonArray(value: String): JSONArray {
        Log.e("vdv",""+value+".."+titlepos)
//        adapter1!!.selectedPosition = titlepos
//        adapter1!!.notifyDataSetChanged()
        var jsonArray: JSONArray = JSONArray()
        if(value == "Student Information"){
            loadProgress(1)
            if(sharedHelper.studentInfo.length() != 0){
                jsonArray = sharedHelper.studentInfo
            }
            else{
                val jsonObject1 = JSONObject()
                jsonObject1.put("type","editText")
                jsonObject1.put("title","First Name *")
                jsonObject1.put("hint","Enter First Name")
                jsonObject1.put("hintText","Enter First Name")
                jsonObject1.put("showValue","")
                jsonObject1.put("value","")
                jsonObject1.put("inputType","name")
                jsonArray.put(jsonObject1)

                val jsonObject2 = JSONObject()
                jsonObject2.put("type","editText")
                jsonObject2.put("title","Last Name *")
                jsonObject2.put("hint","Enter Last Name")
                jsonObject2.put("hintText","Enter Last Name")
                jsonObject2.put("showValue","")
                jsonObject2.put("value","")
                jsonObject2.put("inputType","name")
                jsonArray.put(jsonObject2)

                val jsonObject3 = JSONObject()
                jsonObject3.put("type","calender")
                jsonObject3.put("title","Date Of Birth *")
                jsonObject3.put("hint","yyyy/MM/DD")
                jsonObject3.put("hintText","yyyy/MM/DD")
                jsonObject3.put("showValue","")
                jsonObject3.put("value","")
                jsonObject3.put("inputType","name")
                jsonArray.put(jsonObject3)

                val jsonObject4 = JSONObject()
                jsonObject4.put("type","spinner")
                jsonObject4.put("title","Gender *")
                jsonObject4.put("hint","Gender")
                jsonObject4.put("hintText","Select..")
                jsonObject4.put("showValue","")
                jsonObject4.put("value","")
                jsonObject4.put("inputType","name")
                jsonArray.put(jsonObject4)

                val jsonObject04 = JSONObject()
                jsonObject04.put("type","spinner")
                jsonObject04.put("title","Blood Group *")
                jsonObject04.put("hint","Blood Group")
                jsonObject04.put("hintText","Select..")
                jsonObject04.put("showValue","")
                jsonObject04.put("value","")
                jsonObject04.put("inputType","name")
                jsonArray.put(jsonObject04)

                val jsonObject5 = JSONObject()
                jsonObject5.put("type","spinner")
                jsonObject5.put("title","Nationality *")
                jsonObject5.put("hint","Nationality")
                jsonObject5.put("hintText","Select..")
                jsonObject5.put("showValue","")
                jsonObject5.put("value","")
                jsonObject5.put("inputType","text")
                jsonArray.put(jsonObject5)

                val jsonObject6 = JSONObject()
                jsonObject6.put("type","spinner")
                jsonObject6.put("title","Religion *")
                jsonObject6.put("hint","Religion")
                jsonObject6.put("hintText","Select..")
                jsonObject6.put("showValue","")
                jsonObject6.put("value","")
                jsonObject6.put("inputType","text")
                jsonArray.put(jsonObject6)

                val jsonObject7 = JSONObject()
                jsonObject7.put("type","spinner")
                jsonObject7.put("title","Category *")
                jsonObject7.put("hint","Category")
                jsonObject7.put("hintText","Select..")
                jsonObject7.put("showValue","")
                jsonObject7.put("value","")
                jsonObject7.put("inputType","text")
                jsonArray.put(jsonObject7)

                val jsonObject8 = JSONObject()
                jsonObject8.put("type","editText")
                jsonObject8.put("title","Aadhaar Number *")
                jsonObject8.put("hint","Enter Aadhaar Number")
                jsonObject8.put("hintText","Enter Aadhaar Number")
                jsonObject8.put("showValue","")
                jsonObject8.put("value","")
                jsonObject8.put("inputType","number")
                jsonArray.put(jsonObject8)

                val jsonObject9 = JSONObject()
                jsonObject9.put("type","editText")
                jsonObject9.put("title","Student Phone Number *")
                jsonObject9.put("hint","Enter Phone Number")
                jsonObject9.put("hintText","Enter Phone Number")
                jsonObject9.put("showValue",sharedHelper.mobileNumber)
                jsonObject9.put("value","")
                jsonObject9.put("inputType","number")
                jsonArray.put(jsonObject9)

                val jsonObject10 = JSONObject()
                jsonObject10.put("type","editText")
                jsonObject10.put("title","Student Email *")
                jsonObject10.put("hint","Enter Email")
                jsonObject10.put("hintText","Enter Email")
                jsonObject10.put("showValue","")
                jsonObject10.put("value","")
                jsonObject10.put("inputType","email")
                jsonArray.put(jsonObject10)

                val jsonObject11 = JSONObject()
                jsonObject11.put("type","editText")
                jsonObject11.put("title","Pincode *")
                jsonObject11.put("hint","Pincode")
                jsonObject11.put("hintText","Pincode")
                jsonObject11.put("showValue","")
                jsonObject11.put("value","")
                jsonObject11.put("inputType","number")
                jsonArray.put(jsonObject11)

                val jsonObject12 = JSONObject()
                jsonObject12.put("type","spinner")
                jsonObject12.put("title","Country *")
                jsonObject12.put("hint","select..")
                jsonObject12.put("hintText","select..")
                jsonObject12.put("showValue","")
                jsonObject12.put("value","")
                jsonObject12.put("inputType","name")
                jsonArray.put(jsonObject12)

                /*val jsonObject13 = JSONObject()
                jsonObject13.put("type","editText")
                jsonObject13.put("title","Aadhaar Number")
                jsonObject13.put("hint","Aadhaar Number")
                jsonObject13.put("hintText","Enter Aadhaar Number")
                jsonObject13.put("showValue","")
                jsonObject13.put("value","")
                jsonObject13.put("inputType","number")
                jsonArray.put(jsonObject13)*/

                val jsonObject14 = JSONObject()
                jsonObject14.put("type","spinner")
                jsonObject14.put("title","State *")
                jsonObject14.put("hint","select..")
                jsonObject14.put("hintText","select..")
                jsonObject14.put("showValue","")
                jsonObject14.put("value","")
                jsonObject14.put("inputType","text")
                jsonArray.put(jsonObject14)

                val jsonObject15 = JSONObject()
                jsonObject15.put("type","spinner")
                jsonObject15.put("title","City *")
                jsonObject15.put("hint","select..")
                jsonObject15.put("hintText","select..")
                jsonObject15.put("showValue","")
                jsonObject15.put("value","")
                jsonObject15.put("inputType","text")
                jsonArray.put(jsonObject15)

                val jsonObject16 = JSONObject()
                jsonObject16.put("type","address")
                jsonObject16.put("title","Address *")
                jsonObject16.put("hint","Enter Full Address")
                jsonObject16.put("hintText","Enter Full Address")
                jsonObject16.put("showValue","")
                jsonObject16.put("value","")
                jsonObject16.put("inputType","text")
                jsonArray.put(jsonObject16)

                /*val jsonObject17 = JSONObject()
                jsonObject17.put("type","editText")
                jsonObject17.put("title","Identification Number *")
                jsonObject17.put("hint","Identification Number")
                jsonObject17.put("hintText","Select Identification Number")
                jsonObject17.put("showValue","")
                jsonObject17.put("value","")
                jsonObject17.put("inputType","text")
                jsonArray.put(jsonObject17)

                val jsonObject18 = JSONObject()
                jsonObject18.put("type","editText")
                jsonObject18.put("title","ABC ID")
                jsonObject18.put("hint","ABC ID")
                jsonObject18.put("hintText","Enter ABC ID")
                jsonObject18.put("showValue","")
                jsonObject18.put("value","")
                jsonObject18.put("inputType","text")
                jsonArray.put(jsonObject18)

                val jsonObject19 = JSONObject()
                jsonObject19.put("type","editText")
                jsonObject19.put("title","Lead ID *")
                jsonObject19.put("hint","Lead ID")
                jsonObject19.put("hintText","Enter Lead ID")
                jsonObject19.put("showValue",sharedHelper.leadId)
                jsonObject19.put("value","")
                jsonObject19.put("inputType","text")
                jsonArray.put(jsonObject19)*/

            }
        }
        else if(value == "Parent / Guardian Info"){
            loadProgress(2)
            if(sharedHelper.guardianInfo.length() != 0){
                jsonArray = sharedHelper.guardianInfo
            }
            else{
                val jsonObject1 = JSONObject()
                jsonObject1.put("type","editText")
                jsonObject1.put("title","Father’s Name *")
                jsonObject1.put("hint","Enter Father’s Name")
                jsonObject1.put("hintText","Enter Father’s Name")
                jsonObject1.put("showValue","")
                jsonObject1.put("value","")
                jsonObject1.put("inputType","name")
                jsonArray.put(jsonObject1)

                val jsonObject2 = JSONObject()
                jsonObject2.put("type","editText")
                jsonObject2.put("title","Mother’s Name *")
                jsonObject2.put("hint","Enter Mother Name")
                jsonObject2.put("hintText","Enter Mother Name")
                jsonObject2.put("showValue","")
                jsonObject2.put("value","")
                jsonObject2.put("inputType","text")
                jsonArray.put(jsonObject2)

                val jsonObject3 = JSONObject()
                jsonObject3.put("type","editText")
                jsonObject3.put("title","Guardian’s Name")
                jsonObject3.put("hint","Enter Guardian Number")
                jsonObject3.put("hintText","Enter Guardian Number")
                jsonObject3.put("showValue","")
                jsonObject3.put("value","")
                jsonObject3.put("inputType","name")
                jsonArray.put(jsonObject3)

                val jsonObject4 = JSONObject()
                jsonObject4.put("type","editText")
                jsonObject4.put("title","Father’s Occupation *")
                jsonObject4.put("hint","Enter Father’s Occupation")
                jsonObject4.put("hintText","Enter Father’s Occupation")
                jsonObject4.put("showValue","")
                jsonObject4.put("value","")
                jsonObject4.put("inputType","name")
                jsonArray.put(jsonObject4)

                val jsonObject5 = JSONObject()
                jsonObject5.put("type","editText")
                jsonObject5.put("title","Mother’s Occupation*")
                jsonObject5.put("hint","Enter Mother’s Occupation")
                jsonObject5.put("hintText","Enter Mother’s Occupation")
                jsonObject5.put("showValue","")
                jsonObject5.put("value","")
                jsonObject5.put("inputType","text")
                jsonArray.put(jsonObject5)

                val jsonObject6 = JSONObject()
                jsonObject6.put("type","editText")
                jsonObject6.put("title","Parent’s Contact Number*")
                jsonObject6.put("hint","Enter Contact Number")
                jsonObject6.put("hintText","Enter Contact Number")
                jsonObject6.put("showValue","")
                jsonObject6.put("value","")
                jsonObject6.put("inputType","number")
                jsonArray.put(jsonObject6)

                val jsonObject7 = JSONObject()
                jsonObject7.put("type","editText")
                jsonObject7.put("title","Emergency Contact Number*")
                jsonObject7.put("hint","Enter Emergency Contact Number")
                jsonObject7.put("hintText","Enter Emergency Contact Number")
                jsonObject7.put("showValue","")
                jsonObject7.put("value","")
                jsonObject7.put("inputType","number")
                jsonArray.put(jsonObject7)

                val jsonObject8 = JSONObject()
                jsonObject8.put("type","editText")
                jsonObject8.put("title","Parent’s Email Address *")
                jsonObject8.put("hint","Enter Email")
                jsonObject8.put("hintText","Enter Email")
                jsonObject8.put("showValue","")
                jsonObject8.put("value","")
                jsonObject8.put("inputType","text")
                jsonArray.put(jsonObject8)

                val jsonObject9 = JSONObject()
                jsonObject9.put("type","address")
                jsonObject9.put("title","Parent’s Address *")
                jsonObject9.put("hint","Enter Full Address")
                jsonObject9.put("hintText","Enter Full Address")
                jsonObject9.put("showValue","")
                jsonObject9.put("value","")
                jsonObject9.put("inputType","text")
                jsonArray.put(jsonObject9)
            }
        }
        else if(value == "Academic Information"){
            loadProgress(3)
            if(sharedHelper.academicInfo.length() != 0){
                jsonArray = sharedHelper.academicInfo
            }
            else{
                val jsonObject1 = JSONObject()
                jsonObject1.put("type","editText")
                jsonObject1.put("title","Previous School Name *")
                jsonObject1.put("hint","Enter Previous School Name")
                jsonObject1.put("hintText","Enter Previous School Name")
                jsonObject1.put("showValue","")
                jsonObject1.put("value","")
                jsonObject1.put("inputType","name")
                jsonArray.put(jsonObject1)

                val jsonObject2 = JSONObject()
                jsonObject2.put("type","spinner")
                jsonObject2.put("title","Previous Class Attended *")
                jsonObject2.put("hint","Select...")
                jsonObject2.put("hintText","Select...")
                jsonObject2.put("showValue","")
                jsonObject2.put("value","")
                jsonObject2.put("inputType","name")
                jsonArray.put(jsonObject2)

                val jsonObject3 = JSONObject()
                jsonObject3.put("type","spinner")
                jsonObject3.put("title","Class Applying For *")
                jsonObject3.put("hint","Select...")
                jsonObject3.put("hintText","Select...")
                jsonObject3.put("showValue","")
                jsonObject3.put("value","")
                jsonObject3.put("inputType","name")
                jsonArray.put(jsonObject3)

                val jsonObject4 = JSONObject()
                jsonObject4.put("type","spinner")
                jsonObject4.put("title","Board of Education *")
                jsonObject4.put("hint","Select...")
                jsonObject4.put("hintText","Select...")
                jsonObject4.put("showValue","")
                jsonObject4.put("value","")
                jsonObject4.put("inputType","number")
                jsonArray.put(jsonObject4)

            }
        }
        else if(value == "Document Upload"){
            loadProgress(4)
            jsonArray = sharedHelper.documentUpload
        }
        return jsonArray
    }

    fun loadProgress(value : Int){
        when {
            value == 1 -> {
                UiUtils.viewBgColor(binding.view1,null,R.color.colorPrimary)
                UiUtils.viewBgColor(binding.view2,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view3,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view4,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view5,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view6,"#D9D9D9",null)
            }
            value == 2 -> {
                UiUtils.viewBgColor(binding.view1,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view2,null,R.color.colorPrimary)
                UiUtils.viewBgColor(binding.view3,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view4,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view5,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view6,"#D9D9D9",null)
            }
            value == 3 -> {
                UiUtils.viewBgColor(binding.view1,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view2,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view3,null,R.color.colorPrimary)
                UiUtils.viewBgColor(binding.view4,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view5,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view6,"#D9D9D9",null)
            }
            value == 4 -> {
                UiUtils.viewBgColor(binding.view1,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view2,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view3,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view4,null,R.color.colorPrimary)
                UiUtils.viewBgColor(binding.view5,"#D9D9D9",null)
                UiUtils.viewBgColor(binding.view6,"#D9D9D9",null)
            }
            value == 5 -> {
                UiUtils.viewBgColor(binding.view1,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view2,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view3,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view4,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view5,null,R.color.colorPrimary)
                UiUtils.viewBgColor(binding.view6,"#D9D9D9",null)
            }
            value == 6 -> {
                UiUtils.viewBgColor(binding.view1,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view2,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view3,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view4,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view5,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view6,null,R.color.colorPrimary)
            }
            value == 7 -> {
                UiUtils.viewBgColor(binding.view1,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view2,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view3,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view4,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view5,"#54CF8B",null)
                UiUtils.viewBgColor(binding.view6,"#54CF8B",null)
            }
        }
    }

    fun openDocList(){
        if(BaseUtils.isPermissionsEnabled(this, Constants.IntentKeys.STORAGE)){
            var i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            // i = Intent(Intent.ACTION_OPEN_DOCUMENT)
            // i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            i.setType("*/*")
            i.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "image/*",
                "application/pdf"
                /* "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                 "application/msword",
                 "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                 "application/vnd.ms-powerpoint",
                 "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                 "application/vnd.ms-excel",
                 "application/zip",
                 "application/x-excel",
                 "text/plain"*/))
            i.type = "*/*"
            startActivityForResult(i, 12)
        }
        else{
            BaseUtils.permissionsEnableRequest(this,Constants.IntentKeys.STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 12){
            if (resultCode == RESULT_OK) {
                val uri = data?.data
                var filePart: MultipartBody.Part? = null
                if (uri != null) {
                    val documentFile = DocumentFile.fromSingleUri(this, uri)
                    val fileInputStream = contentResolver.openInputStream(uri)
                    val mimeType = contentResolver.getType(uri)

                    val buffer = fileInputStream?.readBytes()
                    fileInputStream?.close()

                    if (buffer != null && mimeType != null) {
                        val fileSize = buffer.size
                        val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                        filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                        val fileType = contentResolver.getType(uri)
                        Log.d("dv1", "" + documentFile?.name)
                        Log.d("dv0", "" + uri.path)
                        Log.d("dv3", "" + fileSize.div(1024))
                        Log.d("dv4", "" + fileType)
                        val pdfName =  documentFile?.name
                        var path =  uri.path
                        /*val fSize = fileSize.div(1024)
                        if (fSize > 300 && adapter!!.list.getJSONObject(adapter!!.selectedPosition).getString("title").contains("Less than 300kb JPG")) {
                            UiUtils.showSnack("File must be less than 300 KB", binding.root, false)
                            return
                        }*/
                        val title = adapter!!.list.getJSONObject(adapter!!.selectedPosition).getString("title")
                        val fSize = buffer.size.div(1024)
                        Log.d("edrftg",""+title)
                        Log.d("edrftg",""+fSize)
                        if (title.contains("Less than 300kb JPG")){
                            if (fSize <= 300) {
                                DialogUtils.showLoader(this)
                                ApiConnection.getInstance().uploadDoc(this, filePart, adapter!!.list.getJSONObject(adapter!!.selectedPosition).getString("hint")).observe(this) {
                                    it?.let {
                                        DialogUtils.dismissLoader()
                                        it.success.let { success ->
                                            if (success && it.result.isNotEmpty()) {
                                                UiUtils.showSnack(it.msg, binding.root, true)
                                                adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("showValue", pdfName)
                                                adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("value", path)
                                                adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("status", "pending")
                                                adapter!!.notifyItemChanged(adapter!!.selectedPosition)
                                                saveData()
                                            } else {
                                                UiUtils.showSnack(it.msg, binding.root, false)
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                UiUtils.showSnack("File must be less than 300 KB", binding.root, false)
                            }
                        }
                        else {
                            DialogUtils.showLoader(this)
                            ApiConnection.getInstance().uploadDoc(this,filePart,adapter!!.list.getJSONObject(adapter!!.selectedPosition).getString("hint")).observe(this) {
                                it?.let {
                                    DialogUtils.dismissLoader()
                                    it.success.let { success ->
                                        if (success && it.result.isNotEmpty()) {
                                            UiUtils.showSnack(it.msg, binding.root,true)
                                            adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("showValue",pdfName)
                                            adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("value",path)
                                            adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("status","pending")
                                            adapter!!.notifyItemChanged(adapter!!.selectedPosition)
                                            saveData()
                                        }
                                        else {
                                            UiUtils.showSnack(it.msg, binding.root,false)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}