package com.lms.sch.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.multidex.BuildConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.facebook.share.Share
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lms.sch.R
import com.lms.sch.activity.ComplaintsActivity
import com.lms.sch.activity.KidsExamActivity
import com.lms.sch.activity.StudentAttendanceActivity
import com.lms.sch.activity.StudentInfoActivity
import com.lms.sch.activity.TeacherAssignmentActivity
import com.lms.sch.activity.TeacherExamActivity
import com.lms.sch.activity.TeacherHomeWorkActivity
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.activity.TeacherProjectActivity
import com.lms.sch.activity.TeacherTimeTableActivity
import com.lms.sch.adapter.EventsPagerAdapter
import com.lms.sch.adapter.GetComplaintsAdapter
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.TimeTableTeacherAdapter
import com.lms.sch.adapter.WeekDayAdapter
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.databinding.FragmentTeacherHome2Binding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.response.AttendanceProgressResponse
import com.lms.sch.response.GetOverallStudentAttendProgressRes
import com.lms.sch.response.GetTeacherProfileResponse.MyStudentClassDetails
import com.lms.sch.response.GetTeacherScheduleResponse
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.response.TeacherAttendanceResponse
import com.lms.sch.session.Constants
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

class TeacherHome2Fragment : BaseFragment() {
    lateinit var binding: FragmentTeacherHome2Binding
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var selectedYear : Int = 0
    var selectedMonth : Int = 0
    var timeTableRes = ArrayList<GetTeacherScheduleResponse.PeriodDetails>()
    var resultAtt = AttendanceProgressResponse.Result()
    var timeTableDay = ""
    var eventDate = ""
    var clickedValue = ""
    var selectedRole = "STUDENT"
    var programId = ""
    var isAttendanceFilter = false
    var attendanceFilters = ArrayList<String>()
    var calendar = Calendar.getInstance()
    var noticeList = ArrayList<NoticeBoardResponse.Result>()
    lateinit var timer: CountDownTimer
    private var currentMonthDays = ArrayList<Date>()
    lateinit var attenDanceProgress : PieChart
    override fun onCreateView(  inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentTeacherHome2Binding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(0)
        binding.teacherName.text = SharedHelper(mActivity).name
        attenDanceProgress = binding.attenDanceProgress
        Log.d("hghg","${SharedHelper(mActivity).standard}")
        UiUtils.textViewGradient(binding.shineTxt, "#232B68", "#4555CE")

        binding.logo.setOnLongClickListener{
            if(BuildConfig.DEBUG){
                ApiDataDialog(mActivity).show(mActivity)
            }
            return@setOnLongClickListener true
        }

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().profile(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            UiUtils.loadImage(binding.profile,it.result!!.userprofile!!.img_url!!)
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
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(), null, false)
        }
        binding.timeTableViewAll.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherTimeTableActivity(), null, false)
        }
        binding.yourprogress.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_my_class)
        }
        binding.complaintView.setOnClickListener {
            BaseUtils.startActivity(mActivity, ComplaintsActivity(), null, false)
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
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacherProfile(mActivity, mActivity.sharedHelper.id).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.myStudentClass != null && it.result!!.teacherPreference!!.myStudentClass!!.studentClass != null && it.result!!.teacherPreference!!.myStudentClass!!.studentClass!!.name != null && it.result!!.teacherPreference!!.myStudentClass!!.section!!.name != null) {
                            binding.shineTxt.text = "You're guiding: Grade ${it.result!!.teacherPreference!!.myStudentClass!!.studentClass!!.name} - section ${it.result!!.teacherPreference!!.myStudentClass!!.section!!.name}"
                            programId = it.result!!.teacherPreference!!.myStudentClass!!._id!!
                            selectedRole = "STUDENT"
                            if (selectedRole == "STUDENT"){
                                teacherAttendance()
                            }else{
                                selectedRole = "TEACHER"
                                teacherAttendance()
                            }
                            complaints()
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
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacherStats(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            binding.totalStd.text = it.result!!.totalStudents!!
                            binding.totabsubjects.text = it.result!!.totalsubject!!
                            binding.tatalClasses.text = it.result!!.totalclasses!!
                            binding.today.text = it.result!!.todayperiods!!
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
        currentMonthDays = getCurrentMonthDays(years[selectedYear].toInt(), selectedMonth)
        binding.dtText.text = sdfMon
        binding.dtText1.text = sdfMon
        timeTableDay = api
        eventDate = date
        getClassTimeTableHome()

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
        binding.homeWorkTab.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherHomeWorkActivity(), null, false)
        }
        binding.assTab.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherAssignmentActivity(), null, false)
        }
        binding.projectTab.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProjectActivity(), null, false)
        }
        binding.timeTableTab.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherTimeTableActivity(), null, false)
        }
        binding.examTab.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,"exam")
            BaseUtils.startActivity(mActivity, TeacherExamActivity(), bundle, false)
        }
        binding.testTAb.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,"classTest")
            BaseUtils.startActivity(mActivity, TeacherExamActivity(), bundle, false)
        }
        binding.progressTab.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_my_class)
        }
        binding.myclassStudents.setOnClickListener {
            BaseUtils.startActivity(mActivity, StudentInfoActivity(), null, false)
        }
        binding.myclassatt.setOnClickListener {
            BaseUtils.startActivity(mActivity, StudentAttendanceActivity(), null, false)
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
        mActivity.binding.timeTableDialog.close1.setOnClickListener {
            mActivity.binding.timeTableDialog.root.visibility = View.GONE
        }
        return view
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
                                        mActivity.binding.timeTableDialog.root.visibility =  View.VISIBLE

                                    }
                                })
                                val layoutManager = GridLayoutManager( mActivity, 4,RecyclerView.VERTICAL,false)
                                binding.todayTimeTableRecycler.layoutManager = layoutManager
                                binding.todayTimeTableRecycler.adapter = adapter
                            } else {
                                binding.timeTableNoData.txt.text = "No Time table Available"
                                binding.timeTableNoData.root.visibility = View.VISIBLE
                                binding.todayTimeTableRecycler.visibility = View.GONE
                            }
                        } else {
                            binding.timeTableNoData.txt.text = "No Tabletable Available"
                            binding.timeTableNoData.root.visibility = View.VISIBLE
                            binding.todayTimeTableRecycler.visibility = View.GONE
//
                        //                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
        } else {
            binding.timeTableNoData.root.visibility = View.GONE
//            binding.timetableRecycler.visibility = View.VISIBLE
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
            dot.background = GradientDrawable().apply {4
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
                                    binding.percent.text = "Present \n ${it.result!!.percent!!.present!!}%"
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
                                binding.presentdays.text =
                                    it.result!!.progress!!.presentCount!!.toString()
                                binding.absentDays.text =
                                    it.result!!.progress!!.absentCount!!.toString()
                                binding.halfDays.text =
                                    it.result!!.progress!!.halfDay!!.toString()
                                binding.percent.text =
                                    "Present \n ${it.result!!.progress!!.presentPercentage!!}%"
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
            Color.parseColor("#FF4081"), // Purple (for Half-Day)
            Color.parseColor("#AB47BC")  // Pink (for Absent)
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
                Color.parseColor("#FF4081"), // Mystic Purple (Half-Day)
                Color.parseColor("#AB47BC")  // Bold Pink (Absent)
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
    fun complaints(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getAllComplaints(mActivity,"","", "STUDENT",programId,"unsolved").observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.complaintsRecycler.visibility = View.VISIBLE
                            binding.noData1.root.visibility = View.GONE
                            val adapter = GetComplaintsAdapter(mActivity,it.result!!.rows!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {

                                }
                            })
                            val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
                            binding.complaintsRecycler.layoutManager = layoutManager
                            binding.complaintsRecycler.adapter = adapter
                        }
                        else {
                            binding.complaintsRecycler.visibility = View.GONE
                            binding.noData1.root.visibility = View.VISIBLE
                        }
                    }
                    else {
                        binding.complaintsRecycler.visibility = View.GONE
                        binding.noData1.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
}