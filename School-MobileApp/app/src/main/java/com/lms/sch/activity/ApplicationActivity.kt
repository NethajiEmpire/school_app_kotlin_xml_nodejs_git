package com.lms.sch.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.CardsAdapter
import com.lms.sch.adapter.GuestAdapter
import com.lms.sch.adapter.GuestTermFeesAdapter
import com.lms.sch.adapter.GuestTermFeesPreviewAdapter
import com.lms.sch.databinding.ActivityApplicationBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GuestFeesResponse
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.toString

class ApplicationActivity : BaseActivity() {

    lateinit var binding : ActivityApplicationBinding
    var titlepos = 0
    var adapter:GuestAdapter? = null
    var adapter1: GuestTermFeesAdapter? = null
    var titles = ArrayList<String>()
    var profileRes = ProfileDetailsResponse.Result()
    var guestFees = ArrayList<GuestFeesResponse.Result.Terms>()
    var whomPaid = ""
    var page = ""
    var clicked = ""
    var isStudentCreated = false
    var isApplicationSubmit = false
    var payableAmt = 0
    var termsArr = JSONArray()
    var selectedValue = -1
    var count = 0
    var isPayment = false
    var annualPay = false
    var payOption = "full-payment"
    var id = ""
    var isOpened = false
    var selectedPos = ArrayList<Int>()
    var amount = ""
    var signUrl1 = ""
    var signUrl2 = ""
    var attach : ArrayList<String> = ArrayList()
    var urlName:ArrayList<JSONObject> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        page = intent.getStringExtra(Constants.IntentKeys.KEY).toString()
        UiUtils.log("oiu",page)
//        page = intent.getStringExtra(Constants.IntentKeys.KEY1).toString()
        setContentView(binding.root)
        getProfile()
        titles.clear()
        titles.add("Student Information")
        titles.add("Parent / Guardian Info")
        titles.add("Academic Information")
        titles.add("Document Upload")
        titles.add("Fee Payment")
        titles.add("Acknowledgement")
        titles.add("Waiting for Admin Approval")
        binding.back.setOnClickListener {
            onBackPressed()
        }
//        binding.back.visibility = View.GONE

        if (page == "informationFormPen"){
            binding.includePayment1.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
            binding.noData.root.visibility = View.GONE
            titlepos = 0
            binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
            binding.title.text = titles[titlepos]
            loadRecycler(titles[titlepos])
//            loadRecycler("Student Information")
        }
        else if (page == "academicInfoPen"){
            binding.includePayment1.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
            binding.noData.root.visibility = View.GONE
            titlepos = 2
            binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
            binding.title.text = titles[titlepos]
//            loadRecycler(titles[titlepos])
            loadProgress(titlepos + 1)
            getProfile()
        }
        else if (page == "parentInfoPen"){
            binding.includePayment1.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
            binding.noData.root.visibility = View.GONE
            titlepos = 1
            binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
            binding.title.text = titles[titlepos]
            loadRecycler(titles[titlepos])
            loadProgress(titlepos + 1)
        }
        else if (page == "documentInfoPen"){
            binding.includePayment1.root.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
            titlepos = 3
            binding.steps.text = "0${titlepos + 1} of 0"+titles.size
            binding.title.text = titles[titlepos]
            loadRecycler(titles[titlepos])
            loadProgress(titlepos + 1)
        }
        else if (page == "payment"){
            val status = intent.getStringExtra(Constants.IntentKeys.KEY1).toString()
            /*if (status == "pending"){
                binding.includePayment1.root.visibility = View.GONE
                binding.noData.root.visibility = View.GONE
                binding.linDocVerify.visibility = View.VISIBLE
                binding.next.visibility = View.GONE
                binding.recycler.visibility = View.GONE
                titlepos = 5
                binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
                binding.title.text = titles[titlepos]
                loadProgress(titlepos + 1)
            }
            else {

            }*/
            binding.includePayment1.root.visibility = View.VISIBLE
            binding.includeAcknow.root.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.GONE
            binding.recycler.visibility = View.GONE
            titlepos = 4
            binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
            binding.title.text = titles[titlepos]
            loadProgress(titlepos + 1)
            loadOnlinePay()
        }
        else if (page == "acknowledgementPen"){
            binding.includePayment1.root.visibility = View.GONE
            binding.includeAcknow.root.visibility = View.VISIBLE
            binding.noData.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.GONE
            binding.recycler.visibility = View.GONE
            titlepos = 5
            binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
            binding.title.text = titles[titlepos]
            loadProgress(titlepos + 1)
        }
        else if (page == "verificationPen"){
            binding.includePayment1.root.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            binding.linDocVerify.visibility = View.VISIBLE
            binding.next.visibility = View.GONE
            binding.includeAcknow.root.visibility = View.GONE
            binding.recycler.visibility = View.GONE
            titlepos = 6
            binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
            binding.title.text = titles[titlepos]
            loadProgress(titlepos + 1)
        }
        else {
            binding.steps.text = "01 of 0"+titles.size
            loadRecycler(titles[titlepos])
            binding.title.text = titles[titlepos]
        }
        binding.includeAcknow.studentAttach.setOnClickListener {
            clicked = "student"
            openDocList1()
        }
        binding.includeAcknow.parentAttach.setOnClickListener {
            clicked = "parent"
            openDocList1()
        }

