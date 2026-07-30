package com.lms.sch.fragment

import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.activity.ParentProfileActivity
import com.lms.sch.activity.ProfileActivity
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.adapter.GetSubjectNameColorAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.StudentAttendanceAdapter
import com.lms.sch.adapter.SubjectNameAdapter
import com.lms.sch.adapter.SubjectsNameAdapter
import com.lms.sch.charts.SubjectRoundedBarChartRenderer
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.CustomMarkerView2
import com.lms.sch.customviews.CustomMarkerView3
import com.lms.sch.customviews.ExamTopRendarCurveColor
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.customviews.GroupTopRoundedBarChartRenderer
import com.lms.sch.customviews.TopRendarCurveBarChartColors
import com.lms.sch.customviews.TopRoundedBarChartRenderer
import com.lms.sch.databinding.FragmentParentChildProgressBinding
import com.lms.sch.databinding.FragmentProgressBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.response.AttendanceProgressResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.GetStudentExamProgressResponse
import com.lms.sch.response.SubmissionProgressResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class ParentChildProgressFragment : BaseFragment() {
    lateinit var binding: FragmentParentChildProgressBinding
    lateinit var barChart: BarChart
    lateinit var pieChart: PieChart
    lateinit var pieChartAtt : PieChart
    var attendanceFilters = ArrayList<String>()
    var datefilter = ""
    var resultClsTest = ArrayList<GetStudentClassTestProgress.Result>()
    var resultExam = ArrayList<GetStudentExamProgressResponse.Result>()
    var examProgressResult = ArrayList<DropdownResponse.Result>()
    var result1 = GetStudentExamProgressResponse.Result()
    var resultAtt = GetStudentAttenDanceResponse.Result()
    lateinit var attenDanceProgress : PieChart
    var examFilter = ArrayList<String>()
    var clsTestFilter = ArrayList<String>()
    var isFilter = false
    var isAttendanceFilter = false
    var examId = ""
    var childAttend = ""
    var clsTestValue = ""
    var type = ""
    var status = ""
    lateinit var barChart1 : BarChart
    lateinit var barCharts : BarChart
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParentChildProgressBinding.inflate(inflater, container, false)
        val view = binding.root
        barChart = binding.barChart

        pieChart = binding.pieChart
        barChart1 = binding.barChart1
        barCharts = binding.barCharts
        attenDanceProgress = binding.attenDanceProgress
        // pieChartAtt = binding.pieChartAtt
        mActivity.selectBottomNav(1)

        binding.logo.setOnLongClickListener{
            if(BuildConfig.DEBUG){
                ApiDataDialog(mActivity).show(mActivity)
            }
            return@setOnLongClickListener true
        }
        setupBarChart()
        setupBarChart1()
        type = "homework"
        submissionProgress()
//        setupPieChart()
//        loadProgressBar(60f)
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, ParentProfileActivity(), null, false)
        }
        binding.viewAllSubjects.setOnClickListener {
            BaseUtils.startActivity(mActivity, SubjectWiseProgressActivity(),null,false)
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
                                binding.ranks.text = "${it.result!!.rank} Rank "
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
        attendanceFilters.clear()
        attendanceFilters.add("All")
        attendanceFilters.add("Last 7 days")
        attendanceFilters.add("This Week")
        attendanceFilters.add("Last Week")
        attendanceFilters.add("This Month")
        attendanceFilters.add("Last Month")

        val adapter = SpinnerAdapter(mActivity, attendanceFilters)
        binding.spinnerFilterAttendance.adapter = adapter

        binding.spinnerFilterAttendance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val value = attendanceFilters[position]
                isAttendanceFilter = true
                if (value == "All"){
                    datefilter = "All"
                }
                else if (value == "Last 7 days"){
                    datefilter = "last7days"
                }
                else if (value == "This Week"){
                    datefilter = "thisweek"
                }
                else if (value == "Last Week") {
                    datefilter = "lastweek"
                }
                else if (value == "This Month"){
                    datefilter = "thismonth"
                }
                else if (value == "Last Month") {
                    datefilter = "lastmonth"
                }
                else{
                    datefilter = "All"
                }
                getStudentAttendance()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        getStudentExamProgress()

        binding.tabClsTest.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.tabClsTest,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabClsTest, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabExam, null, R.color.black_varient6)
            binding.spinner.visibility = View.VISIBLE
//            binding.barChart1.visibility = View.VISIBLE
//            binding.barCharts.visibility = View.VISIBLE
            binding.sidelay.visibility = View.VISIBLE
            binding.subjectColorRecyclerView.visibility = View.VISIBLE
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
        binding.tabExam.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.tabExam,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabExam, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable( binding.tabClsTest, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabClsTest, null, R.color.black_varient6)
            binding.spinner.visibility = View.VISIBLE
