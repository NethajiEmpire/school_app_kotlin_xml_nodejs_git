package com.lms.sch.fragment

import RoleSwitchAdapter
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.activity.ParentProfileActivity
import com.lms.sch.activity.ProfileActivity
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.ClassTestAdapter
import com.lms.sch.adapter.EventsPagerAdapter
import com.lms.sch.adapter.HomeworkFilterLabelAdapter
import com.lms.sch.adapter.HomeworkFilterValueAdapter
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.NoticeBoardAdapter
import com.lms.sch.adapter.OverAllProgressAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.StudentAssignmentAdapter
import com.lms.sch.adapter.StudentAssignmentAdapter1
import com.lms.sch.adapter.StudentExaminationAdapter
import com.lms.sch.adapter.StudentFeesAdapter
import com.lms.sch.adapter.StudentHomeworkAdapter
import com.lms.sch.adapter.StudentHomeworkAdapter1
import com.lms.sch.adapter.StudentProjectAdapter
import com.lms.sch.adapter.StudentProjectAdapter1
import com.lms.sch.adapter.TimeTableAdapter
import com.lms.sch.adapter.WeekDayAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.charts.SubjectRoundedBarChartRenderer
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.customviews.CustomMarkerView2
import com.lms.sch.customviews.CustomMarkerView3
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.customviews.TopRendarCurveBarChartColors
import com.lms.sch.customviews.TopRoundedBarChartRenderer
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.databinding.FilterAssignmentBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.databinding.FragmentHomeParentsBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.TimeTableSlots
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.response.ClassTestResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetClassTimeTableResponse
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.response.GetStudentAssignmentRes
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.GetStudentExamProgressResponse
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.response.StudentClassTestResponse
import com.lms.sch.response.StudentProjectResponse
import com.lms.sch.response.SubmissionProgressResponse
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

class HomeParentsFragment : BaseFragment() {
    lateinit var binding: FragmentHomeParentsBinding
    lateinit var pieChart: PieChart
    lateinit var barChart1 : BarChart
    lateinit var barCharts : BarChart

    lateinit var attenDanceProgress : PieChart
    var hwStatusArr = ArrayList<String>()
    var markStatus = ArrayList<String>()
    var hwResponse = ArrayList<GetHomeworkResponse.Result>()
    var projectRes = ArrayList<StudentProjectResponse.Result>()
    var resultClsTest = ArrayList<GetStudentClassTestProgress.Result>()
    var resultExam = ArrayList<GetStudentExamProgressResponse.Result>()
    var examProgressResult = ArrayList<DropdownResponse.Result>()
    var clsTestFilter = ArrayList<String>()
    var examFilter = ArrayList<String>()
    var result1 = GetStudentExamProgressResponse.Result()
    var homeworkAttach = ArrayList<String>()
    var clsTestValue = ""
//    var examTabClicked = ""
    var type = ""
    var hwStatus = ""
    var tabPos = ""
    var search = ""
    var classTestSts = ""
    var timeTableDay = ""
    var examStatus = ""
    var examId = ""
    var isFilter = false
    var clickedDialog = ""
    var resultAtt = GetStudentAttenDanceResponse.Result()
    var ststyp = ""
    var cDate = ""
    var assignmentStatus = ""
    var projectStatus = ""
    var timeTableRes = ArrayList<GetClassTimeTableResponse.Period>()
    var assignmentRes = ArrayList<GetStudentAssignmentRes.Result>()
    var clsTestRes = ArrayList<ClassTestResponse.Result>()
    var todayTimeTable = ArrayList<TimeTableSlots>()
    private var currentMonthDays = ArrayList<Date>()
    var calendar = Calendar.getInstance()
    var noticeList = ArrayList<NoticeBoardResponse.Result>()
    lateinit var timer: CountDownTimer
    var eventDate = ""
    var tabClicked = ""
    var childId = ""
    var examTabClicked = ""
    var subArr = ArrayList<String>()
    var selectedMonth : Int = 0
    var selectedYear : Int = 0
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var classTestAttach = ArrayList<String>()
    var count = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View?{
        binding = FragmentHomeParentsBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(0)

        binding.logo.setOnLongClickListener{
            if(BuildConfig.DEBUG){
                ApiDataDialog(mActivity).show(mActivity)
            }
            return@setOnLongClickListener true
        }

        initAdapter(inflater, binding.root)
        pieChart = binding.pieChart
        barChart1 = binding.barChart1
        barCharts = binding.barCharts
        attenDanceProgress = binding.attenDanceProgress
        binding.shineTxt.text = "Here’s how ${SharedHelper(mActivity).childName!!} is doing today!"
        Log.d("jhsgdgf",SharedHelper(mActivity).childName!!)
        UiUtils.textViewGradient(binding.shineTxt,"#232B68","#4555CE")

//        binding.eventDate.text = cDate
       //getHomework()
        hwStatus = ""
        studentHomework()
        Log.d("lkjhgfttryu", hwStatus)
        getStudentAssignment()
        getProject()
        getStudentAttendance()
        getClassTest()
        getClassTimeTableHome()

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
        eventDate = date
        cDate = date
        binding.dtText.text = sdfMon
        timeTableDay = api
        if (cYear != -1) {
            selectedYear = cYear
        }
        if (cMon != -1) {
            selectedMonth = cMon
        }
        binding.parentName.text = SharedHelper(mActivity).name!!
        binding.name.text = SharedHelper(mActivity).childName!!
        Log.d("hghsgdf",SharedHelper(mActivity).name!!)
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
                        binding.cardNoNoticeData.txt.text = "No events present today!"
                        binding.cardNoNoticeData.root.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.dotsContainer.visibility = View.GONE
                    }
                }
            }
        }
        attachSearchWatcher(binding.search) { query ->
            search = query
            studentHomework()
        }
        attachSearchWatcher(binding.search2) { query ->
            search = query
            getStudentAssignment()
            getProject()
        }
        attachSearchWatcher(binding.search3) { query ->
            search = query
            getExamination()
        }

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().parentChildProfile(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.userprofile != null){
                            if (it.result!!.userprofile!!.grade_level != null && it.result!!.userprofile!!.grade_level!!.name!!.isNotEmpty()){
                                binding.rollNo.text = UiUtils.getOrdinalSuffix(it.result!!.userprofile!!.grade_level!!.name!!.toInt())+"th Standard | Roll No:11"
                            }
                            else{
                                binding.rollNo.text = "--/-- th Standard | Roll No:11"
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

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getStatsProgressPoints(mActivity).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){

                            if (it.result!!.score != null) {
                                binding.scorePoints.text = "${it.result!!.score} Points "
                            } else {
                                binding.scorePoints.text = "0 Points"
                            }

                            if (it.result!!.rank != null){
                                binding.ranks.text = "${it.result!!.rank}th Rank "
                            }
                            else{
                                binding.ranks.text = "--/--"
                            }
                            if (it.result!!.streaks != null){
                                binding.streakss.text = "${it.result!!.streaks} Str "
                            }
                            else {
                                binding.streakss.text = "0 Str"
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

/*
        ApiConnection.getInstance().getStudentOverallProgresss(mActivity).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                           // childId = it.result!!._id

                            binding.noDataReport.root.visibility = View.GONE
                            binding.reportRecycler.visibility = View.VISIBLE
                            val adapter = OverAllProgressAdapter(mActivity,1,it.result!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                }
                            })
                            val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
                            binding.reportRecycler.layoutManager = layoutManager
                            binding.reportRecycler.adapter = adapter
                        }
                        else {
                            binding.noDataReport.root.visibility = View.VISIBLE
                            binding.reportRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noDataReport.root.visibility = View.VISIBLE
                        binding.reportRecycler.visibility = View.GONE
                    }
                }
            }
        }
*/
        getFees()
        currentMonthDays = getCurrentMonthDays()
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

        /*binding.date.setOnClickListener {
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
            showCalender{
                val sdfDate = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
                val dt = sdfDate.format(it)
                val api = sdfApi.format(it).toLowerCase(Locale.getDefault())
                binding.dtText.text = dt
                timeTableDay = api
                getClassTimeTable()
            }
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
        }*/
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
                    getProject()
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
                    getProject()
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
                    getProject()
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
                    getProject()
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
                    getExamination()
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
                    getExamination()
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
                    getExamination()
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
                    getExamination()
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
        binding.attendance.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.attendance, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.attendance, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabclasstesthome, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabclasstesthome, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabexamTxt, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabexamTxt, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabAssignmnet, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAssignmnet, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.black_varient6)
            binding.homeworkSubmission.visibility = View.GONE
            binding.attendanceprogresshome.visibility = View.GONE
            binding.classtextProgress.visibility = View.GONE
            binding.barChart1.visibility = View.GONE
            binding.barCharts.visibility = View.GONE
            getStudentAttendance()

        }
        binding.tabclasstesthome.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.attendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.attendance, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabclasstesthome, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabclasstesthome, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabexamTxt, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabexamTxt, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabAssignmnet, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAssignmnet, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.black_varient6)
            binding.homeworkSubmission.visibility = View.GONE
            binding.attendanceprogresshome.visibility = View.GONE
            binding.classtextProgress.visibility = View.GONE
            binding.spinner.visibility = View.VISIBLE
            binding.barChart1.visibility = View.VISIBLE
            binding.barCharts.visibility = View.VISIBLE
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
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }

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
                    }else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }


        binding.tabexamTxt.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.attendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.attendance, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabexamTxt, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabexamTxt, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabclasstesthome, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabclasstesthome, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabAssignmnet, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAssignmnet, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.black_varient6)
            binding.homeworkSubmission.visibility = View.GONE
            binding.attendanceprogresshome.visibility = View.GONE
            binding.classtextProgress.visibility = View.GONE
            binding.spinner.visibility = View.VISIBLE
            binding.barChart1.visibility = View.GONE
            binding.barCharts.visibility = View.GONE
            examProgressBarChart(resultExam)
            binding.proTitle.text = "Examination Insights"
            binding.proDesc.text = "Term Based exam results"
            val adapter = SpinnerAdapter(mActivity, examFilter)
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
        }
        binding.tabHomeWork.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.attendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.attendance, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabclasstesthome, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabclasstesthome, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabexamTxt, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabexamTxt, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabAssignmnet, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAssignmnet, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.black_varient6)
            binding.homeworkSubmission.visibility = View.GONE
            binding.attendanceprogresshome.visibility = View.GONE
            binding.classtextProgress.visibility = View.GONE
            binding.barChart1.visibility = View.GONE
            binding.barCharts.visibility = View.GONE
            type = "homework"
            submissionProgress()
        }
        binding.tabAssignmnet.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.attendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.attendance, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabAssignmnet, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabAssignmnet, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabclasstesthome, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabclasstesthome, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabexamTxt, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabexamTxt, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.black_varient6)
            binding.homeworkSubmission.visibility = View.GONE
            binding.attendanceprogresshome.visibility = View.GONE
            binding.classtextProgress.visibility = View.GONE
            binding.barChart1.visibility = View.GONE
            binding.barCharts.visibility = View.GONE
            type = "assignment"
            submissionProgress()
        }
        binding.tabProjects.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.attendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.attendance, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabclasstesthome, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabclasstesthome, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabexamTxt, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabexamTxt, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabAssignmnet, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAssignmnet, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.black_varient6)
            binding.homeworkSubmission.visibility = View.GONE
            binding.attendanceprogresshome.visibility = View.GONE
            binding.classtextProgress.visibility = View.GONE
            binding.barChart1.visibility = View.GONE
            binding.barCharts.visibility = View.GONE
            type = "project"
            submissionProgress()
        }
        
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, ParentProfileActivity(), null, false)
        }
        binding.viewAllPlaceChild.setOnClickListener {
            binding.tabLayoutAll.getTabAt(1)?.select()
        }
        binding.feeTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.feeTab,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.pendingFeeTab,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.penFeeNum,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.dueDueFee,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.dueTab,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.dueNum,R.drawable.ic_round_line_3)
        }
        binding.dueDueFee.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.feeTab,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.pendingFeeTab,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.penFeeNum,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.dueDueFee,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.dueTab,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.dueNum,R.drawable.ic_round_line2)
        }
        binding.homeworkpending.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.homeworkpending,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.homework,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.count1,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.pendingassignment,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.assignment,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count2,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.pendingproject,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.project,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count3,R.drawable.ic_round_line_3)
        }
        binding.pendingassignment.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.homeworkpending,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.homework,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count1,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.pendingassignment,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.assignment,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.count2,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.pendingproject,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.project,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count3,R.drawable.ic_round_line_3)
        }
        binding.pendingproject.setOnClickListener{
            UiUtils.linearLayoutBgDrawable(binding.homeworkpending,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.homework,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count1,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.pendingassignment,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.assignment,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count2,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.pendingproject,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.project,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.count3,R.drawable.ic_round_line2)
        }
        binding.pendingproject.setOnClickListener{
            UiUtils.linearLayoutBgDrawable(binding.homeworkpending,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.homework,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count1,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.pendingassignment,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.assignment,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.count2,R.drawable.ic_round_line_3)
            UiUtils.linearLayoutBgDrawable(binding.pendingproject,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.project,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.count3,R.drawable.ic_round_line2)
        }
        binding.profileLay.setOnClickListener {
            mActivity.binding.roleSwitch.root.visibility = View.VISIBLE
            val adapter = RoleSwitchAdapter(mActivity, mActivity.sharedHelper.childList,object: OnClickListener{
                override fun onClickItem(pos: Int) {
                    mActivity.sharedHelper.childId = mActivity.sharedHelper.childList[pos].user_id!!._id!!
                    recreate(mActivity)
                }
            })
            val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            mActivity.binding.roleSwitch.recycler.layoutManager = layoutManager
            mActivity.binding.roleSwitch.recycler.adapter = adapter
            UiUtils.animation(mActivity,mActivity.binding.roleSwitch.topLay,R.anim.slide_in_from_bottom,true)
        }
        mActivity.binding.roleSwitch.cancel.setOnClickListener {
            mActivity.binding.roleSwitch.root.visibility = View.GONE
        }
        mActivity.binding.roleSwitch.close.setOnClickListener {
            mActivity.binding.roleSwitch.root.visibility = View.GONE
        }

        binding.tabToday.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabToday, R.drawable.border_line_curve_24dp_primary )
            UiUtils.linearLayoutBgDrawable( binding.tabNotCompleted, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable( binding.tabComplete, R.drawable.border_line_curve_24dp_grey )
//            UiUtils.textViewTextColor(binding.textToday, "#232B68", null)
//            UiUtils.textViewTextColor(binding.textNotCmpltd, "#5B5B5B", null)
//            UiUtils.textViewTextColor(binding.textCompleted,"#5B5B5B", null)
//            UiUtils.textviewCustomDrawable(binding.countToday,R.drawable.ic_round_line2)
//            UiUtils.textviewCustomDrawable(binding.countNotCompleted,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.countComplete,R.drawable.ic_round_line_3)
            hwStatus = "today"
           // getHomework()
            studentHomework()
        }
        binding.tabNotCompleted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabNotCompleted, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabToday, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabComplete, R.drawable.border_line_curve_24dp_grey )
