package com.lms.sch.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
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
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.lms.sch.R
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.activity.TeacherTimeTableActivity
import com.lms.sch.adapter.GetSubjectNameColorAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.SubjectNameAdapter
import com.lms.sch.charts.SubjectRoundedBarChartRenderer
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.CustomMarkerView2
import com.lms.sch.customviews.CustomMarkerView3
import com.lms.sch.customviews.CustomMarkerView4
import com.lms.sch.customviews.ExamTopRendarCurveColor
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.customviews.GroupTopRoundedBarChartRenderer
import com.lms.sch.customviews.TopRendarCurveBarChartColors
import com.lms.sch.customviews.TopRoundedBarChartRenderer
import com.lms.sch.databinding.FragmentTeacherProgressBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetOverallStudentAttendProgressRes
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.GetStudentExamProgressResponse
import com.lms.sch.response.SubjectWiseClassExamProResponse
import com.lms.sch.response.SubmissionProgressTeacherSide
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class TeacherProgressFragment : BaseFragment() {
    lateinit var binding : FragmentTeacherProgressBinding
    lateinit var attenDanceProgress : PieChart
    lateinit var pieChart1 : PieChart
    lateinit var barChart4 : BarChart
    lateinit var barChart1 : BarChart
    lateinit var barCharts : BarChart
    var examProgressResult = ArrayList<DropdownResponse.Result>()
    var resultClsTest = ArrayList<SubjectWiseClassExamProResponse.Result>()
    var examRes = ArrayList<GetStudentExamProgressResponse.Result>()
    var clsTestValue = ""
    var selectedRole = ""
    var attendanceFilters = ArrayList<String>()
    var clickedValue = "All"
    var isAttendanceFilter = false
    var type = "homework"
    var programId = ""
    var examId = ""
    var datefilter = ""
    var isFilter = false
    var examFilter = ArrayList<String>()
    var clsTestFilter = ArrayList<String>()
    override fun onCreateView(  inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTeacherProgressBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(1)
        attenDanceProgress = binding.attenDanceProgress
        pieChart1 = binding.pieChartAss
        barChart4 = binding.barChart4
        barChart1 = binding.barChart1
        barCharts = binding.barCharts
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacherProfile(mActivity,mActivity.sharedHelper.id).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.myStudentClass != null){
                            programId = it.result!!.teacherPreference!!.myStudentClass!!._id!!
                            UiUtils.loadImage(binding.profile,it.result!!.userProfile!!.imgUrl)
                            binding.tabClsTest.post {
                                binding.tabClsTest.performClick()
                            }
                            type = "homework"
                            submissionProgress()
                        }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
        binding.viewAllSubjects.setOnClickListener {
            BaseUtils.startActivity(mActivity, SubjectWiseProgressActivity(), null, false)
        }
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(),null, false)
        }

        type = "homework"
        submissionProgress()
        examprogress()
        binding.tabClsTest.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabClsTest, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabClsTest, null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabExam, null, R.color.black_varient6)
            binding.spinner.visibility = View.VISIBLE
            binding.sidelay.visibility = View.VISIBLE
            binding.subjectColorRecyclerView.visibility = View.VISIBLE
            binding.line.visibility = View.VISIBLE
            binding.proTitle.text = "Class Test Insights"
            binding.proDesc.text = "Average Mark Scored"
            type = "classTest"
            clsTestFilter.clear()
            clsTestFilter.add("This Week")
            clsTestFilter.add("This Month")
            examprogress()
            clsTestBarChart(resultClsTest)
            clsTestVerticalBarChart(resultClsTest)
            val adapter1 = SpinnerAdapter(mActivity, clsTestFilter)
            binding.spinnerFilter.adapter = adapter1

            binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val clickedValue = parent.getItemAtPosition(position) as String
                    clsTestValue = clickedValue
                    clsTestBarChart(resultClsTest)
                    clsTestVerticalBarChart(resultClsTest)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.tabExam.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabExam,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabExam,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabClsTest,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabClsTest,null,R.color.black_varient6)
            binding.spinner.visibility = View.VISIBLE
            binding.sidelay.visibility = View.GONE
            binding.subjectColorRecyclerView.visibility = View.VISIBLE
            binding.line.visibility = View.VISIBLE
            type = "exam"
            examprogress()
            examProgressBarChart(resultClsTest)
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
                    examprogress()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        attendanceFilters = arrayListOf("All", "This Week", "Today", "This Month", "Last Month")