//            binding.barChart1.visibility = View.VISIBLE
//            binding.barCharts.visibility = View.GONE
            binding.sidelay.visibility = View.GONE
            binding.subjectColorRecyclerView.visibility = View.GONE
            examProgressBarChart(resultExam)
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
        binding.tabClsTest.performClick()

        binding.tabHomeWork.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.tabHomeWork, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable( binding.tabAttendance,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAttendance, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable( binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.black_varient6)
            type = "homework"
            submissionProgress()
        }
        binding.tabAttendance.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.tabHomeWork,  R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabAttendance,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabAttendance, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable( binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.black_varient6)
            type = "assignment"
            submissionProgress()
        }
        binding.tabProjects.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tabHomeWork, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable( binding.tabAttendance, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabAttendance, null, R.color.black_varient6)
            UiUtils.textviewCustomDrawable( binding.tabProjects, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabProjects, null, R.color.colorPrimary)
            type = "project"
            submissionProgress()
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
        binding.feeTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.feeTab,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.pendingFeeTab,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.penFeeNum,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.dueDueFee,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.dueTab,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.dueNum,R.drawable.ic_round_line_3)
        }
        binding.dueDueFee.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.dueDueFee,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.dueTab,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.dueNum,R.drawable.ic_round_line2)
            UiUtils.linearLayoutBgDrawable(binding.feeTab,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.pendingFeeTab,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.penFeeNum,R.drawable.ic_round_line_3)
        }
        return view
    }

    private fun loadProgressBar(result: AttendanceProgressResponse.Result) {
        val entries: ArrayList<PieEntry>
        if (result.progress != null &&(result.progress!!.presentPercentage != null || result.progress!!.absentPercentage != null)){
            val present = result.progress!!.presentPercentage!!.toFloat()
            val absent = result.progress!!.absentPercentage!!.toFloat()
            entries = arrayListOf(
                PieEntry(present,"present"),
                PieEntry(absent,"absent"))
        }else{
            entries = arrayListOf(PieEntry(1f, "No Data"))
        }
        val colors = listOf(
            Color.parseColor("#32B138"), // green
            Color.parseColor("#EA5455")  // red
        )

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 8f
            selectionShift = 5f
            setDrawValues(false)
        }

        val pieData = PieData(dataSet)

        pieChartAtt.apply {
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

    private fun setupBarChart1() {
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
            // Add Marker for Tooltips
            val markerView = CustomMarkerView(mActivity)
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

    private fun setupBarChart() {

        val entries1 = listOf(
            BarEntry(0f, 70f),
            BarEntry(1f, 85f),
            BarEntry(2f, 15f)
        )

        val entries2 = listOf(
            BarEntry(0f, 60f),
            BarEntry(1f, 75f),
            BarEntry(2f, 25f)
        )

        val entries3 = listOf(
            BarEntry(0f, 90f),
            BarEntry(1f, 65f),
            BarEntry(2f, 35f)
        )

        val terms = listOf("1st Term", "2nd Term", "3rd Term")

        val dataSet1 = BarDataSet(entries1, "Set 1").apply {
            val startColor = Color.parseColor("#ffffff")
            val endColor = Color.parseColor("#ffffff")
            colors = listOf(
                startColor,
                endColor
            )
            setGradientColor(startColor, endColor)
            setDrawValues(false)
        }
        val dataSet2 = BarDataSet(entries2, "Set 2").apply {
            val startColor = Color.parseColor("#ffffff")
            val endColor = Color.parseColor("#ffffff")
            colors = listOf(
                startColor,
                endColor
            )
            setGradientColor(startColor, endColor)
            setDrawValues(false)
        }
        val dataSet3 = BarDataSet(entries3, "Set 3").apply {
            val startColor = Color.parseColor("#ffffff")
            val endColor = Color.parseColor("#ffffff")
            colors = listOf(
                startColor,
                endColor
            )
            setGradientColor(startColor, endColor)
            setDrawValues(false)
        }

        barChart.renderer = GroupTopRoundedBarChartRenderer(
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

                axisMinimum = 0f  // Start at 0 to align bars correctly
                axisMaximum =
                    groupCount + (groupCount * (barWidth + barSpace))  // Auto-calculate max value
                setLabelCount(terms.size, false)
                setCenterAxisLabels(true)  // Required for proper alignment
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
            this.marker = markerView

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)
            setExtraOffsets(20f, 20f, 20f, 20f)

            invalidate()
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

    private fun setupPieChart() {
        val entries = arrayListOf(
            PieEntry(25f), // red
            PieEntry(15f), // yellow
            PieEntry(60f)  // Green
        )

        val colors = listOf(
            Color.parseColor("#FF7475"), // red
            Color.parseColor("#FF9900"), // yellow
            Color.parseColor("#32B138")  // Green
        )

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 0f  // Space between segments
            selectionShift = 5f
            setDrawValues(false) // Hide percentage values
        }

        val pieData = PieData(dataSet)

        pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            setDrawEntryLabels(false)
            setDrawHoleEnabled(true)
            holeRadius = 80f
            setTouchEnabled(false)
            legend.isEnabled = false

//            renderer = CurvedPieChartRenderer(this, animator, this.viewPortHandler)

            invalidate()
        }
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

        val entries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloatOrNull() ?: 0f
            val spacedIndex = index * 2f
            entries.add(BarEntry(spacedIndex, percentage))

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
            setDrawValues(true)
            valueTextSize = 11f
            valueTextColor = Color.BLACK
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
            }
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.8f
        }

        barChart1.renderer = TopRendarCurveBarChartColors(
            barChart1,
            barChart1.animator,
            barChart1.viewPortHandler
        )

        barChart1.apply {
            data = barData

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                setDrawLabels(false)
                axisMinimum = -1f
                axisMaximum = (result.size * 2).toFloat()
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

            setExtraOffsets(10f, 10f, 10f, 10f)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)

            // ✅ Apply marker that handles its own click
            marker = CustomMarkerView2(mActivity, result)

            setOnChartValueSelectedListener(null)

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

    private fun examProgressBarChart(result: ArrayList<GetStudentExamProgressResponse.Result>) {
        barChart1.clear()
        barChart1.marker = null

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        // Populate entries and labels without colors
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
        }

        // Handle no data
        if (entries.isEmpty()) {
            entries.add(BarEntry(0f, 0f, "N/A"))
            labels.add("N/A\nNo Exam")
        }

        // Transparent bars
        val dataSet = BarDataSet(entries, "").apply {
            color = Color.TRANSPARENT // 👈 Transparent bar color
            valueTextSize = 10f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
            }
        }

        val barData = BarData(dataSet).apply { barWidth = 0.8f }

        // RecyclerView: only subject names, no color
        val subjectList = result.map {
            GetStudentClassTestProgress.Result().apply {
                subject = it.subject ?: "--"
            }
        }

        val adapter = GetSubjectNameColorAdapter(
            mActivity,
            ArrayList(subjectList),
            emptyMap() // 👈 No color map passed
        )

        binding.subjectColorRecyclerView.apply {
            layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
            this.adapter = adapter
        }

        // Setup chart
        barChart1.apply {
            data = barData

            // Custom renderer for curved bars (will still render gradients if coded inside)
            val customRenderer = ExamTopRendarCurveColor(this, animator, viewPortHandler).apply {
                subjectColorMap = emptyMap() // 👈 No color mapping
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
        val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
        val adapter = SubjectsNameAdapter(mActivity, result, colors)
        binding.subjectColorRecyclerView.layoutManager = linearLayoutManager
        binding.subjectColorRecyclerView.adapter = adapter


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

    private fun examProgressBarChart1(result: ArrayList<GetStudentExamProgressResponse.Result>) {
        barChart1.clear()
        barChart1.marker = null

        // Prepare bar entries from percentage data
        val entries = ArrayList<BarEntry>()
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloatOrNull() ?: 0f
            entries.add(BarEntry(index.toFloat(), percentage))
        }

        if (entries.isEmpty()) {
            entries.add(BarEntry(0f, 0f))
        }

        // Prepare subject labels for X-axis
        val subjects = result.map {
            when (it.subject) {
                "Social Science" -> "Social"
                "General Knowledge" -> "GK"
                else -> it.subject ?: "--"
            }
        }

        // Assign bar colors based on subject
        val colors = result.map {
            when (it.subject) {
                "Computer" -> Color.parseColor("#3B3BBF")
                "Maths" -> Color.parseColor("#F85F73")
                "Science" -> Color.parseColor("#F9B233")
                "English" -> Color.parseColor("#9C27B0")
                "Tamil" -> Color.parseColor("#4CAF50")
                "Social Science" -> Color.parseColor("#2196F3")
                "Telugu" -> Color.parseColor("#E91E63")
                "Hindi" -> Color.parseColor("#673AB7")
                "General Knowledge" -> Color.parseColor("#FF9800")
                else -> Color.parseColor("#607D8B")
            }
        }

        // Set up BarDataSet
        val dataSet = BarDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 10f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
        }

        // Use custom renderer for rounded bars
        barChart1.renderer = TopRoundedBarChartRenderer(
            barChart1,
            barChart1.animator,
            barChart1.viewPortHandler
        )

        // Assign bar data
        val barData = BarData(dataSet)
        barData.barWidth = 0.3f

        // Setup chart
        barChart1.apply {
            data = barData

            // X-Axis
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(subjects)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                labelRotationAngle = -45f // Rotate to avoid overlap
            }

            // Y-Axis (Left)
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

            // Y-Axis (Right)
            axisRight.isEnabled = false

            // Chart Styling
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            setExtraOffsets(10f, 10f, 10f, 30f) // Bottom offset for labels

            // Custom marker (tooltips)
            marker = CustomMarkerView3(mActivity, result)

            // Animate and refresh
            animateY(1000)
            invalidate()
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
                        Log.d("hgsd",it.result!!.toString())
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

        override fun onResume() {
        super.onResume()
    }
}

