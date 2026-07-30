package com.lms.sch.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.activity.GuestStudentActivity
import com.lms.sch.activity.StaffInformationActivity
import com.lms.sch.activity.StudentInfoActivity
import com.lms.sch.activity.TeacherInfoActivity
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.adapter.AdminTimeTableAdapter
import com.lms.sch.adapter.TimeTableFilter2Adapter
import com.lms.sch.adapter.TimeTableRemove2Adapter
import com.lms.sch.adapter.TimetableFilterAdapter
import com.lms.sch.adapter.TimetableFilterRemoveAdapter
import com.lms.sch.adapter.UsersAdapter
import com.lms.sch.databinding.FragmentAdminUserManagementBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.BatchDropdownResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetAdminStatsReponse
import com.lms.sch.response.GetGuestInfoResponse
import com.lms.sch.response.GetStaffResponse
import com.lms.sch.response.GetStudentResponse
import com.lms.sch.response.GetTeacherResponse
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import kotlin.toString

class UserManagementFragment  : BaseFragment() {
    lateinit var binding: FragmentAdminUserManagementBinding
    var guestList = ArrayList<GetGuestInfoResponse.Result.Row>()
    var studentList = ArrayList<GetStudentResponse.Result.Row>()
    var teacherList = ArrayList<GetTeacherResponse.Result.Row>()
    var staffList = ArrayList<GetStaffResponse.Result.Row>()
    var adminDashboardStatsRes : GetAdminStatsReponse.Result? = null
    var boardRes =  ArrayList<StudentBoardResponse.Result>()
    var batchRes =  ArrayList<BatchDropdownResponse.Result>()
    var classRes =  ArrayList<DropdownResponse.Result>()
    var program = ""
    var boardId = ""
    var classname = ""
    var role = ""
    var search = ""
    var selectedTab = ""
    var batchName = ""
    var batchId = ""
    var classId = ""
    var selectedPos = -1
    var selectedPos1 = -1
    var filterArr = ArrayList<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminUserManagementBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(1)
        getAdminStatsCount()
//        val userType = arguments?.getString("guest")
//        val userType1 = arguments?.getString("guest1")
//        val userType2 = arguments?.getString("guest2")
//        if (userType == "guest"){
//            binding.guestTap.performClick()
//        }
//        else if (userType1 == "student"){
//            binding.studentTap.performClick()
//        }
//        else if (userType2 == "teacher"){
//            binding.teacherTap.performClick()
//        }
//        else{
//            binding.guestTap.performClick()
//        }
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().profile(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.userprofile != null) {
                            UiUtils.loadImage(binding.profile,it.result!!.userprofile!!.img_url!!)
                        }
                        else {
                            UiUtils.loadImage(binding.profile,R.drawable.ic_user_profile.toString())
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (role == "Guest"){
                    search = binding.search.text.toString()
                    guestList()
                }
                else if (role == "Student"){
                    search = binding.search.text.toString()
                    studentList()
                }
                else if (role == "Teacher"){
                    search = binding.search.text.toString()
                    teacherList()
                }
                else if (role == "Staff"){
                    search = binding.search.text.toString()
                    staffList()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (role == "Guest"){
                    search = binding.search.text.toString()
                    guestList()
                }
                else if (role == "Student"){
                    search = binding.search.text.toString()
                    studentList()
                }
                else if (role == "Teacher"){
                    search = binding.search.text.toString()
                    teacherList()
                }
                else if (role == "Staff"){
                    search = binding.search.text.toString()
                }
            }
            false
        })

        binding.swipeRefresh.setOnRefreshListener {
            if (role == "Guest"){
                guestList.clear()
                search = ""
                binding.guestTap.performClick()
            }
            else if (role == "Student"){
                studentList.clear()
                search = ""
                binding.studentTap.performClick()
            }
            else if (role == "Teacher"){
                teacherList.clear()
                search = ""
                binding.teacherTap.performClick()
            }
            else if (role == "Staff"){
                staffList.clear()
                search = ""
                binding.staffTap.performClick()
            }
            binding.swipeRefresh.isRefreshing = false
        }
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(), null, false)
        }
        binding.guestTap.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.guestTap, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.guest,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.guestCount,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.studentTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.student,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.studentCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.teacherTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.teacher,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.teacherCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.staffTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.staff,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.staffCount,R.drawable.ic_round_line_3)
            role = "Guest"
            if (guestList.isEmpty()){
                guestList()
            }
            else if(role == "Guest"){
                guestList()
            }
            else {
                loadAdapter()
            }
        }
        binding.studentTap.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.studentTap, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.student,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.studentCount,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.guestTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.guest,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.guestCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.teacherTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.teacher,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.teacherCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.staffTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.staff,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.staffCount,R.drawable.ic_round_line_3)
            role = "Student"
            if (studentList.isEmpty()){
                studentList()
            }
            else if(role == "Student"){
                studentList()
            }
            else {
                loadAdapter()
            }
        }
        binding.teacherTap.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.teacherTap, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.teacher,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.teacherCount,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.guestTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.guest,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.guestCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.studentTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.student,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.studentCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.staffTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.staff,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.staffCount,R.drawable.ic_round_line_3)
            role = "Teacher"
            if (teacherList.isEmpty()){
                teacherList()
            }
            else if(role == "Teacher"){
                teacherList()
            }
            else {
                loadAdapter()
            }
        }
        binding.staffTap.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.teacherTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.teacher,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.teacherCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.guestTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.guest,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.guestCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.studentTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.student,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.studentCount,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.staffTap, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.staff,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.staffCount,R.drawable.ic_round_line2)
            role = "Staff"
            if (staffList.isEmpty()){
                staffList()
            }
            else if(role == "Staff"){
                staffList()
            }
            else {
                loadAdapter()
            }
        }
        binding.guestTap.performClick()
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentBoard(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            boardRes = it.result!!
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().batchDropdown(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            batchRes = it.result!!
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentClsDropdown(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            classRes = it.result!!
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
        mActivity.binding.dialogFilter.tabBatch.setOnClickListener {
            selectedTab = "batch"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogFilter.tabBatch,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogFilter.tabBatch,"#F7FBFE",null)
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogFilter.tabClass,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogFilter.tabClass,"#FFFFFF",null)
            mActivity.binding.dialogFilter.title.text = "Select Batch"
            loadBatch()
        }
        mActivity.binding.dialogFilter.tabClass.setOnClickListener {
            selectedTab = "classes"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogFilter.tabClass,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogFilter.tabClass,"#F7FBFE",null)
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogFilter.tabBatch,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogFilter.tabBatch,"#FFFFFF",null)
            mActivity.binding.dialogFilter.title.text = "Select Student Class"
            loadClass()
        }
        binding.filter.setOnClickListener {
            mActivity.binding.dialogFilter.root.visibility = View.VISIBLE
            if (selectedTab == "classes"){
                mActivity.binding.dialogFilter.tabClass.performClick()
            }
            else {
                mActivity.binding.dialogFilter.tabBatch.performClick()
            }
            mActivity.binding.dialogFilter.root.visibility = View.VISIBLE
            UiUtils.animation(mActivity,mActivity.binding.dialogFilter.root,R.anim.slide_in_from_bottom,true)
        }
        mActivity.binding.dialogFilter.cancel.setOnClickListener {
            mActivity.binding.dialogFilter.root.visibility = View.GONE
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
        }
        mActivity.binding.dialogFilter.close.setOnClickListener {
            mActivity.binding.dialogFilter.root.visibility = View.GONE
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
        }
        mActivity.binding.dialogFilter.apply.setOnClickListener {
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
            mActivity.binding.dialogFilter.root.visibility = View.GONE
            program()
        }

        return view
    }

    private fun getAdminStatsCount(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getAdminDashboardStats(mActivity).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){
                            adminDashboardStatsRes = it.result!!

                            if (adminDashboardStatsRes!!.totalGuest != null){
                                binding.guestCount.text = it.result!!.totalGuest.toString()
                            }
                            else{
                                binding.guestCount.text = "0"
                            }
                            if (adminDashboardStatsRes!!.totalStudents != null){
                                binding.studentCount.text = it.result!!.totalStudents.toString()
                            }
                            else{
                                binding.studentCount.text = "0"
                            }
                            if (adminDashboardStatsRes!!.totalTeachers != null){
                                binding.teacherCount.text = it.result!!.totalTeachers.toString()
                            }
                            else{
                                binding.teacherCount.text = "0"
                            }
                            if (adminDashboardStatsRes?.totalEmployees != null) {
                                binding.staffCount.text = adminDashboardStatsRes!!.totalEmployees.toString()
                            } else {
                                binding.staffCount.text = "0"
                            }
                        }
                        else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun guestList(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getGuestInfo(mActivity,search).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            guestList = it.result!!.rows!!
                            loadAdapter()
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    fun studentList(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getStudentList(mActivity,search,"").observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            studentList = it.result!!.rows!!
                            loadAdapter()
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    fun teacherList(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getTeacherInfo(mActivity,search).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            teacherList = it.result!!.rows!!
                            loadAdapter()
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    fun staffList(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getStaffInfo(mActivity,search).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            staffList = it.result!!.rows!!
                            loadAdapter()
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    fun loadAdapter() {
        val adapter = UsersAdapter(mActivity,role,guestList,studentList,teacherList,staffList,object : OnClickListener {
            override fun onClickItem(pos: Int) {
                if (role == "Guest"){
                    val bundle = Bundle()
                    bundle.putString(Constants.IntentKeys.KEY,"Guest Info")
                    bundle.putString(Constants.IntentKeys.KEY1,guestList[pos]._id!!)
                    Log.d("hsgdfjdsgfk",guestList[pos]!!._id!!)
                    BaseUtils.startActivity(mActivity, GuestStudentActivity(),bundle,false)
                }
                else if (role == "Student"){
                    val bundle = Bundle()
                    bundle.putString(Constants.IntentKeys.KEY,"Student Info")
                    bundle.putString(Constants.IntentKeys.KEY1,studentList[pos].student!!._id)
                    BaseUtils.startActivity(mActivity, StudentInfoActivity(),bundle,false)
                }
                else if (role == "Teacher"){
                    val bundle = Bundle()
                    bundle.putString(Constants.IntentKeys.KEY,"Teacher Info")
                    bundle.putString("role",teacherList[pos].role!!.name!!)
                    bundle.putString(Constants.IntentKeys.KEY1, teacherList[pos]._id)
                    BaseUtils.startActivity(mActivity, TeacherInfoActivity(),bundle,false)

                }
                else if (role == "Staff"){
                    val bundle = Bundle()
                    bundle.putString(Constants.IntentKeys.KEY,"Staff Info")
                    bundle.putString("role",staffList[pos].role!!.name!!)
                    bundle.putString(Constants.IntentKeys.KEY1, staffList[pos]._id)
                    BaseUtils.startActivity(mActivity, TeacherInfoActivity(),bundle,false)
                }
                else {

                }
            }
        })
        val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        binding.filterRecycler.layoutManager = layoutManager
        binding.filterRecycler.adapter = adapter
    }
    fun loadFilter(){
        val adapter = TimeTableRemove2Adapter(mActivity,this,filterArr,object : OnClickListener {
            override fun onClickItem(pos: Int) {
                val value = filterArr[pos]
                if (value == batchName){
                    batchId = ""
                    batchName = ""
                }
                else if (value == classname){
                    classId = ""
                    classname = ""
                }
                program()
            }
        })
        val layoutManager = LinearLayoutManager(mActivity,RecyclerView.HORIZONTAL,false)
        binding.filterRecycler.layoutManager = layoutManager
        binding.filterRecycler.adapter = adapter
    }
    fun program(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().program(mActivity,search).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.programRecycler.visibility = View.VISIBLE
                            val adapter = AdminTimeTableAdapter(mActivity,it.result!!.rows!!,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    program = it.result!!.rows!![pos]._id!!
                                }
                            })
                            val layoutManager = GridLayoutManager(mActivity,2,RecyclerView.VERTICAL,false)
                            binding.programRecycler.layoutManager = layoutManager
                            binding.programRecycler.adapter = adapter
                        } else {
                            binding.noData.txt.text = "No Program Available"
                            binding.noData.root.visibility = View.VISIBLE
                            binding.programRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noData.txt.text = "No Program Available"
                        binding.noData.root.visibility = View.VISIBLE
                        binding.programRecycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun loadBatch() {
        if (batchRes.isNotEmpty()) {
            val adapter = TimeTableFilter2Adapter( mActivity,this, selectedTab, batchRes, classRes,  object : OnClickListener {
                    override fun onClickItem(pos: Int) {
                        if (selectedTab == "batch") {
                            batchId = batchRes[pos].value!!
                            val selected = batchRes[pos].label!!
                            val previousName = batchName
                            batchName = selected
                            for (i in filterArr.size - 1 downTo 0) {
                                if (filterArr[i] == previousName) {
                                    filterArr.removeAt(i)
                                    break
                                }
                            }
                            filterArr.add(selected)
                        }
                    }
                }
            )
            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
            mActivity.binding.dialogFilter.recycler.layoutManager = layoutManager
            mActivity.binding.dialogFilter.recycler.adapter = adapter
        }
        else {
            DialogUtils.showLoader(mActivity)
            ApiConnection.getInstance().batchDropdown(mActivity).observe(mActivity) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.result != null && it.result!!.isNotEmpty()){
                                batchRes = it.result!!
                                val adapter = TimeTableFilter2Adapter(mActivity,this,selectedTab,batchRes,classRes,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        if (selectedTab == "batch"){
                                            batchId = batchRes[pos].value!!
                                            val selected = batchRes[pos].label!!
                                            val previousName = batchName
                                            batchName = selected
                                            for (i in filterArr.size - 1 downTo 0) {
                                                if (filterArr[i] == previousName) {
                                                    filterArr.removeAt(i)
                                                    break
                                                }
                                            }
                                            filterArr.add(selected)
                                        }
                                    }
                                })
                                val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                                mActivity.binding.dialogFilter.recycler.layoutManager = layoutManager
                                mActivity.binding.dialogFilter.recycler.adapter = adapter
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                }
            }
        }
    }
    fun loadClass(){
        if (classRes.isNotEmpty()){
            val adapter = TimeTableFilter2Adapter(mActivity,this,selectedTab,batchRes,classRes,object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    if (selectedTab == "classes"){
                        classId = classRes[pos].value!!
                        val selected = classRes[pos].label!!
                        val pName = classname
                        classname = UiUtils.getOrdinalSuffix(selected.toInt())
                        Log.d("ahgsdhagsfd",classname)
                        for (i in filterArr.size - 1 downTo 0) {
                            if (filterArr[i] == pName) {
                                filterArr.removeAt(i)
                                break
                            }
                        }
                        val res = UiUtils.getOrdinalSuffix(selected.toInt())
                        filterArr.add(res)
                    }
                }
            })
            val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
            mActivity.binding.dialogFilter.recycler.layoutManager = layoutManager
            mActivity.binding.dialogFilter.recycler.adapter = adapter
        }
        else {
            DialogUtils.showLoader(mActivity)
            ApiConnection.getInstance().batchDropdown(mActivity).observe(mActivity) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.result != null && it.result!!.isNotEmpty()){
                                batchRes = it.result!!
                                val adapter = TimeTableFilter2Adapter(mActivity,this,selectedTab,batchRes,classRes,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        if (selectedTab == "classes"){
                                            classId = classRes[pos].value!!
                                            val selected = classRes[pos].label!!
                                            val pName = classname
                                            classname = UiUtils.getOrdinalSuffix(selected.toInt())
                                            for (i in filterArr.size - 1 downTo 0) {
                                                if (filterArr[i] == pName) {
                                                    filterArr.removeAt(i)
                                                    break
                                                }
                                            }
                                            val res = UiUtils.getOrdinalSuffix(selected.toInt())
                                            filterArr.add(res)
                                        }
                                    }
                                })
                                val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                                mActivity.binding.dialogFilter.recycler.layoutManager = layoutManager
                                mActivity.binding.dialogFilter.recycler.adapter = adapter
                            }
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