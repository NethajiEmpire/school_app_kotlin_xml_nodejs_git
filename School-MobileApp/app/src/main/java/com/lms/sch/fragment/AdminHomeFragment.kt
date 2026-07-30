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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.activity.AcademicActivity
import com.lms.sch.activity.AdminCircularActivity
import com.lms.sch.activity.ComplaintsActivity
import com.lms.sch.activity.LeaveApprovalActivity
import com.lms.sch.activity.AdminTimeTableActivity
import com.lms.sch.activity.ExaminationActivity
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.adapter.EventsPagerAdapter
import com.lms.sch.adapter.GetComplaintsAdapter
import com.lms.sch.adapter.LeaveRequestAdapter
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.WeekDayAdapter
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.customviews.TopRendarCurveChartbar
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.databinding.FragmentAdminHomeBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.response.AdminAttendanceResponse
import com.lms.sch.response.GetAdminOverallFeeBarchartResponse
import com.lms.sch.response.GetAdminStatsReponse
import com.lms.sch.response.StudentBoardResponse
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

class AdminHomeFragment : BaseFragment() {
    lateinit var binding: FragmentAdminHomeBinding
    var calendar = Calendar.getInstance()
    var attendanceFilters = ArrayList<String>()
    var isAttendanceFilter = false
    var datefilter = ""
    lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    lateinit var timer: CountDownTimer
    var result = ArrayList<GetStudentAssignmentResponse.Result>()
    private var currentMonthDays = ArrayList<Date>()
    var noticeList = ArrayList<NoticeBoardResponse.Result>()
    var adminDashboardStatsRes: GetAdminStatsReponse.Result? = null
    var fees: GetAdminOverallFeeBarchartResponse.Result.Result? = null
    var boardResult = ArrayList<StudentBoardResponse.Result>()

    var eventDate = ""
    var timeTableDay = ""
    var role = ""
    var classId = ""
    var selectedRole = "TEACHER"
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep","Oct", "Nov", "Dec")
    var selectedYear: Int = 0
    var selectedMonth: Int = 0
    lateinit var attenDanceProgress: PieChart

