package com.lms.sch.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.documentfile.provider.DocumentFile
import androidx.multidex.BuildConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.activity.LeaderBoardActivity
import com.lms.sch.activity.ProfileActivity
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.ClassTestAdapter
import com.lms.sch.adapter.EventsPagerAdapter
import com.lms.sch.adapter.GetSubjectNameColorAdapter
import com.lms.sch.adapter.HomeworkFilterLabelAdapter
import com.lms.sch.adapter.HomeworkFilterValueAdapter
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.NoticeBoardAdapter
import com.lms.sch.adapter.OverAllProgressAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.StudentHomeworkAdapter
import com.lms.sch.adapter.StudentAssignmentAdapter
import com.lms.sch.adapter.StudentAssignmentAdapter1
import com.lms.sch.adapter.StudentClassTestAdapter
import com.lms.sch.adapter.StudentExaminationAdapter
import com.lms.sch.adapter.StudentFeesAdapter
import com.lms.sch.adapter.StudentHomeworkAdapter1
import com.lms.sch.adapter.StudentProjectAdapter
import com.lms.sch.adapter.StudentProjectAdapter1
import com.lms.sch.adapter.TimeTableAdapter
import com.lms.sch.adapter.WeekDayAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.charts.SubjectRoundedBarChartRenderer
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.CustomMarkerView2
import com.lms.sch.customviews.CustomMarkerView3
import com.lms.sch.customviews.ExamTopRendarCurveColor
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.customviews.TopRendarCurveBarChartColors
import com.lms.sch.customviews.TopRoundedBarChartRenderer
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.databinding.FilterAssignmentBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.databinding.FragmentHomeBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.response.ClassTestResponse
import com.lms.sch.response.GetClassTimeTableResponse
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.GetStudentExamProgressResponse
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetExamResponse
import com.lms.sch.response.GetStudentAssignmentRes
import com.lms.sch.response.StudentClassTestResponse
import com.lms.sch.response.StudentProjectResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.text.toInt

class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    var calendar = Calendar.getInstance()
    private lateinit var barChart: BarChart
    lateinit var attenDanceProgress : PieChart
    var result1 = ArrayList<GetHomeworkResponse.Result>()
    var resulte = GetStudentExamProgressResponse.Result()
    var result = ArrayList<GetStudentAssignmentRes.Result>()
    var result2 = ArrayList<StudentProjectResponse.Result>()
    var result3 = ArrayList<StudentClassTestResponse.Result>()
    var result4 = ArrayList<GetClassTimeTableResponse.Period>()
    var result5 = ArrayList<GetExamResponse.Row>()
    var resultExam = ArrayList<GetStudentExamProgressResponse.Result>()
    var resultClsTest = ArrayList<GetStudentClassTestProgress.Result>()
    var clsTestFilter = ArrayList<String>()
    var clsTestValue = ""
    var search = ""
    var examTabClicked = ""
    var examFilter = ArrayList<String>()
    var examProgressResult = ArrayList<DropdownResponse.Result>()
    var resultAtt = GetStudentAttenDanceResponse.Result()
    var noticeList = ArrayList<NoticeBoardResponse.Result>()
    val type = ""
    var assignmentStatus = ""
    var tabPos = ""
    var projectStatus = ""
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var selectedYear : Int = 0
    var selectedMonth : Int = 0
    var hwStatus = ""
    var ststyp = ""
    var ests = ""
    var count = 0
    var clickedDialog = ""
    var eventDate = ""
    var timeTableDay = ""
    var isDiary = true
    var homeworkAttach = ArrayList<String>()
    var assignmentAttach = ArrayList<String>()
    var timer: CountDownTimer ?= null
    var projectAttach = ArrayList<String>()
    var examStatus = ""
    var isFilter = false
    var examId = ""
    var classTestSts = ""
    private var currentMonthDays = ArrayList<Date>()
    var hwStatusArr = ArrayList<String>()
    var markStatus = ArrayList<String>()
    var subArr = ArrayList<String>()
    lateinit var barChart1 : BarChart
    lateinit var barCharts : BarChart
    var classTestAttach = ArrayList<String>()
    var attendanceFilters = ArrayList<String>()
    var datefilter = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(0)
        binding.pageMyDiary.visibility = View.VISIBLE
        binding.pageHomeWork.visibility = View.GONE
        binding.commonCalender.visibility = View.GONE
        attenDanceProgress = binding.attenDanceProgress
        initAdapter(inflater, binding.root)
//        initAdapter1(inflater,binding.root)
        currentMonthDays = getCurrentMonthDays()
        binding.rollName.text = SharedHelper(mActivity).name
        Log.d("hhg",SharedHelper(mActivity).standard)
        binding.shineTxt.text = "Let’s shine in Class ${UiUtils.getOrdinalSuffix(SharedHelper(mActivity).standard.toInt())} –  A Sec!"
        UiUtils.textViewGradient(binding.shineTxt,"#232B68","#4555CE")
        binding.logo.setOnLongClickListener{
            if(BuildConfig.DEBUG){
                ApiDataDialog(mActivity).show(mActivity)
            }
            return@setOnLongClickListener true
        }
        getStudentAttendance()
        studentHomework()
        getStudentClassTestProgress()
        clsTestFilter.clear()
        clsTestFilter.add("This Week")
        clsTestFilter.add("This Month")
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().examProgressDropdown(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()){
                            examFilter.clear()
                            examProgressResult = it.result!!
                            for (items in it.result!!){
                                examFilter.add(items.label!!)
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        getStudentAssignment()
        getStudentProject()
//        studentHomework()
        getStudentExamination()

        attachSearchWatcher(binding.search) { query ->
            search = query
            studentHomework()
        }
        attachSearchWatcher(binding.search2) { query ->
            search = query
            getStudentAssignment()
            getStudentProject()
        }
        attachSearchWatcher(binding.search3) { query ->
            search = query
            getStudentExamination()
        }

        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfDay = SimpleDateFormat("EEEE", Locale.getDefault())
        val sdfMon = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(currentDate)
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        val currentMonth = SimpleDateFormat("MMM", Locale.getDefault()).format(Date())
        val api = sdfDay.format(currentDate).toLowerCase(Locale.getDefault())
        val date = sdfDate.format(currentDate).toLowerCase(Locale.getDefault())
        val cYear = years.indexOf(currentYear.toString())
        val cMon = months.indexOf(currentMonth.toString())
        if (cYear != -1) {
            selectedYear = cYear
        }
        if (cMon != -1) {
            selectedMonth = cMon
        }
        binding.dtText.text = sdfMon
        timeTableDay = api
        eventDate = date
        getClassTimeTableHome()
        studentFees()

        ApiConnection.getInstance().getNoticeBoard(mActivity,"","").observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result.isNotEmpty()){
                            noticeList = it.result
                            loadDates1()
                            getEventsPager()
                        }
                        else {
                            loadDates1()
                        }
                    }
                    else{
//                        UiUtils.showSnack(it.msg, binding.root,false)
                        binding.cardNoNoticeData.txt.text = "No events present today!"
                        binding.cardNoNoticeData.root.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.dotsContainer.visibility = View.GONE
                    }
                }
            }
        }

      //  barChart = binding.barChart
        barChart1 = binding.barChart1
        barCharts = binding.barCharts
        mActivity.binding.timeTableDialog.close.setOnClickListener {
            mActivity.binding.timeTableDialog.root.visibility = View.GONE
        }

        var hwFilterLabel = ArrayList<String>()
        hwFilterLabel.add("Subject")
        hwFilterLabel.add("Status")
        hwFilterLabel.add("Mark Status")
        hwFilterLabel.add("Due Date")

        val labelAdapter = HomeworkFilterLabelAdapter(mActivity,hwFilterLabel,object : OnClickListener{
            override fun onClickItem(pos: Int) {

            }
        })
        val labelLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        binding.filterHw.titleRecycler.layoutManager = labelLayoutManager
        binding.filterHw.titleRecycler.adapter = labelAdapter

        hwStatusArr.clear()
        hwStatusArr.add("All")
        hwStatusArr.add("Today")
        hwStatusArr.add("Not Completed")
        hwStatusArr.add("Completed")

        markStatus.clear()
        markStatus.add("All")
        markStatus.add("Pending")
        markStatus.add("Completed")

        val markStsAdapter = HomeworkFilterValueAdapter(mActivity,markStatus,object : OnClickListener{
            override fun onClickItem(pos: Int) {

            }
        })
        val markStsLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        binding.filterHw.recycler.layoutManager = markStsLayoutManager
        binding.filterHw.recycler.adapter = markStsAdapter

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().programBasedSubject(mActivity).observe(mActivity){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let {success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            subArr.clear()
                            subArr.add("All")
                            for (items in it.result!!){
                                if (items.subject != null){
                                    subArr.add(items.subject!!.name!!)
                                }
                            }
                            val subAdapter = HomeworkFilterValueAdapter(mActivity,markStatus,object : OnClickListener{
                                override fun onClickItem(pos: Int) {

                                }
                            })
                            val subLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
                            binding.filterHw.recycler.layoutManager = subLayoutManager
                            binding.filterHw.recycler.adapter = subAdapter
                        }
                    }
                    else {

                    }
                }
            }
        }
        ApiConnection.getInstance().leaderBoard(mActivity,"","").observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            when(it.result!!.rows!!.size){
                                0 -> { }
                                1 -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status
                                }
                                2 -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status

                                    binding.rank2Name.text = "${it.result!!.rows!![1].name}"
                                    binding.points2.text = it.result!!.rows!![1].scoredMark
                                    binding.status2.text = it.result!!.rows!![1].status
                                }
                                3 -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status

                                    binding.rank2Name.text = "${it.result!!.rows!![1].name}"
                                    binding.points2.text = it.result!!.rows!![1].scoredMark
                                    binding.status2.text = it.result!!.rows!![1].status

                                    binding.rank3Name.text = "${it.result!!.rows!![2].name}"
                                    binding.points3.text = it.result!!.rows!![2].scoredMark
                                    binding.status3.text = it.result!!.rows!![2].status
                                }
                                else -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status

                                    binding.rank2Name.text = "${it.result!!.rows!![1].name}"
                                    binding.points2.text = it.result!!.rows!![1].scoredMark
                                    binding.status2.text = it.result!!.rows!![1].status

                                    binding.rank3Name.text = "${it.result!!.rows!![2].name}"
                                    binding.points3.text = it.result!!.rows!![2].scoredMark
                                    binding.status3.text = it.result!!.rows!![2].status
                                }
                            }
                        }
                        else{

                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }

        /*binding.filter.setOnClickListener {
            binding.filterHw.title.text = "Select Status"
            val hwStsAdapter = HomeworkFilterValueAdapter(mActivity,hwStatusArr,object : OnClickListener{
                override fun onClickItem(pos: Int) {

                }
            })
            val hwStsLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            binding.filterHw.recycler.layoutManager = hwStsLayoutManager
            binding.filterHw.recycler.adapter = hwStsAdapter

            binding.commonCalender.visibility = View.GONE
            binding.filterHw.root.visibility = View.VISIBLE
            UiUtils.animation(mActivity,binding.filterHw.root,R.anim.slide_in_from_bottom,true)
        }*/

        binding.filterHw.cancel.setOnClickListener {
            binding.commonCalender.visibility = View.VISIBLE
            binding.filterHw.root.visibility = View.GONE
        }

        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(mActivity)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
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
            if (hwStatus == "today"){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (hwStatus == "pending"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (hwStatus == "completed"){
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
                hwStatus = ""
                studentHomework()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                hwStatus = "pending"
                studentHomework()
                popupWindow.dismiss()
            }
            bind.today.setOnClickListener {
                hwStatus = "today"
                studentHomework()
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                hwStatus = "completed"
                studentHomework()
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

        binding.filter2.setOnClickListener {
            val inflater = LayoutInflater.from(mActivity)
            val bind : FilterAssignmentBinding = FilterAssignmentBinding.inflate(inflater)
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
            if (tabPos == "assignment") {
                if (assignmentStatus == "ongoing"){
                    UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (assignmentStatus == "pending"){
                    UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (assignmentStatus == "completed"){
                    UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                }
                else {
                    UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
            }
            else if (tabPos == "project") {
                if (projectStatus == "ongoing"){
                    UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (projectStatus == "pending"){
                    UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (projectStatus == "completed"){
                    UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                }
                else {
                    UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
            }

            bind.all.setOnClickListener {
                if (tabPos == "assignment"){
                    assignmentStatus = ""
                    getStudentAssignment()
                }
                else if (tabPos == "project"){
                    projectStatus = ""
                    getStudentProject()
                }
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                if (tabPos == "assignment"){
                    assignmentStatus = "pending"
                    getStudentAssignment()
                }
                else if (tabPos == "project"){
                    projectStatus = "pending"
                    getStudentProject()
                }
                popupWindow.dismiss()
            }
            bind.ongoing.setOnClickListener {
                if (tabPos == "assignment"){
                    assignmentStatus = "ongoing"
                    getStudentAssignment()
                }
                else if (tabPos == "project"){
                    projectStatus = "ongoing"
                    getStudentProject()
                }
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                if (tabPos == "assignment"){
                    assignmentStatus = "completed"
                    getStudentAssignment()
                }
                else if (tabPos == "project"){
                    projectStatus = "completed"
                    getStudentProject()
                }
                popupWindow.dismiss()
            }

            val anchorView = binding.filter2
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

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().updatesCount(mActivity).observe(mActivity){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            binding.count1.text = it.result!!.homeworkcount
                            binding.count2.text = it.result!!.assignmentcount
                            binding.count3.text = it.result!!.projectcount
                            binding.count4.text = it.result!!.Examcount
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

       /* binding.notify.setOnClickListener {
            BaseUtils.startActivity(mActivity, GuestLandingActivity(), null, false)
        }*/

        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, ProfileActivity(), null, false)
        }
        binding.leaderBoardViewAll.setOnClickListener {
            BaseUtils.startActivity(mActivity, LeaderBoardActivity(), null, false)
        }
        binding.feesViewall.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_fees)
        }
        binding.notify.setOnClickListener {
            /*BaseUtils.startActivity(mActivity, LeaderBoardActivity(), null, false)*/
//            BaseUtils.startActivity(mActivity,StaffInformationActivity(), null, false)
        }
        binding.date.setOnClickListener {
            showCalender{
                val sdfDate = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
                val dt = sdfDate.format(it)
                val api = sdfApi.format(it).toLowerCase(Locale.getDefault())
                binding.dtText1.text = dt
                timeTableDay = api
                getClassTimeTable()
            }
        }
        binding.date1.setOnClickListener {
            val dialog = Dialog(mActivity)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_month_picker)
            val bind: DialogMonthPickerBinding = DialogMonthPickerBinding.inflate(LayoutInflater.from(mActivity))
            dialog.setContentView(bind.root)
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(mActivity, R.color.transparent))
            )
            var width: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//        var height: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setGravity(Gravity.CENTER)
            var sMonth = selectedMonth
            var sYear = selectedYear
            val yearAdapter = YearAdapter(mActivity, years,selectedYear, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    sYear = pos
                    bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                }
            })
            bind.yearRecycler.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
            bind.yearRecycler.adapter = yearAdapter

            val currentYearIndex = years.indexOf(currentYear.toString())
            bind.yearRecycler.post {
                if (currentYearIndex != RecyclerView.NO_POSITION) {
                    val layoutManager = bind.yearRecycler.layoutManager as LinearLayoutManager
                    val recyclerWidth = bind.yearRecycler.width - bind.yearRecycler.paddingLeft - bind.yearRecycler.paddingRight
                    val itemWidth = resources.getDimensionPixelSize(R.dimen._70dp)
                    val offset = (recyclerWidth / 2) - (itemWidth / 2)
                    layoutManager.scrollToPositionWithOffset(currentYearIndex, offset)
                }
            }
            val monthAdapter = MonthsAdapter(mActivity, months,years[selectedYear].toInt(),selectedMonth, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    sMonth = pos
                    bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                }
            })
            bind.monthRecycler.layoutManager = GridLayoutManager(mActivity, 3, GridLayoutManager.VERTICAL, false)
            bind.monthRecycler.adapter = monthAdapter

            bind.select.setOnClickListener {
                selectedYear = sYear
                selectedMonth = sMonth
                currentMonthDays = getCurrentMonthDays(years[sYear].toInt(),selectedMonth)
                binding.dtText.text = "${months[selectedMonth]} ${years[selectedYear]}"
                val adapter = WeekDayAdapter(mActivity,currentMonthDays,currentDate,noticeList,object : OnClickListener{
                    override fun onClickItem(pos: Int) {
                        val dt = sdfDate.format(currentMonthDays[pos])
                        eventDate = dt
                        getEventsPager()
                    }
                })
                val linearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.dateRecycler.layoutManager = linearLayoutManager
                binding.dateRecycler.adapter = adapter
//                getEventsPager()
                dialog.dismiss()
            }
            bind.cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setOnDismissListener {

            }
            dialog.show()
        }

        binding.date2.setOnClickListener {
            showCalender{
                val sdfDate = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
                val dt = sdfDate.format(it)
                val api = sdfApi.format(it).toLowerCase(Locale.getDefault())
                binding.dtText1.text = dt
                timeTableDay = api
                getClassTimeTable()
            }
        }

        binding.viewHomeWork.setOnClickListener {
            binding.tabLayout.getTabAt(1)?.select()
        }

        binding.pendingViewAll.setOnClickListener {
            binding.tabLayout.getTabAt(1)?.select()
        }

        mActivity.binding.dialogHomework.close.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
        }
        mActivity.binding.dialogHomework.cancel.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
        }

        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > binding.header.height) {
                binding.tabLayout.translationY = scrollY.toFloat() - binding.header.height
                binding.tabLayout.elevation = 8f
            } else {
                binding.tabLayout.translationY = 0f
                binding.tabLayout.elevation = 0f
            }
        }
        var tabClicked = ""

        binding.tabClassTest.setOnClickListener {
            binding.spinLay.visibility = View.VISIBLE
            binding.roundProgress.visibility = View.GONE
            binding.sidelay.visibility = View.VISIBLE
            binding.subjectColorRecyclerView.visibility = View.VISIBLE
            binding.barChart1.visibility = View.VISIBLE
            binding.barCharts.visibility = View.VISIBLE
            UiUtils.textviewCustomDrawable( binding.tabClassTest, R.drawable.border_line_curve_24dp_primary )
            UiUtils.textViewTextColor(binding.tabClassTest, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabExamination,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tabExamination, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable( binding.tabAttendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAttendance, null, R.color.black_varient6)
            binding.spinner.visibility = View.VISIBLE
            binding.proTitle.text = "Class Test Insights"
            binding.proDesc.text = "Average Mark Scored"
            clsTestBarChart(resultClsTest)
            clsTestVerticalBarChart(resultClsTest)
            val adapter1 = SpinnerAdapter(mActivity, clsTestFilter)
            binding.spinnerFilter.adapter = adapter1
            binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,view: View?,position: Int,id: Long) {
                    val clickedValue: String = parent.getItemAtPosition(position) as String
                    if(position != 0){
                        clsTestValue = clickedValue
                    }
                    else{
                        clsTestValue = clickedValue
                    }
                    clsTestBarChart(resultClsTest)
                    clsTestVerticalBarChart(resultClsTest)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        attendanceFilters.clear()
        attendanceFilters.add("All")
        attendanceFilters.add("Last 7 days")
        attendanceFilters.add("This Week")
        attendanceFilters.add("Last Week")
        attendanceFilters.add("This Month")
        attendanceFilters.add("Last Month")

        binding.tabExamination.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabExamination,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabExamination,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabClassTest,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabClassTest,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable( binding.tabAttendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAttendance, null, R.color.black_varient6)
            binding.spinner.visibility = View.VISIBLE
            binding.sidelay.visibility = View.GONE
            binding.subjectColorRecyclerView.visibility = View.VISIBLE
            binding.roundProgress.visibility = View.GONE
            binding.barChart1.visibility = View.VISIBLE
            binding.barCharts.visibility = View.GONE
            examProgressBarChart1(resultExam)
            binding.proTitle.text = "Examination Insights"
            binding.proDesc.text = "Term Based exam results"
            val adapter = SpinnerAdapter(mActivity, attendanceFilters)
            binding.spinnerFilter.adapter = adapter

            binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val value = attendanceFilters[position]

                    isFilter = true
                    datefilter = when (value) {
                        "All" -> "All"
                        "Last 7 days" -> "last7days"
                        "This Week" -> "thisweek"
                        "Last Week" -> "lastweek"
                        "This Month" -> "thismonth"
                        "Last Month" -> "lastmonth"
                        else -> "All"
                    }

                    getStudentExamProgress()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
            /*val adapter = SpinnerAdapter(mActivity, examFilter)
            binding.spinnerFilter.adapter = adapter
            binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,view: View?,position: Int,id: Long) {
                    val clickedValue: String = parent.getItemAtPosition(position) as String
                    if(position != -1){
                        examId = examProgressResult[position].value!!
                        isFilter = true
                        getStudentExamProgress()
                    }
                    else{
                        examId = ""
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }*/

        binding.tabClassTest.performClick()
        binding.tabAttendance.setOnClickListener {
            getStudentAttendance()
            binding.roundProgress.visibility = View.VISIBLE
            binding.spinLay.visibility = View.GONE
//            binding.spinner.visibility = View.GONE
//            binding.sidelay.visibility = View.GONE
//            binding.examBar.visibility = View.GONE
//            binding.viewBar.visibility = View.GONE
//            binding.subjectColorRecyclerView.visibility = View.GONE
//            binding.barChart1.visibility = View.GONE
//            binding.barCharts.visibility = View.GONE
            UiUtils.textviewCustomDrawable( binding.tabClassTest,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabClassTest, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable( binding.tabAttendance, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabAttendance, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabExamination,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tabExamination, null, R.color.black_varient6)
           /* loadProgressBar(60f)*/
        }
        binding.lin1.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.count1,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count2,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count3,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count4,null,R.color.gray)
//            binding.classTest.visibility = View.GONE
            tabClicked = "homeWork"
            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
            val adapter = StudentHomeworkAdapter(mActivity, true,result1, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    getHomework(pos)
                }
            })
            binding.updateRecycler.layoutManager = linearLayoutManager
            binding.updateRecycler.adapter = adapter
        }
        binding.lin2.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.count2,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count3,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count4,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count1,null,R.color.gray)
