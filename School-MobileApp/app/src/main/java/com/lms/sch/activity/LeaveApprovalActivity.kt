package com.lms.sch.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lms.sch.R
import com.lms.sch.adapter.LeaveRequestAdapter
import com.lms.sch.databinding.ActivityLeaveApprovalBinding
import com.lms.sch.databinding.FilterAssignmentBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.LeaveRequestResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class LeaveApprovalActivity : BaseActivity() {
    lateinit var binding: ActivityLeaveApprovalBinding
    var search = ""
    var status = ""
    var leaveStatus = ""
    var leaveId = ""
    var result = ArrayList<LeaveRequestResponse.Rows>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveApprovalBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.reasonLay.visibility = View.GONE
        binding.page1.visibility = View.VISIBLE
        binding.page2.visibility = View.GONE
        if (intent.getIntExtra(Constants.IntentKeys.KEY2,0) == 1){
            binding.page1.visibility = View.GONE
            binding.page2.visibility = View.VISIBLE
            leaveId = intent.getStringExtra(Constants.IntentKeys.KEY)!!
            val pos = intent.getIntExtra(Constants.IntentKeys.KEY1,-1)
            leaveRequest()
            if (pos != -1){
                DialogUtils.showLoader(this)
                ApiConnection.getInstance().leaveRequest(this,status,search,"").observe(this) {
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success->
                            if (success) {
                                if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                                    binding.leaveRequestRecycler.visibility = View.VISIBLE
                                    binding.noData.root.visibility = View.GONE
                                    result = it.result!!.rows!!
                                    if (result[pos].status == "rejected"){}
                                    loadLeaveDetails(pos)
                                }
                                else {
                                    binding.leaveRequestRecycler.visibility = View.GONE
                                    binding.noData.root.visibility = View.VISIBLE
                                }
                            }
                            else {
                                binding.leaveRequestRecycler.visibility = View.GONE
                                binding.noData.root.visibility = View.VISIBLE
                                UiUtils.showSnack(it.msg, binding.root, false)
                            }
                        }
                    }
                }
            }
        }
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search.text.toString()
                leaveRequest()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                leaveRequest()
            }
            false
        })
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)

            bind.today.text = "Rejected"
            bind.completed.text = "Approved"
            val popupView : View = bind.root
            val widthInDp = 120
            val density = resources.displayMetrics.density
            val widthInPx = (widthInDp * density).toInt()

            val popupWindow = PopupWindow(popupView,widthInPx,ViewGroup.LayoutParams.WRAP_CONTENT,true)
            popupWindow.isOutsideTouchable = true
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow.elevation = 8f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(8f)
            }
            if (status == "All"){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "rejected"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "pending"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "approved"){
                UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
            }
            else {
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            bind.all.setOnClickListener {
                status = ""
                leaveRequest()
                popupWindow.dismiss()
            }
            bind.today.setOnClickListener {
                status = "rejected"
                leaveRequest()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                status = "pending"
                leaveRequest()
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                status = "approved"
                leaveRequest()
                popupWindow.dismiss()
            }
            val anchorView = binding.filter
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)

            val endGapDp = 8
            val topGapDp = 8
            val endGapPx = (endGapDp * density).toInt()
            val topGapPx = (topGapDp * density).toInt()
            val xPos = location[0] + anchorView.width - widthInPx - endGapPx
            val yPos = location[1] + anchorView.height + topGapPx

            popupWindow.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                xPos,
                yPos
            )
        }
      /*  binding.pendingTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.pendingTab, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.rejectTab, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.approvedTab, R.drawable.border_line_curve_24dp_grey)
            status = "pending"
            leaveRequest()
        }
        binding.rejectTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.rejectTab, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.pendingTab, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.approvedTab, R.drawable.border_line_curve_24dp_grey)
            status = "rejected"
            leaveRequest()
        }
        binding.approvedTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.approvedTab, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.rejectTab, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.pendingTab, R.drawable.border_line_curve_24dp_grey)
            status = "approved"
            leaveRequest()
        }*/
        binding.reject.setOnClickListener {
            UiUtils.textviewImgDrawable(binding.reject, R.drawable.redbtn,"start")
            UiUtils.textViewTextColor(binding.reject, "#E85A5B", null)
            UiUtils.textviewImgDrawable(binding.approve, R.drawable.radio_empty,"start")
            UiUtils.textViewTextColor(binding.approve, "#868686", null)
            leaveStatus = "rejected"
            binding.reasonLay.visibility = View.VISIBLE
        }
        binding.approve.setOnClickListener {
            UiUtils.textviewImgDrawable(binding.reject, R.drawable.radio_empty,"start")
            UiUtils.textViewTextColor(binding.reject, "#868686", null)
            UiUtils.textviewImgDrawable(binding.approve, R.drawable.greenbtn,"start")
            UiUtils.textViewTextColor(binding.approve, "#32B138", null)
            leaveStatus = "approved"
            binding.reasonLay.visibility = View.GONE
        }
        binding.cancel.setOnClickListener {
            UiUtils.textviewImgDrawable(binding.reject, R.drawable.radio_empty,"start")
            UiUtils.textViewTextColor(binding.reject, "#868686", null)
            UiUtils.textviewImgDrawable(binding.approve, R.drawable.radio_empty,"start")
            UiUtils.textViewTextColor(binding.approve, "#868686", null)
            leaveStatus = ""
            binding.reason.setText("")
            binding.reasonLay.visibility = View.GONE
            onBackPressed()
        }
        binding.pendingTab.performClick()

        binding.submit.setOnClickListener {
            if (leaveStatus.isNotEmpty()){
                if (leaveStatus == "rejected"){
                    if (binding.reason.text.toString().isEmpty()){
                        UiUtils.showSnack("Please enter reason", binding.root, false)
                    }
                    else {
                        approveStatus()
                    }
                }
                else {
                    approveStatus()
                }
            }
            else {
                UiUtils.showSnack("Please select status", binding.root, false)
            }
        }
    }

    fun approveStatus(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().leaveApprove(this,leaveStatus,leaveId,binding.reason.text.toString()).observe(this) {
            DialogUtils.dismissLoader()
            it.let {
                it.success.let { success ->
                    if (success) {
                        UiUtils.showSnack(it.msg,binding.root,true)
                        binding.cancel.performClick()
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.page2.visibility == View.VISIBLE){
            binding.page1.visibility = View.VISIBLE
            binding.page2.visibility = View.GONE
            leaveRequest()
        }
        else {
            super.onBackPressed()
        }
    }

    fun leaveRequest(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().leaveRequest(this,status,search,"").observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.leaveRequestRecycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            result = it.result!!.rows!!
                            val adapter = LeaveRequestAdapter(this,it.result!!.rows!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    binding.page1.visibility = View.GONE
                                    binding.page2.visibility = View.VISIBLE
                                    leaveId = result[pos]._id.toString()
                                    if (pos != -1){
                                        loadLeaveDetails(pos)
                                    }
                                }
                            })
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                            binding.leaveRequestRecycler.layoutManager = layoutManager
                            binding.leaveRequestRecycler.adapter = adapter
                        }
                        else {
                            binding.leaveRequestRecycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
                        }
                    }
                    else {
                        binding.leaveRequestRecycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun loadLeaveDetails(pos : Int) {
        if (result[pos].createdBy != null){
            binding.name.text = result[pos].createdBy!!.firstName + " " + result[pos].createdBy!!.lastName
            Glide.with(this).load(result[pos].createdBy!!.img_url).into(binding.img)
            if (result[pos].createdBy!!.role != null){
                binding.role.text = result[pos].createdBy!!.role!!.name
            }
        }
        if (result[pos].startDate != null){
            binding.from.text = BaseUtils.getFormattedDate(result[pos].startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        }
        if (result[pos].endDate != null){
            binding.to.text = BaseUtils.getFormattedDate(result[pos].endDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        }
        if (result[pos].createdAt != null){
            binding.requested.text = BaseUtils.getFormattedDate(result[pos].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        }
        if (result[pos].numberOfDays != null){
            binding.days.text = result[pos].numberOfDays
        }
        if (result[pos].type != null){
            binding.type.text = result[pos].type!!.name
        }
        if (result[pos].requestId != null){
            binding.requestId.text = result[pos].requestId
        }
        if (status == "approved"){
            binding.approve.performClick()
        }
        else if (status == "rejected"){
            binding.reject.performClick()
            binding.reason.setText(result[pos].rejectReason)
        }
        else {
            UiUtils.textviewImgDrawable(binding.reject, R.drawable.radio_empty,"start")
            UiUtils.textViewTextColor(binding.reject, "#868686", null)
            UiUtils.textviewImgDrawable(binding.approve, R.drawable.radio_empty,"start")
            UiUtils.textViewTextColor(binding.approve, "#868686", null)
            leaveStatus = ""
            binding.reason.setText("")
            binding.reasonLay.visibility = View.GONE
        }
        if (sharedHelper.role == "TEACHER"){
            binding.clsLay.visibility = View.GONE
            binding.boardLay.visibility = View.GONE
            binding.line1.visibility = View.GONE
            binding.line2.visibility = View.GONE
        }
        else {
            binding.clsLay.visibility = View.VISIBLE
            binding.line1.visibility = View.VISIBLE
            binding.boardLay.visibility = View.VISIBLE
            binding.line2.visibility = View.VISIBLE
            if (result[pos].studentDetails != null) {
                if (result[pos].studentDetails!!.standard != null && result[pos].studentDetails!!.section != null) {
                    val res = UiUtils.getOrdinalSuffix(result[pos].studentDetails!!.standard!!.name!!.toInt())
                    binding.standard.text = res + " - " + result[pos].studentDetails!!.section!!.name + "Sec"
                }
                if (result[pos].studentDetails!!.board != null){
                    binding.board.text = result[pos].studentDetails!!.board!!.name
                }
            }
        }
        if (result[pos].title != null){
            binding.reasonTxt.setContent(result[pos].title)
            binding.desc.setContent(result[pos].description)
        }
        if (result[pos].type != null){
            binding.leaveType.text = result[pos].type!!.name!!
        }
    }
}