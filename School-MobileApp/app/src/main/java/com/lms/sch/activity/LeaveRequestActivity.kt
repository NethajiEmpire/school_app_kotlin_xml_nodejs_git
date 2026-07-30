package com.lms.sch.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.adapter.AvailableLeaveAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.TeacherLeaveRequestAdapter
import com.lms.sch.databinding.ActivityLeaveRequestBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetRoleResponse
import com.lms.sch.response.LeaveDropdownResponse
import com.lms.sch.response.LeaveRequestResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.Calendar

class LeaveRequestActivity : BaseActivity() {
    lateinit var binding: ActivityLeaveRequestBinding
    var status = ""
    var search = ""
    var count = 0
    var leaveAttach = ArrayList<String>()
    var roleList = ArrayList<GetRoleResponse.Result>()
    var roles = ArrayList<GetRoleResponse.Result>()
    var leaveTypeId = ""
    var roleId = ""
    var leaveId = ""
    var role = ""
    var isLeaveTypeLoaded = false
    var result = ArrayList<LeaveRequestResponse.Rows>()
    var type = ArrayList<LeaveDropdownResponse.Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        availableLeave()
        role = "TEACHER"
        status = ""



        search = ""
        leaveRequest(search)
        if (sharedHelper.role == "TEACHER"){
            binding.tabLayout.visibility = View.VISIBLE
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().getRole(this).observe(this){
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.result != null && it.result!!.isNotEmpty()){
                                roleList = it.result!!
                                roles.clear()
                                var count = 0
                                for (items in roleList) {
                                    if (sharedHelper.role == items.name) {
                                        roles.add(0, items)
                                        roleId = items._id!!
                                        count++
                                        binding.totalTab.performClick()
                                    }
                                    if (items.name == "STUDENT") {
                                        roles.add(items)
                                        count++
                                    }
                                    if (count == 2) {
                                        break
                                    }
                                }
                                initAdapter(layoutInflater,binding.root)
                            } else {
                                UiUtils.showSnack(it.msg, binding.root, false)
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
        }
        else {
            binding.tabLayout.visibility = View.GONE
            role = "STUDENT"
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = p0.toString()
                leaveRequest(search)
            }
        })
        leaveType()
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
//        binding.totalTab.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.totalTab, R.drawable.border_line_curve_8dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.pendingTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.rejectedTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.approveTab, R.drawable.border_line_curve_8dp_grey)
//            status = ""
//            search = ""
//            binding.search.setText("")
//            BaseUtils.hideForceKeyboard(binding.root)
//            leaveRequest(search)
//        }
//        binding.pendingTab.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.totalTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.pendingTab, R.drawable.border_line_curve_8dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.rejectedTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.approveTab, R.drawable.border_line_curve_8dp_grey)
//            status = "pending"
//            search = ""
//            binding.search.setText("")
//            BaseUtils.hideForceKeyboard(binding.root)
//            leaveRequest(search)
//
//        }
//        binding.rejectedTab.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.totalTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.pendingTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.rejectedTab, R.drawable.border_line_curve_8dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.approveTab, R.drawable.border_line_curve_8dp_grey)
//            status = "rejected"
//            search = ""
//            binding.search.setText("")
//            BaseUtils.hideForceKeyboard(binding.root)
//            leaveRequest(search)
//        }
//        binding.approveTab.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.totalTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.pendingTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.rejectedTab, R.drawable.border_line_curve_8dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.approveTab, R.drawable.border_line_curve_8dp_primary)
//            status = "approved"
//            search = ""
//            binding.search.setText("")
//            BaseUtils.hideForceKeyboard(binding.root)
//            leaveRequest(search)
//        }
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
            val popupView : View = bind.root
            bind.today.text = "Pending"
            bind.pending.text = "Rejected"
            bind.completed.text = "Approved"

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
            if (status == ""){
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "pending"){
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
            else if (status == "approved"){
                UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
            }
            else {
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }

            bind.all.setOnClickListener {
                status = ""
                search = ""
                binding.search.setText("")
                BaseUtils.hideForceKeyboard(binding.root)
                leaveRequest(search)
                popupWindow.dismiss()
            }
            bind.today.setOnClickListener {
                status = "pending"
                search = ""
                binding.search.setText("")
                BaseUtils.hideForceKeyboard(binding.root)
                leaveRequest(search)
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                status = "rejected"
                search = ""
                binding.search.setText("")
                BaseUtils.hideForceKeyboard(binding.root)
                leaveRequest(search)
                popupWindow.dismiss()
            }

            bind.completed.setOnClickListener {
                status = "approved"
                search = ""
                binding.search.setText("")
                BaseUtils.hideForceKeyboard(binding.root)
                leaveRequest(search)
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
        if (sharedHelper.role != "TEACHER"){
            binding.totalTab.performClick()
        }
        binding.create.setOnClickListener {
            binding.page2.visibility = View.VISIBLE
            binding.page1.visibility = View.GONE
            binding.page3.visibility = View.GONE
            binding.create.visibility = View.GONE
            binding.txt.text = "Create Leave Request"
        }
        binding.attach.setOnClickListener {
                openDocList()
        }
        binding.startDate.setOnClickListener {
            showCalender { selectedDate ->
                binding.startDate.setText(selectedDate)
            }
        }
        binding.endDate.setOnClickListener {
            showCalender { selectedDate ->
                binding.endDate.setText(selectedDate)
            }
        }
        binding.apply.setOnClickListener {
            val title = binding.title.text.toString()
            val desc = binding.desc.text.toString()
            val startDate = binding.startDate.text.toString()
            val endDate = binding.endDate.text.toString()
            if (title.isEmpty()) {
                UiUtils.showSnack("Please enter title", binding.root, false)
                return@setOnClickListener
            }
            else if (leaveTypeId.isEmpty()) {
                UiUtils.showSnack("Please select a leave type", binding.root, false)
                return@setOnClickListener
            }
            else if (desc.isEmpty()) {
                UiUtils.showSnack("Please enter description", binding.root, false)
                return@setOnClickListener
            }
            else if (startDate.isEmpty()) {
                UiUtils.showSnack("Please select start date", binding.root, false)
                return@setOnClickListener
            }
            else if (endDate.isEmpty()) {
                UiUtils.showSnack("Please select end date", binding.root, false)
                return@setOnClickListener
            }
            else if (leaveAttach.isEmpty()) {
                UiUtils.showSnack("Please upload the Attachment", binding.root, false)
                return@setOnClickListener
            }
            else {
                if (leaveAttach != null) {
                    ApiConnection.getInstance().leaveUpd(this,title,desc,leaveTypeId,startDate,endDate,leaveAttach).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg, binding.root, true)
                                    binding.title.setText("")
                                    binding.desc.setText("")
                                    binding.startDate.text = ""
                                    binding.endDate.text = ""
                                    if (isLeaveTypeLoaded) {
                                        binding.spinnerFilter.setSelection(0)
                                    }
                                    binding.attach.text = "Attach File"
                                    leaveAttach.clear()
                                    leaveTypeId = ""
                                    binding.page2.visibility = View.GONE
                                    binding.page1.visibility = View.VISIBLE
                                    binding.create.visibility = View.VISIBLE
                                    leaveRequest(search)
                                } else {
                                    UiUtils.showSnack(it.msg, binding.root, false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Please upload the Attachment", binding.root, false)
                }
            }

        }
    }

    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.clearOnTabSelectedListeners()
        for (i in roles.indices) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
            val tabView: View = inflater.inflate(R.layout.custom_tab, container, false)
            val tabText = tabView.findViewById<TextView>(R.id.tab)
            if (roles[i].name == sharedHelper.role){
                tabText.text = "My Requests"
            }
            else {
                tabText.text = roles[i].name!!.lowercase().replaceFirstChar { it.uppercase() }
            }
            UiUtils.textViewTextColor(tabText, null, R.color.colorPrimary)
            tabText.setTextAppearance(R.style.FontMedium)
            binding.tabLayout.getTabAt(i)?.customView = tabView
        }

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_CENTER
        binding.tabLayout.tabMode = TabLayout.MODE_FIXED

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                role = if (tab.position == 0) "TEACHER" else "STUDENT"
                val linear0 = tab.customView ?: return
                val txttab0 = linear0.findViewById<TextView>(R.id.tab)

                UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                txttab0.setTextAppearance(R.style.FontMedium)
                if (tab.position < roleList.size) {
                    roleId = roles[tab.position]._id!!
                    role = getSelectedRole()
                    leaveRequest(search)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                val linear1 = tab.customView ?: return
                val txttab1 = linear1.findViewById<TextView>(R.id.tab)

                UiUtils.textViewTextColor(txttab1, null, R.color.black)
                txttab1.setTextAppearance(R.style.FontMedium)
            }
            private fun getSelectedRole(): String {
                val selectedTabPosition = binding.tabLayout.selectedTabPosition
                return if (selectedTabPosition == 0) "TEACHER" else "STUDENT"
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    override fun onBackPressed() {
        if (binding.page2.visibility == View.VISIBLE){
            binding.page2.visibility = View.GONE
            binding.page3.visibility = View.GONE
            binding.page1.visibility = View.VISIBLE
            binding.create.visibility = View.VISIBLE
            binding.txt.text = "Leave Request"
        }
        else if (binding.page3.visibility == View.VISIBLE){
            binding.page2.visibility = View.GONE
            binding.page3.visibility = View.GONE
            binding.page1.visibility = View.VISIBLE
            binding.create.visibility = View.VISIBLE
            binding.txt.text = "Leave Request"
        }
        else {
            super.onBackPressed()
        }
    }
    fun leaveRequest(search : String){
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().leaveRequest(this,status,search,role).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.leaveRequestRecycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            result = it.result!!.rows!!
                            val adapter = TeacherLeaveRequestAdapter(this,result,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    binding.page3.visibility = View.VISIBLE
                                    binding.page1.visibility = View.GONE
                                    binding.page2.visibility = View.GONE
                                    binding.txt.text = "Leave Request"
                                    binding.create.visibility = View.GONE
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
    fun availableLeave(){
        ApiConnection.getInstance().availableleaves(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success) {
                        if (it.result != null){
                            val adapter = AvailableLeaveAdapter(this,it.result!!)
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                            binding.leaves.layoutManager = layoutManager
                            binding.leaves.adapter = adapter
                        }
                        else {
                            binding.leaves.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
                        }
                    }
                    else {
                        binding.leaves.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun loadLeaveDetails(pos : Int){
        if (result[pos].createdBy != null){
            binding.name.text = result[pos].createdBy!!.firstName + " " + result[pos].createdBy!!.lastName
            UiUtils.loadImage(binding.profile,result[pos].createdBy!!.img_url!!)
//          Glide.with(this).load(result[pos].createdBy!!.img_url).into(binding.img)
            binding.role.text = "+91 ${result[pos].createdBy!!.mobile}"
        }
        else{
            binding.role.text = "--/--"
        }
        binding.closepage.setOnClickListener {
            onBackPressed()
        }
        binding.cancel.setOnClickListener {
            onBackPressed()
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
        if (result[pos].status == "approved"){
            binding.approvOrRejectLay.visibility = View.GONE
            binding.closepage.visibility = View.VISIBLE

//            binding.approve.performClick()
        }
        else if (result[pos].status == "rejected"){
//            binding.reject.performClick()
            binding.approvOrRejectLay.visibility = View.GONE
            binding.closepage.visibility = View.VISIBLE
//            binding.reason.setText(result[pos].rejectReason)
        }
        else if(result[pos].status == "pending") {
            binding.approvOrRejectLay.visibility = View.VISIBLE
            binding.closepage.visibility = View.GONE
            binding.approve.setOnClickListener {
                UiUtils.textViewTextColor(binding.approve,"#32B138",null)
                UiUtils.textviewImgDrawable(binding.approve, R.drawable.greenbtn,"start")
                UiUtils.textViewTextColor(binding.reject,"#868686",null)
                UiUtils.textviewImgDrawable(binding.reject, R.drawable.radio_empty,"start")
                status = "approved"
                binding.reasonLay.visibility = View.GONE
            }
            binding.reject.setOnClickListener {
                UiUtils.textViewTextColor(binding.reject,"#EA5455",null)
                UiUtils.textviewImgDrawable(binding.reject, R.drawable.redbtn,"start")
                UiUtils.textViewTextColor(binding.approve,"#868686",null)
                UiUtils.textviewImgDrawable(binding.approve, R.drawable.radio_empty,"start")
                status = "rejected"
                binding.reasonLay.visibility = View.GONE
            }
            binding.submit1.setOnClickListener {
                approveStatus()
                leaveRequest("")
            }
//            UiUtils.textviewImgDrawable(binding.reject, R.drawable.radio_empty,"start")
//            UiUtils.textViewTextColor(binding.reject, "#868686", null)
//            UiUtils.textviewImgDrawable(binding.approve, R.drawable.radio_empty,"start")
//            UiUtils.textViewTextColor(binding.approve, "#868686", null)
//            status = ""
//            binding.reason.setText("")
//            binding.reasonLay.visibility = View.GONE
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
            binding.desc1.setContent(result[pos].description)
        }
        if (result[pos].type != null){
            binding.leaveType1.text = result[pos].type!!.name!!
        }
    }
    fun leaveType() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().leaveDropdown(this).observe(this) {
            DialogUtils.dismissLoader()
            it.success.let { success ->
                if (success) {
                    if (it.result != null && it.result!!.isNotEmpty()) {
                        val leaveTypes = ArrayList<String>()
                        leaveTypes.add("Select Leave Type")
                        for (item in it.result!!) {
                            leaveTypes.add(item.label!!)
                        }
                        val adapter = SpinnerAdapter(this, leaveTypes)
                        binding.spinnerFilter.adapter = adapter

                        isLeaveTypeLoaded = true

                        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                                val clickedValue: String = parent.getItemAtPosition(position) as String
                                if (position != 0) {
                                    leaveTypeId = it.result!![position - 1].value.toString()
                                } else {
                                    leaveTypeId = ""
                                }
                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                } else {
                    UiUtils.showSnack(it.msg, binding.root, false)
                }
            }
        }
    }
    fun openDocList() {
        if (BaseUtils.isPermissionsEnabled(this, Constants.IntentKeys.STORAGE)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.putExtra(
                Intent.EXTRA_MIME_TYPES, arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg",
                    "application/pdf",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
                    "application/msword"
                )
            )
            startActivityForResult(intent, 12)
        } else {
            BaseUtils.permissionsEnableRequest(this, Constants.IntentKeys.STORAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK) {
            if (data != null){
                if (data.clipData != null){
                    var docs = data.clipData
                    for (items in 0 until  docs!!.itemCount){
                        count++
                        val uri = docs.getItemAt(items).uri
                        var filePart: MultipartBody.Part? = null
                        if (uri != null) {
                            val documentFile = DocumentFile.fromSingleUri(this, uri)
                            val fileInputStream = this.contentResolver.openInputStream(uri)
                            val mimeType = this.contentResolver.getType(uri)
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
                                if (fileSizeInMB <= 5){
                                    val json = JSONObject()
                                    json.put("name",documentFile?.name)
                                    json.put("size",size)
                                    json.put("type",mimeType)
//                                    urlName.add(json)
                                    upload(filePart)
                                } else {
                                    UiUtils.showSnack("File size exceeds 50 MB", binding.root,false)
                                }
                            }
                        }
                    }
                } else {
                    val uri = data?.data
                    var filePart: MultipartBody.Part? = null
                    count++
                    if (uri != null) {
                        val documentFile = DocumentFile.fromSingleUri(this, uri)
                        val fileInputStream = this.contentResolver.openInputStream(uri)
                        val mimeType = this.contentResolver.getType(uri)
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
                            if (fileSizeInMB <= 5){
                                val json = JSONObject()
                                json.put("name",documentFile?.name)
                                json.put("size",size)
                                json.put("type",mimeType)
                                binding.attach.text = documentFile?.name
                                upload(filePart)
                            } else {
                                UiUtils.showSnack("File size exceeds 5 MB", binding.root,false)
                            }
                        }
                    }
                }
            }
        }
    }
    fun upload(filepart: MultipartBody.Part){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().uploadFile(this, filepart).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        val url = it.result[0].location!!
                        leaveAttach.add(url)

                        /*if (complaintAttach.isNotEmpty()){
                            binding.dialogComplaint.attachRecycler.visibility = View.VISIBLE
                            val adapter = AttachAdapter(this,complaintAttach)
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                            binding.dialogComplaint.attachRecycler.layoutManager = layoutManager
                            binding.dialogComplaint.attachRecycler.adapter = adapter
                        }
                        else {
                            binding.dialogComplaint.attachRecycler.visibility = View.GONE
                        }*/
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }
    fun showCalender(onDateSelected: (String) -> Unit) {
        var datePickerDialog: DatePickerDialog? = null
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day

        // Date picker dialog
        datePickerDialog = DatePickerDialog(this,
            { view, year, monthOfYear, dayOfMonth ->
                var sDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                sDate = BaseUtils.getFormattedDate(sDate, "dd/MM/yyyy", "yyyy-MM-dd")
                onDateSelected(sDate)
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }

    fun approveStatus(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().leaveApprove(this,status,leaveId).observe(this) {
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
}