//            binding.classTest.visibility = View.GONE
            tabClicked = "assignment"
            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
            val adapter = StudentAssignmentAdapter(mActivity,true,result,object : OnClickListener{
                override fun onClickItem(pos: Int) {
//                    getAssignmentStatus(pos)
                }

            })
            binding.updateRecycler.layoutManager = layoutManager
            binding.updateRecycler.adapter = adapter
        }
        binding.lin3.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.count3,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count1,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count4,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count2,null,R.color.gray)
//            binding.classTest.visibility = View.GONE
            tabClicked = "project"
            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
            val adapter = StudentProjectAdapter(mActivity,true, result2,object : OnClickListener{
                override fun onClickItem(pos: Int) {
                    getProjectResult(pos)
                }

            })
            binding.updateRecycler.layoutManager = layoutManager
            binding.updateRecycler.adapter = adapter
        }
        binding.lin4.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.count4,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count3,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count2,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.count1,null,R.color.gray)
//            binding.classTest.visibility = View.VISIBLE
            tabClicked = "Examination"
            if (result5.isNotEmpty()){
                binding.noUpdates.root.visibility = View.GONE
                binding.updateRecycler.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                val adapter = StudentExaminationAdapter(mActivity,true, result5,object : OnClickListener{
                    override fun onClickItem(pos: Int) {
                        // getProjectResult(pos)
                    }

                })
                binding.updateRecycler.layoutManager = layoutManager
                binding.updateRecycler.adapter = adapter
            }
            else {
                binding.noUpdates.root.visibility = View.VISIBLE
                binding.updateRecycler.visibility = View.GONE
            }
        }

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getStudentOverallProgress(mActivity).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noUpdates.root.visibility = View.GONE
                            binding.updateRecycler.visibility = View.VISIBLE
                            val adapter = OverAllProgressAdapter(mActivity,2,it.result!!,object: OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    if(it.result!![pos].type == "homework"){
                                        binding.tabLayout.getTabAt(1)?.select()
                                    }
                                    else if(it.result!![pos].type == "project"){
                                        binding.tabLayout.getTabAt(5)?.select()
                                    }
                                    else if(it.result!![pos].type == "assignment"){
                                        binding.tabLayout.getTabAt(4)?.select()
                                    }
                                    else if(it.result!![pos].type == "classtest"){
                                        binding.tabLayout.getTabAt(3)?.select()
                                    }
                                }
                            })
                            val layoutManager = GridLayoutManager(mActivity,2 ,LinearLayoutManager.VERTICAL, false)
                            binding.updateRecycler.layoutManager = layoutManager
                            binding.updateRecycler.adapter = adapter
                        }
                        else {
                            binding.noUpdates.root.visibility = View.VISIBLE
                            binding.updateRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noUpdates.root.visibility = View.VISIBLE
                        binding.updateRecycler.visibility = View.GONE
                    }
                }
            }
        }

        binding.viewHomeWork.setOnClickListener {
            if (tabClicked == "homeWork") {
                binding.tabLayout.getTabAt(1)?.select()
            } else if (tabClicked == "assignment") {
                binding.tabLayout.getTabAt(4)?.select()
            } else if (tabClicked == "project") {
                binding.tabLayout.getTabAt(5)?.select()
            } else if (tabClicked == "Examination") {
                binding.tabLayout.getTabAt(3)?.select()
            }
        }
        binding.timeTableViewAll.setOnClickListener {
            binding.tabLayout.getTabAt(2)?.select()
        }
        binding.yourprogress.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_progress)
        }
        binding.feesViewall.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_fees)
        }

        binding.tabToday.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabToday, R.drawable.border_line_curve_24dp_primary )
            UiUtils.linearLayoutBgDrawable( binding.tabNotCompleted, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable( binding.tabComplete, R.drawable.border_line_curve_24dp_grey )/*
            UiUtils.textViewTextColor(binding.textToday, "#232B68", null)
            UiUtils.textViewBgTint(binding.countToday,"#232B68", null)
            UiUtils.textViewTextColor(binding.textNotCmpltd, "#5B5B5B", null)
            UiUtils.textViewBgTint(binding.countNotCompleted,"#232B68", null)
            UiUtils.textViewBgTint(binding.textCompleted,"#5B5B5B", null)
            UiUtils.textViewBgTint(binding.countComplete,"#232B68", null)*/
            hwStatus = "today"
            studentHomework()
        }
        binding.tabNotCompleted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabNotCompleted, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabToday, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabComplete, R.drawable.border_line_curve_24dp_grey )/*
            UiUtils.textViewTextColor(binding.textToday, "#232B68", null)
            UiUtils.textViewBgTint(binding.countToday,"#5B5B5B", null)
            UiUtils.textViewTextColor(binding.textNotCmpltd, "#232B68", null)
            UiUtils.textViewBgTint(binding.countToday,"#232B68", null)
            UiUtils.textViewBgTint(binding.textCompleted,"#D4D4D4", null)
            UiUtils.textViewBgTint(binding.countComplete,"#5B5B5B", null)*/
            hwStatus = "pending"
            studentHomework()
        }
        binding.tabComplete.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabComplete, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabToday, R.drawable.border_line_curve_24dp_grey )/*
            UiUtils.textViewTextColor(binding.textToday, "#5B5B5B", null)
            UiUtils.textViewBgTint(binding.countToday,"#5B5B5B", null)
            UiUtils.textViewTextColor(binding.textNotCmpltd, "#5B5B5B",null)
            UiUtils.textViewBgTint(binding.countToday,"#5B5B5B", null)
            UiUtils.textViewBgTint(binding.textCompleted,"#232B68",null)
            UiUtils.textViewBgTint(binding.countComplete,"#232B68",null)*/
            hwStatus = "completed"
            studentHomework()
        }

        binding.tabTimeTable.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabTimeTable,R.drawable.border_line_curve_24dp_primary )
            UiUtils.linearLayoutBgDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_grey)
        }
        binding.tabExam.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabExam, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable( binding.tabTimeTable, R.drawable.border_line_curve_24dp_grey)
        }

        binding.filter3.setOnClickListener {
            val inflater = LayoutInflater.from(mActivity)
            val bind : FilterAssignmentBinding = FilterAssignmentBinding.inflate(inflater)
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
            if (examTabClicked == "classTest") {
                bind.ongoing.text = "Today"
                bind.pending.text = "Upcoming"
                if (classTestSts == "today"){
                    UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (classTestSts == "upcoming"){
                    UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (classTestSts == "completed"){
                    UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                }
                else {
                    UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
            }
            else if (examTabClicked == "exam") {
                bind.ongoing.text = "Ongoing"
                bind.pending.text = "Upcoming"
                if (examStatus == "ongoing"){
                    UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (examStatus == "upcoming"){
                    UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (examStatus == "completed"){
                    UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                }
                else {
                    UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
            }

            bind.all.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = ""
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = ""
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "today"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "ongoing"
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            bind.ongoing.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "upcoming"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "upcoming"
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "completed"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "completed"
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            val anchorView = binding.filter3
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

        binding.classTest.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classTest, null,R.color.white)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.black_varient3)
            examTabClicked = "classTest"
            classTestSts = ""
            getClassTest()
//            binding.tabclassTestToday.performClick()
        }
        /*binding.tabclassTestToday.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabclassTestToday,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.clsTstCountToday,R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.clsTstCountUpcoming,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.clsTstCountTcompleted,R.drawable.ic_round_line_3)
            UiUtils.textViewTextColor(binding.todayClassTestId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.ClassTestupcomingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.classTestcompletedId,null, R.color.black_varient6)
            classTestSts = "today"
            getClassTest()
        }
        binding.tabClassTestUpcoming.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestUpcoming,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabclassTestToday,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.clsTstCountToday,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.clsTstCountUpcoming,R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.clsTstCountTcompleted,R.drawable.ic_round_line_3)
            UiUtils.textViewTextColor(binding.ClassTestupcomingId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.todayClassTestId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.classTestcompletedId,null, R.color.black_varient6)
            classTestSts = "upcoming"
            getClassTest()
        }
        binding.tabClassTestCompleted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestCompleted,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabclassTestToday,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.clsTstCountToday,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.clsTstCountUpcoming,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.clsTstCountTcompleted,R.drawable.ic_round_line2)
            UiUtils.textViewTextColor(binding.classTestcompletedId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.todayClassTestId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.ClassTestupcomingId,null, R.color.black_varient6)
            classTestSts = "completed"
            getClassTest()
        }*/
        binding.classExam.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classExam, null,R.color.white)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.black_varient3)
            examTabClicked = "exam"
            examStatus = ""
            getStudentExamination()
//            binding.tabExamOngoing.performClick()
        }
        /*binding.tabExamOngoing.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExamOngoing,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabExamCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.examCount1,R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.examCount2,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.examCount3,R.drawable.ic_round_line_3)
            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.completedId,null, R.color.black_varient6)
            examStatus = "ongoing"
            getStudentExamination()
        }
        binding.tabExamUpcoming.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExamUpcoming,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamOngoing,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabExamCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.examCount1,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.examCount2,R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.examCount3,R.drawable.ic_round_line_3)
            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.completedId,null, R.color.black_varient6)
            examStatus = "upcomming"
            getStudentExamination()
        }
        binding.tabExamCompleted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExamCompleted,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamOngoing,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabExamUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.examCount1,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.examCount2,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.examCount3,R.drawable.ic_round_line2)
            UiUtils.textViewTextColor(binding.completedId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.black_varient6)
            examStatus = "completed"
            getStudentExamination()
        }*/

        binding.dialogExam2.close.setOnClickListener {
            binding.dialogExam2.root.visibility = View.GONE
        }
        binding.dialogExam2.payment.setOnClickListener {
            binding.dialogExam2.root.visibility = View.GONE
        }

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().assignmentCount(mActivity).observe(mActivity){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            binding.countOngoing.text = it.result!!.ongoing
                            binding.countnotcompleted2.text = it.result!!.notcompleted
                            binding.countcomplete2.text = it.result!!.completed
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
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().projectCount(mActivity).observe(mActivity){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            binding.countOngoing.text = it.result!!.ongoing
                            binding.countnotcompleted2.text = it.result!!.notcompletd
                            binding.countcomplete2.text = it.result!!.completed
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
        binding.tabOngoing.setOnClickListener {
                UiUtils.linearLayoutBgDrawable(binding.tabOngoing,R.drawable.border_line_curve_24dp_primary)
                UiUtils.linearLayoutBgDrawable( binding.tabNotCompleted2, R.drawable.border_line_curve_24dp_grey )
                UiUtils.linearLayoutBgDrawable( binding.tabComplete2, R.drawable.border_line_curve_24dp_grey)
                UiUtils.textViewTextColor(binding.ongoing,null, R.color.colorPrimary)
                UiUtils.textViewTextColor(binding.notCompleted,null, R.color.black_varient6)
                UiUtils.textViewTextColor(binding.completed,null, R.color.black_varient6)
                UiUtils.textviewCustomDrawable(binding.countOngoing, R.drawable.ic_round_line2)
                UiUtils.textviewCustomDrawable(binding.countnotcompleted2, R.drawable.ic_round_line_3)
                UiUtils.textviewCustomDrawable(binding.countcomplete2, R.drawable.ic_round_line_3)
                if (tabPos == "assignment") {
                    assignmentStatus = "ongoing"
                    getStudentAssignment()
                } else if (tabPos == "project") {
                    projectStatus = "ongoing"
                    getStudentProject()
                }
        }
        binding.tabNotCompleted2.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabOngoing, R.drawable.border_line_curve_24dp_grey )
            UiUtils.linearLayoutBgDrawable( binding.tabNotCompleted2,  R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(  binding.tabComplete2,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.ongoing,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.notCompleted,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.completed,null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.countOngoing, R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.countnotcompleted2, R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.countcomplete2, R.drawable.ic_round_line_3)
            if (tabPos == "assignment") {
            assignmentStatus = "overdue"
            getStudentAssignment()
            }
            else if (tabPos == "project") {
            projectStatus = "overdue"
            getStudentProject()
            }
        }
        binding.tabComplete2.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabOngoing,R.drawable.border_line_curve_24dp_grey )
            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable( binding.tabComplete2, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.ongoing,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.notCompleted,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.completed,null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.countOngoing, R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.countnotcompleted2, R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.countcomplete2, R.drawable.ic_round_line2)
            if (tabPos == "assignment") {
                assignmentStatus = "submitted"
                getStudentAssignment()
            } else if (tabPos == "project") {
                projectStatus = "completed"
                getStudentProject()
            }
        }

        binding.tabAnnouncement.setOnClickListener{
            UiUtils.textviewCustomDrawable(binding.tabAnnouncement,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.tabEvents,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAnnouncement, null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabEvents, null,R.color.black_varient6)
        }
        binding.tabEvents.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabEvents, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.tabAnnouncement, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAnnouncement, null,R.color.black_varient6)
            UiUtils.textViewTextColor(binding.tabEvents, null,R.color.colorPrimary)