    override fun onCreateView(  inflater: LayoutInflater,  container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(0)
        UiUtils.textViewGradient(binding.greet, "#232B68", "#4555CE")
        barChart = binding.barChart
        pieChart = binding.pieChart
        attenDanceProgress = binding.attenDanceProgress

        //   loadEvents()
       // setupBarChart()
        setupPieChart()
        getAdminStats()
        getFeesChartbar()
        //getFeeCharBar()
        binding.roleName.text = SharedHelper(mActivity).name
        Log.d("hhg", SharedHelper(mActivity).standard)
        binding.logo.setOnLongClickListener {
            if (BuildConfig.DEBUG) {
                ApiDataDialog(mActivity).show(mActivity)
            }
            return@setOnLongClickListener true
        }
        currentMonthDays = getCurrentMonthDays()

        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfDay = SimpleDateFormat("EEEE", Locale.getDefault())
        val sdfMon = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(currentDate)
        val api = sdfDay.format(currentDate).toLowerCase(Locale.getDefault())
        val date = sdfDate.format(currentDate).toLowerCase(Locale.getDefault())
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        val currentMonth = SimpleDateFormat("MMM", Locale.getDefault()).format(Date())
        val cYear = years.indexOf(currentYear.toString())
        val cMon = months.indexOf(currentMonth.toString())
        if (cYear != -1) {
            selectedYear = cYear
        }
        if (cMon != -1) {
            selectedMonth = cMon
        }
        binding.dtText.text = sdfMon
        eventDate = date
        ApiConnection.getInstance().getNoticeBoard(mActivity, "", "").observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result.isNotEmpty()) {
                            noticeList = it.result
                            //boardId = boardResult[0]._id!!
                            loadDates1()
                            getEventsPager()
                        } else {
                            loadDates1()
                        }
                    } else {
                        binding.cardNoNoticeData.txt.text = "No events present today!"
                        binding.cardNoNoticeData.root.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.dotsContainer.visibility = View.GONE
                    }
                }
            }
        }
        attendanceSpinner(selectedRole)
        leaveRequest()
        complaints()
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().profile(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            UiUtils.loadImage(binding.profile,it.result!!.userprofile!!.img_url)
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
        binding.view1.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_Admin_Finance)
        }
        binding.guest.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("guest", "guest")
            mActivity.navController!!.navigate(R.id.navigation_Admin_User_Management,bundle)
        }
        binding.students.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("guest1", "student")
            mActivity.navController!!.navigate(R.id.navigation_Admin_User_Management)
        }
        binding.teacher.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("guest2", "teacher")
            mActivity.navController!!.navigate(R.id.navigation_Admin_User_Management)
        }
        binding.revenue.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_Admin_Finance)
        }
        binding.timeTableTab.setOnClickListener {
            BaseUtils.startActivity(mActivity, AdminTimeTableActivity(), null, false)
        }
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(), null, false)
        }
        binding.requestTap.setOnClickListener {
            BaseUtils.startActivity(mActivity, LeaveApprovalActivity(), null, false)
        }
        binding.view4.setOnClickListener {
            BaseUtils.startActivity(mActivity, LeaveApprovalActivity(), null, false)
        }
        binding.circularTap.setOnClickListener {
            BaseUtils.startActivity(mActivity, AdminCircularActivity(), null, false)
        }
        binding.examTap.setOnClickListener {
            BaseUtils.startActivity(mActivity, ExaminationActivity(), null, false)
        }

        binding.academicTap.setOnClickListener {
            BaseUtils.startActivity(mActivity, AcademicActivity(), null, false)
        }
        binding.complaintView.setOnClickListener {
            BaseUtils.startActivity(mActivity, ComplaintsActivity(), null, false)
        }
        binding.complaintTab.setOnClickListener {
            BaseUtils.startActivity(mActivity, ComplaintsActivity(), null, false)
        }
        binding.roles.setOnClickListener {
            mActivity.navController!!.navigate(R.id.navigation_Admin_User_Management)
        }
        binding.date1.setOnClickListener {
            val dialog = Dialog(mActivity)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_month_picker)
            val bind: DialogMonthPickerBinding =
                DialogMonthPickerBinding.inflate(LayoutInflater.from(mActivity))
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
            val yearAdapter = YearAdapter(mActivity, years, selectedYear, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    sYear = pos
                    bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                }
            })
            bind.yearRecycler.layoutManager =
                LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
            bind.yearRecycler.adapter = yearAdapter

            val currentYearIndex = years.indexOf(currentYear.toString())
            bind.yearRecycler.post {
                if (currentYearIndex != RecyclerView.NO_POSITION) {
                    val layoutManager = bind.yearRecycler.layoutManager as LinearLayoutManager
                    val recyclerWidth =
                        bind.yearRecycler.width - bind.yearRecycler.paddingLeft - bind.yearRecycler.paddingRight
                    val itemWidth = resources.getDimensionPixelSize(R.dimen._70dp)
                    val offset = (recyclerWidth / 2) - (itemWidth / 2)
                    layoutManager.scrollToPositionWithOffset(currentYearIndex, offset)
                }
            }
            val monthAdapter = MonthsAdapter(
                mActivity,
                months,
                years[selectedYear].toInt(),
                selectedMonth,
                object : OnClickListener {
                    override fun onClickItem(pos: Int) {
                        sMonth = pos
                        bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                    }
                })
            bind.monthRecycler.layoutManager =
                GridLayoutManager(mActivity, 3, GridLayoutManager.VERTICAL, false)
            bind.monthRecycler.adapter = monthAdapter

            bind.select.setOnClickListener {
                selectedYear = sYear
                selectedMonth = sMonth
                currentMonthDays = getCurrentMonthDays(years[sYear].toInt(), selectedMonth)
                binding.dtText.text = "${months[selectedMonth]} ${years[selectedYear]}"
                val adapter = WeekDayAdapter(
                    mActivity,
                    currentMonthDays,
                    currentDate,
                    noticeList,
                    object : OnClickListener {
                        override fun onClickItem(pos: Int) {
                            val dt = sdfDate.format(currentMonthDays[pos])
                            eventDate = dt
                            getEventsPager()
                        }
                    })
                val linearLayoutManager =
                    LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
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


        binding.teacherTab.setOnClickListener {
            UiUtils.textviewCustomDrawable(
                binding.teacherTab,
                R.drawable.border_line_curve_24dp_primary
            )
            UiUtils.textviewCustomDrawable(
                binding.studentTab,
                R.drawable.border_line_curve_24dp_grey
            )
            UiUtils.textViewTextColor(binding.teacherTab, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.studentTab, null, R.color.black_varient6)
            selectedRole = "TEACHER"
            adminAttendance(datefilter)
        }
        binding.studentTab.setOnClickListener {
            UiUtils.textviewCustomDrawable(
                binding.studentTab,
                R.drawable.border_line_curve_24dp_primary
            )
            UiUtils.textviewCustomDrawable(
                binding.teacherTab,
                R.drawable.border_line_curve_24dp_grey
            )
            UiUtils.textViewTextColor(binding.teacherTab, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.studentTab, null, R.color.colorPrimary)
            selectedRole = "STUDENT"
            adminAttendance(datefilter)
        }
        return view
    }

    private fun getAdminStats() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getAdminDashboardStats(mActivity).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            adminDashboardStatsRes = it.result!!

                            if (adminDashboardStatsRes!!.totalGuest != null) {
                                binding.totalGuests.text = it.result!!.totalGuest.toString()
                            } else {
                                binding.totalGuests.text = "0"
                            }
                            if (adminDashboardStatsRes!!.totalStudents != null) {
                                binding.totalStudent.text = it.result!!.totalStudents.toString()
                            } else {
                                binding.totalStudent.text = "0"
                            }
                            if (adminDashboardStatsRes!!.totalTeachers != null) {
                                binding.totalTeachers.text = it.result!!.totalTeachers.toString()
                            } else {
                                binding.totalTeachers.text = "0"
                            }
                            if (adminDashboardStatsRes?.totalRevenue != null) {
                                binding.totalRevenue.text =
                                    "₹${adminDashboardStatsRes!!.totalRevenue.toString()}"
                            } else {
                                binding.totalRevenue.text = "₹0"
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

    fun getEventsPager() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getNoticeBoard(mActivity, "", eventDate).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result.isNotEmpty()) {
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
                            binding.cardNoNoticeData.txt.text = "No events present today!"
                            binding.cardNoNoticeData.root.visibility = View.VISIBLE
                            binding.viewPager.visibility = View.GONE
                            binding.dotsContainer.visibility = View.GONE
                        }
                    } else {
                        binding.cardNoNoticeData.txt.text = "No events present today!"
                        binding.cardNoNoticeData.root.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.dotsContainer.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getFeesChartbar() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().adminFeeChartbar(mActivity, classId).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.result.isNotEmpty()) {
                            setupBarChart(it.result!!.result)
                            if (it.result != null && it.result!!.summary != null && it.result!!.summary!!.collected != null) {
                                binding.colectAmt.text = "₹ ${it.result!!.summary!!.collected}"
                            } else {
                                binding.colectAmt.text = "0"
                            }
                            if (it.result != null && it.result!!.summary != null && it.result!!.summary!!.pending != null) {
                                binding.pendingAmt.text = "₹ ${it.result!!.summary!!.pending}"
                            } else {
                                binding.pendingAmt.text = "0"
                            }
                            if (it.result != null && it.result!!.summary != null && it.result!!.summary!!.overdue != null) {
                                binding.overdueAmt.text = "₹ ${it.result!!.summary!!.overdue}"
                            } else {
                                binding.overdueAmt.text = "0"
                            }
                            if (it.result != null && it.result!!.summary != null && it.result!!.summary!!.totalFees != null) {
                                binding.totalFee.text = "₹ ${it.result!!.summary!!.totalFees}"
                            } else {
                                binding.totalFee.text = "0"
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

    private fun setupBarChart(results: ArrayList<GetAdminOverallFeeBarchartResponse.Result.Result>) {
        barChart.clear()
        barChart.marker = null

        val stackedEntries = mutableListOf<BarEntry>()
        val classLabels = mutableListOf<String>()
        val colors = mutableListOf<Int>()

        // Gradient-like color tones
        val lightGreen = Color.parseColor("#63E66A")     // Collected (completed)
        val lightRed = Color.parseColor("#F43D3D")       // Pending
        val lightOrange = Color.parseColor("#F4C366")    // Overdue
        val gray = Color.parseColor("#BDBDBD")           // All-zero fallback

        results.forEachIndexed { index, item ->
            val collectedPer = item.collectedPer?.toFloatOrNull() ?: 0f
            val pendingPer = item.pendingPer?.toFloatOrNull() ?: 0f
            val overduePer = item.overduePer?.toFloatOrNull() ?: 0f

            val total = collectedPer + pendingPer + overduePer

            if (total == 0f) {
                // Show a single gray bar if all values are zero
                stackedEntries.add(BarEntry(index.toFloat(), floatArrayOf(100f)))
                colors.add(gray)
            } else {
                val values = mutableListOf<Float>()
                val segmentColors = mutableListOf<Int>()

                if (collectedPer > 0f) {
                    values.add(collectedPer)
                    segmentColors.add(lightGreen)
                }
                if (pendingPer > 0f) {
                    values.add(pendingPer)
                    segmentColors.add(lightRed)
                }
                if (overduePer > 0f) {
                    values.add(overduePer)
                    segmentColors.add(lightOrange)
                }
                stackedEntries.add(BarEntry(index.toFloat(), values.toFloatArray()))
                colors.addAll(segmentColors)
            }
            val className = item.className ?: "Class ${index + 1}"
            classLabels.add(className)
        }
        val stackedDataSet = BarDataSet(stackedEntries, "").apply {
            setColors(colors)
            setDrawValues(false)
        }
        val data = BarData(stackedDataSet).apply {
            barWidth = 0.4f
        }
        barChart.apply {
            this.data = data

            // Removed custom renderer
            // renderer = TopRendarCurveChartbar(this, animator, viewPortHandler)

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(classLabels)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                setLabelCount(classLabels.size)
                axisMinimum = -0.5f
                axisMaximum = classLabels.size - 0.5f
                textSize = 12f
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
            marker = CustomMarkerView(mActivity)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            animateY(1000)
            setExtraOffsets(20f, 20f, 20f, 20f)

            invalidate()
        }
    }

    fun leaveRequest() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().leaveRequest(mActivity, "", "", "").observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.leaveRequestRecycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            val adapter = LeaveRequestAdapter(
                                mActivity,
                                it.result!!.rows!!,
                                object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        val bundle = Bundle()
                                        bundle.putString(  Constants.IntentKeys.KEY,it.result!!.rows!![pos]._id )
                                        bundle.putInt(Constants.IntentKeys.KEY1, pos)
                                        bundle.putInt(Constants.IntentKeys.KEY2, 1)
                                        BaseUtils.startActivity(  mActivity, LeaveApprovalActivity(), bundle,false)
                                    }
                                })
                            val layoutManager = LinearLayoutManager(
                                mActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            binding.leaveRequestRecycler.layoutManager = layoutManager
                            binding.leaveRequestRecycler.adapter = adapter
                        } else {
                            binding.leaveRequestRecycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
                        }
                    } else {
                        binding.leaveRequestRecycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun complaints() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getAllComplaints(mActivity, "", "", "","").observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.complaintsRecycler.visibility = View.VISIBLE
                            binding.noData1.root.visibility = View.GONE
                            val adapter = GetComplaintsAdapter(
                                mActivity,
                                it.result!!.rows!!,
                                object : OnClickListener {
                                    override fun onClickItem(pos: Int) {

                                    }
                                })
                            val layoutManager = LinearLayoutManager(
                                mActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            binding.complaintsRecycler.layoutManager = layoutManager
                            binding.complaintsRecycler.adapter = adapter
                        } else {
                            binding.complaintsRecycler.visibility = View.GONE
                            binding.noData1.root.visibility = View.VISIBLE
                        }
                    } else {
                        binding.complaintsRecycler.visibility = View.GONE
                        binding.noData1.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
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
        //binding.dateRecycler.visibility = view?.visibility!!

        val currentDayIndex =
            currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex != -1) {
            binding.dateRecycler.centerItem(currentDayIndex)
        }
    }

    /*
        private fun setupBarChart() {
            val entries1 = listOf(
                BarEntry(0f, 40f), // 1st Term
                BarEntry(1f, 42f), // 2nd Term
                BarEntry(2f, 72f), // 3rd Term
                BarEntry(2f, 72f),  // 4th Term
                BarEntry(2f, 72f),  // 5th Term
                BarEntry(2f, 72f), // 6th Term
            )

            val entries2 = listOf(
                BarEntry(0f, 40f),
                BarEntry(1f, 28f),
                BarEntry(1f, 28f),
                BarEntry(1f, 28f),
                BarEntry(1f, 28f),
                BarEntry(1f, 28f),
            )
            val entries3 = listOf(
                BarEntry(0f, 20f),
                BarEntry(1f, 33f),
                BarEntry(1f, 33f),
                BarEntry(1f, 33f),
                BarEntry(1f, 33f),
                BarEntry(1f, 33f),
            )

            val terms = listOf("1st std", "2nd std", "3rd std", "4th std","5th std", "6th std")

            val dataSet1 = BarDataSet(entries1, "Set 1").apply {
                val color = Color.parseColor("#232B68")
                colors = listOf(color, color, color, color)
                setGradientColor(color, color)
                setDrawValues(false)
            }

            val dataSet2 = BarDataSet(entries2, "Set 2").apply {
                val color = Color.parseColor("#32B138")
                colors = listOf(color, color, color, color)
                setGradientColor(color, color)
                setDrawValues(false)
            }

            val dataSet3 = BarDataSet(entries3, "Set 3").apply {
                val color = Color.parseColor("#EA5455")
                colors = listOf(color, color, color, color)
                setGradientColor(color, color)
                setDrawValues(false)
            }

            barChart.renderer = GroupTopRoundedBarChartRender2(
                barChart,
                barChart.animator,
                barChart.viewPortHandler
            )

            val barWidth = 0.15f
            val barSpace = 0.05f
            val groupSpace = 0.4f
            val groupCount = terms.size.toFloat()

            val data = BarData(dataSet1, dataSet2, dataSet3).apply {
                this.barWidth = barWidth
                groupBars(0f, groupSpace, barSpace)
            }

            barChart.apply {
                this.data = data

                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(terms)
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    textSize = 12f
                    typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)

                    axisMinimum = 0f
                    axisMaximum = groupCount  // Important: this matches the number of terms
                    setLabelCount(terms.size, false)
                    setCenterAxisLabels(true)
                }
                axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = 100f
                    granularity = 20f
                    setLabelCount(6, true)
                    textSize = 11f
                    typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return "${value.toInt()}%"
                        }
                    }
                    enableGridDashedLine(10f, 10f, 0f)
                    setDrawGridLines(true)
                    gridColor = Color.GRAY
                }

                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)

                val markerView = CustomMarkerView(mActivity)
                marker = markerView

                setDrawBarShadow(false)
                setDrawValueAboveBar(false)
                animateY(1000)
                setExtraOffsets(20f, 20f, 20f, 20f)

                invalidate()
            }
        }
    */

    /*   axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = 100000f
                    granularity = 20000f
                    setLabelCount(6, true)
                    textSize = 11f
                    typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return when {
                                value >= 1000f -> "${(value / 1000).toInt()}k"
                                else -> value.toInt().toString()
                            }
                        }
                    }
                    enableGridDashedLine(10f, 10f, 0f)
                    setDrawGridLines(true)
                    gridColor = Color.GRAY
                }*/

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
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
            }
        })
    }

    /*
    private fun setupPieChart() {
        val entries = arrayListOf(
            PieEntry(80f),  // Green
            PieEntry(10f),  // Red
            PieEntry(10f)   // Blue
        )

        val colors = listOf(
            Color.parseColor("#32B138"),  // Green
            Color.parseColor("#FF7475"),  // Red
            Color.parseColor("#1170E4")   // Blue
        )

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 8f  // Reduced space for smoother transition
            selectionShift = 0f  // Disable selection shift
            setDrawValues(true)
            valueTextSize = 0f
        }

        val pieData = PieData(dataSet).apply {
            setValueTextSize(0f)
        }

        pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            setDrawEntryLabels(true)
            setDrawHoleEnabled(true)
            holeRadius = 75f
            setTouchEnabled(false)
            legend.isEnabled = false

            // Additional styling for smoother appearance
            setTransparentCircleAlpha(0)
            setHoleColor(Color.TRANSPARENT)
            setDrawRoundedSlices(true)  // This creates smoother curved edges
            isDrawHoleEnabled = true
            setEntryLabelColor(Color.WHITE)

            // Animation
            animateY(500, Easing.EaseInOutQuad)

            invalidate()
        }
    }
*/

    private fun setupPieChart() {
        val entries = arrayListOf(
            PieEntry(80f),  // Green
            PieEntry(10f),  // Red
            PieEntry(10f)   // Blue
        )

        val colors = listOf(
            Color.parseColor("#32B138"),  // Green
            Color.parseColor("#FF7475"),  // Red
            Color.parseColor("#1170E4")   // Blue
        )

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 8f  // Scaled to match thinner stroke
            setDrawValues(false)
            selectionShift = 0f
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

            // Transparent center for aesthetic match
            setHoleColor(Color.TRANSPARENT)

            // Use custom renderer
            renderer = CurvedPieChartRenderer(this, animator, viewPortHandler)

            // Animation
            animateY(500, Easing.EaseInOutCubic)

            invalidate()
        }
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

    fun adminAttendance(date: String) {
        DialogUtils.showLoader(mActivity)
        UiUtils.log("ftgg", selectedRole)
        UiUtils.log("ftgg", date)
        ApiConnection.getInstance().adminattendance(mActivity, date, selectedRole)
            .observe(mActivity) {
                DialogUtils.dismissLoader()
                it?.let {
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null) {
                                if (it.result!! != null) {
                                    if (selectedRole == "TEACHER") {
                                        binding.totalWarkingDays.text =
                                            it.result!!.count!!.total.toString()
                                        binding.presentdays.text =
                                            it.result!!.count!!.present!!.toString()
                                        binding.absentDays.text =
                                            it.result!!.count!!.absent!!.toString()
                                        binding.percent.text = "Present \n ${it.result!!.percent!!.present!!}%"
                                        binding.halfDays.text =
                                            it.result!!.count!!.halfDay!!.toString()
                                        studentAttendanceProgress(it.result!!)
                                    } else if (selectedRole == "STUDENT") {
                                        binding.totalWarkingDays.text =
                                            it.result!!.count!!.total.toString()
                                        binding.presentdays.text =
                                            it.result!!.count!!.present!!.toString()
                                        binding.absentDays.text =
                                            it.result!!.count!!.absent!!.toString()
                                        binding.percent.text = "Present \n ${it.result!!.percent!!.present!!}%"
                                        binding.halfDays.text =
                                            it.result!!.count!!.halfDay!!.toString()
                                        teacherAttendanceProgress(it.result!!)
                                    } else {
                                        binding.totalWarkingDays.text =
                                            it.result!!.count!!.total.toString()
                                        binding.presentdays.text =
                                            it.result!!.count!!.present!!.toString()
                                        binding.absentDays.text =  it.result!!.count!!.absent!!.toString()
                                        binding.percent.text = "Present \n ${it.result!!.percent!!.present!!}%"
                                        binding.halfDays.text =
                                            it.result!!.count!!.halfDay!!.toString()
                                    }
                                    /* binding.present.text = it.result!!.count!!.present!!.toString()
                            binding.absent.text = it.result!!.count!!.absent!!.toString()
                            binding.halfday.text = it.result!!.count!!.halfDay!!.toString()
                            binding.percent.text = "Present \n ${it.result!!.percent!!.present!!}%"*/
                                    //setupPieChart(it.result!!)
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

    private fun studentAttendanceProgress(result: AdminAttendanceResponse.Result) {
        val present = result.percent?.present?.toFloat() ?: 0f
        val absent = result.percent?.absent?.toFloat() ?: 0f
        val halfDay = result.percent?.halfDay?.toFloat() ?: 0f

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

    private fun teacherAttendanceProgress(result: AdminAttendanceResponse.Result) {
        val present = result.percent?.present?.toFloat() ?: 0f
        val absent = result.percent?.absent?.toFloat() ?: 0f
        val halfDay = result.percent?.halfDay?.toFloat() ?: 0f
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

    private fun setupPieChart(result: AdminAttendanceResponse.Result) {
        val entries: ArrayList<PieEntry>
        if (result.percent != null && (result.percent!!.present != null || result.percent!!.absent != null || result.percent!!.halfDay != null)) {
            val ontime = result.percent!!.present!!.toFloat()
            val missed = result.percent!!.absent!!.toFloat()
            val late = result.percent!!.halfDay!!.toFloat()
            entries = arrayListOf(
                PieEntry(ontime, "Ontime"),
                PieEntry(missed, "missed"),
                PieEntry(late, "late")
            )
        } else {
            entries = arrayListOf(
                PieEntry(0f, "Ontime"),
                PieEntry(0f, "missed"),
                PieEntry(0f, "late")
            )

        }
        val colors = listOf(
            Color.parseColor("#32B138"), // green
            Color.parseColor("#EA5455"), // blue
            Color.parseColor("#1170E4")  // red
        )

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

    fun attendanceSpinner(selectedRole: String) {
        var role = selectedRole
        attendanceFilters = arrayListOf("All", "This Week", "Today", "This Month", "Last Month")
//        attendanceFilters.clear()
//        attendanceFilters.add("All")
//        attendanceFilters.add("This Week")
//        attendanceFilters.add("Today")
//        attendanceFilters.add("This Month")
//        attendanceFilters.add("Last Month")

        val adapter = SpinnerAdapter(mActivity, attendanceFilters)
        binding.spinnerFilterAttendance.adapter = adapter

        binding.spinnerFilterAttendance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                val value = attendanceFilters[position]
                isAttendanceFilter = true
                datefilter = when (attendanceFilters[position]) {
                    "All" -> "All"
                    "This Week" -> "thisweek"
                    "Today" -> "today"
                    "This Month" -> "this month"
                    "Last Month" -> "lastmonth"
                    else -> "All"
                }
                adminAttendance(datefilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}