//        attendanceFilters.clear()
//        attendanceFilters.add("All")
//        attendanceFilters.add("This Week")
//        attendanceFilters.add("Today")
//        attendanceFilters.add("This Month")
//        attendanceFilters.add("Last Month")
        val adapter = SpinnerAdapter(mActivity, attendanceFilters)
        binding.spinnerFilterAttendance.adapter = adapter
        binding.spinnerFilterAttendance.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
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
                    selectedRole = "STUDENT"
                    studentAttendance()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.tabHomeWork.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabHomeWork,  R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab1, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab2, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab3, null, R.color.black_varient6)
            type = "homework"
            submissionProgress()
        }
        binding.tabAttendance1.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab1, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance1, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab2, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable( binding.tabProjects,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab3, null, R.color.black_varient6)
            type = "assignment"
            submissionProgress()
        }
        binding.tabProjects.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabHomeWork, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab1, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance1,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab2, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_primary )
            UiUtils.textViewTextColor(binding.tab3, null, R.color.colorPrimary)
            type = "project"
            submissionProgress()
        }
        return view
    }
    fun examprogress() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().examClassTestProgress(mActivity, type,programId)
            .observe(mActivity) {
                DialogUtils.dismissLoader()
                it?.let {
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null) {
                                if (it.result!! != null) {
                                    if (type == "classTest"){
                                        resultClsTest = it.result!!
                                        clsTestBarChart(it.result)
                                        clsTestVerticalBarChart(it.result)
                                    }else{
                                        examProgressBarChart(it.result)
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
    }
    private fun clsTestVerticalBarChart(result: ArrayList<SubjectWiseClassExamProResponse.Result>) {
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
        val validResults = result.filter { it.percentage?.toFloat() ?: 0f > 0f }
            .toCollection(ArrayList())

        val segmentPercentages = FloatArray(validResults.size) { i ->
            validResults[i].percentage?.toFloat() ?: 0f
        }

        val entries = ArrayList<BarEntry>()
        if (validResults.isNotEmpty()) {
            entries.add(BarEntry(1f, segmentPercentages))
        } else {
            entries.add(BarEntry(1f, floatArrayOf(0f)))
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
        barData.barWidth = 0.8f // Wider bar for prominent cap

        barCharts.apply {
            data = barData

            description.isEnabled = false
            legend.isEnabled = false

            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false

            setDrawGridBackground(false)
            setDrawBorders(false)
            setExtraOffsets(10f, 40f, 10f, 10f)

            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDragEnabled(false)

            marker = CustomMarkerView4(mActivity, validResults)

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)

            // Fix X axis range for bar width
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = 2f

            invalidate()
        }
    }

    private fun clsTestBarChart(result: ArrayList<SubjectWiseClassExamProResponse.Result>) {
        barChart1.clear()
        barChart1.marker = null

        val colorPalette = listOf(
            Color.parseColor("#AEB8FE"),
            Color.parseColor("#FFB3BA"),
            Color.parseColor("#FFE0A3"),
            Color.parseColor("#D1B3FF"),
            Color.parseColor("#B2F2BB"),
            Color.parseColor("#B3E5FC"),
            Color.parseColor("#FFD180"),
            Color.parseColor("#F8BBD0"),
            Color.parseColor("#D1C4E9"),
            Color.parseColor("#CFD8DC")
        )

        val entries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloat() ?: 0f
            entries.add(BarEntry(index.toFloat(), percentage))
            colors.add(colorPalette[index % colorPalette.size])
        }

        val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
        val adapter = SubjectNameAdapter(mActivity, result, colors)
        binding.subjectColorRecyclerView.layoutManager = linearLayoutManager
        binding.subjectColorRecyclerView.adapter = adapter

        val dataSet = BarDataSet(entries, "").apply {
            this.colors = colors
            setDrawValues(true)
            valueTextSize = 11f
            valueTextColor = Color.BLACK
            valueTypeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return ""
                    //${barEntry?.y?.toInt() ?: 0}%
                }
            }
        }

        barChart1.renderer = TopRendarCurveBarChartColors(
            barChart1,
            barChart1.animator,
            barChart1.viewPortHandler
        )

        val barData = BarData(dataSet).apply {
            barWidth = 0.8f
        }

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

            marker = CustomMarkerView4(mActivity, result)
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
                override fun onNothingSelected() {}
            })

            setExtraOffsets(10f, 10f, 10f, 10f)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true) //  Value above bar
            animateY(1000)
            invalidate()
        }
    }

    private fun examProgressBarChart(result: ArrayList<SubjectWiseClassExamProResponse.Result>) {
        barChart1.clear()
        barChart1.marker = null

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val colors = ArrayList<Int>()
        val barColor = Color.parseColor("#232b68")

        // Populate data entries with transparent bars
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloat() ?: 0f
            val rawSubject = item.subject?.trim() ?: "--"
            val subject = rawSubject.replace("\\s+".toRegex(), " ").replaceFirstChar { it.uppercase() }

            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
            val adapter = SubjectNameAdapter(mActivity, result, listOf(barColor))
            binding.subjectColorRecyclerView.layoutManager = linearLayoutManager
            binding.subjectColorRecyclerView.adapter = adapter

            entries.add(BarEntry(index.toFloat(), percentage, subject))

            val subjectLabel = when (subject) {
                "Social Science" -> "Social"
                "General Knowledge" -> "GK"
                else -> subject
            }

            val examName = "" // Placeholder for exam name if needed
            labels.add("$subjectLabel\n$examName")

            // Apply transparent color for all bars
            colors.add(Color.TRANSPARENT)
        }

        // If no entries exist, add a placeholder
        if (entries.isEmpty()) {
            entries.add(BarEntry(0f, 0f, "N/A"))
            labels.add("N/A\nNo Exam")
            colors.add(Color.GRAY)
        }

        // Create dataset
        val dataSet = BarDataSet(entries, "").apply {
            this.colors = colors
            valueTextSize = 10f
            valueTextColor = Color.BLACK
            setDrawValues(false)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
            }
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.6f
        }

        // Setup chart
        barChart1.apply {
            data = barData

            // Custom renderer (optional - handles curved bars)
            renderer = ExamTopRendarCurveColor(this, animator, viewPortHandler).apply {
                initBuffers()
            }

            // X Axis setup
            xAxis.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }

            // Y Axis setup
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

            marker = CustomMarkerView3(mActivity, examRes)

            animateY(1000)
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
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
//                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
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

        pieChart1.apply {
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

}