//            ststyp = "event"
//           getnoticeBoard()
        }
        binding.tabClassTest.performClick()

        binding.tabDay.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabDay, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabDay, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabWeek, R.drawable.border_curve_6dp)
/*
            val adapter1 = TimeTableAdapter(mActivity, result4)
            val linearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            binding.timetableRecycler.layoutManager = linearLayoutManager
            binding.timetableRecycler.adapter = adapter1
            binding.timetableRecycler.visibility = View.VISIBLE
            binding.weekLay.visibility = View.GONE*/
        }

       /* binding.tabWeek.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabWeek, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabWeek, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabDay, R.drawable.border_curve_6dp)

            val columnWidthPx = (110 * resources.displayMetrics.density).toInt()
            val adapter = WeeklyTimeTableAdapter(mActivity, allTimeSlots)
            val gridLayoutManager =
                object : GridLayoutManager(mActivity, 6, GridLayoutManager.VERTICAL, false) {
                    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                        // Force our custom width
                        lp?.width = columnWidthPx
                        return super.checkLayoutParams(lp)
                    }
                }
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when {
                        allTimeSlots[position].isBreak -> 1 // Full width for breaks
                        else -> 1 // Normal items take 1 column
                    }
                }
            }

            binding.timetableWeeklyRecycler.layoutManager = gridLayoutManager
            binding.timetableWeeklyRecycler.adapter = adapter
            binding.timetableRecycler.visibility = View.GONE
            binding.weekLay.visibility = View.VISIBLE
        }*/

        binding.tabDay1.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabDay1,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabDay1,R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabDay1,null,R.color.white)
            UiUtils.textViewTextColor(binding.tabWeek1,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabWeek1,R.drawable.border_curve_6dp)
            ststyp = "day"
            getnoticeBoard()

        }
        binding.tabWeek1.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabWeek1, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabWeek1, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabWeek1, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabDay1, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabDay1, R.drawable.border_curve_6dp)
            ststyp = ""
            getnoticeBoard()

        }
        binding.tabDay.performClick()