//            UiUtils.textViewTextColor(binding.textToday, "#5B5B5B", null)
//            UiUtils.textViewTextColor(binding.textNotCmpltd, "#232B68", null)
//            UiUtils.textViewTextColor(binding.textCompleted,"#5B5B5B", null)
//            UiUtils.textviewCustomDrawable(binding.countToday,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.countComplete,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.countNotCompleted,R.drawable.ic_round_line2)
            hwStatus = "pending"
            //getHomework()
            studentHomework()
        }
        binding.tabComplete.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabComplete, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabToday, R.drawable.border_line_curve_24dp_grey )
//            UiUtils.textViewTextColor(binding.textToday, "#5B5B5B", null)
//            UiUtils.textViewTextColor(binding.textCompleted,"#232B68", null)
//            UiUtils.textViewTextColor(binding.textNotCmpltd, "#5B5B5B",null)
//            UiUtils.textviewCustomDrawable(binding.countToday,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.countComplete,R.drawable.ic_round_line2)
//            UiUtils.textviewCustomDrawable(binding.countNotCompleted,R.drawable.ic_round_line_3)
            hwStatus = "completed"
          //  getHomework()
            studentHomework()
        }
        
       
//        binding.parentExamDialog.close.setOnClickListener {
//            binding.parentExamDialog.root.visibility = View.GONE
//        }

        binding.viewAll2.setOnClickListener {
            mActivity.navController?.navigate(R.id.navigation_fees)
        }
        
        binding.scrollViewTop.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > binding.headerTop.height) {
                binding.tabLayoutAll.translationY = scrollY.toFloat() - binding.headerTop.height
                binding.tabLayoutAll.elevation = 8f
            } else {
                binding.tabLayoutAll.translationY = 0f
                binding.tabLayoutAll.elevation = 0f
            }
        }
      /*  binding.scrollViewTop.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            // Stop TabLayout at the top during scrolling
            if (scrollY > binding.tabLayoutAll.height) {
                binding.tabLayoutAll.translationY = scrollY.toFloat() - binding.tabLayoutAll.height
                binding.tabLayoutAll.elevation = 8f
            } else {
                binding.tabLayoutAll.translationY = 0f
                binding.tabLayoutAll.elevation = 0f
            }
        }*/

        binding.search2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.tabsLay.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        binding.tabsLay.visibility = View.GONE
                    }

                binding.filterLay2.visibility = View.VISIBLE
                binding.filterLay2.alpha = 0f
                binding.filterLay2.animate()
                    .alpha(1f)
                    .setDuration(200)

                binding.scrollViewTop.post {
                    binding.scrollViewTop.smoothScrollTo(0, binding.tabLayoutAll.top)
                }
            } else {
                binding.filterLay2.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        binding.filterLay2.visibility = View.GONE
                    }

                binding.tabsLay.visibility = View.VISIBLE
                binding.tabsLay.alpha = 0f
                binding.tabsLay.animate()
                    .alpha(1f)
                    .setDuration(200)
            }
        }
        binding.lin1.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.num1,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num2,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num3,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num4,null,R.color.gray)
//            binding.classTest.visibility = View.GONE
            tabClicked = "homeWork"
            if (hwResponse.isNotEmpty()){
                binding.noDataReport.root.visibility = View.GONE
                binding.reportRecycler.visibility = View.VISIBLE
                val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                val adapter = StudentHomeworkAdapter(mActivity, true,hwResponse, object : OnClickListener {
                    override fun onClickItem(pos: Int) {
                    //getHomework()
                   studentHomework()
                    }
                })
                binding.reportRecycler.layoutManager = linearLayoutManager
                binding.reportRecycler.adapter = adapter
            }
            else {
                binding.noDataReport.root.visibility = View.VISIBLE
                binding.reportRecycler.visibility = View.GONE
            }
        }
        binding.lin2.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.num2,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num3,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num4,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num1,null,R.color.gray)
//            binding.classTest.visibility = View.GONE
            tabClicked = "classTest"
            if (clsTestRes.isNotEmpty()){
                binding.noDataReport.root.visibility = View.GONE
                binding.reportRecycler.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                val adapter = ClassTestAdapter(mActivity,clsTestRes,object : OnClickListener{
                    override fun onClickItem(pos: Int) {
//                    getAssignmentStatus(pos)
                        getProjectResult(pos)

                    }

                })
                binding.reportRecycler.layoutManager = layoutManager
                binding.reportRecycler.adapter = adapter
            }
            else {
                binding.noDataReport.root.visibility = View.VISIBLE
                binding.reportRecycler.visibility = View.GONE
            }
        }
        binding.lin3.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.num3,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num1,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num4,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num2,null,R.color.gray)
//            binding.classTest.visibility = View.GONE
            tabClicked = "assignment"
            if (assignmentRes.isNotEmpty()){
                binding.noDataReport.root.visibility = View.GONE
                binding.reportRecycler.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                val adapter = StudentAssignmentAdapter(mActivity,true,assignmentRes,object : OnClickListener{
                    override fun onClickItem(pos: Int) {
                 //   getAssignmentStatus(pos)
                        getAssignmentResult(pos)
                    }

                })
                binding.reportRecycler.layoutManager = layoutManager
                binding.reportRecycler.adapter = adapter
            }
            else {
                binding.noDataReport.root.visibility = View.VISIBLE
                binding.reportRecycler.visibility = View.GONE
            }
        }
        binding.lin4.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lin3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lin4, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab4,null, R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.num4,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tab3,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num3,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab2,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num2,null,R.color.gray)
            UiUtils.textViewTextColor(binding.tab1,null, R.color.black_varient6)
            UiUtils.textViewBgTint(binding.num1,null,R.color.gray)
