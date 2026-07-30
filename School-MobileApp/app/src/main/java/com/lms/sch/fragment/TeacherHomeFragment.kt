package com.lms.sch.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayout
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.activity.ClassesActivity
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.adapter.EventsPagerAdapter
import com.lms.sch.adapter.ClassTestMasterAdapter
import com.lms.sch.adapter.ExamListAdapter
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.MySubjectsAdapter
import com.lms.sch.adapter.ProjectAdapter
import com.lms.sch.adapter.NoticeBoardAdapter
import com.lms.sch.adapter.TeacherHomeworkAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.TeacherAssignmentAdapter
import com.lms.sch.adapter.TimeTableTeacherAdapter
import com.lms.sch.adapter.WeekDayAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.databinding.FilterAssignmentBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.databinding.FragmentTeacherHomeBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.ProjectModelClass
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.response.GetTeacherAssignmentResponse
import com.lms.sch.response.GetTeacherClassTestResponse
import com.lms.sch.response.GetTeacherHomeWorkResponse
import com.lms.sch.response.GetTeacherScheduleResponse
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.response.AdminAttendanceResponse
import com.lms.sch.response.AttendanceProgressResponse
import com.lms.sch.response.GetOverallStudentAttendProgressRes
import com.lms.sch.response.SubmissionProgressTeacherSide
import com.lms.sch.response.TeacherAttendanceResponse
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.text.toInt

class TeacherHomeFragment : BaseFragment() {
    lateinit var binding: FragmentTeacherHomeBinding
    var classProject = ArrayList<ProjectModelClass>()
    var attendanceFilters = ArrayList<String>()
    var teacherAssignmentSts = ""
    var hwResult = ArrayList<GetTeacherHomeWorkResponse.Result.Rows>()
    var classTest = ArrayList<GetTeacherClassTestResponse.Result.Rows>()
    var timeTableRes = ArrayList<GetTeacherScheduleResponse.PeriodDetails>()
    var resultAtt = AttendanceProgressResponse.Result()
    var timeTableDay = ""
    var examTabClicked = ""
    var classTestSts = ""
    var eventDate = ""
    var type = ""
    var homeworkStatus = ""
    var clastestStatus = ""
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var selectedYear : Int = 0
    var selectedMonth : Int = 0
    var projectStatus = ""
    var testMenu = ""
    var examStatus = ""
    var eventType = ""
    var datefilter = ""
    var subId = ""
    var search = ""
    var isAttendanceFilter = false
    var clickedValue = ""
    var calendar = Calendar.getInstance()
    lateinit var timer: CountDownTimer
    lateinit var pieChart: PieChart
    lateinit var pieChart2: PieChart
    var result = ArrayList<GetTeacherAssignmentResponse.Result.Rows>()
    var tabPos = ""
    private var currentMonthDays = ArrayList<Date>()
    var noticeList = ArrayList<NoticeBoardResponse.Result>()
    var programId = ""
    var section = ""
    var name = ""
    var studentClass = ""
    var selectedRole = "STUDENT"
    lateinit var attenDanceProgress : PieChart


