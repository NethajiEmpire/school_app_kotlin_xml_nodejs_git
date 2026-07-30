package com.lms.sch.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.google.protobuf.Api
import com.lms.sch.R
import com.lms.sch.activity.StudentListActivity
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.adapter.SubjectStudentsAdapter
import com.lms.sch.adapter.MySubjectsAdapter
import com.lms.sch.adapter.SectionAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.TopRoundedBarChartRenderer
import com.lms.sch.databinding.FragmentMySubjectBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.SubjectWiseClassExamProResponse
import com.lms.sch.response.SubmissionProgressTeacherSide
import com.lms.sch.response.TeacherAttendanceResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class MySubjectFragment : BaseFragment() {
    lateinit var binding: FragmentMySubjectBinding
    lateinit var pieChart : PieChart
    lateinit var pieChart1 : PieChart
    lateinit var barChart: BarChart
    lateinit var barChart1: BarChart
    var attendanceFilters = ArrayList<String>()
    var programId = ""
    var type = ""
    var clickedValue = ""
    var isAttendanceFilter = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentMySubjectBinding.inflate(inflater, container, false)
        mActivity.selectBottomNav(2)
        pieChart = binding.pieChart
        pieChart1 = binding.pieChartAss
        barChart1 = binding.barChart1
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
                                    binding.tabClsTest.performClick()
                                    type = "classTest"
                                    examprogress()
                                    getStudentsList()
                                    binding.tabHomeWorktab.post {
                                        binding.tabHomeWorktab.performClick()
                                    }
                                }
                            })
                            val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
                            binding.subRecycler.layoutManager = layoutManager
                            binding.subRecycler.adapter = adapter
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
        binding.viewAll.setOnClickListener {
            if (programId != null && programId.isNotEmpty()){
                val bundle = Bundle()
                bundle.putString(Constants.IntentKeys.KEY,programId)
                BaseUtils.startActivity(mActivity, StudentListActivity(),bundle,false)
            }
        }
        teacherAttoveral()
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(),null, false)
        }
        binding.tabClsTest.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabClsTest,null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabClsTest, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabExam,null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_grey)
            type = "classTest"
            examprogress()
        }
        binding.tabExam.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabExam,null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabExam, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabClsTest,null, R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tabClsTest, R.drawable.border_line_curve_24dp_grey)
            type = "exam"
            examprogress()
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
                        teacherAttoveral()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.tabHomeWorktab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabHomeWorktab,  R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab1, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance1, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab2, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabProjects, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab3, null, R.color.black_varient6)
            type = "homework"
            submissionProgress()
        }

        binding.tabAttendance1.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabHomeWorktab, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab1, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance1, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tab2, null, R.color.colorPrimary)
            UiUtils.linearLayoutBgDrawable( binding.tabProjects,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab3, null, R.color.black_varient6)
            type = "assignment"
            submissionProgress()
        }
        binding.tabProjects.setOnClickListener {
            UiUtils.linearLayoutBgDrawable( binding.tabHomeWorktab, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tab1, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable( binding.tabAttendance1,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tab2, null, R.color.black_varient6)
            UiUtils.linearLayoutBgDrawable(binding.tabProjects, R.drawable.border_line_curve_24dp_primary )
            UiUtils.textViewTextColor(binding.tab3, null, R.color.colorPrimary)
            type = "project"
            submissionProgress()
        }
//        setupBarChart()
//        setupPieChart()
//        setupPieChart1()
        return binding.root
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
                                    clsTestBarChart(it.result!!)
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
    fun teacherAttoveral() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacheroverlAtt(mActivity, clickedValue)
            .observe(mActivity) {
                DialogUtils.dismissLoader()
                it?.let {
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null) {
                                if (it.result!! != null) {

                                    binding.totalWorking.text = "Total Working Days : ${it.result!!.progress!!.total}"

                                    Log.d("totalWorking",it.result!!.progress!!.total!!.toString())

                                    binding.present.text = it.result!!.progress!!.presentCount!!.toString()

                                    Log.d("present",it.result!!.progress!!.presentCount!!.toString())

                                    binding.absent.text =it.result!!.progress!!.absentCount!!.toString()

                                    Log.d("absent",it.result!!.progress!!.absentCount!!.toString())

                                    binding.percent.text = "Present \n ${it.result!!.progress!!.presentPercentage!!}%"

                                    Log.d("persent",it.result!!.progress!!.presentPercentage!!.toString())

                                    setupPieChart1(it.result!!)

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
            sliceSpace = 12f
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
            sliceSpace = 12f
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
    fun getStudentsList(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentListAnalytics(mActivity,programId).observe(mActivity) {
            Log.d("zhdjsgdhhgdhsgdhagsd",programId)
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            val adapter1 = SubjectStudentsAdapter(mActivity,true,it.result!!.rows!!)
                            val layoutManager1 = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
                            binding.studentListRecycler.layoutManager = layoutManager1
                            binding.studentListRecycler.adapter = adapter1
                        }
                        Log.d("hjhghjgsize",it.result!!.rows!!.size!!.toString())
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    private fun setupPieChart() {
        val entries = arrayListOf(
            PieEntry(25f), // red
            PieEntry(15f), // blue
            PieEntry(60f)  // Green
        )

        val colors = listOf(
            Color.parseColor("#FF7475"), // red
            Color.parseColor("#1170E4"), // blue
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
//    private fun setupPieChart1() {
//        val entries = arrayListOf(
//            PieEntry(15f), // red
//            PieEntry(15f), // yellow
//            PieEntry(70f)  // Green
//        )
//
//        val colors = listOf(
//            Color.parseColor("#FF7475"), // red
//            Color.parseColor("#FF9900"), // yellow
//            Color.parseColor("#32B138")  // Green
//        )
//
//        val dataSet = PieDataSet(entries, "").apply {
//            setColors(colors)
//            sliceSpace = 0f  // Space between segments
//            selectionShift = 5f
//            setDrawValues(false) // Hide percentage values
//        }
//
//        val pieData = PieData(dataSet)
//
//        pieChart1.apply {
//            data = pieData
//            description.isEnabled = false
//            isRotationEnabled = false
//            setDrawEntryLabels(false)
//            setDrawHoleEnabled(true)
//            holeRadius = 80f
//            setTouchEnabled(false)
//            legend.isEnabled = false
//
////            renderer = CurvedPieChartRenderer(this, animator, this.viewPortHandler)
//
//            invalidate()
//        }
//    }
private fun clsTestBarChart(result: ArrayList<SubjectWiseClassExamProResponse.Result>) {
    barChart1.clear()
    barChart1.marker = null
    val entries = ArrayList<BarEntry>()
    if (result.isNotEmpty()) {
        result.forEachIndexed { index, item ->
            val percentage = item.percentage?.toFloat() ?: 0f
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
//            val markerView = CustomMarkerView2(mActivity, result)
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
}