//            binding.classTest.visibility = View.VISIBLE
            tabClicked = "project"
            if (projectRes.isNotEmpty()){
                binding.noDataReport.root.visibility = View.GONE
                binding.reportRecycler.visibility = View.VISIBLE
                val adapter = StudentProjectAdapter(mActivity,true, projectRes,object : OnClickListener{
                    override fun onClickItem(pos: Int) {
                        getProjectResult(pos)
                    }

                })
                val layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                binding.reportRecycler.layoutManager = layoutManager
                binding.reportRecycler.adapter = adapter
            }
            else {
                binding.noPlan.root.visibility = View.VISIBLE
                binding.reportRecycler.visibility = View.GONE
            }
        }

        DialogUtils.showLoader(mActivity)

        Log.d("kjhgfdghbnvbgf", SharedHelper(mActivity).childId)
        ApiConnection.getInstance().getStudentOverallProgresss(mActivity).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    Log.d("lkjhgf", success.toString())
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noDataReport.root.visibility = View.GONE
                            binding.reportRecycler.visibility = View.VISIBLE
                            val adapter = OverAllProgressAdapter(mActivity,2,it.result!!,object: OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    if(it.result!![pos].type == "homework"){
                                        binding.tabLayoutAll.getTabAt(1)?.select()
                                    }
                                    else if(it.result!![pos].type == "project"){
                                        binding.tabLayoutAll.getTabAt(5)?.select()
                                    }
                                    else if(it.result!![pos].type == "assignment"){
                                        binding.tabLayoutAll.getTabAt(4)?.select()
                                    }
                                    else if(it.result!![pos].type == "classtest"){
                                        binding.tabLayoutAll.getTabAt(3)?.select()
                                    }
                                }
                            })
                            val layoutManager = GridLayoutManager(mActivity,2 ,LinearLayoutManager.VERTICAL, false)
                            binding.reportRecycler.layoutManager = layoutManager
                            binding.reportRecycler.adapter = adapter
                        }
                        else {
                            binding.noDataReport.root.visibility = View.VISIBLE
                            binding.reportRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noDataReport.root.visibility = View.VISIBLE
                        binding.reportRecycler.visibility = View.GONE
                    }
                }
            }
        }

       /* binding.ongoingTab.setOnClickListener{
            UiUtils.linearLayoutBgDrawable(binding.ongoingTab,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted2,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabSubmitted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.txtNotCompleted, "#5B5B5B", null)
            UiUtils.textViewTextColor(binding.txtOngoing,"#232B68", null)
            UiUtils.textViewTextColor(binding.txtSubmitted, "#5B5B5B",null)
            UiUtils.textviewCustomDrawable(binding.countSubmitted,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.countOngoing,R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.countNotCompleted2,R.drawable.ic_round_line_3)
            if (tabPos == "assignment") {
                assignmentStatus = "ongoing"
                getAssignment()
            }
            else if (tabPos == "project") {
                projectStatus = "ongoing"
                getProject()
            }
        }
        binding.tabNotCompleted2.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.ongoingTab, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted2, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabSubmitted, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.txtNotCompleted, "#232B68", null)
            UiUtils.textViewTextColor(binding.txtOngoing,"#5B5B5B", null)
            UiUtils.textViewTextColor(binding.txtSubmitted, "#5B5B5B",null)
            UiUtils.textviewCustomDrawable(binding.countSubmitted,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.countOngoing,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.countNotCompleted2,R.drawable.ic_round_line2)
            if (tabPos == "assignment"){
                assignmentStatus = "overdue"
                getAssignment()
            }
            else if (tabPos == "project") {
                projectStatus = "overdue"
                getProject()
            }
        }
        binding.tabSubmitted.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.ongoingTab, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabSubmitted, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.txtNotCompleted, "#5B5B5B", null)
            UiUtils.textViewTextColor(binding.txtOngoing,"#5B5B5B", null)
            UiUtils.textViewTextColor(binding.txtSubmitted, "#232B68",null)
            UiUtils.textviewCustomDrawable(binding.countSubmitted,R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.countOngoing,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.countNotCompleted2,R.drawable.ic_round_line_3)
            if (tabPos == "assignment"){
                assignmentStatus = "submitted"
                getAssignment()
            }
            else if (tabPos == "project") {
                projectStatus = "completed"
                getProject()
            }
        }
*/
        binding.tabTimeTable.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabTimeTable, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_grey)
        }
        binding.tabExam.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabTimeTable, R.drawable.border_line_curve_24dp_grey)
        }

        binding.tabAnnouncement.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabAnnouncement, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.tabEvents, R.drawable.border_line_curve_24dp_grey)
        }
        binding.tabEvents.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabEvents, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.tabAnnouncement, R.drawable.border_line_curve_24dp_grey)
        }

        binding.tabDay.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabDay, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabDay, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabWeek, R.drawable.border_curve_6dp)

            /* val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
           val adapter = AdminStdTimeTableAdapter(mActivity, false, todayTimeTable,)
            binding.timetableRecycler.layoutManager = linearLayoutManager
            binding.timetableRecycler.adapter = adapter
            binding.timetableRecycler.visibility = View.VISIBLE
            binding.weekLay.visibility = View.GONE*/
        }

        binding.tabWeek.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabWeek, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabWeek, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabDay, R.drawable.border_curve_6dp)

            /*val columnWidthPx = (110 * resources.displayMetrics.density).toInt()
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
            binding.weekLay.visibility = View.VISIBLE*/
        }
        binding.tabDay1.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabDay1,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabDay1,R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabDay1,null,R.color.white)
            UiUtils.textViewTextColor(binding.tabMonth,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabMonth,R.drawable.border_curve_6dp)
            ststyp = "day"
            binding.eventType.text = "Today Announcement"
            binding.eventDate.text = cDate
            getNoticeBoard()

        }
        binding.tabMonth.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabMonth, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabMonth, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabMonth, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabDay1, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabDay1, R.drawable.border_curve_6dp)
            ststyp = "week"
            binding.eventType.text = "Monthly Announcement"
            binding.eventDate.text = "This month events"
            getNoticeBoard()
        }
        binding.tabDay1.performClick()
        binding.classTest.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classTest, null,R.color.white)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.black_varient3)
            binding.pageExam.visibility = View.VISIBLE
            binding.classTest.visibility = View.VISIBLE
            binding.classTestRecycler.visibility = View.VISIBLE
            examTabClicked = "classTest"
            classTestSts = ""
            getClassTest()
            //binding.tabclassTestToday.performClick()
        }
        binding.tabclassTestToday.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabclassTestToday,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestUpcoming,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTestCompleted,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.clsTstCountToday,R.drawable.ic_round_line2)
            UiUtils.textviewCustomDrawable(binding.clsTstCountUpcoming,R.drawable.ic_round_line_3)
            UiUtils.textviewCustomDrawable(binding.clsTstCountTcompleted,R.drawable.ic_round_line_3)
            UiUtils.textViewTextColor(binding.todayClassTestId,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.ClassTestupcomingId,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.classTestcompletedId,null, R.color.black_varient6)
           // classTestSts = "today"
            //getClassTest()
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
          //  classTestSts = "upcoming"
            //getClassTest()
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
           // classTestSts = "completed"
            //getClassTest()
        }

        binding.classExam.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classExam, null,R.color.white)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.black_varient3)
            binding.pageExam.visibility = View.VISIBLE
            binding.classTestRecycler.visibility = View.VISIBLE
            examTabClicked = "exam"
            examStatus = ""
            getExamination()
        }

      /*  binding.tabclassTestToday.setOnClickListener {
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
        }

        binding.tabExamOngoing.setOnClickListener {
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
            getExamination()
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
            getExamination()
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
            getExamination()
        }*/

        binding.viewAllPlaceChild.setOnClickListener {
            if (tabClicked == "homeWork") {
                binding.tabLayoutAll.getTabAt(1)?.select()
            }
            /*else if (tabClicked == "classTest") {
                binding.tabLayoutAll.getTabAt(4)?.select()
            } */
            else if (tabClicked == "assignment") {
                binding.tabLayoutAll.getTabAt(4)?.select()
            } else if (tabClicked == "project") {
                binding.tabLayoutAll.getTabAt(5)?.select()
            } else if (tabClicked == "Examination") {
                binding.tabLayoutAll.getTabAt(3)?.select()
            }
            else if (tabClicked == "Progress") {
                binding.tabLayoutAll.getTabAt(6)?.select()
            }
        }
        return view
    }

/*
    fun getHomework() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentHomework(mActivity,"",hwStatus,"").observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            binding.noData.root.visibility = View.GONE
                            binding.hwRecycler.visibility = View.VISIBLE
                            hwResponse = it.result!!
                            binding.lin1.performClick()
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = StudentHomeworkAdapter(mActivity,false,hwResponse, object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    getHomeworkResult(pos)
                                }
                            })
                            binding.hwRecycler.layoutManager = linearLayoutManager
                            binding.hwRecycler.adapter = adapter
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.hwRecycler.visibility = View.GONE
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData.root.visibility = View.VISIBLE
                        binding.hwRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }
*/

