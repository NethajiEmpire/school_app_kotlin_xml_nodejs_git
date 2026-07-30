package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.PaymentSummaryAdapter
import com.lms.sch.databinding.ActivityPaymentSummaryBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject

class PaymentSummaryActivity : BaseActivity() {
    lateinit var binding : ActivityPaymentSummaryBinding
    var title = ""
    var fees_id = ""
    var payOption = "installment"
    var termsArr = JSONArray()
    var pos = -1
    var isClicked = false
    var  result = ProfileDetailsResponse.Result()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPaymentSummaryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        title = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        if (title != null){
            binding.pageHeading.text = title
        }
        binding.next.setOnClickListener {
            if (isClicked){
                feesPay()
            }
            else {
                UiUtils.showSnack("Please agree with Acknowledgement",binding.root,false)
            }
        }
        binding.check.setOnClickListener {
            isClicked = !isClicked
            if (isClicked){
                UiUtils.imageviewDrawable(binding.check,R.drawable.green_tick)
            }
            else {
                UiUtils.imageviewDrawable(binding.check,R.drawable.rectangle_checkbox)
            }
        }
        getFees()

        ApiConnection.getInstance().parentChildProfile(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.userprofile != null){
                            result = it.result!!
                            if (result.userprofile!!.firstName != null && result.userprofile!!.lastName != null) {
                                binding.name.text = "${result.userprofile!!.firstName} ${result.userprofile!!.lastName}"
                            }
                            else{
                                binding.name.text = "--/--"
                            }
                            if (result.userprofile!!.email != null){
                                binding.email.text = result.userprofile!!.email
                            }
                            else{
                                binding.email.text = "--/--"
                            }
                            if (result.userprofile!!.mobile != null){
                                binding.mobile.text = result.userprofile!!.mobile
                            }
                            else{
                                binding.mobile.text = "--/--"
                            }
                            if (result.userprofile!!.grade_level != null && result.userprofile!!.grade_level!!.name!!.isNotEmpty()){
                                binding.grade.text = UiUtils.getOrdinalSuffix(result.userprofile!!.grade_level!!.name!!.toInt())
                            }
                            else{
                                binding.grade.text = "--/--"
                            }
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

    fun getFees(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentFees(this).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            for (i in 0 until it.result!!.terms!!.size){
                                if (it.result!!.terms!![i]._id == TempSingleton.getInstance().feesPos){
                                    pos = i
                                }
                            }
                            termsArr = JSONArray()
                            val term = JSONObject()
                            term.put("_id", it.result!!.terms!![pos]._id)
                            termsArr.put(term)
                            fees_id = it.result!!._id!!
                            if (it.result!!.terms!![pos].status == "paid"){
                                binding.next.isEnabled = false
                                binding.next.text = "Paid"
                                binding.dateTxt.text = "Paid On"
                                binding.acknowledgeLay.visibility = View.GONE
                                if (it.result!!.terms!![pos].paidOn != null){
                                    binding.dueDate.text = BaseUtils.getFormattedDate(it.result!!.terms!![pos].paidOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                                }
                                else{
                                    binding.dueDate.text = "--/--"
                                }
                            }
                            else {
                                binding.next.isEnabled = true
                                binding.next.text = "Proceed to Pay"
                                binding.dateTxt.text = "Due Date"
                                binding.acknowledgeLay.visibility = View.VISIBLE
                                if (it.result!!.terms!![pos].dueDate != null){
                                    binding.dueDate.text = BaseUtils.getFormattedDate(it.result!!.terms!![pos].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
                                }
                                else{
                                    binding.dueDate.text = "--/--"
                                }
                            }
                            binding.pageHeading.text = it.result!!.terms!![pos].name
                            binding.totalAmount.text = "₹${it.result!!.terms!![pos].totalAmount}"
                            val adapter = PaymentSummaryAdapter(this,it.result!!.terms!![pos].types!!)
                            val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                            binding.recycler.layoutManager = layoutManager
                            binding.recycler.adapter = adapter
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    private fun feesPay(){
        if (fees_id.isNotEmpty()){
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().feesPay(this,fees_id,false,payOption,termsArr).observe(this){
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if(success){
                            if (it.result != null){
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

    override fun onBackPressed() {
        super.onBackPressed()
        TempSingleton.getInstance().feesPos = ""
    }
}