//        binding.tabDay1.performClick()


        binding.dialogRresult.cancel.setOnClickListener {
            binding.dialogRresult.root.visibility = View.GONE
        }

        return view
    }

    private fun setupDotsIndicator(events: ArrayList<NoticeBoardResponse.Result>) {
        binding.dotsContainer.removeAllViews()

        for (event in events) {
            val eventName = event.type!!.name ?: continue
            val dot = ImageView(context)

            val eventColors = mapOf(
                "announcement" to Color.parseColor("#F5A623"),
                "meeting" to Color.parseColor("#E85A5B"),
                "event" to Color.parseColor("#5E31B8"),
                "holiday" to Color.parseColor("#6BE9A3"),
                "festival" to Color.parseColor("#5E31B8"),
                "exam" to Color.parseColor("#FF7475")
            )
            val color = eventColors[eventName] ?: Color.GRAY
            dot.background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(color)
                setSize(16, 16)
            }
            dot.layoutParams = LinearLayout.LayoutParams(16, 16).apply {
                setMargins(8, 0, 8, 0)
            }
            binding.dotsContainer.addView(dot)
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
            }
        })
    }

    private fun updateDots(selectedPosition: Int) {
        for (i in 0 until binding.dotsContainer.childCount) {
            val dot = binding.dotsContainer.getChildAt(i) as ImageView
            val isSelected = (i == selectedPosition)
            dot.isSelected = isSelected

            if (isSelected) {
                dot.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .setDuration(200)
                    .start()
            } else {
                dot.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
        }

//        // Optional: Change dot container background based on current event
//        val currentColor = UiUtils.getColorFromEventName(events[selectedPosition].name)
//        binding.dotsContainer.background = GradientDrawable().apply {
//            cornerRadius = 16f
//            setColor(adjustAlpha(currentColor, 0.2f)) // Semi-transparent version
//        }
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).toInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

   /* fun loadProgressBar(value: Float) {
        val circularProgressBar: CircularProgressBar = binding.progress
        circularProgressBar.progressBarColor =
            ContextCompat.getColor(mActivity, R.color.progress_bar)
        circularProgressBar.setBackgroundColor(
            ContextCompat.getColor(
                mActivity,
                R.color.progress_bar_back
            )
        )
        circularProgressBar.progressBarWidth = resources.getDimension(R.dimen._6sp)
        circularProgressBar.backgroundProgressBarWidth = resources.getDimension(R.dimen._6sp)
        val animationDuration = 1500L
        circularProgressBar.setProgressWithAnimation(value, animationDuration)
    }*/

    private fun setupBarChart() {
        val entries = listOf(
            BarEntry(0f, 100f),
            BarEntry(1f, 80f),
            BarEntry(2f, 60f),
            BarEntry(3f, 40f),
            BarEntry(4f, 20f)
        )

        val subjects = listOf("Tamil", "English", "Maths", "Science", "Social")

        val dataSet = BarDataSet(entries, "").apply {
            val startColor = Color.parseColor("#ffffff")
            val endColor = Color.parseColor("#ffffff")
            colors = listOf(
                startColor,
                endColor
            )
            setGradientColor(startColor, endColor)
            setDrawValues(false)
        }

        barChart.renderer = TopRoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.3f

        barChart.apply {
            data = barData

            xAxis.valueFormatter = IndexAxisValueFormatter(subjects)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.textSize = 11f
            xAxis.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)

            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 100f
            axisLeft.granularity = 20f
            axisLeft.setLabelCount(5, true)
            axisLeft.textSize = 11f
            axisLeft.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
            axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}%"
                }
            }

            axisLeft.enableGridDashedLine(10f, 10f, 0f)
            axisLeft.setDrawGridLines(true)
            axisLeft.gridColor = Color.GRAY
            axisRight.isEnabled = false

            // Chart styling
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            // Add Marker for Tooltips
            val markerView = CustomMarkerView(mActivity)
            barChart.marker = markerView

            // Add rounded corners to bars
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            // Animation
            animateY(1000)
            setExtraOffsets(10f, 10f, 10f, 10f)

            invalidate()
        }
    }

    fun RecyclerView.centerItem(position: Int, itemWidth: Int) {
        post {
            val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
            val visibleWidth = width - paddingLeft - paddingRight
            val exactCenter = (visibleWidth / 2) - (itemWidth / 2)
            layoutManager.scrollToPositionWithOffset(position, exactCenter)
        }
    }

    private fun getCurrentMonthDays(): ArrayList<Date> {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.set(Calendar.DAY_OF_MONTH, 1) // Start from the 1st of the month
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val daysList = ArrayList<Date>()

        for (i in 1..daysInMonth) {
            val tempCalendar = Calendar.getInstance()
            tempCalendar.set(Calendar.YEAR, currentYear)
            tempCalendar.set(Calendar.MONTH, currentMonth)
            tempCalendar.set(Calendar.DAY_OF_MONTH, i)
            daysList.add(tempCalendar.time)
        }

        return daysList
    }

    private fun getCurrentMonthDays(year: Int? = null, month: Int? = null): ArrayList<Date> {
        val calendar = Calendar.getInstance()

        // Use provided year and month, or default to the current year and month
        calendar.set(Calendar.YEAR, year ?: calendar.get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, month ?: calendar.get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, 1) // Start from the 1st of the month

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val daysList = ArrayList<Date>()

        for (i in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, i) // Update day in the loop
            daysList.add(calendar.time)
        }

        return daysList
    }


    /*private fun getCurrentYearMonthEvents(): ArrayList<Any> {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        val result = ArrayList<Any>()

        // Add current month's name and year as the first index
        result.add("${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())}, $currentYear")

        // Get dates for the current month
        val currentMonthDates = getMonthDates(currentYear, currentMonth)
        result.add(currentMonthDates)

        // Move to the next month
        calendar.add(Calendar.MONTH, 1)
        val nextYear = calendar.get(Calendar.YEAR)
        val nextMonth = calendar.get(Calendar.MONTH)

        // Get the next month's name and year
        result.add("${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())}, $nextYear")

        // Get dates for the next month
        val nextMonthDates = getMonthDates(nextYear, nextMonth)
        result.add(nextMonthDates)

        return result
    }

    private fun getMonthDates(year: Int, month: Int): List<String> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dates = mutableListOf<String>()

        for (i in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            dates.add(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time))
        }

        return dates
    }*/


    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {
        /* listOfBioFragment.clear()
         listOfBioFragment.add(listChildFragment1)
         listOfBioFragment.add(listChildFragment2)
         listOfBioFragment.add(listChildFragment3)*/

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        // binding.tabLayout.tabGravity = TabLayout.GRAVITY_START
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_START
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        val linear0: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab0 = linear0.findViewById<TextView>(R.id.tab)
        val icon0 = linear0.findViewById<ImageView>(R.id.icon)
        txttab0.text = "My Diary"
        UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
        UiUtils.imageviewDrawable(icon0,R.drawable.tab_diary)
        UiUtils.imageViewTint(icon0,null,R.color.colorPrimary)
        txttab0.setTextAppearance(R.style.FontMedium)
//        UiUtils.linearLayoutBgTint(lin0,"#F2D9DA",null)
        binding.tabLayout.getTabAt(0)!!.customView = linear0

        val linear1: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab1 = linear1.findViewById<TextView>(R.id.tab)
        val icon1 = linear1.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon1,R.drawable.tab_homework)
        UiUtils.imageViewTint(icon1,null,R.color.black_varient3)
        txttab1.text = "Homework"
        UiUtils.textViewTextColor(txttab1, null, R.color.black_varient3)
        txttab1.setTextAppearance(R.style.FontRegular)
        binding.tabLayout.getTabAt(1)!!.customView = linear1

        val linear2: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab2 = linear2.findViewById<TextView>(R.id.tab)
        val icon2 = linear2.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon2,R.drawable.tab_timetable)
        UiUtils.imageViewTint(icon2,null,R.color.black_varient3)
        txttab2.text = "Time Table"
        UiUtils.textViewTextColor(txttab2, null, R.color.black_varient3)
        txttab2.setTextAppearance(R.style.FontRegular)
        binding.tabLayout.getTabAt(2)!!.customView = linear2

        val linear3: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab3 = linear3.findViewById<TextView>(R.id.tab)
        val icon3 = linear3.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon3,R.drawable.tab_exam)
        UiUtils.imageViewTint(icon3,null,R.color.black_varient3)
        txttab3.text = "Examination"
        UiUtils.textViewTextColor(txttab3, null, R.color.black_varient3)
        txttab3.setTextAppearance(R.style.FontRegular)
        binding.tabLayout.getTabAt(3)!!.customView = linear3

        val linear4: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab4 = linear4.findViewById<TextView>(R.id.tab)
        val icon4 = linear4.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon4,R.drawable.tab_assignment)
        UiUtils.imageViewTint(icon4,null,R.color.black_varient3)
        txttab4.text = "Assignment"
        UiUtils.textViewTextColor(txttab4, null, R.color.black_varient3)
        txttab4.setTextAppearance(R.style.FontRegular)
        binding.tabLayout.getTabAt(4)!!.customView = linear4

        val linear5: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab5 = linear5.findViewById<TextView>(R.id.tab)
        val icon5 = linear5.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon5,R.drawable.tab_project)
        UiUtils.imageViewTint(icon5,null,R.color.black_varient3)
        txttab5.text = "Projects"
        UiUtils.textViewTextColor(txttab5, null, R.color.black_varient3)
        txttab5.setTextAppearance(R.style.FontRegular)
        binding.tabLayout.getTabAt(5)!!.customView = linear5

        val linear6: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab6 = linear6.findViewById<TextView>(R.id.tab)
        val icon6 = linear6.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon6,R.drawable.tab_event)
        UiUtils.imageViewTint(icon6,null,R.color.black_varient3)
        txttab6.text = "News & Events"
        UiUtils.textViewTextColor(txttab6, null, R.color.black_varient3)
        txttab6.setTextAppearance(R.style.FontRegular)
        binding.tabLayout.getTabAt(6)!!.customView = linear6
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val linear0: View = tab.customView!!
                val txttab0 = linear0.findViewById<TextView>(R.id.tab)
                val icon0 = linear0.findViewById<ImageView>(R.id.icon)
                UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                UiUtils.imageViewTint(icon0,null,R.color.colorPrimary)
                txttab0.setTextAppearance(R.style.FontMedium)
                if (tab.position == 0) {
                    binding.pageMyDiary.visibility = View.VISIBLE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.commonCalender.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    isDiary = true
                    getClassTimeTableHome()
                    loadDates1()
                } else if (tab.position == 1) {
                    binding.pageMyDiary.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    isDiary = false

//                    binding.tabToday.performClick()
                    loadDates2()
                } else if (tab.position == 2) {
                    binding.pageMyDiary.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    getClassTimeTable()
                    isDiary = false
                    loadDates2()
                } else if (tab.position == 3) {
                    binding.pageMyDiary.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageExam.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.pageNews.visibility = View.GONE
                    isDiary = false
                    binding.classTest.performClick()
                    loadDates2()
                } else if (tab.position == 4) {
                    binding.pageMyDiary.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    isDiary = false
                    tabPos = "assignment"
//                    binding.tabOngoing.performClick()
                    getStudentAssignment()
                    loadDates2()
                } else if (tab.position == 5) {
                    binding.pageMyDiary.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    isDiary = false
                    tabPos = "project"
                    getStudentProject()
//                    binding.tabOngoing.performClick()
                    loadDates2()
                } else if (tab.position == 6) {
                    binding.pageMyDiary.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    isDiary = false
                    binding.pageExam.visibility = View.GONE
                    binding.tabDay1.performClick()
                    loadDates2()
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val linear1: View = tab.customView!!
                val txttab1 = linear1.findViewById<TextView>(R.id.tab)
                val icon1 = linear1.findViewById<ImageView>(R.id.icon)
                UiUtils.textViewTextColor(txttab1, null, R.color.black_varient3)
                UiUtils.imageViewTint(icon1,null,R.color.black_varient3)
                txttab1.setTextAppearance(R.style.FontRegular)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun studentHomework() {
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().studentHomework(mActivity,search,hwStatus,"").observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            binding.noUpdates.root.visibility = View.GONE
                            binding.noData.root.visibility = View.GONE
                            binding.updateRecycler.visibility = View.VISIBLE
                            binding.tabHomeworkRecycler.visibility = View.VISIBLE
                            result1 = it.result!!
//                            binding.lin1.performClick()
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = StudentHomeworkAdapter1(mActivity,false,result1, object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    getHomework(pos)
                                }
                            })
                            binding.tabHomeworkRecycler.layoutManager = linearLayoutManager
                            binding.tabHomeworkRecycler.adapter = adapter
                        } else {
                            binding.noUpdates.root.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.VISIBLE
                            binding.updateRecycler.visibility = View.GONE
                            binding.tabHomeworkRecycler.visibility = View.GONE
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noUpdates.root.visibility = View.VISIBLE
                        binding.noData.root.visibility = View.VISIBLE
                        binding.updateRecycler.visibility = View.GONE
                        binding.tabHomeworkRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getStudentAssignment() {
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getStudentAssignment1(mActivity,search,assignmentStatus,"").observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            result = it.result!!
                            binding.recycler2.visibility = View.VISIBLE
                            binding.noData1.root.visibility = View.GONE
                            val LinearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = StudentAssignmentAdapter1(mActivity,false,result,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
//                                    getAssignmentStatus(pos)
                                }
                            })
                            binding.recycler2.layoutManager = LinearLayoutManager
                            binding.recycler2.adapter = adapter
                        }
                        else{
                            binding.recycler2.visibility = View.GONE
                            binding.noData1.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } 
                    else {
                        binding.recycler2.visibility = View.GONE
                        binding.noData1.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getStudentProject(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getStudentProject(mActivity,projectStatus, search).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result.isNotEmpty()){
                            binding.noData1.root.visibility = View.GONE
                            binding.recycler2.visibility = View.VISIBLE
                            result2 = it.result
                            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = StudentProjectAdapter1(mActivity,false,it.result,object :OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    getProjectResult(pos)
                                }
                            })
                            binding.recycler2.layoutManager= layoutManager
                            binding.recycler2.adapter = adapter
                        }
                        else{
                            binding.noData1.root.visibility = View.VISIBLE
                            binding.recycler2.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
                        binding.noData1.root.visibility = View.VISIBLE
                        binding.recycler2.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
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
        datePickerDialog = DatePickerDialog(mActivity,
            { view, year, monthOfYear, dayOfMonth ->
                var sDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                sDate = BaseUtils.getFormattedDate(sDate, "dd/MM/yyyy", "yyyy-MM-dd")
                onDateSelected(sDate)
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }

    fun getEventsPager(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getNoticeBoard(mActivity,"",eventDate).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result.isNotEmpty()){
//                            noticeList = it.result
//                            loadDates1()
                            binding.cardNoNoticeData.root.visibility = View.GONE
                            binding.viewPager.visibility = View.VISIBLE
                            binding.dotsContainer.visibility = View.VISIBLE
                            binding.viewPager.adapter = EventsPagerAdapter(mActivity,it.result)
                            setupDotsIndicator(it.result)
                            binding.viewPager.setPageTransformer { page, position ->
                                val offset = abs(position)
                                with(page) {
                                    scaleY = 1 - (offset * 0.1f)
                                    alpha = 1 - (offset * 0.3f)
                                }
                            }
                            if (timer != null){
                                timer?.cancel()
                            }
                            timer = object : CountDownTimer(Long.MAX_VALUE, 3000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    val currentPage = binding.viewPager.currentItem
                                    if (currentPage < it.result.size - 1) {
                                        binding.viewPager.setCurrentItem(currentPage + 1, true)
                                    } else {
                                        binding.viewPager.setCurrentItem(0, false)
                                    }
                                }

                                override fun onFinish() { }
                            }
                            timer?.start()
                        }
                        else {
                           loadDates1()
                            binding.cardNoNoticeData.txt.text = "No events present today!"
                            binding.cardNoNoticeData.root.visibility = View.VISIBLE
                            binding.viewPager.visibility = View.GONE
                            binding.dotsContainer.visibility = View.GONE
                        }
                    }
                    else{
//                        UiUtils.showSnack(it.msg, binding.root,false)
                        binding.cardNoNoticeData.txt.text = "No events present today!"
                        binding.cardNoNoticeData.root.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.dotsContainer.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun loadDates1(){
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        val adapter = WeekDayAdapter(mActivity,currentMonthDays,currentDate,noticeList,object : OnClickListener{
            override fun onClickItem(pos: Int) {
                val dt = sdfDate.format(currentMonthDays[pos])
                UiUtils.log("kiuygf",dt)
                eventDate = dt
                getEventsPager()
            }
        })
        val linearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler.layoutManager = linearLayoutManager
        binding.dateRecycler.adapter = adapter

        val currentDayIndex = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex != -1) {
            val itemWidth = resources.getDimensionPixelSize(R.dimen._55dp)
            binding.dateRecycler.centerItem(currentDayIndex, itemWidth)
        }
    }

    fun loadDates2(){
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
            UiUtils.log("gfhjk",""+selectedDate)
            val dt = sdfApi.format(selectedDate).toLowerCase(Locale.getDefault())
            timeTableDay = dt
            getClassTimeTable()

        }
        val linearLayoutManager1 = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler1.layoutManager = linearLayoutManager1
        binding.dateRecycler1.adapter = adapter1

        val currentDayIndex1 = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex1 != -1) {
            val itemWidth = resources.getDimensionPixelSize(R.dimen._55dp)
            binding.dateRecycler1.centerItem(currentDayIndex1, itemWidth)
        }
    }

    private fun getnoticeBoard(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getNoticeBoard(mActivity,ststyp,"").observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result.isNotEmpty()){
                             binding.noData4.root.visibility = View.GONE
                            binding.recycler3.visibility = View.VISIBLE
                            val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                            val adapter =  NoticeBoardAdapter(mActivity,ststyp,it.result)
                            binding.recycler3.layoutManager = layoutManager
                            binding.recycler3.adapter = adapter
                        }
                        else {
                            binding.noData4.root.visibility = View.VISIBLE
                            binding.recycler3.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root,false)
                        }
                    }
                    else{
                        binding.noData4.root.visibility = View.VISIBLE
                        binding.recycler3.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }

    private fun getClassTest(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getStudentClassTest(mActivity,search,classTestSts, classTestAttach).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noData3.root.visibility = View.GONE
                            binding.classTestRecycler.visibility = View.VISIBLE
                            result3 = it.result!!
                            val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                            val adapter = StudentClassTestAdapter(mActivity,it.result!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    getClsTestDialog(pos)
                                }
                            })
                            binding.classTestRecycler.layoutManager = layoutManager
                            binding.classTestRecycler.adapter = adapter
                        }else{
//                            UiUtils.showSnack(it.msg, binding.root,false)
                            binding.noData3.root.visibility = View.VISIBLE
                            binding.classTestRecycler.visibility = View.GONE
                        }
                    }
                    else{
//                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData3.root.visibility = View.VISIBLE
//                        binding.updateRecycler.visibility = View.GONE
                        binding.classTestRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun studentFees() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentFees(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            binding.feesRecycler.visibility = View.VISIBLE
                            binding.noDataFees.root.visibility = View.GONE
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                            val adapter = StudentFeesAdapter(mActivity,it.result!!.terms!!)
                            binding.feesRecycler.layoutManager = linearLayoutManager
                            binding.feesRecycler.adapter = adapter
                        } else {
                            binding.feesRecycler.visibility = View.GONE
                            binding.noDataFees.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.feesRecycler.visibility = View.GONE
                        binding.noDataFees.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getStudentExamination(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getExam(mActivity,search, examStatus).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.noData3.root.visibility = View.GONE
                            binding.classTestRecycler.visibility = View.VISIBLE
                            result5 = it.result!!.rows!!
                            val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                            val adapter = StudentExaminationAdapter(mActivity,false,it.result!!.rows!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
//                                    getExam(pos)
                                }
                            })
                            binding.classTestRecycler.layoutManager = layoutManager
                            binding.classTestRecycler.adapter = adapter
                        }  else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData3.root.visibility = View.VISIBLE
                            binding.classTestRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData3.root.visibility = View.VISIBLE
                        binding.classTestRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getClassTimeTable(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getClassTimetable(mActivity,"",timeTableDay).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.periods!!.isNotEmpty()){
                            binding.noDataTimeTable.root.visibility = View.GONE
                            binding.timetableRecycler.visibility = View.VISIBLE
                            val adapter = TimeTableAdapter(mActivity,false,it.result!!.periods!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    mActivity.binding.timeTableDialog.root.visibility = View.VISIBLE
                                    mActivity.binding.timeTableDialog.close1.setOnClickListener{
                                        mActivity.binding.timeTableDialog.root.visibility = View.GONE
                                    }
                                }
                            })
                            val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                            binding.timetableRecycler.layoutManager = layoutManager
                            binding.timetableRecycler.adapter = adapter
                        } else {
                            binding.noDataTimeTable.txt.text = "No Tabletable Available"
                            binding.noDataTimeTable.root.visibility = View.VISIBLE
                            binding.timetableRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noDataTimeTable.txt.text = "No Tabletable Available"
                        binding.noDataTimeTable.root.visibility = View.VISIBLE
//                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getClassTimeTableHome(){
        if (result4.isEmpty()){
            DialogUtils.showLoader(mActivity)
            ApiConnection.getInstance().getClassTimetable(mActivity,"subject",timeTableDay).observe(mActivity){
                it?.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if(success){
                            if (it.result != null && it.result!!.periods!!.isNotEmpty()){
                                binding.timeTableNoData.root.visibility = View.GONE
                                binding.todayTimeTableRecycler.visibility = View.VISIBLE
                                result4 = it.result!!.periods!!
                                Log.d("poiuytree", it.result!!.periods.toString())
                                val adapter = TimeTableAdapter(mActivity,true,it.result!!.periods!!,object : OnClickListener{
                                    override fun onClickItem(pos: Int) {
                                        getTimeTableDialog(pos)
                                    }
                                })
                                val layoutManager = GridLayoutManager(mActivity,4,RecyclerView.VERTICAL,false)
                                binding.todayTimeTableRecycler.layoutManager = layoutManager
                                binding.todayTimeTableRecycler.adapter = adapter
                            } else {
                                binding.timeTableNoData.txt.text = "No Tabletable Available"
                                binding.timeTableNoData.root.visibility = View.VISIBLE
                                binding.todayTimeTableRecycler.visibility = View.GONE
                            }
                        }
                        else{
                            binding.timeTableNoData.txt.text = "No Tabletable Available"
                            binding.timeTableNoData.root.visibility = View.VISIBLE
                            binding.todayTimeTableRecycler.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
        }
        else {
            binding.noDataTimeTable.root.visibility = View.GONE
            binding.timetableRecycler.visibility = View.VISIBLE
            val adapter = TimeTableAdapter(mActivity,true,result4,object : OnClickListener{
                override fun onClickItem(pos: Int) {
                    mActivity.binding.timeTableDialog.root.visibility = View.VISIBLE
                }
            })
            val layoutManager = GridLayoutManager(mActivity,4,RecyclerView.VERTICAL,false)
            binding.todayTimeTableRecycler.layoutManager = layoutManager
            binding.todayTimeTableRecycler.adapter = adapter
        }
    }
    fun getTimeTableDialog(pos: Int){
        mActivity.binding.timeTableDialog.root.visibility = View.VISIBLE
        mActivity.binding.timeTableDialog.close1.setOnClickListener{
            mActivity.binding.timeTableDialog.root.visibility = View.GONE
        }
    }
    fun getClsTestDialog(pos: Int){
        mActivity.binding.examDialog.close.setOnClickListener {
            mActivity.binding.examDialog.root.visibility = View.GONE
        }
        mActivity.binding.examDialog.next.setOnClickListener {
            mActivity.binding.examDialog.root.visibility = View.GONE
        }
        if(result3[pos].subject != null && result3[pos].subject!!.name != null){
            mActivity.binding.examDialog.subName.text = result3[pos].subject!!.name
        }
        else {
            mActivity.binding.examDialog.subName.text = "--/--"
        }
        if (result3[pos].classTest != null && result3[pos].classTest!!.title != null){
            mActivity.binding.examDialog.testTitle.text = result3[pos].classTest!!.title
            mActivity.binding.examDialog.chapter.text = result3[pos].classTest!!.title
        }
        else{
            mActivity.binding.examDialog.testTitle.text = "--/--"
        }
        if (result3[pos].classTest != null && result3[pos].classTest!!.description != null){
            mActivity.binding.examDialog.description.text = " * ${result3[pos].classTest!!.description}"
        }
        else{
            mActivity.binding.examDialog.description.text = "--/--"
        }
        if (result3[pos].classTest != null && result3[pos].classTest!!.totalMarks != null){
            mActivity.binding.examDialog.totalMarks.text = result3[pos].classTest!!.totalMarks!!.toString()
            if (result3[pos].classTest!!.createdBy != null){
                mActivity.binding.examDialog.studentName.text = "${result3[pos].classTest!!.createdBy!!.firstName} ${result3[pos].classTest!!.createdBy!!.lastName}"
            }
            else{
                mActivity.binding.examDialog.studentName.text = "--/--"
            }
        }
        else{
            mActivity.binding.examDialog.totalMarks.text = "--/--"
        }

        if (result3[pos].status == "completed"){
            if (result3[pos].scored_marks != null){
                mActivity.binding.examDialog.yourMarks.visibility = View.VISIBLE
                mActivity.binding.examDialog.scoredMarks.text = result3[pos].scored_marks!!.toString()
            }
            else{
                mActivity.binding.examDialog.scoredMarks.text = "--/--"
            }
        }
        else{
            mActivity.binding.examDialog.yourMarks.visibility = View.GONE
        }
        if (result3[pos].scheduledOn != null){
            mActivity.binding.examDialog.date.text = BaseUtils.getFormattedDate(result3[pos].scheduledOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            mActivity.binding.examDialog.date.text = "--/--"
        }
        if (result3[pos].remarks != null){
            mActivity.binding.examDialog.teacherRemarkContent.text = result3[pos].remarks
        }
        else{
            mActivity.binding.examDialog.teacherRemarkContent.text = "--/--"
        }
        if (result3[pos].attachment!! != null && result3[pos].attachment!!.isNotEmpty()){
            mActivity.binding.examDialog.examRecycle.visibility = View.VISIBLE
            val adapter = AttachAdapter(mActivity,result3[pos].attachment!!)
            val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
            mActivity.binding.examDialog.examRecycle.layoutManager = layoutManager
            mActivity.binding.examDialog.examRecycle.adapter = adapter
        }
        else {
            mActivity.binding.examDialog.examRecycle.visibility = View.GONE
        }
        if (result3[pos].status == "completed"){
            mActivity.binding.examDialog.examStatus.text = "Completed"
            mActivity.binding.examDialog.yourMarks.visibility = View.VISIBLE
            mActivity.binding.examDialog.next.text = "Okay"
            if (result3[pos].scored_marks != null){
                mActivity.binding.examDialog.scoredMarks.text = result3[pos].scored_marks!!.toString()
            }
            else{
                mActivity.binding.examDialog.scoredMarks.text = "--/--"
            }
            mActivity.binding.examDialog.answerSheet.visibility = View.VISIBLE
            mActivity.binding.examDialog.tRemarks.visibility = View.VISIBLE
            mActivity.binding.examDialog.credits.text = "+${result3[pos].credits} Points"
            UiUtils.textViewTextColor(mActivity.binding.examDialog.credits, "#348F23",null)
            UiUtils.textviewCustomDrawable(mActivity.binding.examDialog.examStatus, R.drawable.border_curve_16dp)
            UiUtils.textViewBgTint(mActivity.binding.examDialog.examStatus, "#E6FFE2",null)
            UiUtils.textViewTextColor(mActivity.binding.examDialog.examStatus,"#348F23",null)
        }
        else if (result3[pos].status == "today"){
            mActivity.binding.examDialog.examStatus.text = "Today"
            mActivity.binding.examDialog.yourMarks.visibility = View.GONE
            mActivity.binding.examDialog.next.text = "Okay"
            mActivity.binding.examDialog.answerSheet.visibility = View.GONE
            mActivity.binding.examDialog.tRemarks.visibility = View.GONE
            UiUtils.textviewCustomDrawable(mActivity.binding.examDialog.examStatus, R.drawable.border_curve_16dp)
            UiUtils.textViewBgTint(mActivity.binding.examDialog.examStatus, "#61A4F3",null)
            UiUtils.textViewTextColor(mActivity.binding.examDialog.examStatus,"#DFEDFD",null)
        }
        else if(result3[pos].status == "ongoing"){
            mActivity.binding.examDialog.examStatus.text = "On Going"
            mActivity.binding.examDialog.yourMarks.visibility = View.GONE
            mActivity.binding.examDialog.next.text = "Okay , i’ll Prepare for it"
            mActivity.binding.examDialog.answerSheet.visibility = View.GONE
            mActivity.binding.examDialog.tRemarks.visibility = View.GONE
            UiUtils.textviewCustomDrawable(mActivity.binding.examDialog.examStatus, R.drawable.border_curve_16dp)
            UiUtils.textViewTextColor(mActivity.binding.examDialog.examStatus,"#61A4F3", null)
            UiUtils.textViewBgTint(mActivity.binding.examDialog.examStatus,"#DFEDFD", null)
        }
        else if(result3[pos].status == "pending"){
            mActivity.binding.examDialog.examStatus.text = "Upcoming"
            mActivity.binding.examDialog.yourMarks.visibility = View.GONE
            mActivity.binding.examDialog.next.text = "Okay , i’ll Prepare for it"
            mActivity.binding.examDialog.answerSheet.visibility = View.GONE
            mActivity.binding.examDialog.tRemarks.visibility = View.GONE
            mActivity.binding.examDialog.credits.visibility = View.GONE
            UiUtils.textviewCustomDrawable(mActivity.binding.examDialog.examStatus, R.drawable.border_curve_16dp)
            UiUtils.textViewBgTint(mActivity.binding.examDialog.examStatus,"#EDF3FF", null)
            UiUtils.textViewTextColor(mActivity.binding.examDialog.examStatus,"#3F8BFB", null)
        }
        mActivity.binding.examDialog.root.visibility = View.VISIBLE

    }
    fun getExam(pos : Int){

        /*if (result5[pos].details != null){
            binding.dialogExam.subName.text = result5!![pos].details!!
        }
        else{
            binding.dialogExam.subName.text = "--/--"
        }*/
        /*if (result6[pos].date != null){
            binding.dialogExam.date.text = BaseUtils.getFormattedDate(result6[pos].date!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            binding.dialogExam.date.text = "--/--"
        }
        if (result6[pos].duration != null){
            binding.dialogExam.duration.text = result6[pos].duration
        }
        else{
            binding.dialogExam.duration.text = "--/--"
        }*/
    }
    fun getProjectResult(pos : Int){
        clickedDialog = "project"
        mActivity.binding.dialogProject.close.setOnClickListener {
            mActivity.binding.dialogProject.root.visibility = View.GONE
            projectAttach.clear()
        }

        mActivity.binding.dialogProject.cancel.setOnClickListener {
            mActivity.binding.dialogProject.root.visibility = View.GONE
            projectAttach.clear()
        }
        mActivity.binding.dialogProject.done.setOnClickListener {
            mActivity.binding.dialogProject.root.visibility = View.GONE
            projectAttach.clear()
        }
        mActivity.binding.dialogProject.attach.setOnClickListener {
            openDocList()
        }
        mActivity.binding.dialogProject.upload.setOnClickListener {
            if (projectAttach.isNotEmpty()){
                DialogUtils.showLoader(mActivity)
                val projectId = result2[pos].project!!._id!!
                if (projectId != null){
                    ApiConnection.getInstance().projectStsUpdate(mActivity,projectId,projectAttach).observe(mActivity){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg,binding.root,true)
                                    projectAttach.clear()
                                    mActivity.binding.dialogProject.root.visibility = View.GONE
                                    getStudentProject()
                                }
                                else {
                                    UiUtils.showSnack(it.msg,binding.root,false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("project id is not present",binding.root,false)
                }
            }
            else {
                UiUtils.showSnack("Please upload your project",binding.root,false)
            }
        }

        if (result2[pos].subject != null && result2[pos].subject!!.name != null ){
            mActivity.binding.dialogProject.subject.text = result2[pos].subject!!.name!!
        }
        else {
            mActivity.binding.dialogProject.subject.text = "--/--"
        }
        if (result2[pos].project != null){
            mActivity.binding.dialogProject.projectTitle.text = result2[pos].project!!.title
            mActivity.binding.dialogProject.tMarks.text = " / ${result2[pos].project!!.totalMarks}"
            mActivity.binding.dialogProject.desc.setContent(result2[pos].project!!.description)
            if (result2[pos].project!!.createdBy != null){
                mActivity.binding.dialogProject.teacher.text = result2[pos].project!!.createdBy!!.firstName +" "+ result2[pos].project!!.createdBy!!.lastName
            }
            else {
                mActivity.binding.dialogProject.teacher.text = "--/--"
            }
        }
        else{
            mActivity.binding.dialogProject.desc.text = "--/--"
            mActivity.binding.dialogProject.projectTitle.text = "--/--"
            mActivity.binding.dialogProject.tMarks.text = "--/--"
            mActivity.binding.dialogProject.teacher.text = "--/--"
        }

        val dueDate = BaseUtils.getFormattedDate(result2[pos].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        val submitDate = BaseUtils.getFormattedDate(result2[pos].submittedOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        if (result2[pos].status == "completed"){
            mActivity.binding.dialogProject.uploadLay.visibility = View.GONE
            mActivity.binding.dialogProject.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.attachLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.status.text = "Submitted"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogProject.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogProject.status,"#e6ffe7", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogProject.status,"#32B138", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogProject.points,"#32B138", null)
            mActivity.binding.dialogProject.submitOn.text = "Submitted On : "
            mActivity.binding.dialogProject.submitdate.text = submitDate
            if (result2[pos].submittedOnTime){
                mActivity.binding.dialogProject.note.text = "Good job! Your Project has been submitted successfully, You’ve gained points for your submission!"
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogProject.noteLay,"#EFFFF0",null)
            }
            else {
                mActivity.binding.dialogProject.note.text = "Your Project was submitted late. Great effort! Aim to submit on time to maximize your points."
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogProject.noteLay,"#fafce3",null)
            }

            if (result2[pos].credits != null && result2[pos].credits!!.isNotEmpty() && result2[pos].credits!!.toInt() > 0){
                mActivity.binding.dialogProject.points.text = "+${result2[pos].credits} Points"
                UiUtils.textViewTextColor(mActivity.binding.dialogProject.points,"#32B138", null)
            }
            else {
                mActivity.binding.dialogProject.points.text = "0 Point"
                UiUtils.textViewTextColor(mActivity.binding.dialogProject.points,"#EA5455", null)
            }
            if (result2[pos].markStatus == "pending"){
                mActivity.binding.dialogProject.remarks.text = "Not yet updated"
                mActivity.binding.dialogHomework.remarkLay.visibility = View.GONE
                UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#333333",null) //orange
            }
            else {
                mActivity.binding.dialogProject.sMarks.text = result2[pos].scored_marks
                when(result2[pos].remarks){
                    "verygood" -> {
                        mActivity.binding.dialogProject.remarks.text = "Outstanding performance! You’re doing great."
//                        UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#32B138",null)//green
                        UiUtils.textViewGradient(mActivity.binding.dialogProject.remarks,"#32B138","#138f18")//green
                    }
                    "good" -> {
                        mActivity.binding.dialogProject.remarks.text = "Great job! Keep improving steadily."
                        UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#3F8BFB",null) //blue
                    }
                    "poor" -> {
                        mActivity.binding.dialogProject.remarks.text = "Keep trying; you’ll get there soon."
                        UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#F69300",null) //orange
                    }
                    "need_attention" -> {
                        mActivity.binding.dialogProject.remarks.text = "Work harder; success is within reach."
                        UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#F69300",null) //orange
                    }
                }
            }

            if (result2[pos].attachment != null && result2[pos].attachment!!.isNotEmpty()){
                mActivity.binding.dialogProject.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(mActivity,result2[pos].attachment!!)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
                mActivity.binding.dialogProject.attachRecycler.layoutManager = layoutManager
                mActivity.binding.dialogProject.attachRecycler.adapter = adapter
            }
            else {
                mActivity.binding.dialogProject.attachRecycler.visibility = View.GONE
            }
        }
        else if (result2[pos].status == "overdue"){
            mActivity.binding.dialogProject.uploadLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.attachLay.visibility = View.GONE
            mActivity.binding.dialogProject.resultLay.visibility = View.GONE
            mActivity.binding.dialogProject.status.text = "Not Completed"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogProject.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogProject.status,"#fce6e6", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogProject.status,"#EA5455", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogProject.points,"#EA5455", null)
            mActivity.binding.dialogProject.submitOn.text = "Last Date : "
            mActivity.binding.dialogProject.submitdate.text = dueDate
            mActivity.binding.dialogProject.note.text = "This Project was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"

        }
        else if (result2[pos].status == "pending"){
            mActivity.binding.dialogProject.uploadLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.attachLay.visibility = View.GONE
            mActivity.binding.dialogProject.points.visibility = View.GONE
            mActivity.binding.dialogProject.resultLay.visibility = View.GONE
            mActivity.binding.dialogProject.status.text = "Ongoing"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogProject.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogProject.status,"#fff2d9", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogProject.status,"#F69300", null)
            mActivity.binding.dialogProject.submitOn.text = "Last Date : "
            mActivity.binding.dialogProject.submitdate.text = dueDate
            mActivity.binding.dialogProject.note.text = "Don't forget! You need to complete and submit before the Last date to earn points, if you miss the deadline, you won't receive any points."
            UiUtils.linearLayoutBgTint(mActivity.binding.dialogProject.noteLay,"#FFF2F2",null)
        }

        UiUtils.animation(mActivity,mActivity.binding.dialogProject.cardView,R.anim.slide_in_from_bottom,true)
        mActivity.binding.dialogProject.root.visibility = View.VISIBLE
    }

/*    fun getAssignmentStatus(pos: Int){
        clickedDialog = "assignment"
        mActivity.binding.dialogAssignment.cancel.setOnClickListener {
            mActivity.binding.dialogAssignment.root.visibility = View.GONE
        }
        mActivity.binding.dialogAssignment.doneAssingment.setOnClickListener {
            mActivity.binding.dialogAssignment.root.visibility = View.GONE
        }
        mActivity.binding.dialogAssignment.close.setOnClickListener {
            mActivity.binding.dialogAssignment.root.visibility = View.GONE
        }
        mActivity.binding.dialogAssignment.attach.setOnClickListener {
            openDocList()
        }
        mActivity.binding.dialogAssignment.makeasdone.setOnClickListener {
            if (assignmentAttach.isNotEmpty()){
                DialogUtils.showLoader(mActivity)
                val assignmentId = result[pos].assignment!!._id
                if (assignmentId != null){
                    ApiConnection.getInstance().assignmentStsUpdate(mActivity,assignmentId,assignmentAttach).observe(mActivity){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg,binding.root,true)
                                    mActivity.binding.dialogAssignment.root.visibility = View.GONE
                                    getStudentAssignment()
                                } else {
                                    UiUtils.showSnack(it.msg, binding.root, false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Assignment id is not present",binding.root,false)
                }
            }
            else {
                UiUtils.showSnack("Please upload your Assignment",binding.root,false)
            }
        }

        if (result[pos].teacher != null && result[pos].teacher!!.firstName != null && result[pos].teacher!!.lastName != null){
            mActivity.binding.dialogAssignment.teacher.text = "Teacher : ${result[pos].teacher!!.firstName} ${result[pos].teacher!!.lastName}"
        }
        else{
            mActivity.binding.dialogAssignment.teacher.text = "--/--"
        }
        if (result[pos].subject != null && result[pos].subject!!.name!!.isNotEmpty()){
            mActivity.binding.dialogAssignment.subject.text = "Subject : ${result[pos].subject!!.name}"
        }
        else{
            mActivity.binding.dialogAssignment.subject.text = "--/--"
        }
        if (result[pos].assignment != null && result[pos].assignment!!.title != null && result[pos].assignment!!.description != null){
            mActivity.binding.dialogAssignment.assTitle.text = result[pos].assignment!!.title
            mActivity.binding.dialogAssignment.tMarks.text = " / ${result[pos].assignment!!.totalMarks}"
            mActivity.binding.dialogAssignment.assdsc.setContent(result[pos].assignment!!.description)
        }
        else{
            mActivity.binding.dialogAssignment.assTitle.text = "--/--"
            mActivity.binding.dialogAssignment.assdsc.text = "--/--"
        }
        if (result[pos].createdAt != null){
            mActivity.binding.dialogAssignment.givendate.text = BaseUtils.getFormattedDate(result[pos].createdAt!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            mActivity.binding.dialogAssignment.givendate.text = "--/--"
        }
        var dueDate = BaseUtils.getFormattedDate(result[pos].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        var submitDate = BaseUtils.getFormattedDate(result[pos].submittedOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)

        if (result[pos].status == "completed"){
            mActivity.binding.dialogAssignment.resultLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.uploadLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.attachLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.status.text = "Submitted"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogAssignment.status,"#e6ffe7", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.status,"#32B138", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.points,"#32B138", null)
            mActivity.binding.dialogAssignment.submitOn.text = "Submitted On : "
            mActivity.binding.dialogAssignment.submitdate.text = submitDate
            if (result[pos].submittedOnTime){
                mActivity.binding.dialogAssignment.note.text = "Good job! Your Assignment has been submitted successfully, You’ve gained points for your submission!"
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogAssignment.noteLay,"#EFFFF0",null)
            }
            else {
                mActivity.binding.dialogAssignment.note.text = "Your Assignment was submitted late. Great effort! Aim to submit on time to maximize your points."
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogAssignment.noteLay,"#fafce3",null)
            }

            if (result[pos].credits != null && result[pos].credits!!.isNotEmpty() && result[pos].credits!!.toInt() > 0){
                mActivity.binding.dialogAssignment.points.text = "+${result[pos].credits} Points"
                UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.points,"#32B138", null)
            }
            else {
                mActivity.binding.dialogAssignment.points.text = "0 Point"
                UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.points,"#EA5455", null)
            }
            if (result[pos].markStatus == "pending"){
                mActivity.binding.dialogAssignment.remarks.text = "Not yet updated"
                UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#333333",null) //orange
            }
            else {
                mActivity.binding.dialogAssignment.sMarks.text = result[pos].scored_marks
                when(result[pos].remarks){
                    "verygood" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Outstanding performance! You’re doing great."
//                        UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#32B138",null)//green
                        UiUtils.textViewGradient(mActivity.binding.dialogAssignment.remarks,"#32B138","#138f18")//green
                    }
                    "good" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Great job! Keep improving steadily."
                        UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#3F8BFB",null) //blue
                    }
                    "poor" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Keep trying; you’ll get there soon."
                        UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#F69300",null) //orange
                    }
                    "need_attention" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Work harder; success is within reach."
                        UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#F69300",null) //orange
                    }
                }
            }

            if (result[pos].attachment!! != null && result[pos].attachment!!.isNotEmpty()){
                mActivity.binding.dialogAssignment.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(mActivity,result[pos].attachment!!)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
                mActivity.binding.dialogAssignment.attachRecycler.layoutManager = layoutManager
                mActivity.binding.dialogAssignment.attachRecycler.adapter = adapter
            }
            else {
                mActivity.binding.dialogAssignment.attachRecycler.visibility = View.GONE
            }
        }
        else if (result[pos].status == "overdue"){
            mActivity.binding.dialogAssignment.resultLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.uploadLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.attachLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.status.text = "Not Completed"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogAssignment.status,"#fce6e6", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.status,"#EA5455", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.points,"#EA5455", null)
            mActivity.binding.dialogAssignment.submitOn.text = "Last Date : "
            mActivity.binding.dialogAssignment.submitdate.text = dueDate
            mActivity.binding.dialogAssignment.note.text = "This Assingment was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"

        }
        else if (result[pos].status == "pending"){
            mActivity.binding.dialogAssignment.resultLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.uploadLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.attachLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.points.visibility = View.GONE
            mActivity.binding.dialogAssignment.status.text = "Ongoing"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogAssignment.status,"#fff2d9", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.status,"#F69300", null)
            mActivity.binding.dialogAssignment.submitOn.text = "Last Date : "
            mActivity.binding.dialogAssignment.submitdate.text = dueDate
            mActivity.binding.dialogAssignment.note.text = "Don't forget! You need to complete and submit before the Last date to earn points, if you miss the deadline, you won't receive any points."
            UiUtils.linearLayoutBgTint(mActivity.binding.dialogAssignment.noteLay,"#FFF2F2",null)
        }

        UiUtils.animation(mActivity,mActivity.binding.dialogAssignment.topLay,R.anim.slide_in_from_bottom,true)
        mActivity.binding.dialogAssignment.root.visibility = View.VISIBLE
    }*/

    fun getHomework(pos: Int){
        clickedDialog = "homework"
        mActivity.binding.dialogHomework.close.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
            homeworkAttach.clear()
            mActivity.binding.dialogHomework.attach.text = ""
        }
        mActivity.binding.dialogHomework.cancel.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
            homeworkAttach.clear()
            mActivity.binding.dialogHomework.attach.text = ""
        }
        mActivity.binding.dialogHomework.doneHw.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
            homeworkAttach.clear()
            mActivity.binding.dialogHomework.attach.text = ""
        }
        mActivity.binding.dialogHomework.attach.setOnClickListener {
            openDocList()
        }
        mActivity.binding.dialogHomework.makeasdone.setOnClickListener {
            if (homeworkAttach.isNotEmpty()){
                DialogUtils.showLoader(mActivity)
                val homeworkId = result1[pos].homework!!._id
                if (homeworkId != null){
                    ApiConnection.getInstance().homeStsUpdate(mActivity,homeworkId,homeworkAttach).observe(mActivity){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg,binding.root,true)
                                    mActivity.binding.dialogHomework.root.visibility = View.GONE
                                    studentHomework()
                                    homeworkAttach.clear()
                                    mActivity.binding.dialogHomework.attach.text = ""
                                }
                                else {
                                    UiUtils.showSnack(it.msg,binding.root,false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Homework id is not present",binding.root,false)
                }
            }
            else {
                UiUtils.showSnack("Please upload your Homework",binding.root,false)
            }
        }

        mActivity.binding.dialogHomework.noteCheck.setOnClickListener {
            val homeworkId = result1[pos].homework!!._id
            if (homeworkId != null){
                AlertDialog.Builder(mActivity)
                    .setTitle("Undo Homework")
                    .setMessage("This will permanently delete your homework progress.")
                    .setPositiveButton("Yes Undo") { dialog, _ ->
                        dialog.dismiss()
                        DialogUtils.showLoader(mActivity)
                        ApiConnection.getInstance().undoHomework(mActivity,homeworkId).observe(mActivity){
                            it.let {
                                DialogUtils.dismissLoader()
                                it.success.let { success->
                                    if (success){
                                        UiUtils.imageviewDrawable(mActivity.binding.dialogHomework.noteCheck,R.drawable.rectangle_checkbox)
                                        UiUtils.showSnack(it.msg,binding.root,true)
                                        mActivity.binding.dialogHomework.root.visibility = View.GONE
                                        studentHomework()
                                    }
                                    else {
                                        UiUtils.showSnack(it.msg,binding.root,false)
                                    }
                                }
                            }
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            else {
                UiUtils.showSnack("Homework id is not present",binding.root,false)
            }

        }

        mActivity.binding.dialogHomework.points.text = "+${result1[pos].credits} Points"
        if (result1[pos].subject != null){
            mActivity.binding.dialogHomework.subject.text = "Subject : ${result1[pos].subject!!.name}"
        }
        else {
            mActivity.binding.dialogHomework.subject.text = "Subject : --/--"
        }

        if (result1[pos].homework != null){
            mActivity.binding.dialogHomework.que.text = result1[pos].homework!!.title
            mActivity.binding.dialogHomework.desc.text = result1[pos].homework!!.description
            if (result1[pos].homework!!.createdBy != null){
                mActivity.binding.dialogHomework.teacher.text = "Teacher : ${result1[pos].homework!!.createdBy!!.firstName +" "+ result1[pos].homework!!.createdBy!!.lastName}"
            }
            else {
                mActivity.binding.dialogHomework.teacher.text = "Teacher : --/--"
            }
        }
        else {
            mActivity.binding.dialogHomework.que.text = "--/--"
            mActivity.binding.dialogHomework.desc.text = "--/--"
        }
        val givenDate = BaseUtils.getFormattedDate(result1[pos].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        val dueDate = BaseUtils.getFormattedDate(result1[pos].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        if (result1[pos].status == "completed"){
            mActivity.binding.dialogHomework.uploadLay.visibility = View.GONE
            mActivity.binding.dialogHomework.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.attachLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.doneHw.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.remarkLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.status.text = "Submitted"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogHomework.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogHomework.status,"#e6ffe7", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogHomework.status,"#32B138", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#32B138", null)
            val submittedOn = BaseUtils.getFormattedDate(result1[pos].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            mActivity.binding.dialogHomework.submitOn.text = "Submitted On : "
            mActivity.binding.dialogHomework.submitDate.text = submittedOn
            mActivity.binding.dialogHomework.givenDate.text = givenDate

            if (result1[pos].credits != null && result1[pos].credits!!.isNotEmpty() && result1[pos].credits!!.toInt() > 0){
                mActivity.binding.dialogHomework.points.text = "+${result1[pos].credits} Points"
                UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#32B138", null)
            }
            else {
                mActivity.binding.dialogHomework.points.text = "0 Point"
                UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#EA5455", null)
            }
            if (result1[pos].markStatus == "pending"){
                mActivity.binding.dialogHomework.noteCheck.visibility = View.VISIBLE
                UiUtils.imageviewDrawable(mActivity.binding.dialogHomework.noteCheck,R.drawable.green_tick)
                mActivity.binding.dialogHomework.remarkLay.visibility = View.GONE
                mActivity.binding.dialogHomework.note.text = "You’ve marked this homework as completed.If you tapped it by mistake, just uncheck and click Done again to update."
            }
            else {
                mActivity.binding.dialogHomework.remarkLay.visibility = View.VISIBLE
                mActivity.binding.dialogHomework.noteCheck.visibility = View.GONE
                if (result1[pos].submittedOnTime){
                    mActivity.binding.dialogHomework.note.text = "Good job! Your Homework has been submitted successfully, You’ve gained points for your submission!"
                    UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#EFFFF0",null)
                }
                else {
                    mActivity.binding.dialogHomework.note.text = "Your Homework was submitted late. Great effort! Aim to submit on time to maximize your points."
                    UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#fafce3",null)
                }
                when(result1[pos].remarks){
                    "verygood" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Outstanding performance! You’re doing great."
                        UiUtils.textViewGradient(mActivity.binding.dialogHomework.remarks,"#32B138","#138f18")//green
                    }
                    "good" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Great job! Keep improving steadily."
                        UiUtils.textViewTextColor(mActivity.binding.dialogHomework.remarks,"#3F8BFB",null) //blue
                    }
                    "poor" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Keep trying; you’ll get there soon."
                        UiUtils.textViewTextColor(mActivity.binding.dialogHomework.remarks,"#F69300",null) //orange
                    }
                    "need_attention" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Work harder; success is within reach."
                        UiUtils.textViewTextColor(mActivity.binding.dialogHomework.remarks,"#F69300",null) //orange
                    }
                }
            }

            if (result1[pos].attachment!! != null && result1[pos].attachment!!.isNotEmpty()){
                mActivity.binding.dialogHomework.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(mActivity,result1[pos].attachment!!)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
                mActivity.binding.dialogHomework.attachRecycler.layoutManager = layoutManager
                mActivity.binding.dialogHomework.attachRecycler.adapter = adapter
            }
            else {
                mActivity.binding.dialogHomework.attachRecycler.visibility = View.GONE
            }
        }
        else if (result1[pos].status == "overdue"){
            mActivity.binding.dialogHomework.uploadLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.attachLay.visibility = View.GONE
            mActivity.binding.dialogHomework.doneHw.visibility = View.GONE
            mActivity.binding.dialogHomework.remarkLay.visibility = View.GONE
            mActivity.binding.dialogHomework.noteCheck.visibility = View.GONE
            mActivity.binding.dialogHomework.status.text = "Not Completed"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogHomework.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogHomework.status,"#fce6e6", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogHomework.status,"#EA5455", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#EA5455", null)
            mActivity.binding.dialogHomework.submitOn.text = "Last Date : "
            mActivity.binding.dialogHomework.submitDate.text = dueDate
            mActivity.binding.dialogHomework.givenDate.text = givenDate
            mActivity.binding.dialogHomework.note.text = "This Homework was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"
        }
        else if (result1[pos].status == "pending"){
            mActivity.binding.dialogHomework.uploadLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.attachLay.visibility = View.GONE
            mActivity.binding.dialogHomework.points.visibility = View.GONE
            mActivity.binding.dialogHomework.doneHw.visibility = View.GONE
            mActivity.binding.dialogHomework.remarkLay.visibility = View.GONE
            mActivity.binding.dialogHomework.noteCheck.visibility = View.GONE
            mActivity.binding.dialogHomework.status.text = "Ongoing"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogHomework.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogHomework.status,"#fff2d9", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogHomework.status,"#F69300", null)
            mActivity.binding.dialogHomework.submitOn.text = "Last Date : "
            mActivity.binding.dialogHomework.submitDate.text = dueDate
            mActivity.binding.dialogHomework.givenDate.text = givenDate
            mActivity.binding.dialogHomework.note.text = "Mark as Done if you’ve completed this homework. This is just for you to remember what you’ve finished!"
            UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#FFF2F2",null)
        }
        UiUtils.animation(mActivity,mActivity.binding.dialogHomework.topLay,R.anim.slide_in_from_bottom,true)
        mActivity.binding.dialogHomework.root.visibility = View.VISIBLE
    }

    fun openDocList() {
        if (BaseUtils.isPermissionsEnabled(mActivity, Constants.IntentKeys.STORAGE)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
            BaseUtils.permissionsEnableRequest(mActivity, Constants.IntentKeys.STORAGE)
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
                            val documentFile = DocumentFile.fromSingleUri(mActivity, uri)
                            val fileInputStream = mActivity.contentResolver.openInputStream(uri)
                            val mimeType = mActivity.contentResolver.getType(uri)
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
                        val documentFile = DocumentFile.fromSingleUri(mActivity, uri)
                        val fileInputStream = mActivity.contentResolver.openInputStream(uri)
                        val mimeType = mActivity.contentResolver.getType(uri)
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
//                                urlName.add(json)
                                if (clickedDialog == "homework"){
                                    mActivity.binding.dialogHomework.attach.text = documentFile?.name
                                }
                                else if (clickedDialog == "assignment"){
                                    mActivity.binding.dialogAssignment.attach.text = documentFile?.name
                                }
                                else if (clickedDialog == "project"){
                                    mActivity.binding.dialogProject.attach.text = documentFile?.name
                                }
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

    fun upload(filepart:MultipartBody.Part){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().uploadFile(mActivity, filepart).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        val url = it.result[0].location!!
                        if (clickedDialog == "homework"){
                            homeworkAttach.add(url)
                        }
                        else if (clickedDialog == "assignment"){
                            assignmentAttach.add(url)
                        }
                        else if (clickedDialog == "project"){
                            projectAttach.add(url)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }

    fun getStudentClassTestProgress(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentClassTestProgress(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            resultClsTest = it.result
                            clsTestBarChart(resultClsTest)
                            clsTestVerticalBarChart(resultClsTest)
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

    fun getStudentExamProgress(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getStudentExamProgress(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null ) {
                            resultExam = it.result!!
                            if (isFilter){
                                isFilter = false
                                examProgressBarChart1(resultExam)
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
    }

    private fun getStudentAttendance(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getStudentAttendance(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        resultAtt = it.result!!
                        Log.d("hgsd","result = ${result}")
                        if (it.result != null ) {
                            loadProgressBar(resultAtt)
                            if (resultAtt!!.streaks != null){
                                binding.streaks.text = resultAtt.streaks.toString()
                            }
                            else{
                                binding.streaks.text = "--/--"
                            }
                            if (resultAtt.progress!!.presentCount != null){
                                binding.presentdays.text = resultAtt!!.progress!!.presentCount.toString()
                            }
                            else{
                                binding.presentdays.text = "--/--"
                            }
                            if (resultAtt.progress!!.absentCount != null){
                                binding.absentDays.text = resultAtt.progress!!.absentCount.toString()
                            }
                            else{
                                binding.absentDays.text = "--/--"
                            }
                            if (resultAtt.progress!!.halfDayCount != null){
                                binding.halfDays.text = resultAtt.progress!!.halfDayCount.toString()
                            }
                            else{
                                binding.halfDays.text = "--/--"
                            }
                            if (resultAtt.progress!!.presentPercentage != null){
                                binding.percent.text = "Overall\n${resultAtt.progress!!.presentPercentage} %"
                            }
                            else{
                                binding.percent.text = "--/--"
                            }
                            if (resultAtt.progress!!.total != null){
                                binding.totalWarkingDays.text = resultAtt.progress!!.total.toString()
                            }
                            else{
                                binding.totalWarkingDays.text = "--/--"
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
    }

    private fun loadProgressBar(result: GetStudentAttenDanceResponse.Result) {
        val present = result.progress?.presentPercentage?.toFloat() ?: 0f
        val absent = result.progress?.absentPercentage?.toFloat() ?: 0f
        val halfDay = result.progress?.halfDayPercentage?.toFloat() ?: 0f

        val entries = arrayListOf(
            PieEntry(present, "Present"),
            PieEntry(halfDay, "Half-Day"),
            PieEntry(absent, "Absent")
        )

        val colors = listOf(
            Color.parseColor("#FFC107"), // Yellow (for Present)
            Color.parseColor("#AB47BC"), // Purple (for Half-Day)
            Color.parseColor("#FF4081")  // Pink (for Absent)
        )

        val dataSet = PieDataSet(entries, "").apply {
            setDrawValues(false)
            sliceSpace = 3f // Reduced space between segments for better visibility
            this.colors = colors // Assign colors to entries
        }

        val pieData = PieData(dataSet)

        attenDanceProgress.apply {
            data = pieData
            description.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            setDrawHoleEnabled(true)
            holeRadius = 120f // Adjusted for better proportion within 300dp height
            setHoleColor(Color.BLACK) // Black background in the center
            transparentCircleRadius = 0f
            legend.isEnabled = false
            isRotationEnabled = false
            setTouchEnabled(false)

            setMaxAngle(360f) // Full circle
            rotationAngle = 270f // Start from the top

            // Customize the center text (remove since TextView in layout will handle this)
            setCenterText("") // Let the TextView in the layout display the percentage
            //  setCenterTextColor(Color.WHITE)
            //  setCenterTextSize(0f)

            // Add padding to ensure the chart doesn't touch the edges
            setExtraOffsets(50f, 50f, 50f, 50f)
            dataSet.selectionShift = 0f // No shift on selection

            renderer = GradientPieChartRenderer(
                this, animator, viewPortHandler,
                colors
            )

            animateY(1000, Easing.EaseInOutCubic)
            invalidate()
        }
    }

    /*
        private fun loadProgressBar(result: GetStudentAttenDanceResponse.Result) {
            val present = result.progress?.presentPercentage?.toFloat() ?: 0f
            val absent = result.progress?.absentPercentage?.toFloat() ?: 0f

            val entries = arrayListOf(
                PieEntry(present, "Present"),
                PieEntry(absent, "Absent")
            )

            val presentColor = Color.parseColor("#5E4AFF") // Indigo-violet (for Present)
            val absentColor = Color.parseColor("#FF7A4A")  // Warm orange (for Absent)

            val dataSet = PieDataSet(entries, "").apply {
                setDrawValues(false)
                sliceSpace = 2f
            }

            val pieData = PieData(dataSet)

            attenDanceProgress.apply {
                data = pieData
                description.isEnabled = false
                setUsePercentValues(true)
                setDrawEntryLabels(false)
                setDrawHoleEnabled(true)
                holeRadius = 60f
                transparentCircleRadius = 0f
                legend.isEnabled = false
                isRotationEnabled = false
                setTouchEnabled(false)

                setMaxAngle(360f)         // Half circle
                rotationAngle = 270f      // Rotate to right side

                renderer = GradientPieChartRenderer(
                    this, animator, viewPortHandler,
                    listOf(presentColor, absentColor)
                )

                animateY(1000, Easing.EaseInOutCubic)
                invalidate()
            }
        }
    */

/*
    private fun loadProgressBar(result: GetStudentAttenDanceResponse.Result) {
        val present = result.progress?.presentPercentage?.toFloat() ?: 0f
        val absent = result.progress?.absentPercentage?.toFloat() ?: 0f

        val entries = arrayListOf(
            PieEntry(present, "Present"),
            PieEntry(absent, "Absent")
        )

        val presentColor = Color.parseColor("#5E4AFF") // Indigo-violet (for Present)
        val absentColor = Color.parseColor("#FF7A4A")  // Warm orange (for Absent)

        val dataSet = PieDataSet(entries, "").apply {
            setDrawValues(false)
            sliceSpace = 2f
        }

        val pieData = PieData(dataSet)

        attenDanceProgress.apply {
            data = pieData
            description.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            setDrawHoleEnabled(true)
            holeRadius = 60f
            transparentCircleRadius = 0f
            legend.isEnabled = false
            isRotationEnabled = false
            setTouchEnabled(false)

            setMaxAngle(180f)         // Half circle
            rotationAngle = 270f      // Rotate to right side

            renderer = GradientPieChartRenderer(
                this, animator, viewPortHandler,
                listOf(presentColor, absentColor)
            )

            animateY(1000, Easing.EaseInOutCubic)
            invalidate()
        }
    }
*/

    private fun clsTestBarChart(result: ArrayList<GetStudentClassTestProgress.Result>) {
        barChart1.clear()
        barChart1.marker = null

        val subjectColorMap = mapOf(
            "Computer" to "#3B3BBF",
            "Maths" to "#F85F73",
            "Science" to "#F9B233",
            "English" to "#9C27B0",
            "Tamil" to "#4CAF50",
            "Social Science" to "#2196F3",
            "Telugu" to "#E91E63",
            "Hindi" to "#673AB7",
            "GK" to "#FF9800",
            "Manul Testing" to "#607D8B"
        )

        // Prepare Bar Entries and Colors
        val entries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloatOrNull() ?: 0f
            entries.add(BarEntry(index.toFloat(), percentage))
            val subject = item.subject?.trim() ?: ""
            val colorHex = subjectColorMap[subject] ?: "#607D8B"
            colors.add(Color.parseColor(colorHex))
        }

        val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
        val adapter = GetSubjectNameColorAdapter(mActivity, result, subjectColorMap)
        binding.subjectColorRecyclerView.layoutManager = linearLayoutManager
        binding.subjectColorRecyclerView.adapter = adapter

        val dataSet = BarDataSet(entries, "").apply {
            this.colors = colors
            setDrawValues(false)
        }

        barChart1.renderer = TopRendarCurveBarChartColors(
            barChart1,
            barChart1.animator,
            barChart1.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.4f

        barChart1.apply {
            data = barData

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                setDrawLabels(false)
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 100f
                granularity = 20f
                setLabelCount(5, true)
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
                }
                enableGridDashedLine(10f, 10f, 0f)
                setDrawGridLines(true)
                gridColor = Color.GRAY
            }

            axisRight.isEnabled = false

            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDragEnabled(true)

            marker = CustomMarkerView2(mActivity, result)

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val index = e.x.toInt()
                        if (index in result.indices) {
                            val intent = Intent(mActivity, SubjectWiseProgressActivity::class.java)
                            mActivity.startActivity(intent)
                        }
                    }
                }

                override fun onNothingSelected() {
                    // Optional: Handle case when nothing is selected
                }
            })

            setExtraOffsets(10f, 10f, 10f, 10f)

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)
            invalidate()
        }
    }

    private fun clsTestVerticalBarChart(result: ArrayList<GetStudentClassTestProgress.Result>) {
        barCharts.clear()
        barCharts.marker = null

        val colorPalette = listOf(
            Color.parseColor("#AEB8FE"), // Soft blue-violet
            Color.parseColor("#FFB3BA"), // Light rose pink
            Color.parseColor("#FFE0A3"), // Pale amber
            Color.parseColor("#D1B3FF"), // Light purple
            Color.parseColor("#B2F2BB"), // Mint green
            Color.parseColor("#B3E5FC"), // Soft sky blue
            Color.parseColor("#FFD180"), // Peach
            Color.parseColor("#F8BBD0"), // Baby pink
            Color.parseColor("#D1C4E9"), // Lilac
            Color.parseColor("#CFD8DC")  // Light gray-blue
        )

        // Filter out zero or invalid percentages and convert to ArrayList
        val validResults = result.filter { it.percentage?.toFloatOrNull() ?: 0f > 0f }
            .toCollection(ArrayList())

        val segmentPercentages = FloatArray(validResults.size) { i ->
            validResults[i].percentage?.toFloatOrNull() ?: 0f
        }

        val entries = ArrayList<BarEntry>()
        if (validResults.isNotEmpty()) {
            entries.add(BarEntry(0f, segmentPercentages))
        } else {
            entries.add(BarEntry(0f, floatArrayOf(0f)))
        }

        val subjects = ArrayList<String>()
        for (items in validResults) {
            when (items.subject) {
                "Social Science" -> subjects.add("Social")
                "General Knowledge" -> subjects.add("GK")
                else -> subjects.add(items.subject ?: "Unknown")
            }
        }

        val dataSet = BarDataSet(entries, "").apply {
            colors = colorPalette // Assign colors to match segments
            setDrawValues(false)
            stackLabels = subjects.toTypedArray() // For legend or marker
        }

        barCharts.renderer = SubjectRoundedBarChartRenderer(
            barCharts,
            barCharts.animator,
            barCharts.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.6f // Wider bar for prominent cap

        barCharts.apply {
            data = barData

            description.isEnabled = false
            legend.isEnabled = false

            xAxis.isEnabled = false

            axisLeft.isEnabled = false
            axisRight.isEnabled = false

            setDrawGridBackground(false)
            setDrawBorders(false)

            // Generous top offset for cap visibility
            setExtraOffsets(10f, 40f, 10f, 10f)

            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDragEnabled(false)

            marker = CustomMarkerView2(mActivity, validResults)

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)

            invalidate()
        }
    }

    private fun clsTestBarChart1(result: ArrayList<GetStudentClassTestProgress.Result>) {
        barChart1.clear()
        barChart1.marker = null

        val colorPalette = listOf(
            Color.parseColor("#AEB8FE"), // Soft blue-violet
            Color.parseColor("#FFB3BA"), // Light rose pink
            Color.parseColor("#FFE0A3"), // Pale amber
            Color.parseColor("#D1B3FF"), // Light purple
            Color.parseColor("#B2F2BB"), // Mint green
            Color.parseColor("#B3E5FC"), // Soft sky blue
            Color.parseColor("#FFD180"), // Peach
            Color.parseColor("#F8BBD0"), // Baby pink
            Color.parseColor("#D1C4E9"), // Lilac
            Color.parseColor("#CFD8DC")  // Light gray-blue
        )

        // Prepare Bar Entries and Colors
        val entries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloatOrNull() ?: 0f
            entries.add(BarEntry(index.toFloat(), percentage))
            colors.add(colorPalette[index % colorPalette.size])
        }
/*
        val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
        val adapter = GetSubjectNameColorAdapter(mActivity, result)
        binding.subjectColorRecyclerView.layoutManager = linearLayoutManager
        binding.subjectColorRecyclerView.adapter = adapter*/

        // Bar DataSet
        val dataSet = BarDataSet(entries, "").apply {
            this.colors = colors
            setDrawValues(false)
        }

        // Use custom renderer for curved bars (if required)
        barChart1.renderer = TopRendarCurveBarChartColors(
            barChart1,
            barChart1.animator,
            barChart1.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.4f

        barChart1.apply {
            data = barData

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                setDrawLabels(false)
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 100f
                granularity = 20f
                setLabelCount(5, true)
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
                }
                enableGridDashedLine(10f, 10f, 0f)
                setDrawGridLines(true)
                gridColor = Color.GRAY
            }

            axisRight.isEnabled = false

            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDragEnabled(true)

            marker = CustomMarkerView2(mActivity, result)

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val index = e.x.toInt()
                        if (index in result.indices) {
                            val intent = Intent(mActivity, SubjectWiseProgressActivity::class.java)
                            mActivity.startActivity(intent)
                        }
                    }
                }

                override fun onNothingSelected() {
                    // Optional: Handle case when nothing is selected
                }
            })


            setExtraOffsets(10f, 10f, 10f, 10f)

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)
            invalidate()
        }
    }

    private fun clsTestVerticalBarChart1(result: ArrayList<GetStudentClassTestProgress.Result>) {
        barCharts.clear()
        barCharts.marker = null

        val colorPalette = listOf(
            Color.parseColor("#AEB8FE"), // Soft blue-violet
            Color.parseColor("#FFB3BA"), // Light rose pink
            Color.parseColor("#FFE0A3"), // Pale amber
            Color.parseColor("#D1B3FF"), // Light purple
            Color.parseColor("#B2F2BB"), // Mint green
            Color.parseColor("#B3E5FC"), // Soft sky blue
            Color.parseColor("#FFD180"), // Peach
            Color.parseColor("#F8BBD0"), // Baby pink
            Color.parseColor("#D1C4E9"), // Lilac
            Color.parseColor("#CFD8DC")  // Light gray-blue
        )

        // Filter out zero or invalid percentages and convert to ArrayList
        val validResults = result.filter { it.percentage?.toFloatOrNull() ?: 0f > 0f }
            .toCollection(ArrayList())

        val segmentPercentages = FloatArray(validResults.size) { i ->
            validResults[i].percentage?.toFloatOrNull() ?: 0f
        }

        val entries = ArrayList<BarEntry>()
        if (validResults.isNotEmpty()) {
            entries.add(BarEntry(0f, segmentPercentages))
        } else {
            entries.add(BarEntry(0f, floatArrayOf(0f)))
        }

        val subjects = ArrayList<String>()
        for (items in validResults) {
            when (items.subject) {
                "Social Science" -> subjects.add("Social")
                "General Knowledge" -> subjects.add("GK")
                else -> subjects.add(items.subject ?: "Unknown")
            }
        }

        val dataSet = BarDataSet(entries, "").apply {
            colors = colorPalette // Assign colors to match segments
            setDrawValues(false)
            stackLabels = subjects.toTypedArray() // For legend or marker
        }

        barCharts.renderer = SubjectRoundedBarChartRenderer(
            barCharts,
            barCharts.animator,
            barCharts.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.6f // Wider bar for prominent cap

        barCharts.apply {
            data = barData

            description.isEnabled = false
            legend.isEnabled = false

            xAxis.isEnabled = false

            axisLeft.isEnabled = false
            axisRight.isEnabled = false

            setDrawGridBackground(false)
            setDrawBorders(false)

            // Generous top offset for cap visibility
            setExtraOffsets(10f, 40f, 10f, 10f)

            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDragEnabled(false)

            marker = CustomMarkerView2(mActivity, validResults)

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)

            invalidate()
        }
    }