        binding.next.setOnClickListener {
            if (isValidFields()){
                UiUtils.log("jhgf", "next---------")
                UiUtils.log("jhgf", titles[titlepos])
                UiUtils.log("jhgf", ""+titlepos)
                saveData()
                if (titles[titlepos] == "Student Information"){
                    UiUtils.log("jhgf", sharedHelper.studentInfo.toString())
                    studentInfo()
                }
                else if (titles[titlepos] == "Parent / Guardian Info"){
                    parentInfo()
                }
                else if (titles[titlepos] == "Academic Information"){
                    academicInfo()
                }
                else if (titles[titlepos] == "Document Upload"){
                    documentInfo()
                }
                else if (titles[titlepos] == "Fee Payment"){
                    if (binding.includePayment1.root.visibility == View.VISIBLE){
                        binding.includePayment1.root.visibility = View.GONE
                        binding.includePayment2.root.visibility = View.VISIBLE
                        loadOnlinePay()
                    }
                    else {
                        feesPay()
                    }
                }
                else if (titles[titlepos] == "Acknowledgement"){
                    DialogUtils.showLoader(this)
                    ApiConnection.getInstance().acknowledge(this,signUrl1,signUrl2).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    updTabPos()
                                }
                                else {
                                    UiUtils.showSnack(it.msg,binding.root,false)
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.spinnerList.close.setOnClickListener {
            binding.spinnerList.search.setText("")
            binding.spinnerList.root.visibility = View.GONE
        }
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

        binding.swipeRefresh.setOnRefreshListener {
            UiUtils.log("iuygf",""+page)

            binding.swipeRefresh.isRefreshing = false
        }
    }

    fun getProfile(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().profile(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.userprofile != null){
                            profileRes = it.result!!
                            if (page == "academicInfoPen"){
                                loadRecycler(titles[titlepos])
                            }
                        }
                    } else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    fun updTabPos(){
        titlepos++
        binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
        binding.title.text = titles[titlepos]
        if (titlepos < titles.size) {
            if (titles[titlepos] == "Fee Payment") {
                binding.includePayment1.root.visibility = View.VISIBLE
                binding.noData.root.visibility = View.GONE
                binding.linDocVerify.visibility = View.GONE
                binding.recycler.visibility = View.GONE
                loadProgress(titlepos + 1)
                loadOnlinePay()
            }
            /*else if (titles[titlepos] == "Document Upload") {
                binding.includePayment1.root.visibility = View.GONE
                binding.noData.root.visibility = View.GONE
                binding.linDocVerify.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE

//                loadDocumentPage()
            }*/
            else if (titles[titlepos] == "Waiting for Admin Approval") {
                binding.includePayment1.root.visibility = View.GONE
                binding.recycler.visibility = View.GONE
                binding.linDocVerify.visibility = View.VISIBLE
                binding.next.visibility = View.GONE
                binding.includeAcknow.root.visibility = View.GONE
                loadProgress(titlepos + 1)
                TempSingleton.getInstance().isFormComplete = true
            }
            else if (titles[titlepos] == "Acknowledgment") {
                binding.includePayment1.root.visibility = View.GONE
                binding.recycler.visibility = View.GONE
                binding.linDocVerify.visibility = View.GONE
                binding.noData.root.visibility = View.GONE
                binding.includeAcknow.root.visibility = View.VISIBLE
                loadProgress(titlepos + 1)
            } else {
                binding.includePayment1.root.visibility = View.GONE
                binding.linDocVerify.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
                binding.noData.root.visibility = View.GONE
                loadRecycler(titles[titlepos])
            }
        }
    }

    private fun loadOnlinePay(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().guestFees(this).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success ) {
                        //  UiUtils.showSnack(it.msg, binding.root)
                        Log.d("aergER",TempSingleton.getInstance().isPaymentSuccess.toString())
                        Log.d("aergER",""+it.result!!.payment_status!!)
                        if(TempSingleton.getInstance().isPaymentSuccess && it.result!!.payment_status == "paid"){
                            Log.d("aergER",""+"if----------------------")
                            TempSingleton.getInstance().isPaymentSuccess = false
                            binding.includeAcknow.root.visibility = View.VISIBLE
                            binding.includePayment1.root.visibility = View.GONE
                            binding.includePayment2.root.visibility = View.GONE
                            titlepos = 5
                            binding.next.text = "Next"
                            binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
                            binding.title.text = titles[titlepos]
                            loadProgress(titlepos + 1)
                        }
                        else{
                            id = it.result!!.id!!
                            if (binding.includePayment1.root.visibility == View.VISIBLE){
                                guestFees = it.result!!.terms!!
                                adapter1 = GuestTermFeesAdapter(this,it.result!!.terms!!)
                                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                binding.includePayment1.feesListRecycler.layoutManager = layoutManager
                                binding.includePayment1.feesListRecycler.adapter = adapter1

                                binding.includePayment1.admissionFee.text = "Admission Fee  ₹"+it.result!!.admissionFee

                                if (it.result != null && it.result!!.annualPayment != null){
                                    binding.includePayment1.feesTitle.text = it.result!!.annualPayment!!.feesName
                                    binding.includePayment1.tAmt.text = "₹"+it.result!!.annualPayment!!.totalAmount
                                    binding.includePayment1.pAmt.text = "₹"+it.result!!.annualPayment!!.payable_amount
                                    binding.includePayment1.discount.text = "%"+it.result!!.annualPayment!!.discount
                                }
                                else {
                                    binding.includePayment1.feesTitle.text = "-"
                                    binding.includePayment1.tAmt.text = "-"
                                    binding.includePayment1.pAmt.text = "-"
                                    binding.includePayment1.discount.text = "-"
                                }
                                binding.includePayment1.lin.setOnClickListener {
                                    isOpened = !isOpened
                                    if (isOpened) {
                                        payOption = "full-payment"
                                        selectedPos = arrayListOf(0, 1, 2)
                                        selectedValue = 3
                                        adapter1!!.notifyDataSetChanged()

                                        termsArr = JSONArray()
                                        for (item in guestFees) {
                                            if ((0 until termsArr.length()).any { termsArr.getJSONObject(it).getString("_id") == item._id }) {
                                                continue
                                            }
                                            val tempObj = JSONObject().put("_id", item._id)
                                            termsArr.put(tempObj)
                                        }

                                        Log.e("mnde", "Opened: ${termsArr.toString()}")

                                        annualPay = true
                                        UiUtils.linearLayoutBgDrawable(binding.includePayment1.linMain, R.drawable.border_line_curve_8dp_light_blue)
                                        UiUtils.linearLayoutBgDrawable(binding.includePayment1.lin, R.drawable.border_curve_top_10dp)
                                        UiUtils.imageviewDrawable(binding.includePayment1.down, R.drawable.guest_up)
                                        UiUtils.imageviewDrawable(binding.includePayment1.box, R.drawable.checkbox_tick)
                                        UiUtils.linearLayoutBgTint(binding.includePayment1.lin, "#005DA7", null)
                                        UiUtils.textViewTextColor(binding.includePayment1.feesTitle, "#FFFFFF", null)
                                        binding.includePayment1.linChild.visibility = View.VISIBLE
                                    }
                                    else {
                                        payOption = "installment"
                                        selectedPos = ArrayList()
                                        selectedValue = 0
                                        adapter1!!.notifyDataSetChanged()
                                        termsArr = JSONArray()
                                        /*for (item in guestFees) {
                                            if (item != guestFees[2]) {
                                                val tempObj = JSONObject().put("_id", item._id)
                                                termsArr.put(tempObj)
                                            }
                                        }*/

                                        Log.e("mnde", "Closed: ${termsArr.toString()}")

                                        annualPay = false
                                        UiUtils.linearLayoutBgDrawable(binding.includePayment1.linMain, R.drawable.border_curve_6dp)
                                        UiUtils.imageviewDrawable(binding.includePayment1.down, R.drawable.guest_down)
                                        UiUtils.imageviewDrawable(binding.includePayment1.box, R.drawable.checkbox_untick)
                                        UiUtils.textViewTextColor(binding.includePayment1.feesTitle, "#333333", null)
                                        UiUtils.linearLayoutBgDrawable(binding.includePayment1.lin, R.drawable.border_line_curve_10dp_grey_bg)
                                        binding.includePayment1.linChild.visibility = View.GONE
                                    }
                                }

                            }
                            else{
                                val adapter = GuestTermFeesPreviewAdapter(this,selectedValue,it.result!!.terms!!)
                                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                binding.includePayment2.recycler.layoutManager = layoutManager
                                binding.includePayment2.recycler.adapter = adapter

                                if (it.result!!.userDetails!! != null){
                                    binding.includePayment2.name.text = it.result!!.userDetails!!.firstName+" "+it.result!!.userDetails!!.lastName
                                    binding.includePayment2.email.text = it.result!!.userDetails!!.email
                                    binding.includePayment2.mobileNo.text = it.result!!.userDetails!!.mobile
                                }
                                else {
                                    binding.includePayment2.name.text = "--/--"
                                    binding.includePayment2.email.text = "--/--"
                                    binding.includePayment2.mobileNo.text = "--/--"
                                }
                                if (it.result!!.userDetails!! != null && it.result!!.userDetails!!.classApplying!!.isNotEmpty() ){
                                    binding.includePayment2.grade.text = UiUtils.getOrdinalSuffix(it.result!!.userDetails!!.classApplying!!.toInt())
                                }
                                else {
                                    binding.includePayment2.grade.text = "--/--"
                                }

                                if (annualPay){
                                    binding.includePayment2.line.visibility = View.VISIBLE
                                    binding.includePayment2.line1.visibility = View.VISIBLE
                                    binding.includePayment2.addAmtLay.visibility = View.VISIBLE
                                    binding.includePayment2.tAmtLay.visibility = View.VISIBLE
                                    binding.includePayment2.disLay.visibility = View.VISIBLE
                                    UiUtils.linearLayoutBgTint(binding.includePayment2.linPayAmt,"#005DA7",null)
                                    UiUtils.textViewTextColor(binding.includePayment2.payAmt,"#FFFFFF",null)
                                    UiUtils.textViewTextColor(binding.includePayment2.pAmtTxt,"#FFFFFF",null)
                                    if (it.result != null && it.result!!.annualPayment != null){
                                        binding.includePayment2.discount.visibility = View.GONE

                                        Log.e("jhgf",".. "+it.result!!.annualPayment!!.payable_amount)
                                        binding.includePayment2.totalAmt.text = "₹"+it.result!!.annualPayment!!.totalAmount
//                                        binding.includePayment2.discount.text = "₹"+it.result!!.annualPayment!!.discount
                                        payableAmt = it.result!!.annualPayment!!.payable_amount!!.toInt()
                                        val amount = payableAmt + it.result!!.admissionFee!!.toInt()
                                        binding.includePayment2.admissionAmt.text = "₹"+it.result!!.admissionFee
                                        binding.includePayment2.payAmt.text = "₹"+amount
                                        binding.next.text = "Pay ₹"+amount
                                    }
                                    else {
                                        binding.includePayment2.totalAmt.text = "--/--"
                                        binding.includePayment2.payAmt.text = "--/--"
                                        binding.includePayment2.discount.text = "--/--"
                                    }
                                }
                                else{
                                    for (termPos in 0 until selectedValue) {
                                        val term = it.result!!.terms!!.getOrNull(termPos)
                                        if (term != null) {
                                            for (type in term.types ?: emptyList()) {
                                                payableAmt += type.amount!!.toInt() ?: 0
                                                Log.e("jhgfd","pay .."+payableAmt)
                                            }
                                        }
                                    }
                                    Log.e("jhgf",".. "+payableAmt)
                                    binding.includePayment2.line1.visibility = View.VISIBLE
                                    binding.includePayment2.addAmtLay.visibility = View.VISIBLE
                                    binding.includePayment2.line.visibility = View.GONE
                                    binding.includePayment2.tAmtLay.visibility = View.GONE
                                    binding.includePayment2.disLay.visibility = View.GONE
                                    UiUtils.linearLayoutBgTint(binding.includePayment2.linPayAmt,"#E4F3FF",null)
                                    UiUtils.textViewTextColor(binding.includePayment2.payAmt,"#00A606",null)
                                    UiUtils.textViewTextColor(binding.includePayment2.pAmtTxt,"#333333",null)
                                    val amount = payableAmt + it.result!!.admissionFee!!.toInt()
                                    binding.includePayment2.admissionAmt.text = "₹"+it.result!!.admissionFee
                                    binding.includePayment2.payAmt.text = "₹"+amount
                                    binding.next.text = "Pay ₹"+amount
                                }
                            }
                        }
                    }
                    else {
//                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        UiUtils.log("WDXEV","ouside on res $isPayment")
        if(isPayment){
            UiUtils.log("WDXEV","inside on res $isPayment")
            isPayment = false
            loadOnlinePay()
        }
    }

    fun isValidFields(): Boolean {
        if (titlepos == 0 && titles[titlepos] == "Student Information") {
            for (i in 0 until adapter!!.list.length()) {
                val title = adapter!!.list.getJSONObject(i).optString("title")
                val showValue = adapter!!.list.getJSONObject(i).optString("showValue")
                val type = adapter!!.list.getJSONObject(i).optString("type")
                val isRequired = title.contains("*")
                Log.d("afdv", "Index: $i, Title: $title, ShowValue: $showValue, Type: $type, IsRequired: $isRequired")
                if (i == adapter!!.list.length() - 1 && showValue.isNotEmpty()) {
                    Log.d("afdv", "Last Index ShowValue: $showValue")
                }

                if (isRequired && showValue.isBlank()) {
                    val cleanTitle = title.replace("*", "").trim()
                    if (cleanTitle == "Student Phone Number") {
                        if (showValue.isNotEmpty() && !BaseUtils.isValidPhone(showValue)){
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
                    else if(title == "Date Of Birth *"){
                        try {
                            val dobFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) // format matches your hint
                            val dobDate = dobFormat.parse(showValue)
                            val today = Date()

                            if (dobDate != null && dobDate.after(today)) {
                                UiUtils.showSnack("Date of Birth cannot be a future date.", binding.root, false)
                                return false
                            }
                        } catch (e: Exception) {
                            UiUtils.showSnack("Invalid Date of Birth format.", binding.root, false)
                            return false
                        }

                    }
                    else {
                        if (type == "spinner" && (showValue.isBlank() || showValue == "0")) {
                            if (title == "Gender *") {
                                UiUtils.showSnack("Please select $cleanTitle", binding.root, false)
                                return false
                            }
                        }
                        else {
                            UiUtils.showSnack("Please enter $cleanTitle", binding.root, false)
                            return false
                        }
                    }
                }
            }
            return true
        }
        else if (titlepos == 1 && titles[titlepos] == "Parent / Guardian Info") {
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
        else if (titlepos == 2 && titles[titlepos] == "Academic Information") {
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
        else if (titlepos == 3 && titles[titlepos] == "Document Upload"){
            if (adapter!!.list != null){
                UiUtils.log("iuyhgf",""+adapter!!.list.length())
                for (i in 0 until adapter!!.list.length()) {
                    val title = adapter!!.list.getJSONObject(i).optString("title")
                    val value = adapter!!.list.getJSONObject(i).optString("value")
                    val isRequired = title.contains("*")

                    if (isRequired && value.isEmpty()) {
                        val cleanTitle = title.replace("*", "").trim()
                        UiUtils.showSnack("Please choose $cleanTitle", binding.root, false)
                        return false
                    }
                }
            }
            saveData()
            return true
        }
        else if (titlepos == 5 && titles[titlepos] == "Acknowledgement"){
            if (signUrl1.isEmpty()){
                UiUtils.showSnack("Please upload your the signature", binding.root, false)
                return false
            }
            if (signUrl2.isEmpty()){
                UiUtils.showSnack("Please upload parent the signature", binding.root, false)
                return false
            }
            if (!binding.includeAcknow.check.isChecked){
                UiUtils.showSnack("Please accept the declaration.", binding.root, false)
                return false
            }
            return true
        }
        return true
    }

    override fun onBackPressed() {
        if (binding.spinnerList.root.visibility == View.VISIBLE) {
            binding.spinnerList.root.visibility = View.GONE
        }

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
            if (binding.includePayment2.root.visibility == View.VISIBLE){
                binding.includePayment1.root.visibility = View.VISIBLE
                binding.includePayment2.root.visibility = View.GONE
                binding.next.text = "Next"
                payableAmt = 0
                loadOnlinePay()
            }
            else {
                titlepos--
                loadRecycler(titles[titlepos])
                binding.steps.text = "0${titlepos+1} of 0"+titles.size
                binding.title.text = titles[titlepos]
                binding.includePayment1.root.visibility = View.GONE
                binding.linDocVerify.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
            }
        }
        else if (titles[titlepos] == "Student Information") {
            finish()
        }
        else {
            super.onBackPressed()
        }
    }

    fun loadRecycler(value: String){
        adapter = GuestAdapter(this, getJsonArray(value))
        binding.recycler.adapter = adapter
        binding.recycler.scrollToPosition(titlepos)
    }

    private fun studentInfo(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentInfo(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        updTabPos()
//                        getProfile()
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    private fun parentInfo(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().parentInfo(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        updTabPos()
                        getProfile()
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    private fun academicInfo(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().academicInfo(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        updTabPos()
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    private fun documentInfo(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().documentInfo(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        titlepos++
                        binding.steps.text = "0${titlepos + 1} of 0${titles.size}"
                        binding.title.text = titles[titlepos]
                        binding.includePayment1.root.visibility = View.VISIBLE
                        binding.noData.root.visibility = View.GONE
                        binding.linDocVerify.visibility = View.GONE
                        binding.recycler.visibility = View.GONE
                        loadProgress(titlepos + 1)
                        loadOnlinePay()
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    private fun feesPay(){
        if (id.isNotEmpty()){
            UiUtils.log("WDXEV","before apicall $isPayment")
            isPayment = true
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().feesPay(this,id,true,payOption,termsArr).observe(this){
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if(success){
                            if (it.result != null){
                                UiUtils.log("WDXEV","apicall $isPayment")
                                TempSingleton.getInstance().webUrl = it.result!!.paymentLink!!
                                BaseUtils.startActivity(this, WebviewActivity(),null,false)
                            }
                            else {
                                UiUtils.showSnack(it.msg,binding.root,false)
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                }
            }
        }
        else {
            UiUtils.showSnack("Fees Id is not available",binding.root,false)
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

    fun loadSpinnerRecycler(data: ArrayList<String>, position: Int) {
        binding.spinnerList.root.visibility = View.VISIBLE
        if (data.isNotEmpty()) {
            val adapter = CardsAdapter(this, data, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    val jsonObject = adapter!!.list.getJSONObject(position)
                    val selectedValue = data[pos]
                    if (page == "academicInfoPen" || titles[titlepos] == "Academic Information"){
                        if (jsonObject.getString("hint") == "Class Applying For"){
                            jsonObject.put("value", adapter!!.studentCls[pos].value)
                            jsonObject.put("showValue", adapter!!.studentClsStr[pos])
                        }
                        else if (jsonObject.getString("hint") == "Board of Education") {
                            jsonObject.put("value", adapter!!.board[pos]._id)
                            jsonObject.put("showValue", adapter!!.board[pos].name)
                        }
                    }
                    else {
                        jsonObject.put("value", selectedValue)
                        jsonObject.put("showValue", selectedValue)
                    }

//                    jsonObject.put("value", data[pos])
//                    jsonObject.put("showValue", data[pos])
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
                jsonObject1.put("showValue",sharedHelper.firstname)
                jsonObject1.put("value","")
                jsonObject1.put("inputType","name")
                jsonArray.put(jsonObject1)

                val jsonObject2 = JSONObject()
                jsonObject2.put("type","editText")
                jsonObject2.put("title","Last Name *")
                jsonObject2.put("hint","Enter Last Name")
                jsonObject2.put("hintText","Enter Last Name")
                jsonObject2.put("showValue",sharedHelper.lastname)
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
                jsonObject10.put("showValue",sharedHelper.email)
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
                jsonObject12.put("type","editText")
                jsonObject12.put("title","Country *")
                jsonObject12.put("hint","Country")
                jsonObject12.put("hintText","Enter Country")
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
                jsonObject14.put("type","editText")
                jsonObject14.put("title","State *")
                jsonObject14.put("hint","State")
                jsonObject14.put("hintText","Enter State")
                jsonObject14.put("showValue","")
                jsonObject14.put("value","")
                jsonObject14.put("inputType","text")
                jsonArray.put(jsonObject14)

                val jsonObject15 = JSONObject()
                jsonObject15.put("type","editText")
                jsonObject15.put("title","City *")
                jsonObject15.put("hint","City")
                jsonObject15.put("hintText","Enter City")
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
                jsonObject5.put("inputType","name")
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
                jsonObject8.put("inputType","email")
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
                Log.e("vdv",""+".."+profileRes.userprofile?.pre_school)
                Log.e("vdv",""+".."+profileRes.userprofile?.grade_level?.name)
                Log.e("vdv",""+".."+profileRes.userprofile?.grade_level?.name)

                /*val jsonObject2 = JSONObject()
                jsonObject2.put("type","spinner")
                jsonObject2.put("title","Previous Class Attended *")
                jsonObject2.put("hint","Select...")
                jsonObject2.put("hintText","Select...")
                jsonObject2.put("showValue","")
                jsonObject2.put("value","")
                jsonObject2.put("inputType","name")
                jsonArray.put(jsonObject2)*/


                val jsonObject4 = JSONObject()
                jsonObject4.put("type","spinner")
                jsonObject4.put("title","Board Applying For *")
                jsonObject4.put("hint","Board of Education")
                jsonObject4.put("hintText","Select...")
                jsonObject4.put("showValue",profileRes.userprofile?.board?.name)
                jsonObject4.put("value",profileRes.userprofile?.board?._id)
                jsonObject4.put("inputType","name")
                jsonArray.put(jsonObject4)

                val jsonObject3 = JSONObject()
                jsonObject3.put("type","spinner")
                jsonObject3.put("title","Class Applying For *")
                jsonObject3.put("hint","Class Applying For")
                jsonObject3.put("hintText","Select...")
                jsonObject3.put("showValue", UiUtils.getOrdinalSuffix(profileRes.userprofile?.grade_level?.name!!.toInt()))
                jsonObject3.put("value",profileRes.userprofile?.grade_level?._id)
                jsonObject3.put("inputType","name")
                jsonArray.put(jsonObject3)


                val jsonObject1 = JSONObject()
                jsonObject1.put("type","editText")
                jsonObject1.put("title","Name of Previous Institution")
                jsonObject1.put("hint","Enter Previous School Name")
                jsonObject1.put("hintText","Enter Previous School Name")
                jsonObject1.put("showValue",profileRes.userprofile?.pre_school)
                jsonObject1.put("value","")
                jsonObject1.put("inputType","name")
                jsonArray.put(jsonObject1)
            }
        }
        else if(value == "Document Upload"){
            loadProgress(4)
            if(sharedHelper.documentUpload.length() != 0){
                jsonArray = sharedHelper.documentUpload
            }
            else{
                val jsonObject1 = JSONObject()
                jsonObject1.put("type","textView")
                jsonObject1.put("title","Birth Certificate *")
                jsonObject1.put("hint","birthCertificate")
                jsonObject1.put("hintText","Choose File")
                jsonObject1.put("showValue","")
                jsonObject1.put("value","")
                jsonObject1.put("inputType","name")
                jsonArray.put(jsonObject1)

                val jsonObject2 = JSONObject()
                jsonObject2.put("type","textView")
                jsonObject2.put("title","Aadhaar Card *")
                jsonObject2.put("hint","aadharCard")
                jsonObject2.put("hintText","Choose File")
                jsonObject2.put("showValue","")
                jsonObject2.put("value","")
                jsonObject2.put("inputType","name")
                jsonArray.put(jsonObject2)

                val jsonObject3 = JSONObject()
                jsonObject3.put("type","textView")
                jsonObject3.put("title","Student Photo *")
                jsonObject3.put("hint","studentPhoto")
                jsonObject3.put("hintText","Choose File")
                jsonObject3.put("showValue","")
                jsonObject3.put("value","")
                jsonObject3.put("inputType","name")
                jsonArray.put(jsonObject3)

                val jsonObject4 = JSONObject()
                jsonObject4.put("type","textView")
                jsonObject4.put("title","Previous School Marksheet *")
                jsonObject4.put("hint","previousSchoolMarksheet")
                jsonObject4.put("hintText","Choose File")
                jsonObject4.put("showValue","")
                jsonObject4.put("value","")
                jsonObject4.put("inputType","name")
                jsonArray.put(jsonObject4)

                val jsonObject04 = JSONObject()
                jsonObject04.put("type","textView")
                jsonObject04.put("title","Transfer Certificate *")
                jsonObject04.put("hint","transferCertificate")
                jsonObject04.put("hintText","Choose File")
                jsonObject04.put("showValue","")
                jsonObject04.put("value","")
                jsonObject04.put("inputType","name")
                jsonArray.put(jsonObject04)

                val jsonObject5 = JSONObject()
                jsonObject5.put("type","textView")
                jsonObject5.put("title","Parent Id Proof *")
                jsonObject5.put("hint","parentIdProof")
                jsonObject5.put("hintText","Choose File")
                jsonObject5.put("showValue","")
                jsonObject5.put("value","")
                jsonObject5.put("inputType","text")
                jsonArray.put(jsonObject5)

                val jsonObject6 = JSONObject()
                jsonObject6.put("type","textView")
                jsonObject6.put("title","Address Proof *")
                jsonObject6.put("hint","addressProof")
                jsonObject6.put("hintText","Choose File")
                jsonObject6.put("showValue","")
                jsonObject6.put("value","")
                jsonObject6.put("inputType","text")
                jsonArray.put(jsonObject6)
            }
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
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
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

    fun openDocList1(){
        if(BaseUtils.isPermissionsEnabled(this, Constants.IntentKeys.STORAGE)){
            var i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            // i = Intent(Intent.ACTION_OPEN_DOCUMENT)
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
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
            startActivityForResult(i, 14)
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
                        DialogUtils.showLoader(this)
                        ApiConnection.getInstance().uploadDoc(this, filePart, adapter!!.list.getJSONObject(adapter!!.selectedPosition).getString("hint")).observe(this) {
                            it?.let {
                                DialogUtils.dismissLoader()
                                it.success.let { success ->
                                    if (success && it.result.isNotEmpty()) {
                                        UiUtils.showSnack(it.msg, binding.root, true)
                                        val url = it.result[0].location
                                        adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("showValue", pdfName)
                                        adapter!!.list.getJSONObject(adapter!!.selectedPosition).put("value", url)
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
                }
            }
        }
        else if(requestCode == 14){
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
                        val fileSizeInMB = fileSize / (1024.0 * 1024.0)
                        val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                        filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                        val size = BaseUtils.convertBytes(fileSize.toLong())
                        Log.d("dv1", "" + documentFile?.name)
                        Log.d("dv0", "" + uri.path)
                        Log.d("dv3", "" + size)
                        Log.d("dv4", "" + mimeType)
                        if (fileSizeInMB <= 2){
                            val json = JSONObject()
                            json.put("name",documentFile?.name)
                            json.put("size",size)
                            json.put("type",mimeType)
                            urlName.add(json)
                            if (clicked == "student"){
                                binding.includeAcknow.studentAttach.text = documentFile?.name
                            }
                            else {
                                binding.includeAcknow.parentAttach.text = documentFile?.name
                            }
                            count = 1
                            upload(filePart)
                        } else {
                            UiUtils.showSnack("File size exceeds 2 MB", binding.root,false)
                        }
                    }
                }
            }
        }
    }

    fun upload(filepart:MultipartBody.Part){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().uploadFile(this, filepart).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        val url = it.result[0].location!!
                        if (clicked == "student"){
                            signUrl1 = url
                        }
                        else {
                            signUrl2 = url
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