    override fun onCreateView( inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(0)
        binding.logo.setOnLongClickListener {
            if (BuildConfig.DEBUG) {
                ApiDataDialog(mActivity).show(mActivity)
            }
            return@setOnLongClickListener true
        }

        binding.myRegister.visibility = View.VISIBLE

        pieChart2 = binding.pieChartAss
        attenDanceProgress = binding.attenDanceProgress
        binding.teacherName.text = SharedHelper(mActivity).name
        binding.shineTxt.text = "You're guiding: Grade ${SharedHelper(mActivity).studentClass} - section ${SharedHelper(mActivity).section}"
        UiUtils.textViewGradient(binding.shineTxt, "#232B68", "#4555CE")
        initAdapter(inflater, binding.root)
        currentMonthDays = getCurrentMonthDays()
//        loadEvents()
        binding.showTxt.text = "Showing ${timeTableDay.capitalize()} Timetable"

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacherProgram(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            val adapter = MySubjectsAdapter(mActivity,it.result!!,object: OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    programId = it.result!![pos]._id!!
                                    if (!it.result!![pos].myClass!!){
                                        subId = it.result!![pos].subject!!.subjectId!!._id!!
                                    }
                                    else {
                                        subId = ""
                                    }

                                }
                            })
                        } else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
        getClassTimeTableHome()
        type = "homework"
        submissionProgress()
        selectedRole = "STUDENT"
        studentAttendance()

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
        binding.dtText1.text = sdfMon
        timeTableDay = api
        eventDate = date

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
        binding.date.setOnClickListener {
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
                binding.dtText1.text = "${months[selectedMonth]} ${years[selectedYear]}"
                val adapter = WeekDayAdapter(mActivity,currentMonthDays,currentDate,noticeList,object : OnClickListener{
                    override fun onClickItem(pos: Int) {
                        val dt = sdfDate.format(currentMonthDays[pos])
                        eventDate = dt
//                        getEventsPager()
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
//        getStudentAttendance()
        attendanceFilters = arrayListOf("All", "This Week", "Today", "This Month", "Last Month")
//        attendanceFilters.clear()
//        attendanceFilters.add("All")
//        attendanceFilters.add("This Week")
//        attendanceFilters.add("Today")
//        attendanceFilters.add("This Month")
//        attendanceFilters.add("Last Month")

        val adapter = SpinnerAdapter(mActivity, attendanceFilters)
        binding.spinnerFilterAttendance.adapter = adapter

        binding.spinnerFilterAttendance.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(  parent: AdapterView<*>, view: View?, position: Int,  id: Long )
                {
                     clickedValue = parent.getItemAtPosition(position) as String
//                val value = attendanceFilters[position]
                    isAttendanceFilter = true
                    clickedValue = when (attendanceFilters[position]) {
                        "All" -> "All"
                        "This Week" -> "thisweek"
                        "Today" -> "today"
                        "This Month" -> "this month"
                        "Last Month" -> "lastmonth"
                        else -> "All"
                    }
                    if (selectedRole == "STUDENT"){
                        studentAttendance()
                    }else{
                        selectedRole = "TEACHER"
                        teacherAttendance()
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


//        attendanceSpinner(selectedRole)
        ApiConnection.getInstance().getNoticeBoard(mActivity, "", "").observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result.isNotEmpty()) {
                            noticeList = it.result
                            loadDates1()
                            getEventsPager()
                        } else {
                            loadDates1()
                        }
                    } else {
//                        UiUtils.showSnack(it.msg, binding.root,false)
                        binding.cardNoNoticeData.txt.text = "No events present today!"
                        binding.cardNoNoticeData.root.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.dotsContainer.visibility = View.GONE
                    }
                }
            }
        }
        //loadDates2()

        binding.timeTableViewAll.setOnClickListener {
            binding.tabLayout.getTabAt(1)?.select()
        }

        binding.todayClasses.setOnClickListener {
            BaseUtils.startActivity(mActivity, ClassesActivity(), null, false)
        }
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(), null, false)
        }
        mActivity.binding.timeTableDialog.close1.setOnClickListener {
            mActivity.binding.timeTableDialog.root.visibility = View.GONE
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
        binding.tabStdAtd.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.tabStdAtd, R.drawable.border_line_curve_24dp_primary )
            UiUtils.textViewTextColor(binding.tabStdAtd, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable( binding.tabMyAttendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabMyAttendance, null, R.color.black_varient6)
            selectedRole = "STUDENT"
            binding.roundProgress.visibility = View.VISIBLE
            studentAttendance()
        }
        binding.tabMyAttendance.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.tabMyAttendance, R.drawable.border_line_curve_24dp_primary )
            UiUtils.textViewTextColor(binding.tabMyAttendance, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable( binding.tabStdAtd,  R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabStdAtd, null, R.color.black_varient6)
            selectedRole = "TEACHER"
            binding.roundProgress.visibility = View.VISIBLE
            teacherAttendance()
        }
        binding.tabTimeTable.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabTimeTable, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.classTimeTable, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.examTimeTable, null, R.color.black_varient6)
        }
        binding.tabExam.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabExam, R.drawable.border_line_curve_24dp_primary  )
            UiUtils.textViewTextColor(binding.examTimeTable, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable( binding.tabTimeTable, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.classTimeTable, null, R.color.black_varient6)
        }
        binding.tabDay.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabDay, R.drawable.border_curve_24dp)
            UiUtils.textviewCustomDrawable(binding.tabWeek, R.drawable.border_curve_6dp)
            UiUtils.textViewBgTint(binding.tabDay, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.black_varient3)
        }
        binding.tabWeek.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabWeek, R.drawable.border_curve_24dp)
            UiUtils.textviewCustomDrawable(binding.tabDay, R.drawable.border_curve_6dp)
            UiUtils.textViewBgTint(binding.tabWeek, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.black_varient3)
        }
        binding.search3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search3.text.toString()
                if (examTabClicked == "classTest"){
                    getClassTest()
                } else{
//                    getExam()
                }
            }
        })

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
                    getExam()
                }
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "upcoming"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "upcoming"
                    getExam()
                }
                popupWindow.dismiss()
            }
            bind.ongoing.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "today"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "ongoing"
                    getExam()
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
                    getExam()
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

        binding.classTestTab.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classTestTab,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.examTestTab,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classTestTab, null,R.color.white)
            UiUtils.textViewTextColor(binding.classTestTab, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.examTestTab, null, R.color.black_varient3)
            examTabClicked = "classTest"
            classTestSts = ""
            getClassTest()
        }
        
        binding.examTestTab.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.examTestTab,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classTestTab,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.examTestTab, null,R.color.white)
            UiUtils.textViewTextColor(binding.examTestTab, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classTestTab, null, R.color.black_varient3)
            examTabClicked = "exam"
            examStatus = ""
            getExam()
        }
        
        /*binding.classTestTab.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classTestTab, R.drawable.border_curve_4dp)
            UiUtils.textviewCustomDrawable(binding.examTestTab, R.drawable.border_curve_6dp)
            UiUtils.textViewBgTint(binding.classTestTab, null, R.color.white)
            UiUtils.textViewTextColor(binding.classTestTab, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.examTestTab, null, R.color.black_varient3)
            binding.classTestMenu.visibility = View.VISIBLE
            binding.examMenu.visibility = View.GONE
            clastestStatus = "today"
            testMenu = "classTest"
            getClassTest()
        }
        binding.tabClassTest.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabClassTest,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamination,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabCompletedClassTest,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.todayExam,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.upcomingexam,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.completedExam,null, R.color.black_varient6)
            clastestStatus = "today"
            getClassTest()
        }
        binding.tabExamination.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabExamination,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTest,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabCompletedClassTest,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.upcomingexam,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.todayExam,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.completedExam,null, R.color.black_varient6)
            clastestStatus = "upcoming"
            getClassTest()
        }
        binding.tabCompletedClassTest.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabCompletedClassTest,R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabExamination,R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabClassTest,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.completedExam,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.upcomingexam,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.todayExam,null, R.color.black_varient6)
            clastestStatus = "completed"
            getClassTest()
        }
        binding.examTestTab.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.examTestTab, R.drawable.border_curve_4dp)
            UiUtils.textviewCustomDrawable(binding.classTestTab, R.drawable.border_curve_6dp)
            UiUtils.textViewBgTint(binding.examTestTab, null, R.color.white)
            UiUtils.textViewTextColor(binding.examTestTab, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classTestTab, null, R.color.black_varient3)
            binding.classTestMenu.visibility = View.GONE
            binding.examMenu.visibility = View.VISIBLE
            testMenu = "exam"
            examStatus = "ongoing"
            getExam()
        }
        binding.lay1.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lay1, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lay2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lay3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.text1, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.text2, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.text3, null, R.color.black_varient6)
            examStatus = "ongoing"
            getExam()
        }
        binding.lay2.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lay2, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lay1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lay3, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.text2, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.text1, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.text3, null, R.color.black_varient6)
            examStatus = "upcomming"
            getExam()
        }
        binding.lay3.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.lay3, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.lay2, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.lay1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.text3, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.text2, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.text1, null, R.color.black_varient6)
            examStatus = "completed"
            getExam()
        }*/
        
        binding.midtermexam.close.setOnClickListener {
            binding.midtermexam.root.visibility = View.GONE
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search.text.toString()
                getHomework()
            }
        })
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(mActivity)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
            bind.today.visibility = View.GONE
            bind.linepending.visibility = View.GONE
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
            if (homeworkStatus == "All"){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (homeworkStatus == "pending"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (homeworkStatus == "completed"){
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
                homeworkStatus = ""
                getHomework()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                homeworkStatus = "pending"
                getHomework()
                popupWindow.dismiss()
            }

            bind.completed.setOnClickListener {
                homeworkStatus = "completed"
                getHomework()
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
                if (teacherAssignmentSts == ""){
                    UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (teacherAssignmentSts == "pending"){
                    UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (teacherAssignmentSts  == "completed"){
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
                    teacherAssignmentSts = ""
                    getAssignment()
                }
                else if (tabPos == "project"){
                    projectStatus = ""
                    getProject()
                }
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                if (tabPos == "assignment"){
                    teacherAssignmentSts = "pending"
                    getAssignment()
                }
                else if (tabPos == "project"){
                    projectStatus = "pending"
                    getProject()
                }
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                if (tabPos == "assignment"){
                    teacherAssignmentSts = "completed"
                    getAssignment()
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

        /*
//        binding.tabAllLay.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable( binding.tabAllLay, R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable( binding.tabPendingLay, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.linearLayoutBgDrawable( binding.tabCompletedLay, R.drawable.border_line_curve_24dp_grey )
//            UiUtils.textviewCustomDrawable(binding.count1,R.drawable.ic_round_line2)
//            UiUtils.textviewCustomDrawable(binding.count2,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.count3,R.drawable.ic_round_line_3)
//            UiUtils.textViewTextColor(binding.tabAll, null, R.color.colorPrimary)
//            UiUtils.textViewTextColor(binding.tabPending, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.tabCompleted, null, R.color.black_varient6)
//            homeworkStatus = ""
//            getHomework()
//        }
//        binding.tabPending.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.tabAllLay, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.tabPendingLay, R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.tabCompletedLay, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.textviewCustomDrawable(binding.count2,R.drawable.ic_round_line2)
//            UiUtils.textviewCustomDrawable(binding.count1,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.count3,R.drawable.ic_round_line_3)
//            UiUtils.textViewTextColor(binding.tabAll, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.tabPending, null, R.color.colorPrimary)
//            UiUtils.textViewTextColor(binding.tabCompleted, null, R.color.black_varient6)
//            homeworkStatus = "pending"
//            getHomework()
//        }
//        binding.tabCompletedLay.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.tabAllLay, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.tabCompletedLay, R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.tabPendingLay, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.textviewCustomDrawable(binding.count2,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.count1,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.count3,R.drawable.ic_round_line2)
//            UiUtils.textViewTextColor(binding.tabAll, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.tabPending, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.tabCompleted, null, R.color.colorPrimary)
//            homeworkStatus = "completed"
//            getHomework()
//        }*/

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().TeacherAssCount(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            binding.aCount1.text = it.result!!.totalassignment
                            binding.aCount2.text = it.result!!.pending
                            binding.aCount3.text = it.result!!.completed
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        binding.search2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search2.text.toString()
                if (tabPos == "assignment") {
                    getAssignment()
                } else {
                    getProject()
                }
            }
        })
//        binding.tabAllProject.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable( binding.tabAllProject, R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable( binding.tabNotCompleted2, R.drawable.border_line_curve_24dp_grey )
//            UiUtils.linearLayoutBgDrawable( binding.tabComplete2, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.textviewCustomDrawable(binding.aCount1, R.drawable.ic_round_line2)
//            UiUtils.textviewCustomDrawable(binding.aCount2, R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.aCount3, R.drawable.ic_round_line_3)
//            UiUtils.textViewTextColor(binding.All, null, R.color.colorPrimary)
//            UiUtils.textViewTextColor(binding.pending, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.completed2, null, R.color.black_varient6)
//            if (tabPos == "assignment") {
//                teacherAssignmentSts = ""
//                getAssignment()
//            } else {
//                projectStatus = ""
//                getProject()
//            }
//        }
//        binding.tabNotCompleted2.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted2,  R.drawable.border_line_curve_24dp_primary )
//            UiUtils.linearLayoutBgDrawable(binding.tabAllProject, R.drawable.border_line_curve_24dp_grey )
//            UiUtils.linearLayoutBgDrawable(binding.tabComplete2, R.drawable.border_line_curve_24dp_grey )
//            UiUtils.textviewCustomDrawable(binding.aCount1, R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.aCount2, R.drawable.ic_round_line2)
//            UiUtils.textviewCustomDrawable(binding.aCount3, R.drawable.ic_round_line_3)
//            UiUtils.textViewTextColor(binding.All, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.pending, null, R.color.colorPrimary)
//            UiUtils.textViewTextColor(binding.completed2, null, R.color.black_varient6)
//            if (tabPos == "assignment") {
//                teacherAssignmentSts = "pending"
//                getAssignment()
//            } else {
//                projectStatus = "pending"
//                getProject()
//            }
//        }
//        binding.tabComplete2.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.tabComplete2, R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.tabNotCompleted2, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.tabAllProject, R.drawable.border_line_curve_24dp_grey)
//            UiUtils.textviewCustomDrawable(binding.aCount1,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.aCount2,R.drawable.ic_round_line_3)
//            UiUtils.textviewCustomDrawable(binding.aCount3,R.drawable.ic_round_line2)
//            UiUtils.textViewTextColor(binding.All, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.pending, null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.completed2, null, R.color.colorPrimary)
//            if (tabPos == "assignment") {
//                teacherAssignmentSts = "completed"
//                getAssignment()
//            } else {
//                projectStatus = "completed"
//                getProject()
//            }
//        }

        binding.tabAnnouncement.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabDay1, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabDay1, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabDay1, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabWeek1, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabWeek1, R.drawable.border_curve_6dp)
            eventType = "day"
            getNoticeBoard()
        }
        binding.tabEvents.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabWeek1, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabWeek1, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tabWeek1, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabDay1, null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabDay1, R.drawable.border_curve_6dp)
            eventType = "month"
            getNoticeBoard()
        }
        binding.tabHomeWork.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabHomeWork,  R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab1, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab2, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab3, null, R.color.black_varient6)
            type = "homework"
            submissionProgress()
        }
        binding.tabAttendance.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab1, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab2, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable( binding.tabProjects,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab3, null, R.color.black_varient6)
            type = "assignment"
            submissionProgress()
        }
        binding.tabProjects.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab1, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab2, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_primary )
            UiUtils.textViewTextColor(binding.tab3, null, R.color.colorPrimary)
            type = "project"
            submissionProgress()
        }
        binding.tabHomeWork.performClick()

        binding.tabDay1.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabDay1,R.drawable.border_curve_24dp )
            UiUtils.textviewCustomDrawable(binding.tabWeek1,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.tabDay1, null,R.color.white)
            UiUtils.textViewTextColor(binding.tabDay1, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabWeek1, null, R.color.black_varient3)
            eventType = "day"
            binding.title.text = "Today Announcement"
            binding.cDate.text = eventDate
            getNoticeBoard()
        }

        binding.tabWeek1.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabWeek1, R.drawable.border_curve_24dp)
            UiUtils.textviewCustomDrawable(binding.tabDay1, R.drawable.border_curve_6dp)
            UiUtils.textViewBgTint(binding.tabWeek1, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabWeek1, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabDay1, null, R.color.black_varient3)
            eventType = "month"
            binding.title.text = "Monthly Announcement"
            binding.cDate.text = "month"
            getNoticeBoard()
        }
        return view

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
//    fun getStudentAttendance() {
//        DialogUtils.showLoader(mActivity)
//        ApiConnection.getInstance().attendance(mActivity, "").observe(mActivity) {
//            DialogUtils.dismissLoader()
//            it?.let {
//                it.success.let { success ->
//                    if (success) {
//                        if (it.result != null) {
//                            if (it.result!!.progress != null) {
//                                val progress = it.result!!.progress
//                                binding.totalWorking.text =  "Total Working Days : ${progress!!.total}"
////                                binding.streaks.text = "Streaks : ${it.result!!.streaks}"
//                                binding.present.text = progress.presentCount
//                                binding.absent.text = progress.absentCount
//                                binding.halfDay.text = progress.halfDayCount
//                                binding.percent.text = "Overall\n${progress.presentPercentage}%"
////                                val pro = progress.presentPercentage!!.toFloat()
////                                loadProgressBar(pro)
////                                setupPieChart(progress)
//                            }
//                        } else {
//                            UiUtils.showSnack(it.msg, binding.root, false)
//                        }
//                    } else {
//                        UiUtils.showSnack(it.msg, binding.root, false)
//
//                    }
//                }
//            }
//        }
//    }

    fun submissionProgress() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().submissionteacherSide(mActivity,type,programId).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        val result = it.result!!
                        if (result != null) {
                            setupPieChart2(result)
                            if (result.percentage != null && result.percentage!!.onTime != null) {
                                binding.subPercent.text =  "Overall \n ${result.percentage!!.onTime}%"
                            } else {
                                binding.subPercent.text = "--/--"
                            }
                            if (result.total != null){
                                binding.total.text = "Total Submission : ${result.total}"
                            }
                            else{
                                binding.total.text = "--/--"
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

    fun setupPieChart1(result: TeacherAttendanceResponse.Result) {
        val total = result.progress!!.total?.toFloat()?.coerceAtLeast(1f) ?: 1f
        val presentCount = result.progress!!.presentPercentage?.toFloat() ?: 0f
        val absentCount = result.progress!!.absentPercentage?.toFloat() ?: 0f
//            val halfDayCount = result.count!!.halfDay?.toFloat() ?: 0f

        val isAllZero = presentCount == 0f && absentCount == 0f

        val entries = if (isAllZero) {
            arrayListOf(
                PieEntry(100f, "No Data")
            )
        } else {
            arrayListOf(
                PieEntry((presentCount / total) * 100f, "Present"),  // Green
                PieEntry((absentCount / total) * 100f, "Absent"),   // Red
//                    PieEntry((halfDayCount / total) * 100f, "Half Day") // Blue
            )
        }

        val colors = if (isAllZero) {
            listOf(Color.parseColor("#f5f5f5")) // Grey for no data
        } else {
            listOf(
                Color.parseColor("#32B138"),  // Green for Present
                Color.parseColor("#FF7475")  // Red for Absent
//                    Color.parseColor("#1170E4")   // Blue for Half Day
            )
        }

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 8f
            selectionShift = 5f
            setDrawValues(false)
        }

        val pieData = PieData(dataSet)

        pieChart.apply {
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

            // Animate the chart
            animateY(1000, Easing.EaseInOutCubic)

            // Refresh the chart
            invalidate()
        }
    }

    fun setupPieChart(result: AdminAttendanceResponse.Result) {
        val total = result.count!!.total?.toFloat()?.coerceAtLeast(1f) ?: 1f

        val presentCount = result.count!!.present?.toFloat() ?: 0f
        val absentCount = result.count!!.absent?.toFloat() ?: 0f
        val halfDayCount = result.count!!.halfDay?.toFloat() ?: 0f

        val isAllZero = presentCount == 0f && absentCount == 0f && halfDayCount == 0f

        val entries = if (isAllZero) {
            arrayListOf(
                PieEntry(100f, "No Data")
            )
        } else {
            arrayListOf(
                PieEntry((presentCount / total) * 100f, "Present"),  // Green
                PieEntry((absentCount / total) * 100f, "Absent"),   // Red
                PieEntry((halfDayCount / total) * 100f, "Half Day") // Blue
            )
        }

        val colors = if (isAllZero) {
            listOf(Color.parseColor("#f5f5f5")) // Grey for no data
        } else {
            listOf(
                Color.parseColor("#32B138"),  // Green for Present
                Color.parseColor("#FF7475"),  // Red for Absent
                Color.parseColor("#1170E4")   // Blue for Half Day
            )
        }

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 8f
            selectionShift = 5f
            setDrawValues(false)
        }

        val pieData = PieData(dataSet)

        pieChart.apply {
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

            // Animate the chart
            animateY(1000, Easing.EaseInOutCubic)

            // Refresh the chart
            invalidate()
        }
    }

    fun getClassTimeTableHome() {
        if (timeTableRes.isEmpty()) {
            DialogUtils.showLoader(mActivity)
            ApiConnection.getInstance().timeTableTeacher(mActivity,"subject", timeTableDay).observe(mActivity) {
                    it?.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success ->
                            if (success) {
                                if (it.result != null && it.result!!.periods!!.isNotEmpty()) {
                                    binding.timeTableNoData.root.visibility = View.GONE
                                    binding.todayTimeTableRecycler.visibility = View.VISIBLE
                                    timeTableRes = it.result!!.periods!!
                                    val adapter = TimeTableTeacherAdapter( mActivity,true, it.result!!.periods!!,object : OnClickListener {
                                        override fun onClickItem(pos: Int) {
                                            mActivity.binding.timeTableDialog.root.visibility =
                                                View.VISIBLE
                                        }
                                    })
                                    val layoutManager = GridLayoutManager( mActivity, 4,RecyclerView.VERTICAL,false)
                                    binding.todayTimeTableRecycler.layoutManager = layoutManager
                                    binding.todayTimeTableRecycler.adapter = adapter
                                } else {
                                    binding.timeTableNoData.txt.text = "No Tabletable Available"
                                    binding.timeTableNoData.root.visibility = View.VISIBLE
                                    binding.todayTimeTableRecycler.visibility = View.GONE
                                }
                            } else {
                                binding.timeTableNoData.txt.text = "No Tabletable Available"
                                binding.timeTableNoData.root.visibility = View.VISIBLE
                                binding.todayTimeTableRecycler.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                            }
                        }
                    }
                }
        } else {
            binding.timeTableNoData.root.visibility = View.GONE
            binding.timetableRecycler.visibility = View.VISIBLE
            val adapter =
                TimeTableTeacherAdapter(mActivity, true, timeTableRes, object : OnClickListener {
                    override fun onClickItem(pos: Int) {
                        mActivity.binding.timeTableDialog.root.visibility = View.VISIBLE
                    }
                })
            val layoutManager = GridLayoutManager(mActivity, 4, RecyclerView.VERTICAL, false)
            binding.todayTimeTableRecycler.layoutManager = layoutManager
            binding.todayTimeTableRecycler.adapter = adapter
        }
    }

    fun getTimeTable() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().timeTableTeacher(mActivity,"", timeTableDay).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.periods!!.isNotEmpty()) {
                            binding.noData01.root.visibility = View.GONE
                            binding.timetableRecycler.visibility = View.VISIBLE
                            val adapter = TimeTableTeacherAdapter( mActivity, false,it.result!!.periods!!,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    mActivity.binding.timeTableDialog.root.visibility = View.VISIBLE
                                }
                            })
                            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            binding.timetableRecycler.layoutManager = layoutManager
                            binding.timetableRecycler.adapter = adapter
                        } else {
                            binding.noData01.txt.text = "No Tabletable Available"
                            binding.noData01.root.visibility = View.VISIBLE
                            binding.timetableRecycler.visibility = View.GONE
                        }
                    } else {
                        binding.noData01.txt.text = "No Tabletable Available"
                        binding.noData01.root.visibility = View.VISIBLE
                        binding.timetableRecycler.visibility = View.GONE
//                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getHomework() {
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getTeacherHomework(mActivity,search,programId,homeworkStatus).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            hwResult = it.result!!.rows!!
                            binding.noData.root.visibility = View.GONE
                            binding.homeworkRecycler.visibility = View.VISIBLE
                            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = TeacherHomeworkAdapter(mActivity, false, hwResult, object : OnClickListener {
                                override fun onClickItem(pos: Int) {
//                                    getHomework()
                                }
                            })
                            binding.homeworkRecycler.layoutManager = layoutManager
                            binding.homeworkRecycler.adapter = adapter
                        } else {
                            binding.homeworkRecycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.homeworkRecycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getNoticeBoard() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getNoticeBoard(mActivity, eventType, "").observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result.isNotEmpty()) {
                            binding.noData4.root.visibility = View.GONE
                            binding.recycler3.visibility = View.VISIBLE
                            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = NoticeBoardAdapter(mActivity, eventType, it.result)
                            binding.recycler3.layoutManager = layoutManager
                            binding.recycler3.adapter = adapter
                        } else {
                            binding.noData4.root.visibility = View.VISIBLE
                            binding.recycler3.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root,false)
                        }
                    } else {
                        binding.noData4.root.visibility = View.VISIBLE
                        binding.recycler3.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun getAssignment() {
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getTeacherAssignment(mActivity,search,programId, teacherAssignmentSts,subId).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.recyclerAssignment.visibility = View.VISIBLE
                            binding.noData1.root.visibility = View.GONE
                            val layoutManager =
                                LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = TeacherAssignmentAdapter(
                                mActivity,
                                false,
                                it.result!!.rows!!,
                                object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                    }
                                })
                            binding.recyclerAssignment.layoutManager = layoutManager
                            binding.recyclerAssignment.adapter = adapter
                        } else {
                                binding.recyclerAssignment.visibility = View.GONE
                                binding.noData1.root.visibility = View.VISIBLE
                            }
                        } else {
                            binding.recyclerAssignment.visibility = View.GONE
                            binding.noData1.root.visibility = View.VISIBLE
                        }

                    }
                }
            }
        }

    fun getClassTest() {
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getTeacherClassTest(mActivity,search,subId,programId, classTestSts).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.classTestRecycler.visibility = View.VISIBLE
                            binding.noData3.root.visibility = View.GONE
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = ClassTestMasterAdapter(mActivity, it.result!!.rows!!)
                            binding.classTestRecycler.layoutManager = linearLayoutManager
                            binding.classTestRecycler.adapter = adapter
                        } else {
                            binding.classTestRecycler.visibility = View.GONE
                            binding.noData3.root.visibility = View.VISIBLE
                        }
                    } else {
                        binding.classTestRecycler.visibility = View.GONE
                        binding.noData3.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun getExam() {
        if (search.isEmpty()) {
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getExam(mActivity,programId,search, examStatus).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.noData3.root.visibility = View.GONE
                            binding.classTestRecycler.visibility = View.VISIBLE
                            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = ExamListAdapter(mActivity, it.result!!.rows!!)
                            binding.classTestRecycler.layoutManager = layoutManager
                            binding.classTestRecycler.adapter = adapter
                        } else {
                            binding.noData3.root.visibility = View.VISIBLE
                            binding.classTestRecycler.visibility = View.GONE
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData3.root.visibility = View.VISIBLE
                        binding.classTestRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getProject() {
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().getTeacherProject(mActivity,search,programId,projectStatus).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.noData1.root.visibility = View.GONE
                            binding.recyclerAssignment.visibility = View.VISIBLE
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = ProjectAdapter(mActivity, false, it.result!!.rows!!)
                            binding.recyclerAssignment.layoutManager = linearLayoutManager
                            binding.recyclerAssignment.adapter = adapter
                        } else {
                            binding.recyclerAssignment.visibility = View.GONE
                            binding.noData1.root.visibility = View.VISIBLE
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.recyclerAssignment.visibility = View.GONE
                        binding.noData1.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun getEventsPager() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getNoticeBoard(mActivity, "", eventDate).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result.isNotEmpty()) {
//                            noticeList = it.result
//                            loadDates1()
                            binding.cardNoNoticeData.root.visibility = View.GONE
                            binding.viewPager.visibility = View.VISIBLE
                            binding.dotsContainer.visibility = View.VISIBLE
                            binding.viewPager.adapter = EventsPagerAdapter(mActivity, it.result)
                            setupDotsIndicator(it.result)
                            binding.viewPager.setPageTransformer { page, position ->
                                val offset = abs(position)
                                with(page) {
                                    scaleY = 1 - (offset * 0.1f)
                                    alpha = 1 - (offset * 0.3f)
                                }
                            }
                            timer = object : CountDownTimer(Long.MAX_VALUE, 3000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    val currentPage = binding.viewPager.currentItem
                                    val nextPage =
                                        if (currentPage < it.result.size - 1) currentPage + 1 else 0
                                    binding.viewPager.setCurrentItem(nextPage, true)
                                }

                                override fun onFinish() {}
                            }.start()
                        } else {
//                            loadDates1()
                            binding.cardNoNoticeData.txt.text = "No events present today!"
                            binding.cardNoNoticeData.root.visibility = View.VISIBLE
                            binding.viewPager.visibility = View.GONE
                            binding.dotsContainer.visibility = View.GONE
                        }
                    } else {
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

    fun loadDates1() {
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        val adapter = WeekDayAdapter(
            mActivity,
            currentMonthDays,
            currentDate,
            noticeList,
            object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    val dt = sdfDate.format(currentMonthDays[pos])
                    UiUtils.log("kiuygf", dt)
                    eventDate = dt
                    getEventsPager()
                }
            })
        val linearLayoutManager =
            LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler.layoutManager = linearLayoutManager
        binding.dateRecycler.adapter = adapter

        val currentDayIndex =
            currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex != -1) {
            binding.dateRecycler.centerItem(currentDayIndex)
        }
    }

    fun loadDates2() {
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
            UiUtils.log("gfhjk", "" + selectedDate)
            val dt = sdfApi.format(selectedDate).toLowerCase(Locale.getDefault())
            timeTableDay = dt
            binding.showTxt.text = "Showing ${timeTableDay.capitalize()} Timetable"
            getTimeTable()
        }
        val linearLayoutManager1 = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler1.layoutManager = linearLayoutManager1
        binding.dateRecycler1.adapter = adapter1

        val currentDayIndex1 =
            currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex1 != -1) {
            binding.dateRecycler1.centerItem(currentDayIndex1)
        }
    }

    private fun setupDotsIndicator(events: ArrayList<NoticeBoardResponse.Result>) {
        binding.dotsContainer.removeAllViews()

        for (event in events) {
            val eventName = event.type?.name ?: continue
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

            // Optional: Add animation
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

    fun RecyclerView.centerItem(position: Int) {
        post {
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun calculateDtToFit(
                    viewStart: Int, viewEnd: Int,
                    boxStart: Int, boxEnd: Int,
                    snapPreference: Int
                ): Int {
                    return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return 100f / displayMetrics.densityDpi
                }
            }
            smoothScroller.targetPosition = position
            layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun getCurrentMonthDays(): ArrayList<Date> {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
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

    private fun setupPieChart2(result: SubmissionProgressTeacherSide.Result) {
        val entries = arrayListOf<PieEntry>()

        // Ensure percentages are safely handled
        val onTime = result.percentage?.onTime?.toFloat() ?: 0f
        val missed = result.percentage?.missed?.toFloat() ?: 0f
        val late = result.percentage?.late?.toFloat() ?: 0f

        // Check if all values are zero
        val isNoData = (onTime == 0f && missed == 0f && late == 0f)

        if (isNoData) {
            // Fallback for no data
            entries.add(PieEntry(100f, "No Data"))
        } else {
            // Add actual data entries
            entries.add(PieEntry(onTime, "On Time"))
            entries.add(PieEntry(missed, "Missed"))
            entries.add(PieEntry(late, "Late"))
        }

        val colors = if (isNoData) {
            listOf(Color.parseColor("#f5f5f5")) // Grey for No Data
        } else {
            listOf(
                Color.parseColor("#32B138"), // Green for On Time
                Color.parseColor("#FF7475"), // Red for Missed
                Color.parseColor("#FF9900")  // blue for Late
            )
        }

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 8f
            selectionShift = 5f
            setDrawValues(false)
        }

        val pieData = PieData(dataSet)

        pieChart2.apply {
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

            // Refresh the chart
            invalidate()
        }
    }

    fun studentAttendance() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacherStudentAttendance(mActivity, clickedValue, selectedRole, programId)
            .observe(mActivity) {
                DialogUtils.dismissLoader()
                it?.let {
                    it.success.let { success ->
                        if (success) {
                            if (it.result!! != null) {
                                if (selectedRole == "STUDENT") {
                                    binding.totalWarkingDays.text = it.result!!.count!!.total.toString()
                                    binding.presentdays.text = it.result!!.count!!.present!!.toString()
                                    binding.absentDays.text =it.result!!.count!!.absent!!.toString()
                                    binding.percent.text = "Present \n ${it.result!!.count!!.present!!}%"
                                    binding.halfDays.text = it.result!!.count!!.halfDay!!.toString()
                                    //setupPieChart(it.result!!)
                                    studentAttendanceProgress(it.result!!)
                                }
                            }
                            else {
                                UiUtils.showSnack(it.msg, binding.root, false)
                            }
                        }
                    }
                }
            }
    }

    fun teacherAttendance() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacheroverlAtt(mActivity, clickedValue).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result!! != null) {
                                binding.totalWarkingDays.text = it.result!!.progress!!.total.toString()
                                binding.presentdays.text = it.result!!.progress!!.presentCount!!.toString()
                                binding.absentDays.text = it.result!!.progress!!.absentCount!!.toString()
                                binding.halfDays.text = it.result!!.progress!!.halfDay!!.toString()
                                binding.percent.text = "Present \n ${it.result!!.progress!!.presentPercentage!!}%"
                                // setupPieChart1(it.result!!)
                                teacherAttendanceProgress(it.result!!)

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
    }

    private fun studentAttendanceProgress(result: GetOverallStudentAttendProgressRes.Result) {
        val present = result.percent?.present?.toFloat()?: 0f
        val absent = result.percent?.absent?.toFloat()?: 0f
        val halfDay = result.percent?.halfDay?.toFloat()?: 0f

        val isAllZero = listOf(present, absent, halfDay).all { it == 0f }

        val entries = if (isAllZero) {
            arrayListOf(PieEntry(100f, "Empty"))
        } else {
            arrayListOf(
                PieEntry(present, "Present"),
                PieEntry(halfDay, "Half-Day"),
                PieEntry(absent, "Absent")
            ).filter { it.value > 0f }
        }

        val colors = if (isAllZero) {
            listOf(Color.parseColor("#B0BEC5")) // Soft neutral for empty
        } else {
            listOf(
                Color.parseColor("#FFC107"), // Sunshine Yellow (Present)
                Color.parseColor("#AB47BC"), // Mystic Purple (Half-Day)
                Color.parseColor("#FF4081")  // Bold Pink (Absent)
            ).take(entries.size) // Synchronize with visible entries
        }

        val dataSet = PieDataSet(entries, "").apply {
            setDrawValues(false)
            sliceSpace = if (isAllZero) 0f else 3f
            this.colors = colors
            selectionShift = 0f
        }

        attenDanceProgress.apply {
            data = PieData(dataSet)
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
            setCenterText("") // External TextView takes center stage
            setExtraOffsets(50f, 50f, 50f, 50f)

            // Custom renderer for gradient flair
            renderer = GradientPieChartRenderer(
                this, animator, viewPortHandler, colors
            )

            animateY(1000, Easing.EaseInOutCubic)
            invalidate()
        }
    }

    private fun teacherAttendanceProgress(result: TeacherAttendanceResponse.Result) {
        val present = result.progress?.presentPercentage?.toFloat() ?: 0f
        val absent = result.progress?.absentPercentage?.toFloat() ?: 0f
        val halfDay = result.progress?.halfDay?.toFloat() ?: 0f

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
        txttab0.text = "My Register "
        UiUtils.imageviewDrawable(icon0,R.drawable.tab_diary)
        UiUtils.imageViewTint(icon0,null,R.color.colorPrimary)
        UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
        UiUtils.imageviewDrawable(icon0,R.drawable.tab_diary)
        UiUtils.imageViewTint(icon0,null,R.color.colorPrimary)
        txttab0.setTextAppearance(R.style.FontMedium)
//        UiUtils.linearLayoutBgTint(lin0,"#F2D9DA",null)
        binding.tabLayout.getTabAt(0)!!.customView = linear0

        val linear1: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab1 = linear1.findViewById<TextView>(R.id.tab)
        val icon1 = linear1.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon1,R.drawable.tab_timetable)
        UiUtils.imageViewTint(icon1,null,R.color.black_varient3)
        txttab1.text = "Time Table"
        UiUtils.textViewTextColor(txttab1, null, R.color.black_varient3)
        txttab1.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(1)!!.customView = linear1

        val linear2: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab2 = linear2.findViewById<TextView>(R.id.tab)
        val icon2 = linear2.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon2,R.drawable.tab_homework)
        UiUtils.imageViewTint(icon2,null,R.color.black_varient3)
        txttab2.text = "Homework"
        UiUtils.textViewTextColor(txttab2, null, R.color.black_varient3)
        UiUtils.imageviewDrawable(icon2,R.drawable.tab_homework)
        UiUtils.imageViewTint(icon2,null,R.color.black_varient3)
        txttab2.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(2)!!.customView = linear2

        val linear3: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab3 = linear3.findViewById<TextView>(R.id.tab)
        val icon3 = linear3.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon3,R.drawable.tab_assignment)
        UiUtils.imageViewTint(icon3,null,R.color.black_varient3)
        txttab3.text = "Assignment"
        UiUtils.textViewTextColor(txttab3, null, R.color.black_varient3)
        txttab3.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(3)!!.customView = linear3

        val linear4: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab4 = linear4.findViewById<TextView>(R.id.tab)
        val icon4 = linear4.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon4,R.drawable.tab_project)
        UiUtils.imageViewTint(icon4,null,R.color.black_varient3)
        txttab4.text = "Projects"
        UiUtils.textViewTextColor(txttab4, null, R.color.black_varient3)
        txttab4.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(4)!!.customView = linear4

        val linear5: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab5 = linear5.findViewById<TextView>(R.id.tab)
        val icon5 = linear5.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon5,R.drawable.tab_exam)
        UiUtils.imageViewTint(icon5,null,R.color.black_varient3)
        txttab5.text = "Examination"
        UiUtils.textViewTextColor(txttab5, null, R.color.black_varient3)
        txttab5.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(5)!!.customView = linear5

        val linear6: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab6 = linear6.findViewById<TextView>(R.id.tab)
        val icon6 = linear6.findViewById<ImageView>(R.id.icon)
        UiUtils.imageviewDrawable(icon6,R.drawable.tab_event)
        UiUtils.imageViewTint(icon6,null,R.color.black_varient3)
        txttab6.text = "News & Events"
        UiUtils.textViewTextColor(txttab6, null, R.color.black_varient3)
        txttab6.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(6)!!.customView = linear6
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val linear0: View = tab.customView!!
                val txttab0 = linear0.findViewById<TextView>(R.id.tab)
                UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                txttab0.setTextAppearance(R.style.FontMedium)
                if (tab.position == 0) {
                    binding.myRegister.visibility = View.VISIBLE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    binding.commonCalender.visibility = View.GONE
                    search = ""
                    binding.search.setText("")
                    binding.search2.setText("")
                    binding.search3.setText("")
//                    type = "homework"
//                    submissionProgress()
                } else if (tab.position == 1) {
                    binding.myRegister.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.VISIBLE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    getTimeTable()
                    loadDates2()
                    search = ""
                    binding.search.setText("")
                    binding.search2.setText("")
                    binding.search3.setText("")
                } else if (tab.position == 2) {
                    binding.myRegister.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.VISIBLE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    search = ""
                    tabPos = "homework"
                    loadDates2()
                    binding.search.setText("")
                    binding.search2.setText("")
                    binding.search3.setText("")
                } else if (tab.position == 3) {
                    binding.myRegister.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.VISIBLE
                    binding.pageExam.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    tabPos = "assignment"
                    binding.tabAllProject.performClick()
                    getAssignment()
                    loadDates2()
                    search = ""
                    binding.search.setText("")
                    binding.search2.setText("")
                    binding.search3.setText("")
                } else if (tab.position == 4) {
                    binding.myRegister.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.VISIBLE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    tabPos = "project"
                    binding.tabAllProject.performClick()
                    search = ""
                    loadDates2()
                    binding.search.setText("")
                    binding.search2.setText("")
                    binding.search3.setText("")
                } else if (tab.position == 5) {
                    binding.myRegister.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.GONE
                    binding.pageExam.visibility = View.VISIBLE
                    binding.commonCalender.visibility = View.VISIBLE
                    search = ""
                    loadDates2()
                    binding.search.setText("")
                    binding.search2.setText("")
                    binding.search3.setText("")
                    getClassTest()
                } else if (tab.position == 6) {
                    binding.myRegister.visibility = View.GONE
                    binding.pageHomeWork.visibility = View.GONE
                    binding.pageTimeTable.visibility = View.GONE
                    binding.pageAssignment.visibility = View.GONE
                    binding.pageNews.visibility = View.VISIBLE
                    binding.pageExam.visibility = View.GONE
                    binding.commonCalender.visibility = View.VISIBLE
                    binding.tabDay1.performClick()
                    search = ""
                    loadDates2()
                    binding.search.setText("")
                    binding.search2.setText("")
                    binding.search3.setText("")
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
}