/*
    private fun clsTestBarChart(result: ArrayList<GetStudentClassTestProgress.Result>) {
        barChart.clear()
        barChart.marker = null
        val entries = ArrayList<BarEntry>()
        if (result.isNotEmpty()) {
            result.forEachIndexed { index, item ->
                val percentage = item.percentage?.toFloatOrNull() ?: 0f
                entries.add(BarEntry(index.toFloat(), percentage))
            }
        } else {
            entries.add(BarEntry(0f, 0f))
        }

        val subjects = ArrayList<String>()
        for (items in result) {
            if (items.subject == "Social Science"){
                subjects.add("Social")
            }
            else if (items.subject == "General Knowledge"){
                subjects.add("GK")
            }
            else {
                subjects.add(items.subject!!)
            }
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = listOf(
            Color.parseColor("#ffffff"),
            Color.parseColor("#ffffff")
        )
        dataSet.setGradientColor(
            Color.parseColor("#ffffff"),
            Color.parseColor("#ffffff")
        )
        dataSet.setDrawValues(false)

        barChart.renderer = TopRoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.3f

        barChart.apply {
            data = barData

            xAxis.valueFormatter = IndexAxisValueFormatter(subjects)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.textSize = 11f
            xAxis.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)

            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 100f
            axisLeft.granularity = 20f
            axisLeft.setLabelCount(5, true)
            axisLeft.textSize = 11f
            axisLeft.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
            axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}%"
                }
            }

            axisLeft.enableGridDashedLine(10f, 10f, 0f)
            axisLeft.setDrawGridLines(true)
            axisLeft.gridColor = Color.GRAY

            axisRight.isEnabled = false

            // Chart styling
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDragEnabled(true)
            barChart.marker = null
            val markerView = CustomMarkerView2(mActivity, result)
            barChart.marker = markerView
            setExtraOffsets(10f, 10f, 10f, 10f)
            // Add rounded corners to bars
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            // Animation
            animateY(1000)

            invalidate()
        }
    }
*/

    private fun examProgressBarChart(result: ArrayList<GetStudentExamProgressResponse.Result>) {
        barChart.clear()
        barChart.marker = null
        val entries = ArrayList<BarEntry>()

        if (result.isNotEmpty()) {
            result.forEachIndexed { index, item ->
                val percentage = item.percentage?.toFloatOrNull() ?: 0f
                entries.add(BarEntry(index.toFloat(), percentage))
            }
        } else {
            entries.add(BarEntry(0f, 0f))
        }

        val subjects = ArrayList<String>()
        var index = 0
        for (items in result) {
            index++
            if (items.subject!! == "Social Science"){
                subjects.add("Social")
            }
            else if (items.subject!! == "General Knowledge"){
                subjects.add("GK")
            }
            else {
                subjects.add(items.subject!!)
            }
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = listOf(
            Color.parseColor("#ffffff"),
            Color.parseColor("#ffffff")
        )
        dataSet.setGradientColor(
            Color.parseColor("#ffffff"),
            Color.parseColor("#ffffff")
        )

        barChart.renderer = TopRoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.3f

        barChart.apply {
            data = barData

            xAxis.valueFormatter = IndexAxisValueFormatter(subjects)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.textSize = 11f
            xAxis.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)

            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 100f
            axisLeft.granularity = 20f
            axisLeft.setLabelCount(5, true)
            axisLeft.textSize = 11f
            axisLeft.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
            axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}%"
                }
            }

            axisLeft.enableGridDashedLine(10f, 10f, 0f)
            axisLeft.setDrawGridLines(true)
            axisLeft.gridColor = Color.GRAY

            axisRight.isEnabled = false

            // Chart styling
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            barChart.setScaleEnabled(false)
            // Add Marker for Tooltips
            var markerView = CustomMarkerView3(mActivity, resultExam)
            barChart.marker = markerView

            // Add rounded corners to bars
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            // Animation
            animateY(1000)
            setExtraOffsets(10f, 10f, 10f, 10f)

            invalidate()
        }
    }

    private fun examProgressBarChart1(result: ArrayList<GetStudentExamProgressResponse.Result>) {
        barChart1.clear()
        barChart1.marker = null

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val colors = ArrayList<Int>()

        val subjectColorMap = mapOf(
            "Computer" to "#3B3BBF",
            "Maths" to "#F85F73",
            "Science" to "#F9B233",
            "English" to "#9C27B0",
            "Tamil" to "#4CAF50",
            "Social Science" to "#2196F3",
            "Telugu" to "#E91E63",
            "Hindi" to "#673AB7",
            "General Knowledge" to "#FF9800"
        )

        // Populate data entries
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloatOrNull() ?: 0f
            val subject = item.subject?.trim() ?: "--"
            entries.add(BarEntry(index.toFloat(), percentage, subject))

            val subjectLabel = when (subject) {
                "Social Science" -> "Social"
                "General Knowledge" -> "GK"
                else -> subject
            }
            val examName = item.examType?.name ?: ""
            labels.add("$subjectLabel\n$examName")
            colors.add(Color.parseColor(subjectColorMap[subject] ?: "#607D8B"))
        }

        if (entries.isEmpty()) {
            entries.add(BarEntry(0f, 0f, "N/A"))
            labels.add("N/A\nNo Exam")
            colors.add(Color.GRAY)
        }

        val dataSet = BarDataSet(entries, "").apply {
            this.colors = colors
            valueTextSize = 10f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
            }
        }

        val barData = BarData(dataSet).apply { barWidth = 0.2f } // 👈 Thinner bars

        // Legend recycler view
        val convertedList = ArrayList<GetStudentClassTestProgress.Result>().apply {
            result.forEach {
                add(GetStudentClassTestProgress.Result().apply {
                    subject = it.subject ?: "--"
                })
            }
        }

        val adapter = GetSubjectNameColorAdapter(mActivity, convertedList, subjectColorMap)
        binding.subjectColorRecyclerView.layoutManager =
            LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
        binding.subjectColorRecyclerView.adapter = adapter

        // Setup chart
        barChart1.apply {
            data = barData

            // Attach custom renderer after setting data
            val customRenderer = ExamTopRendarCurveColor(this, animator, viewPortHandler).apply {
                this.subjectColorMap = subjectColorMap
                initBuffers()
            }
            renderer = customRenderer

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 11f
                labelRotationAngle = -30f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 100f
                granularity = 20f
                setLabelCount(6, true)
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
                }
                enableGridDashedLine(10f, 10f, 0f)
                setDrawGridLines(true)
                gridColor = Color.GRAY
            }

            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false

            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setExtraOffsets(10f, 10f, 10f, 30f)

            marker = CustomMarkerView3(mActivity, result)

            animateY(1000)
            invalidate()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onResume() {
        super.onResume()
    }
    private fun attachSearchWatcher(editText: EditText, onSearch: (String) -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().trim()
                onSearch(text)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}