/*
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
                val homeworkId = hwResponse[pos].homework!!._id
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
            val homeworkId = hwResponse[pos].homework!!._id
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

        mActivity.binding.dialogHomework.points.text = "+${hwResponse[pos].credits} Points"
        if (hwResponse[pos].subject != null){
            mActivity.binding.dialogHomework.subject.text = "Subject : ${result1[pos].subject!!.name}"
        }
        else {
            mActivity.binding.dialogHomework.subject.text = "Subject : --/--"
        }

        if (hwResponse[pos].homework != null){
            mActivity.binding.dialogHomework.que.text = hwResponse[pos].homework!!.title
            mActivity.binding.dialogHomework.desc.text = hwResponse[pos].homework!!.description
            if (hwResponse[pos].homework!!.createdBy != null){
                mActivity.binding.dialogHomework.teacher.text = "Teacher : ${hwResponse[pos].homework!!.createdBy!!.firstName +" "+ hwResponse[pos].homework!!.createdBy!!.lastName}"
            }
            else {
                mActivity.binding.dialogHomework.teacher.text = "Teacher : --/--"
            }
        }
        else {
            mActivity.binding.dialogHomework.que.text = "--/--"
            mActivity.binding.dialogHomework.desc.text = "--/--"
        }
        val givenDate = BaseUtils.getFormattedDate(hwResponse[pos].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        val dueDate = BaseUtils.getFormattedDate(hwResponse[pos].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        if (hwResponse[pos].status == "completed"){
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
            val submittedOn = BaseUtils.getFormattedDate(hwResponse[pos].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            mActivity.binding.dialogHomework.submitOn.text = "Submitted On : "
            mActivity.binding.dialogHomework.submitDate.text = submittedOn
            mActivity.binding.dialogHomework.givenDate.text = givenDate

            if (hwResponse[pos].credits != null && hwResponse[pos].credits!!.isNotEmpty() && hwResponse[pos].credits!!.toInt() > 0){
                mActivity.binding.dialogHomework.points.text = "+${hwResponse[pos].credits} Points"
                UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#32B138", null)
            }
            else {
                mActivity.binding.dialogHomework.points.text = "0 Point"
                UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#EA5455", null)
            }
            if (hwResponse[pos].markStatus == "pending"){
                mActivity.binding.dialogHomework.noteCheck.visibility = View.VISIBLE
                UiUtils.imageviewDrawable(mActivity.binding.dialogHomework.noteCheck,R.drawable.green_tick)
                mActivity.binding.dialogHomework.remarkLay.visibility = View.GONE
                mActivity.binding.dialogHomework.note.text = "You’ve marked this homework as completed.If you tapped it by mistake, just uncheck and click Done again to update."
            }
            else {
                mActivity.binding.dialogHomework.remarkLay.visibility = View.VISIBLE
                mActivity.binding.dialogHomework.noteCheck.visibility = View.GONE
                if (hwResponse[pos].submittedOnTime){
                    mActivity.binding.dialogHomework.note.text = "Good job! Your Homework has been submitted successfully, You’ve gained points for your submission!"
                    UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#EFFFF0",null)
                }
                else {
                    mActivity.binding.dialogHomework.note.text = "Your Homework was submitted late. Great effort! Aim to submit on time to maximize your points."
                    UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#fafce3",null)
                }
                when(hwResponse[pos].remarks){
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

            if (hwResponse[pos].attachment!! != null && hwResponse[pos].attachment!!.isNotEmpty()){
                mActivity.binding.dialogHomework.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(mActivity,hwResponse[pos].attachment!!)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
                mActivity.binding.dialogHomework.attachRecycler.layoutManager = layoutManager
                mActivity.binding.dialogHomework.attachRecycler.adapter = adapter
            }
            else {
                mActivity.binding.dialogHomework.attachRecycler.visibility = View.GONE
            }
        }
        else if (hwResponse[pos].status == "overdue"){
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
        else if (hwResponse[pos].status == "pending"){
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
*/

    fun loadDates2(){
      //  binding.dateRecycler1.visibility = View.VISIBLE
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
                            Log.d("kjhgfdssfgh", it.result!!.toString())
                            loadDates2()
                            binding.noDataReport.root.visibility = View.GONE
                            binding.noData.root.visibility = View.GONE
                            binding.reportRecycler.visibility = View.VISIBLE
                            binding.hwRecycler.visibility = View.VISIBLE
                            hwResponse = it.result!!
 //                           binding.lin1.performClick()
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = StudentHomeworkAdapter1(mActivity,false,hwResponse, object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    //getHomeworkResult(pos)
                                }
                            })
                            binding.hwRecycler.layoutManager = linearLayoutManager
                            binding.hwRecycler.adapter = adapter
                        } else {
                            binding.noDataReport.root.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.VISIBLE
                            binding.reportRecycler.visibility = View.GONE
                            binding.hwRecycler.visibility = View.GONE
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noDataReport.root.visibility = View.VISIBLE
                        binding.noData.root.visibility = View.VISIBLE
                        binding.reportRecycler.visibility = View.GONE
                        binding.hwRecycler.visibility = View.GONE
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
                            assignmentRes = it.result!!
                            loadDates2()
                            binding.recycler2.visibility = View.VISIBLE
                            binding.noData1.root.visibility = View.GONE
                            val LinearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = StudentAssignmentAdapter1(mActivity,false,assignmentRes,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                  //  getAssignmentStatus(pos)
                                    getAssignmentResult(pos)
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

    /*
        fun getAssignment() {
            DialogUtils.showLoader(mActivity)
            ApiConnection.getInstance().getStudentAssignment(mActivity,"",assignmentStatus,"").observe(mActivity) {
                it?.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null && it.result!!.isNotEmpty()) {
                                assignmentRes = it.result!!
                                binding.recycler2.visibility = View.VISIBLE
                                binding.noData1.root.visibility = View.GONE
                                val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                                val adapter = StudentAssignmentAdapter1(mActivity,false,assignmentRes,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        getAssignmentResult(pos)
                                    }

                                })
                                binding.recycler2.layoutManager = layoutManager
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
    */

    fun getProject(){
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
                            projectRes = it.result
                            loadDates2()
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

    fun getExamination(){
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
                            loadDates2()
//                            result5 = it.result!!.rows!!
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

    fun getClassTest(){
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getClassTest(mActivity,search,classTestSts).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noData3.root.visibility = View.GONE
                            binding.classTestRecycler.visibility = View.VISIBLE
                            clsTestRes = it.result!!
                            loadDates2()
                            val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                            val adapter = ClassTestAdapter(mActivity,clsTestRes,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    getClsTestDialog(pos)
                                }
                            })
                            binding.classTestRecycler.layoutManager = layoutManager
                            binding.classTestRecycler.adapter = adapter
                        }else{
                            UiUtils.showSnack(it.msg, binding.root,false)
                            binding.noData3.root.visibility = View.VISIBLE
                            binding.classTestRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData3.root.visibility = View.VISIBLE

//                        binding.updateRecycler.visibility = View.GONE
                        binding.classTestRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun getClsTestDialog(pos: Int){
        binding.examDialog.root.visibility = View.VISIBLE
        binding.examDialog.close1.setOnClickListener {
            binding.examDialog.root.visibility = View.GONE
            classTestAttach.clear()
            binding.examDialog.attach.text = ""
        }
        binding.examDialog.cancel.setOnClickListener {
            binding.examDialog.root.visibility = View.GONE
            classTestAttach.clear()
            binding.examDialog.attach.text = ""
        }
        binding.examDialog.makeasdone.setOnClickListener {
            binding.examDialog.root.visibility = View.GONE
            classTestAttach.clear()
            binding.examDialog.attach.text = ""
        }

        binding.examDialog.next.setOnClickListener {
            binding.examDialog.root.visibility = View.GONE
        }
        binding.examDialog.makeasdone.setOnClickListener {
            if (classTestAttach.isNotEmpty()){
                DialogUtils.showLoader(mActivity)
                val classTestId = clsTestRes[pos].classTest!!._id
                if (classTestId != null){
                    ApiConnection.getInstance().classTestStsUpdate(mActivity,classTestId,classTestAttach).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg,binding.root,true)
                                    binding.examDialog.root.visibility = View.GONE
                                    getClassTest()
                                    classTestAttach.clear()
                                    binding.examDialog.attach.text = ""
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
        if(clsTestRes[pos].subject != null && clsTestRes[pos].subject!!.name != null){
            binding.examDialog.subName.text = clsTestRes[pos].subject!!.name
        }
        else {
            binding.examDialog.subName.text = "--/--"
        }
        if (clsTestRes[pos].classTest != null && clsTestRes[pos].classTest!!.title != null){
            binding.examDialog.testTitle.text = clsTestRes[pos].classTest!!.title
            binding.examDialog.chapter.text = clsTestRes[pos].classTest!!.title
        }
        else{
            binding.examDialog.testTitle.text = "--/--"
        }
        if (clsTestRes[pos].classTest != null && clsTestRes[pos].classTest!!.description != null){
            binding.examDialog.description.text = " * ${clsTestRes[pos].classTest!!.description}"
        }
        else{
            binding.examDialog.description.text = "--/--"
        }
        if (clsTestRes[pos].classTest != null && clsTestRes[pos].classTest!!.totalMarks != null){
            binding.examDialog.totalMarks.text = clsTestRes[pos].classTest!!.totalMarks!!.toString()
            if (clsTestRes[pos].classTest!!.createdBy != null){
                binding.examDialog.studentName.text = "${clsTestRes[pos].classTest!!.createdBy!!.firstName} ${clsTestRes[pos].classTest!!.createdBy!!.lastName}"
            }
            else{
                binding.examDialog.studentName.text = "--/--"
            }
        }
        else{
            binding.examDialog.totalMarks.text = "--/--"
        }

        if (clsTestRes[pos].status == "completed" && clsTestRes[pos].markStatus == "completed"){
            binding.examDialog.uploadLay.visibility = View.GONE
            binding.examDialog.attachLay.visibility = View.VISIBLE
            binding.examDialog.okCancel.visibility = View.GONE
            binding.examDialog.remarkLay.visibility = View.VISIBLE
            binding.examDialog.examStatus.text = "Submitted"
            binding.examDialog.next.text = "Done"
            binding.examDialog.next.visibility = View.VISIBLE
            //  UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#e6ffe7", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#32B138", null)
            UiUtils.textViewTextColor(binding.examDialog.remarks,"#32B138", null)
            val submittedOn = BaseUtils.getFormattedDate(clsTestRes[pos].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            if (clsTestRes[pos].credits != null && clsTestRes[pos].credits!!.toInt() > 0){
                binding.examDialog.credits.text = "+${clsTestRes[pos].credits} Points"
                UiUtils.textViewTextColor(binding.examDialog.credits,"#32B138", null)
            }
            else {
                binding.examDialog.credits.text = "0 Point"
                UiUtils.textViewTextColor(binding.examDialog.credits,"#32B138", null)
            }
            when(clsTestRes[pos].remarks){
                "verygood" -> {
                    binding.examDialog.remarks.text = "Outstanding performance! You’re doing great."
                    UiUtils.textViewGradient(binding.examDialog.remarks,"#32B138","#138f18")//green
                }
                "good" -> {
                    binding.examDialog.remarks.text = "Great job! Keep improving steadily."
                    UiUtils.textViewTextColor(binding.examDialog.remarks,"#3F8BFB",null) //blue
                }
                "poor" -> {
                    binding.examDialog.remarks.text = "Keep trying; you’ll get there soon."
                    UiUtils.textViewTextColor(binding.examDialog.remarks,"#F69300",null) //orange
                }
                "need_attention" -> {
                    binding.examDialog.remarks.text = "Work harder; success is within reach."
                    UiUtils.textViewTextColor(binding.examDialog.remarks,"#F69300",null) //orange
                }
            }

            val attachments = clsTestRes[pos].attachment
            if (!attachments.isNullOrEmpty()){
                binding.examDialog.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(mActivity, attachments)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
                binding.examDialog.attachRecycler.layoutManager = layoutManager
                binding.examDialog.attachRecycler.adapter = adapter
            }
            else {
                binding.examDialog.attachRecycler.visibility = View.GONE
            }
        }
        else if (clsTestRes[pos].status == "overdue" && clsTestRes[pos].status == "overdue"){
            binding.examDialog.uploadLay.visibility = View.GONE
            binding.examDialog.attachLay.visibility = View.GONE
            binding.examDialog.okCancel.visibility = View.VISIBLE
            binding.examDialog.uploadTxtt.visibility = View.GONE
            binding.examDialog.next.text = "Okay , I’ll Prepare for it"
            binding.examDialog.next.visibility = View.VISIBLE
            binding.examDialog.remarkLay.visibility = View.VISIBLE
            binding.examDialog.credits.visibility = View.GONE
            binding.examDialog.remarks.text = "Your child class test not updated "
            binding.examDialog.examStatus.text = "Not Completed"
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#fce6e6", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#EA5455", null)
            UiUtils.textViewTextColor(binding.examDialog.remarks,"#EA5455", null)
        }
        else if (clsTestRes[pos].status == "pending" && clsTestRes[pos].status == "pending"){
            binding.examDialog.uploadLay.visibility = View.GONE
            binding.examDialog.attachLay.visibility = View.GONE
            binding.examDialog.uploadTxtt.visibility = View.GONE
            binding.examDialog.okCancel.visibility = View.GONE
            binding.examDialog.next.text = "Okay , I’ll Prepare for it"
            binding.examDialog.next.visibility = View.VISIBLE
            binding.examDialog.remarkLay.visibility = View.VISIBLE
            binding.examDialog.credits.visibility = View.GONE
            binding.examDialog.examStatus.text = "Ongoing"
            binding.examDialog.remarks.text = "Your child class test not updated "
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#fff2d9", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#F69300", null)
        }
        else if (clsTestRes[pos].status == "completed" && clsTestRes[pos].markStatus == "pending"){
            binding.examDialog.uploadLay.visibility = View.GONE
            binding.examDialog.attachLay.visibility = View.VISIBLE
            binding.examDialog.okCancel.visibility = View.GONE
            binding.examDialog.remarkLay.visibility = View.VISIBLE
            binding.examDialog.next.visibility = View.VISIBLE
            binding.examDialog.next.text = "Okay"
            binding.examDialog.examStatus.text = "Submitted"
            binding.examDialog.remarks.text = "Your Points update later"
            binding.examDialog.credits.visibility = View.GONE
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#e6ffe7", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#32B138", null)
            UiUtils.textViewTextColor(binding.examDialog.remarks,"#32B138", null)
            val submittedOn = BaseUtils.getFormattedDate(clsTestRes[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)

            val attachments = clsTestRes[pos].attachment
            if (!attachments.isNullOrEmpty()){
                binding.examDialog.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(mActivity, attachments)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
                binding.examDialog.attachRecycler.layoutManager = layoutManager
                binding.examDialog.attachRecycler.adapter = adapter
            }
            else {
                binding.examDialog.attachRecycler.visibility = View.GONE
            }
        }
    }

    fun getHomeworkResult(pos: Int){
        mActivity.binding.dialogHomework.uploadLay.visibility = View.GONE

        mActivity.binding.dialogHomework.close.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
            mActivity.binding.dialogHomework.attach.text = ""
        }
        mActivity.binding.dialogHomework.cancel.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
            mActivity.binding.dialogHomework.attach.text = ""
        }
        mActivity.binding.dialogHomework.doneHw.setOnClickListener {
            mActivity.binding.dialogHomework.root.visibility = View.GONE
            mActivity.binding.dialogHomework.attach.text = ""
        }
        mActivity.binding.dialogHomework.points.text = "+${hwResponse[pos].credits} Points"
        if (hwResponse[pos].subject != null){
            mActivity.binding.dialogHomework.subject.text = "Subject : ${hwResponse[pos].subject!!.name}"
        }
        else {
            mActivity.binding.dialogHomework.subject.text = "Subject : --/--"
        }

        if (hwResponse[pos].homework != null){
            mActivity.binding.dialogHomework.que.text = hwResponse[pos].homework!!.title
            mActivity.binding.dialogHomework.desc.text = hwResponse[pos].homework!!.description
            if (hwResponse[pos].homework!!.createdBy != null){
                mActivity.binding.dialogHomework.teacher.text = "Teacher : ${hwResponse[pos].homework!!.createdBy!!.firstName +" "+ hwResponse[pos].homework!!.createdBy!!.lastName}"
            }
            else {
                mActivity.binding.dialogHomework.teacher.text = "Teacher : --/--"
            }
        }
        else {
            mActivity.binding.dialogHomework.que.text = "--/--"
            mActivity.binding.dialogHomework.desc.text = "--/--"
        }
        val givenDate = BaseUtils.getFormattedDate(hwResponse[pos].createdAt!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        val dueDate = BaseUtils.getFormattedDate(hwResponse[pos].dueDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        if (hwResponse[pos].status == "completed"){
            mActivity.binding.dialogHomework.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.attachLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.doneHw.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.remarkLay.visibility = View.VISIBLE
            mActivity.binding.dialogHomework.status.text = "Submitted"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogHomework.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogHomework.status,"#e6ffe7", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogHomework.status,"#32B138", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#32B138", null)
            val submittedOn = BaseUtils.getFormattedDate(hwResponse[pos].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            mActivity.binding.dialogHomework.submitOn.text = "Submitted On : "
            mActivity.binding.dialogHomework.submitDate.text = submittedOn
            mActivity.binding.dialogHomework.givenDate.text = givenDate

            if (hwResponse[pos].credits != null && hwResponse[pos].credits!!.isNotEmpty() && hwResponse[pos].credits!!.toInt() > 0){
                mActivity.binding.dialogHomework.points.text = "+${hwResponse[pos].credits} Points"
                UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#32B138", null)
            }
            else {
                mActivity.binding.dialogHomework.points.text = "0 Point"
                UiUtils.textViewTextColor(mActivity.binding.dialogHomework.points,"#EA5455", null)
            }
            if (hwResponse[pos].markStatus == "pending"){
                mActivity.binding.dialogHomework.noteCheck.visibility = View.VISIBLE
                UiUtils.imageviewDrawable(mActivity.binding.dialogHomework.noteCheck,R.drawable.green_tick)
                mActivity.binding.dialogHomework.remarkLay.visibility = View.GONE
                mActivity.binding.dialogHomework.note.text = "Your Child has marked this homework as completed. Please check the status of the homework."
            }
            else {
                mActivity.binding.dialogHomework.remarkLay.visibility = View.VISIBLE
                mActivity.binding.dialogHomework.noteCheck.visibility = View.GONE
                if (hwResponse[pos].submittedOnTime){
                    mActivity.binding.dialogHomework.note.text = "Your child has submitted this homework on time. Keep encouraging them to stay consistent!"
                    UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#EFFFF0",null)
                }
                else {
                    mActivity.binding.dialogHomework.note.text = "The homework was submitted late. Timely submissions can maximize their points and learning potential."
                    UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#fafce3",null)
                }

                when(hwResponse[pos].remarks){
                    "verygood" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Excellent work! Your child is excelling."
                        UiUtils.textViewGradient(mActivity.binding.dialogHomework.remarks,"#32B138","#138f18")//green
                    }
                    "good" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Great effort! Keep improving steadily."
                        UiUtils.textViewTextColor(mActivity.binding.dialogHomework.remarks,"#3F8BFB",null) //blue
                    }
                    "poor" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Needs more attention. Encourage and support them."
                        UiUtils.textViewTextColor(mActivity.binding.dialogHomework.remarks,"#F69300",null) //orange
                    }
                    "need_attention" -> {
                        mActivity.binding.dialogHomework.remarks.text = "Focus is needed. With guidance, success is within reach."
                        UiUtils.textViewTextColor(mActivity.binding.dialogHomework.remarks,"#F69300",null) //orange
                    }
                }
            }

            if (hwResponse[pos].attachment!! != null && hwResponse[pos].attachment!!.isNotEmpty()){
                mActivity.binding.dialogHomework.attachRecycler.visibility = View.VISIBLE
                mActivity.binding.dialogHomework.uploadTxtt.text = "Review Child's Attachments"
                val adapter = AttachAdapter(mActivity,hwResponse[pos].attachment!!)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
                mActivity.binding.dialogHomework.attachRecycler.layoutManager = layoutManager
                mActivity.binding.dialogHomework.attachRecycler.adapter = adapter
            }
            else {
                mActivity.binding.dialogHomework.attachRecycler.visibility = View.GONE
            }
        }
        else if (hwResponse[pos].status == "overdue"){
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
            mActivity.binding.dialogHomework.note.text = "This Homework was due on $dueDate, and it looks like your child haven’t finished it yet. Please guild him/her to complete it"
        }
        else if (hwResponse[pos].status == "pending"){
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
            mActivity.binding.dialogHomework.note.text = "Today's homework has been updated. Track your child's progress."
            UiUtils.linearLayoutBgTint(mActivity.binding.dialogHomework.noteLay,"#FFF2F2",null)
        }
        UiUtils.animation(mActivity,mActivity.binding.dialogHomework.topLay,R.anim.slide_in_from_bottom,true)
        mActivity.binding.dialogHomework.root.visibility = View.VISIBLE
    }

    fun getProjectResult(pos : Int){
        mActivity.binding.dialogProject.close.setOnClickListener {
            mActivity.binding.dialogProject.root.visibility = View.GONE
        }
        mActivity.binding.dialogProject.resultLay.visibility  = View.GONE
        mActivity.binding.dialogProject.done.visibility  = View.VISIBLE
        mActivity.binding.dialogProject.uploadLay.visibility = View.GONE
        mActivity.binding.dialogProject.done.text = "Okay, Got It, I’ll Guide Them"

        mActivity.binding.dialogProject.cancel.setOnClickListener {
            mActivity.binding.dialogProject.root.visibility = View.GONE
        }
        mActivity.binding.dialogProject.done.setOnClickListener {
            mActivity.binding.dialogProject.root.visibility = View.GONE
        }

        if (projectRes[pos].subject != null && projectRes[pos].subject!!.name != null ){
            mActivity.binding.dialogProject.subject.text = projectRes[pos].subject!!.name!!
        }
        else {
            mActivity.binding.dialogProject.subject.text = "--/--"
        }
        if (projectRes[pos].project != null){
            mActivity.binding.dialogProject.projectTitle.text = projectRes[pos].project!!.title
            mActivity.binding.dialogProject.tMarks.text = " / ${projectRes[pos].project!!.totalMarks}"
            mActivity.binding.dialogProject.desc.setContent(projectRes[pos].project!!.description)
            if (projectRes[pos].project!!.createdBy != null){
                mActivity.binding.dialogProject.teacher.text = projectRes[pos].project!!.createdBy!!.firstName +" "+ projectRes[pos].project!!.createdBy!!.lastName
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

        val dueDate = BaseUtils.getFormattedDate(projectRes[pos].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        val submitDate = BaseUtils.getFormattedDate(projectRes[pos].submittedOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        if (projectRes[pos].status == "completed"){
            mActivity.binding.dialogProject.resultLay.visibility  = View.VISIBLE
            mActivity.binding.dialogProject.done.visibility  = View.VISIBLE
            mActivity.binding.dialogProject.done.text = "Done"
            mActivity.binding.dialogProject.uploadLay.visibility = View.GONE
            mActivity.binding.dialogProject.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.attachLay.visibility = View.VISIBLE
            mActivity.binding.dialogProject.done.visibility  = View.VISIBLE
            mActivity.binding.dialogProject.status.text = "Submitted"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogProject.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogProject.status,"#e6ffe7", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogProject.status,"#32B138", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogProject.points,"#32B138", null)
            mActivity.binding.dialogProject.submitOn.text = "Submitted On : "
            mActivity.binding.dialogProject.submitdate.text = submitDate
            if (projectRes[pos].submittedOnTime){
                mActivity.binding.dialogProject.note.text = "Your child has submitted this project on time. Keep encouraging them to stay consistent!"
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogProject.noteLay,"#EFFFF0",null)
            }
            else {
                mActivity.binding.dialogProject.note.text = "The project was submitted late. Timely submissions can maximize their points and learning potential."
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogProject.noteLay,"#fafce3",null)
            }

            if (projectRes[pos].credits != null && projectRes[pos].credits!!.isNotEmpty() && projectRes[pos].credits!!.toInt() > 0){
                mActivity.binding.dialogProject.points.text = "+${projectRes[pos].credits} Points"
                UiUtils.textViewTextColor(mActivity.binding.dialogProject.points,"#32B138", null)
            }
            else {
                mActivity.binding.dialogProject.points.text = "0 Point"
                UiUtils.textViewTextColor(mActivity.binding.dialogProject.points,"#EA5455", null)
            }
            if (projectRes[pos].markStatus == "pending"){
                mActivity.binding.dialogProject.resultLay.visibility = View.GONE
                mActivity.binding.dialogProject.remarks.text = "Not yet updated"
                UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#333333",null)
            }
            else {
                mActivity.binding.dialogProject.resultLay.visibility = View.VISIBLE
                mActivity.binding.dialogProject.sMarks.text = projectRes[pos].scored_marks
                when(projectRes[pos].remarks){
                    "verygood" -> {
                        mActivity.binding.dialogProject.remarks.text = "Excellent work! Your child is excelling."
                        UiUtils.textViewGradient(mActivity.binding.dialogProject.remarks,"#32B138","#138f18")
                    }
                    "good" -> {
                        mActivity.binding.dialogProject.remarks.text = "Great effort! Keep improving steadily."
                        UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#3F8BFB",null)
                    }
                    "poor" -> {
                        mActivity.binding.dialogProject.remarks.text = "Needs more attention. Encourage and support them."
                        UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#F69300",null)
                    }
                    "need_attention" -> {
                        mActivity.binding.dialogProject.remarks.text = "Focus is needed. With guidance, success is within reach."
                        UiUtils.textViewTextColor(mActivity.binding.dialogProject.remarks,"#F69300",null)
                    }
                }
            }

            if (projectRes[pos].attachment != null && projectRes[pos].attachment!!.isNotEmpty()){
                mActivity.binding.dialogProject.attachRecycler.visibility = View.VISIBLE
                mActivity.binding.dialogProject.uploadTxtt.text = "Review Child's Attachments"
                val adapter = AttachAdapter(mActivity,projectRes[pos].attachment!!)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
                mActivity.binding.dialogProject.attachRecycler.layoutManager = layoutManager
                mActivity.binding.dialogProject.attachRecycler.adapter = adapter
            }
            else {
                mActivity.binding.dialogProject.attachRecycler.visibility = View.GONE
            }
        }
        else if (projectRes[pos].status == "overdue"){
            mActivity.binding.dialogProject.resultLay.visibility  = View.GONE
            mActivity.binding.dialogProject.done.visibility  = View.VISIBLE
            mActivity.binding.dialogProject.done.text = "I'll Remind My Child"
            mActivity.binding.dialogProject.uploadLay.visibility = View.GONE
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
            mActivity.binding.dialogProject.note.text = "This Project was due on $dueDate, and it looks like your child hasn't finished it yet. Please guide him/her to complete it as soon as possible."
        }
        else if (projectRes[pos].status == "pending"){
            mActivity.binding.dialogProject.done.visibility  = View.VISIBLE
            mActivity.binding.dialogProject.done.text = "I'll Remind My Child"
            mActivity.binding.dialogProject.uploadLay.visibility = View.GONE
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
            mActivity.binding.dialogProject.note.text = "New project has been given. Track your child's progress."
            UiUtils.linearLayoutBgTint(mActivity.binding.dialogProject.noteLay,"#FFF2F2",null)
        }

        UiUtils.animation(mActivity,mActivity.binding.dialogProject.cardView,R.anim.slide_in_from_bottom,true)
        mActivity.binding.dialogProject.root.visibility = View.VISIBLE
    }


    fun getAssignmentResult(pos: Int){
        mActivity.binding.dialogAssignment.cancel.setOnClickListener {
            mActivity.binding.dialogAssignment.root.visibility = View.GONE
        }
        mActivity.binding.dialogAssignment.resultLay.visibility = View.GONE
        mActivity.binding.dialogAssignment.doneAssingment.setOnClickListener {
            mActivity.binding.dialogAssignment.root.visibility = View.GONE
        }
        mActivity.binding.dialogAssignment.doneAssingment.visibility = View.VISIBLE
        mActivity.binding.dialogAssignment.doneAssingment.text = "Okay, Got It, I’ll Guide Them"

        mActivity.binding.dialogAssignment.close.setOnClickListener {
            mActivity.binding.dialogAssignment.root.visibility = View.GONE
        }
        if (assignmentRes[pos].assignment != null && assignmentRes[pos].assignment!!.createdBy != null && assignmentRes[pos].assignment!!.createdBy!!.firstName != null && assignmentRes[pos].assignment!!.createdBy!!.lastName != null){
            mActivity.binding.dialogAssignment.teacher.text = "Teacher : ${assignmentRes[pos].assignment!!.createdBy!!.firstName} ${assignmentRes[pos].assignment!!.createdBy!!.lastName}"
        }
        else{
            mActivity.binding.dialogAssignment.teacher.text = "--/--"
        }
        if (assignmentRes[pos].subject != null && assignmentRes[pos].subject!!.name!!.isNotEmpty()){
            mActivity.binding.dialogAssignment.subject.text = "Subject : ${assignmentRes[pos].subject!!.name}"
        }
        else{
            mActivity.binding.dialogAssignment.subject.text = "--/--"
        }
        if (assignmentRes[pos].assignment != null && assignmentRes[pos].assignment!!.title != null && assignmentRes[pos].assignment!!.description != null){
            mActivity.binding.dialogAssignment.assTitle.text = assignmentRes[pos].assignment!!.title
            mActivity.binding.dialogAssignment.tMarks.text = " / ${assignmentRes[pos].assignment!!.totalMarks}"
            mActivity.binding.dialogAssignment.assdsc.setContent(assignmentRes[pos].assignment!!.description)
        }
        else{
            mActivity.binding.dialogAssignment.assTitle.text = "--/--"
            mActivity.binding.dialogAssignment.assdsc.text = "--/--"
        }
        if (assignmentRes[pos].createdAt != null){
            mActivity.binding.dialogAssignment.givendate.text = BaseUtils.getFormattedDate(assignmentRes[pos].createdAt!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            mActivity.binding.dialogAssignment.givendate.text = "--/--"
        }
        var dueDate = BaseUtils.getFormattedDate(assignmentRes[pos].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        var submitDate = BaseUtils.getFormattedDate(assignmentRes[pos].submittedOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)

        if (assignmentRes[pos].status == "completed"){
            mActivity.binding.dialogAssignment.doneAssingment.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.doneAssingment.text = "Done"
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
            if (assignmentRes[pos].submittedOnTime){
                mActivity.binding.dialogAssignment.note.text = "Your child has submitted this assignment on time. Keep encouraging them to stay consistent!"
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogAssignment.noteLay,"#EFFFF0",null)
            }
            else {
                mActivity.binding.dialogAssignment.note.text = "The assignment was submitted late. Timely submissions can maximize their points and learning potential."
                UiUtils.linearLayoutBgTint(mActivity.binding.dialogAssignment.noteLay,"#fafce3",null)
            }

            if (assignmentRes[pos].credits != null && assignmentRes[pos].credits!!.toInt() > 0){
                mActivity.binding.dialogAssignment.points.text = "+${assignmentRes[pos].credits} Points"
                UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.points,"#32B138", null)
            }
            else {
                mActivity.binding.dialogAssignment.points.text = "0 Point"
                UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.points,"#EA5455", null)
            }
            if (assignmentRes[pos].markStatus == "pending"){
                mActivity.binding.dialogAssignment.resultLay.visibility = View.GONE
                mActivity.binding.dialogAssignment.remarks.text = "Not yet updated"
                UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#333333",null)
            }
            else {
                mActivity.binding.dialogAssignment.resultLay.visibility = View.VISIBLE
                mActivity.binding.dialogAssignment.sMarks.text = assignmentRes[pos].scored_marks.toString()
                when(assignmentRes[pos].remarks){
                    "verygood" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Excellent work! Your child is excelling."
                        UiUtils.textViewGradient(mActivity.binding.dialogAssignment.remarks,"#32B138","#138f18")
                    }
                    "good" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Great effort! Keep improving steadily."
                        UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#3F8BFB",null)
                    }
                    "poor" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Needs more attention. Encourage and support them."
                        UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#F69300",null)
                    }
                    "need_attention" -> {
                        mActivity.binding.dialogAssignment.remarks.text = "Focus is needed. With guidance, success is within reach."
                        UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.remarks,"#F69300",null)
                    }
                }
            }

            if (assignmentRes[pos].attachment!! != null && assignmentRes[pos].attachment!!.isNotEmpty()){
                mActivity.binding.dialogAssignment.attachRecycler.visibility = View.VISIBLE
                mActivity.binding.dialogAssignment.uploadTxtt.text = "Review Child's Attachments"
                val adapter = AttachAdapter(mActivity,assignmentRes[pos].attachment!!)
                val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
                mActivity.binding.dialogAssignment.attachRecycler.layoutManager = layoutManager
                mActivity.binding.dialogAssignment.attachRecycler.adapter = adapter
            }
            else {
                mActivity.binding.dialogAssignment.attachRecycler.visibility = View.GONE
            }
        }
        else if (assignmentRes[pos].status == "overdue"){
            mActivity.binding.dialogAssignment.doneAssingment.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.doneAssingment.text = "I'll Remind My Child"
            mActivity.binding.dialogAssignment.uploadLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.attachLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.resultLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.status.text = "Not Completed"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogAssignment.status,"#fce6e6", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.status,"#EA5455", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.points,"#EA5455", null)
            mActivity.binding.dialogAssignment.submitOn.text = "Last Date : "
            mActivity.binding.dialogAssignment.submitdate.text = dueDate
            mActivity.binding.dialogAssignment.note.text = "This Assignment was due on $dueDate, and it looks like your child hasn't finished it yet. Please guide him/her to complete it."

        }
        else if (assignmentRes[pos].status == "pending"){
            mActivity.binding.dialogAssignment.resultLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.doneAssingment.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.doneAssingment.text = "I'll Remind My Child"
            mActivity.binding.dialogAssignment.uploadLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.attachLay.visibility = View.GONE
            mActivity.binding.dialogAssignment.points.visibility = View.GONE
            mActivity.binding.dialogAssignment.status.text = "Ongoing"
            UiUtils.textviewCustomDrawable(mActivity.binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(mActivity.binding.dialogAssignment.status,"#fff2d9", null)
            UiUtils.textViewTextColor(mActivity.binding.dialogAssignment.status,"#F69300", null)
            mActivity.binding.dialogAssignment.submitOn.text = "Last Date : "
            mActivity.binding.dialogAssignment.submitdate.text = dueDate
            mActivity.binding.dialogAssignment.note.text = "New assignment has been given. Track your child's progress."
            UiUtils.linearLayoutBgTint(mActivity.binding.dialogAssignment.noteLay,"#FFF2F2",null)
            mActivity.binding.dialogAssignment.doneAssingment.visibility = View.VISIBLE
            mActivity.binding.dialogAssignment.doneAssingment.text = "Okay, Got It, I’ll Guide Them"
        }

        UiUtils.animation(mActivity,mActivity.binding.dialogAssignment.topLay,R.anim.slide_in_from_bottom,true)
        mActivity.binding.dialogAssignment.root.visibility = View.VISIBLE
    }

//    fun loadProgressBar(value: Float) {
//        val circularProgressBar: CircularProgressBar = binding.progress
//        circularProgressBar.progressBarColor =
//            ContextCompat.getColor(mActivity, R.color.progress_bar)
//        circularProgressBar.setBackgroundColor(
//            ContextCompat.getColor(
//                mActivity,
//                R.color.progress_bar_back
//            )
//        )
//        circularProgressBar.progressBarWidth = resources.getDimension(R.dimen._6sp)
//        circularProgressBar.backgroundProgressBarWidth = resources.getDimension(R.dimen._6sp)
//        val animationDuration = 1500L
//        circularProgressBar.setProgressWithAnimation(value, animationDuration)
//    }

/*
    private fun loadProgressBar(result: GetStudentAttenDanceResponse.Result) {
    val entries: ArrayList<PieEntry>
    if (result.progress != null &&
        (result.progress!!.presentPercentage != null || result.progress!!.absentPercentage != null)) {

        val present = result.progress!!.presentPercentage!!.toFloat()
        val absent = result.progress!!.absentPercentage!!.toFloat()

        entries = arrayListOf(
            PieEntry(present, "Present"),
            PieEntry(absent, "Absent")
        )

        Log.d("gsdf", "present $present")
        Log.d("gsdf", "absent $absent")

    } else {
        entries = arrayListOf(
            PieEntry(0f, "Present"),
            PieEntry(0f, "Absent")
        )
    }*/
/*
       Log.d("gsdf","present ${result.progress!!.presentPercentage!!.toFloat()}")
       Log.d("gsdf","absent ${result.progress!!.absentPercentage!!.toFloat()}")*//*


    val colors = listOf(
        Color.parseColor("#32B138"), // red
        Color.parseColor("#FF7475")  // Green
    )

    val dataSet = PieDataSet(entries, "").apply {
        setColors(colors)
        sliceSpace = 8f  // Space between segments
        selectionShift = 5f
        setDrawValues(false) // Hide percentage values
    }

    val pieData = PieData(dataSet)

    attenDanceProgress.apply {
        data = pieData
        description.isEnabled = false
        isRotationEnabled = false
        setDrawEntryLabels(false)
        setDrawHoleEnabled(true)
        holeRadius = 80f
        setTouchEnabled(false)
        legend.isEnabled = false

        renderer = CurvedPieChartRenderer(this, animator, this.viewPortHandler)

        invalidate()
    }
}
*/

    private fun getFees() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentFees(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            binding.feesRecycler.visibility = View.VISIBLE
                            binding.noDataFee.root.visibility = View.GONE
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                            val adapter = StudentFeesAdapter(mActivity,it.result!!.terms!!)
                            binding.feesRecycler.layoutManager = linearLayoutManager
                            binding.feesRecycler.adapter = adapter
                        } else {
                            binding.feesRecycler.visibility = View.GONE
                            binding.noDataFee.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.feesRecycler.visibility = View.GONE
                        binding.noDataFee.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
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
                            if (::timer.isInitialized) {
                                timer.cancel()
                            }
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
                            timer = object : CountDownTimer(Long.MAX_VALUE, 5000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    val currentPage = binding.viewPager.currentItem
                                    val nextPage = (currentPage + 1) % it.result.size
                                    Log.e("SDv", "Current Page: $currentPage, Next Page: $nextPage")
                                    binding.viewPager.setCurrentItem(nextPage, true)
                                }

                                override fun onFinish() {}
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

    fun getNoticeBoard(){
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
                        UiUtils.showSnack(it.msg, binding.root,false) }
                }
            }
        }
    }

    fun getClassTimeTable(){
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
                                }
                            })
                            val layoutManager = LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false)
                            binding.timetableRecycler.layoutManager = layoutManager
                            binding.timetableRecycler.adapter = adapter
                        } else {
                            binding.noDataTimeTable.txt.text = "No Time table Available"
                            binding.noDataTimeTable.root.visibility = View.VISIBLE
                            binding.timetableRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noDataTimeTable.txt.text = "No Time table Available"
                        binding.noDataTimeTable.root.visibility = View.VISIBLE
//                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun getClassTimeTableHome(){
        if (timeTableRes.isEmpty()){
            DialogUtils.showLoader(mActivity)
            ApiConnection.getInstance().getClassTimetable(mActivity,"subject",timeTableDay).observe(mActivity){
                it?.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if(success){
                            if (it.result != null && it.result!!.periods!!.isNotEmpty()){
                                loadDates1()
                                Log.d("oiuytfgvb", it.result!!.periods!!.toString())
                                binding.timeTableNoData.root.visibility = View.GONE
                                binding.todayTimeTableRecycler.visibility = View.VISIBLE
                                timeTableRes = it.result!!.periods!!
                                val adapter = TimeTableAdapter(mActivity,true,it.result!!.periods!!,object : OnClickListener{
                                    override fun onClickItem(pos: Int) {
                                        mActivity.binding.timeTableDialog.root.visibility = View.VISIBLE
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
            val adapter = TimeTableAdapter(mActivity,true,timeTableRes,object : OnClickListener{
                override fun onClickItem(pos: Int) {
                    mActivity.binding.timeTableDialog.root.visibility = View.VISIBLE
                }
            })
            val layoutManager = GridLayoutManager(mActivity,4,RecyclerView.VERTICAL,false)
            binding.todayTimeTableRecycler.layoutManager = layoutManager
            binding.todayTimeTableRecycler.adapter = adapter
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
                getClassTimeTableHome()
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

    fun RecyclerView.centerItem(position: Int, itemWidth: Int) {
        post {
            val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
            val visibleWidth = width - paddingLeft - paddingRight
            val exactCenter = (visibleWidth / 2) - (itemWidth / 2)
            layoutManager.scrollToPositionWithOffset(position, exactCenter)
        }
    }


    /*
        fun RecyclerView.centerItem(position: Int) {
            post {
                val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
                layoutManager.scrollToPosition(position)
                post {
                    val child = layoutManager.findViewByPosition(position) ?: return@post
                    val isVertical = layoutManager.orientation == LinearLayoutManager.VERTICAL
                    val recyclerSize = if (isVertical) height - paddingTop - paddingBottom else width - paddingStart - paddingEnd
                    val itemSize = if (isVertical) child.height else child.width
                    val offset = (recyclerSize / 2) - (itemSize / 2)
                    layoutManager.scrollToPositionWithOffset(position, offset)
                }
            }
        }
    */

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

    private fun submissionProgress(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().submissionProgress(mActivity,type).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        val result = it.result!!
                        if (result != null ) {
                            setupPieChart(result)
                            if (result.total != null){
                                binding.total.text = "Total Submission : ${result.total}"
                            }
                            else{
                                binding.total.text = "--/--"
                            }
                            if (result.points != null){
                                binding.points.text = "Total Points : ${result.points}"
                            }
                            else{
                                binding.points.text = "--/--"
                            }
                            if (result.percentage != null && result.percentage!!.onTime != null){
                                binding.subPercent.text = "On-time \n ${result.percentage!!.onTime}%"
                            }
                            else{
                                binding.subPercent.text = "--/--"
                            }
                            if (result.missed != null){
                                binding.missed.text = "Missed : ${result.missed}"
                            }
                            else{
                                binding.missed.text = "--/--"
                            }
                            if (result.onTime != null){
                                binding.ontime.text = "Ontime : ${result.onTime}"
                            }
                            else{
                                binding.ontime.text = "--/--"
                            }
                            if (result.late != null){
                                binding.late.text = "Late : ${result.late}"
                            }
                            else{
                                binding.late.text = "--/--"
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

    private fun setupPieChart(result: SubmissionProgressResponse.Result) {
        val ontime = result.percentage?.onTime?.toFloat() ?: 0f
        val missed = result.percentage?.missed?.toFloat() ?: 0f
        val late = result.percentage?.late?.toFloat() ?: 0f

        val entries: ArrayList<PieEntry>
        if (ontime == 0f && missed == 0f && late == 0f) {
            entries = arrayListOf(PieEntry(1f, ""))
        } else {
            entries = arrayListOf(
                PieEntry(ontime, "Ontime"),
                PieEntry(missed, "Missed"),
                PieEntry(late, "Late")
            )
            val colors = listOf(
                Color.parseColor("#32B138"), // green
                Color.parseColor("#FF7475"), // blue
                Color.parseColor("#FF9900")  // red
            )
            val dataSet = PieDataSet(entries, "").apply {
                setColors(colors)
                sliceSpace = 8f
                setDrawValues(false)
            }
            val pieData = PieData(dataSet)
            binding.pieChart.apply {
                data = pieData
                description.isEnabled = false
                isRotationEnabled = false
                setDrawEntryLabels(false)
                setDrawHoleEnabled(true)
                holeRadius = 60f
                transparentCircleRadius = 0f
                setTouchEnabled(false)
                legend.isEnabled = false
                setHoleColor(Color.TRANSPARENT)
                renderer = CurvedPieChartRenderer(this, animator, viewPortHandler)
                animateY(1000, Easing.EaseInOutCubic)
                invalidate()
            }
        }
    }

    private fun getStudentAttendance(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getParentChildAttendance(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        resultAtt = it.result!!
                      //  Log.d("hgsd","result = ${result}")
                        if (it.result != null ) {
                            loadProgressBar(resultAtt)
                            if (resultAtt!!.streaks != null){
                                Log.d("lkjhgfghjngfy", resultAtt.toString())
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
                                examProgressBarChart(resultExam)
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
    private fun clsTestBarChart(result: ArrayList<GetStudentClassTestProgress.Result>) {
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

        // Bar DataSet with value labels on top
        val dataSet = BarDataSet(entries, "").apply {
            this.colors = colors
            setDrawValues(true)
            valueTextSize = 11f
            valueTypeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
            valueTextColor = Color.BLACK
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return "${barEntry?.y?.toInt() ?: 0}%"
                }
            }
        }

        // Custom bar renderer (if applicable)
        barChart1.renderer = TopRendarCurveBarChartColors(
            barChart1,
            barChart1.animator,
            barChart1.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.8f

        barChart1.apply {
            data = barData

            // X Axis setup
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 11f
                setDrawLabels(false)
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
            }

            // Y Axis (Left)
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

            // Chart interaction
            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDragEnabled(true)

            // Marker for tooltips
            marker = CustomMarkerView2(mActivity, result)

            // Value selection listener
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
                    // No action needed
                }
            })

            // Layout settings
            setExtraOffsets(10f, 30f, 10f, 10f) // Increased top offset to show labels
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
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

    /*
        private fun clsTestBarChart(result: ArrayList<GetStudentClassTestProgress.Result>) {
            barChart1.clear()
            barChart1.marker = null
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

            barChart1.renderer = TopRoundedBarChartRenderer(
                barChart1,
                barChart1.animator,
                barChart1.viewPortHandler
            )

            val barData = BarData(dataSet)
            barData.barWidth = 0.3f

            barChart1.apply {
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
                barChart1.marker = null
                val markerView = CustomMarkerView2(mActivity, result)
                barChart1.marker = markerView
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
        barChart1.clear()
        barChart1.marker = null
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

        barChart1.renderer = TopRoundedBarChartRenderer(
            barChart1,
            barChart1.animator,
            barChart1.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.3f

        barChart1.apply {
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
            barChart1.setScaleEnabled(false)
            // Add Marker for Tooltips
            var markerView = CustomMarkerView3(mActivity, resultExam)
            barChart1.marker = markerView

            // Add rounded corners to bars
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            // Animation
            animateY(1000)
            setExtraOffsets(10f, 10f, 10f, 10f)

            invalidate()
        }
    }

    private fun loadProgressBar(result: GetStudentAttenDanceResponse.Result) {
        val progress = result.progress
        val present = progress?.presentPercentage?.toFloat() ?: 0f
        val absent = progress?.absentPercentage?.toFloat() ?: 0f
        val halfDay = progress?.halfDayPercentage?.toFloat() ?: 0f

        val entries = arrayListOf<PieEntry>()
        val colors = arrayListOf<Int>()

        val isEmptyData = progress == null || (present == 0f && absent == 0f && halfDay == 0f)

        if (isEmptyData) {
            entries.add(PieEntry(1f, "No Data"))
            colors.add(Color.parseColor("#BDBDBD")) // Gray for empty state
        } else {
            if (present > 0f) {
                entries.add(PieEntry(present, "Present"))
                colors.add(Color.parseColor("#FFC107")) // Yellow
            }
            if (halfDay > 0f) {
                entries.add(PieEntry(halfDay, "Half-Day"))
                colors.add(Color.parseColor("#AB47BC")) // Purple
            }
            if (absent > 0f) {
                entries.add(PieEntry(absent, "Absent"))
                colors.add(Color.parseColor("#FF4081")) // Pink
            }
        }

        val dataSet = PieDataSet(entries, "").apply {
            setDrawValues(false)
            sliceSpace = 3f
            this.colors = colors
            selectionShift = 0f
        }

        val pieData = PieData(dataSet)

        attenDanceProgress.apply {
            clear()
            data = pieData
            description.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            setDrawHoleEnabled(true)
            holeRadius = 120f
            setHoleColor(Color.BLACK)
            transparentCircleRadius = 0f
            legend.isEnabled = false
            isRotationEnabled = false
            setTouchEnabled(false)
            setMaxAngle(360f)
            rotationAngle = 270f
            setCenterText("")
            setExtraOffsets(50f, 50f, 50f, 50f)

            this.renderer = GradientPieChartRenderer(
                this, this.animator, this.viewPortHandler,
                colors
            )

            animateY(1000, Easing.EaseInOutCubic)
            invalidate()
        }
    }

    private fun initAdapter(inflater: LayoutInflater, root: ViewGroup) {
        binding.tabLayoutAll.addTab(binding.tabLayoutAll.newTab().setText(""))
        binding.tabLayoutAll.addTab(binding.tabLayoutAll.newTab().setText(""))
        binding.tabLayoutAll.addTab(binding.tabLayoutAll.newTab().setText(""))
        binding.tabLayoutAll.addTab(binding.tabLayoutAll.newTab().setText(""))
        binding.tabLayoutAll.addTab(binding.tabLayoutAll.newTab().setText(""))
        binding.tabLayoutAll.addTab(binding.tabLayoutAll.newTab().setText(""))
        binding.tabLayoutAll.addTab(binding.tabLayoutAll.newTab().setText(""))
        binding.tabLayoutAll.tabGravity = TabLayout.GRAVITY_START
        binding.tabLayoutAll.tabMode = TabLayout.MODE_SCROLLABLE

        val linear0: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab0 = linear0.findViewById<TextView>(R.id.tab)
        val icon0 = linear0.findViewById<ImageView>(R.id.icon)
        txttab0.text = "Diary"
        UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
        UiUtils.imageviewDrawable(icon0,R.drawable.tab_diary)
        UiUtils.imageViewTint(icon0,null,R.color.colorPrimary)
        txttab0.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(0)!!.customView = linear0

        val linear1: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab1 = linear1.findViewById<TextView>(R.id.tab)
        val icon1 = linear1.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon1,R.drawable.tab_homework)
        UiUtils.imageViewTint(icon1,null,R.color.black_varient3)
        txttab1.text = "Homework"
        UiUtils.textViewTextColor(txttab1, null, R.color.black_varient3)
        txttab1.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(1)!!.customView = linear1

        val linear2: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab2 = linear2.findViewById<TextView>(R.id.tab)
        val icon2 = linear2.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon2,R.drawable.tab_timetable)
        UiUtils.imageViewTint(icon2,null,R.color.black_varient3)
        txttab2.text = "Time Table"
        UiUtils.textViewTextColor(txttab2, null, R.color.black_varient3)
        txttab2.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(2)!!.customView = linear2

        val linear3: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab3 = linear3.findViewById<TextView>(R.id.tab)
        val icon3 = linear3.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon3,R.drawable.tab_exam)
        UiUtils.imageViewTint(icon3,null,R.color.black_varient3)
        txttab3.text = "Examination"
        UiUtils.textViewTextColor(txttab3, null, R.color.black_varient3)
        txttab3.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(3)!!.customView = linear3

        val linear4: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab4 = linear4.findViewById<TextView>(R.id.tab)
        val icon4 = linear4.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon4,R.drawable.tab_assignment)
        UiUtils.imageViewTint(icon4,null,R.color.black_varient3)
        txttab4.text = "Assignment"
        UiUtils.textViewTextColor(txttab4, null, R.color.black_varient3)
        txttab4.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(4)!!.customView = linear4


        val linear5: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab5 = linear5.findViewById<TextView>(R.id.tab)
        val icon5 = linear5.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon5,R.drawable.tab_project)
        UiUtils.imageViewTint(icon5,null,R.color.black_varient3)
        txttab5.text = "Projects"
        UiUtils.textViewTextColor(txttab5, null, R.color.black_varient3)
        txttab5.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(5)!!.customView = linear5


       /* val linear6: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab6 = linear6.findViewById<TextView>(R.id.tab)
        txttab6.text = "Progress"
        UiUtils.textViewTextColor(txttab6, null, R.color.black_varient3)
        txttab6.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(6)!!.customView = linear6*/

        val linear6: View = inflater.inflate(R.layout.custom_tab, root, false)
        val txttab6 = linear6.findViewById<TextView>(R.id.tab)
        val icon6 = linear6.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon6,R.drawable.tab_event)
        UiUtils.imageViewTint(icon6,null,R.color.black_varient3)
        txttab6.text = "News & Events"
        UiUtils.textViewTextColor(txttab6, null, R.color.black_varient3)
        txttab6.setTextAppearance(R.style.FontMedium)
        binding.tabLayoutAll.getTabAt(6)!!.customView = linear6

        binding.tabLayoutAll.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val linear0: View = tab.customView!!
                val txttab0 = linear0.findViewById<TextView>(R.id.tab)
                UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                txttab0.setTextAppearance(R.style.FontMedium)
                if (tab.position == 0) {
                    binding.pageDairy.visibility = View.VISIBLE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.commonCalender.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    loadDates1()
                    getClassTimeTableHome()
                } else if (tab.position == 1) {
                    binding.pageDairy.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.VISIBLE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    tabPos = "homework"
                    loadDates2()
                    studentHomework()
//                    binding.tabToday.performClick()
                } else if (tab.position == 2) {
                    binding.pageDairy.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.VISIBLE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    getClassTimeTable()
                    loadDates2()
                } else if (tab.position == 3) {
                    binding.pageDairy.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageExam.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.pageAssignment.visibility = View.GONE
                   // binding.assignLay.visibility = View.GONE
                    getClassTest()
                    classTestSts = ""
                   // getExamination()
                    loadDates2()
                    binding.pageNews.visibility = View.GONE
                    binding.classTest.performClick()
                } else if (tab.position == 4) {
                    binding.pageDairy.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    //binding.assignLay.visibility = View.GONE
                    binding.pageAssignment.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.pageNews.visibility = View.GONE
                    tabPos = "assignment"
                    getStudentAssignment()
                    loadDates2()
                    binding.ongoingTab.performClick()
                } else if (tab.position == 5) {
                    binding.pageDairy.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    //  binding.assignLay.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    tabPos = "project"
                    getProject()
                    loadDates2()
                    binding.ongoingTab.performClick()
                }
                else if (tab.position == 6) {
                    binding.pageDairy.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    // binding.assignLay.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    binding.pageNews.visibility = View.VISIBLE
                    getClassTimeTableHome()
                    loadDates2()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val linear1: View = tab.customView!!
                val txttab1 = linear1.findViewById<TextView>(R.id.tab)
                UiUtils.textViewTextColor(txttab1, null, R.color.black)
                txttab1.setTextAppearance(R.style.FontMedium)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
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

//    fun openDocList() {
//        if (BaseUtils.isPermissionsEnabled(mActivity, Constants.IntentKeys.STORAGE)) {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "*/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.putExtra(
//                Intent.EXTRA_MIME_TYPES, arrayOf(
//                    "image/png",
//                    "image/jpg",
//                    "image/jpeg",
//                    "application/pdf",
//                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
//                    "application/msword"
//                )
//            )
//            startActivityForResult(intent, 12)
//        } else {
//            BaseUtils.permissionsEnableRequest(mActivity, Constants.IntentKeys.STORAGE)
//        }
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 12 && resultCode == RESULT_OK) {
//            if (data != null){
//                if (data.clipData != null){
//                    var docs = data.clipData
//                    for (items in 0 until  docs!!.itemCount){
//                        count++
//                        val uri = docs.getItemAt(items).uri
//                        var filePart: MultipartBody.Part? = null
//                        if (uri != null) {
//                            val documentFile = DocumentFile.fromSingleUri(mActivity, uri)
//                            val fileInputStream = mActivity.contentResolver.openInputStream(uri)
//                            val mimeType = mActivity.contentResolver.getType(uri)
//                            val buffer = fileInputStream?.readBytes()
//                            fileInputStream?.close()
//                            if (buffer != null && mimeType != null) {
//                                val fileSize = buffer.size
//                                val fileSizeInMB = fileSize / (1024.0 * 1024.0)
//                                val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
//                                filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
//                                val size = BaseUtils.convertBytes(fileSize.toLong())
//                                Log.d("dv1", "" + documentFile?.name)
//                                Log.d("dv0", "" + uri.path)
//                                Log.d("dv3", "" + size)
//                                Log.d("dv4", "" + mimeType)
//                                if (fileSizeInMB <= 5){
//                                    val json = JSONObject()
//                                    json.put("name",documentFile?.name)
//                                    json.put("size",size)
//                                    json.put("type",mimeType)
////                                    urlName.add(json)
//                                    upload(filePart)
//                                } else {
//                                    UiUtils.showSnack("File size exceeds 50 MB", binding.root,false)
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    val uri = data?.data
//                    var filePart: MultipartBody.Part? = null
//                    count++
//                    if (uri != null) {
//                        val documentFile = DocumentFile.fromSingleUri(mActivity, uri)
//                        val fileInputStream = mActivity.contentResolver.openInputStream(uri)
//                        val mimeType = mActivity.contentResolver.getType(uri)
//                        val buffer = fileInputStream?.readBytes()
//                        fileInputStream?.close()
//                        if (buffer != null && mimeType != null) {
//                            val fileSize = buffer.size
//                            val fileSizeInMB = fileSize / (1024.0 * 1024.0)
//                            val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
//                            filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
//                            val size = BaseUtils.convertBytes(fileSize.toLong())
//                            Log.d("dv1", "" + documentFile?.name)
//                            Log.d("dv0", "" + uri.path)
//                            Log.d("dv3", "" + size)
//                            Log.d("dv4", "" + mimeType)
//                            if (fileSizeInMB <= 5){
//                                val json = JSONObject()
//                                json.put("name",documentFile?.name)
//                                json.put("size",size)
//                                json.put("type",mimeType)
////                                urlName.add(json)
//                                binding.examDialog.attach.text = documentFile?.name
//                                upload(filePart)
//                            } else {
//                                UiUtils.showSnack("File size exceeds 5 MB", binding.root,false)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//    fun upload(filepart: MultipartBody.Part){
//        DialogUtils.showLoader(mActivity)
//        ApiConnection.getInstance().uploadFile(mActivity, filepart).observe(this) {
//            it?.let {
//                DialogUtils.dismissLoader()
//                it.success.let { success ->
//                    if (success && it.result.isNotEmpty()) {
//                        UiUtils.showSnack(it.msg, binding.root,true)
//                        val url = it.result[0].location!!
//                        classTestAttach.add(url)
//                        homeworkAttach.add(url)
//                    }
//                    else {
//                        UiUtils.showSnack(it.msg, binding.root,false)
//                    }
//                }
//            }
//        }
//    }
}