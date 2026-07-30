package com.lms.sch.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
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
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.activity.ExaminationActivity
import com.lms.sch.activity.FeeCategoriesActivity
import com.lms.sch.activity.LeaderBoardActivity
import com.lms.sch.activity.StudentProfileActivity
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.adapter.AttendanceAdapter
import com.lms.sch.adapter.ExamListAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.StudentProfileAdapter
import com.lms.sch.charts.SubjectRoundedBarChartRenderer
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.CustomMarkerView2
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.customviews.GroupTopRoundedBarChartRenderer
import com.lms.sch.customviews.TopRoundedBarChartRenderer
import com.lms.sch.databinding.ActivityFeeCategoriesBinding
import com.lms.sch.databinding.FragmentMyClassBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.ClassAttendance
import com.lms.sch.models.FeeStructure
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetOverallStudentAttendProgressRes
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.GetStudentResponse
import com.lms.sch.response.SubjectWiseClassExamProResponse
import com.lms.sch.response.SubmissionProgressTeacherSide
import com.lms.sch.response.TeacherAttendanceResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import kotlin.text.toFloatOrNull

class MyClassFragment : BaseFragment() {
    lateinit var binding : FragmentMyClassBinding
    lateinit var pieChart : PieChart
    lateinit var pieChart1 : PieChart
    lateinit var barCharts : BarChart
    var stdId = ""
    var programId = ""
    var  gender = ""
    var selectedRole = ""
    private lateinit var adapter: AttendanceAdapter
    private val attendanceList = ArrayList<ClassAttendance>()
    var key = ""
    lateinit var barChart: BarChart
    lateinit var barChart2 : BarChart
    lateinit var barChart3 : BarChart
    lateinit var barChart4 : BarChart
    lateinit var barChart1 : BarChart
    lateinit var attenDanceProgress : PieChart

    var isAttendanceFilter = false
    var examStatus = ""
    var type = ""
    var search = ""
    var clickedValue = "All"
    var attendanceFilters = ArrayList<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentMyClassBinding.inflate(inflater,container,false)
        val view = binding.root
        mActivity.selectBottomNav(1)
        pieChart1 = binding.pieChartAss
        barChart = binding.barChart
        barChart1 = binding.barChart1
        barCharts = binding.barCharts
        barChart3 = binding.barChartClsChampions
        barChart4 = binding.barChart4
        attenDanceProgress = binding.attenDanceProgress
//        initAdapter(inflater, binding.root)
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().teacherProfile(mActivity,mActivity.sharedHelper.id).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.myStudentClass != null){
                            programId = it.result!!.teacherPreference!!.myStudentClass!!._id!!
                            binding.tabClsTest.performClick()
                            type = "classTest"
                            examprogress()
                            type = "homework"
                            submissionProgress()
//                            getAttendance()
                        }
                    }else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getTeacherStudentStatsCounts(mActivity).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){

                            if (it.result!!.totalStudents != null) {
                                binding.studentCounts.text = it.result!!.totalStudents.toString()
                            } else {
                                binding.studentCounts.text = "0"
                            }
                            if (it.result!!.boysStudent != null){
                                binding.maleStd.text = it.result!!.boysStudent.toString()
                            }
                            else{
                                binding.maleStd.text = "0"
                            }
                            if (it.result!!.girlsStudent != null){
                                binding.femaleStd.text = it.result!!.girlsStudent.toString()
                            }
                            else {
                                binding.femaleStd.text = "0"
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

        setupBarChart4()
        examStatus = "ongoing"
        getExam()
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
                    selectedRole = "STUDENT"
                    studentAttendance()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        /*binding.stu.root.setOnClickListener {
            BaseUtils.startActivity(mActivity, StudentProfileActivity(),null,false)
        }*/
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(),null, false)
        }

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance() .myClassStudents(mActivity,programId, "STUDENT").observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null ){
                            val result = it.result!!
                            val percent = result.percent
                            val present = percent?.present?.toFloat() ?: 0f
                            val absent = percent?.absent?.toFloat() ?: 0f
                            val halfDay = percent?.halfDay?.toFloat() ?: 0f
                            if (it.result!!.totalStudents != null && it.result!!.percent!! != null){
                                binding.totalstudents.text = it.result!!.totalStudents!!.toString()
                                binding.count1.text = it.result!!.percent!!.present!!.toString()
                                binding.count2.text = it.result!!.percent!!.absent!!.toString()
                                binding.count3.text = it.result!!.percent!!.halfDay!!.toString()
                            }else{
                                binding.count1.text = "--/--"
                                binding.count2.text = "--/--"
                                binding.count3.text = "--/--"
                            }


                            val isAllZero = present == 0f && absent == 0f && halfDay == 0f

                            if (isAllZero) {
                                binding.semiProgress.setProgress(
                                    100f, 100f, "#C0C0C0", // Gray color
                                    0f, "#C0C0C0",
                                    0f, "#C0C0C0"
                                )
                            }
                            else {
                                binding.semiProgress.setProgress(
                                    100f, present, "#32B138", // Green
                                    absent, "#E85A5B",       // Red
                                    halfDay, "#1170E4"       // Blue
                                )
                            }

                        }
                    }else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
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
        binding.examFee.root.setOnClickListener{
            BaseUtils.startActivity(mActivity, FeeCategoriesActivity(), null,false)
        }

        binding.examFee.cExamFee.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("key","ExamFee")
            BaseUtils.startActivity(mActivity, FeeCategoriesActivity(),bundle,false)
        }

        binding.examFee.tutionFeeTab.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("key","TutionFee")
            BaseUtils.startActivity(mActivity, FeeCategoriesActivity(),bundle,false)
        }

        binding.examFee.busFeeTab.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("key","BusFee")
            BaseUtils.startActivity(mActivity, FeeCategoriesActivity(),bundle,false)
            UiUtils.log("sdfds",Constants.IntentKeys.KEY1)
        }

        binding.examFee.bookFeeTab.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("key","BookFee")
            BaseUtils.startActivity(mActivity, FeeCategoriesActivity(),bundle,false)
        }
        /* binding.examCompleted.root.setOnClickListener{
            BaseUtils.startActivity(mActivity, ExaminationActivity(),null,false)
        }*/

        binding.leaderBoard.root.setOnClickListener {
            BaseUtils.startActivity(mActivity, LeaderBoardActivity(),null,false)
        }

        binding.hallticket.close.setOnClickListener {
            binding.hallticket.root.visibility = View.GONE
        }

        binding.result.close.setOnClickListener {
            binding.result.root.visibility = View.GONE
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
//    fun getAttendance(){
//        DialogUtils.showLoader(mActivity)
//        ApiConnection.getInstance().studentAttendance(mActivity,programId).observe(mActivity){
//            it.let {
//                DialogUtils.dismissLoader()
//                it.success.let { success->
//                    if (success){
//                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
//                            val adapter = AttendanceAdapter(mActivity, it.result!!.rows!!)
//                            val layoutManager = LinearLayoutManager(mActivity,  RecyclerView.VERTICAL, false)
//                            binding.attendanceRecycler.layoutManager = layoutManager
//                            binding.attendanceRecycler.adapter = adapter
//                        }
//                        else {
//                            UiUtils.showSnack(it.msg,binding.root,false)
//                        }
//                    }
//                    else {
//                        UiUtils.showSnack(it.msg,binding.root,false)
//                    }
//                }
//            }
//        }
//    }

//    private fun setupBarChart() {
//
//        val entries1 = listOf(
//            BarEntry(0f, 70f),
//            BarEntry(1f, 85f),
//            BarEntry(2f, 15f)
//        )
//
//
//        val entries2 = listOf(
//            BarEntry(0f, 60f),
//            BarEntry(1f, 75f),
//            BarEntry(2f, 25f)
//        )
//
//        val entries3 = listOf(
//            BarEntry(0f, 90f),
//            BarEntry(1f, 65f),
//            BarEntry(2f, 35f)
//        )
//
//        val terms = listOf("1st Term", "2nd Term", "3rd Term")
//
//        val dataSet1 = BarDataSet(entries1, "Set 1").apply {
//            val startColor = Color.parseColor("#ffffff")
//            val endColor = Color.parseColor("#ffffff")
//            colors = listOf(
//                startColor,
//                endColor
//            )
//            setGradientColor(startColor, endColor)
//            setDrawValues(false)
//        }
//        val dataSet2 = BarDataSet(entries2, "Set 2").apply {
//            val startColor = Color.parseColor("#ffffff")
//            val endColor = Color.parseColor("#ffffff")
//            colors = listOf(
//                startColor,
//                endColor
//            )
//            setGradientColor(startColor, endColor)
//            setDrawValues(false)
//        }
//        val dataSet3 = BarDataSet(entries3, "Set 3").apply {
//            val startColor = Color.parseColor("#ffffff")
//            val endColor = Color.parseColor("#ffffff")
//            colors = listOf(
//                startColor,
//                endColor
//            )
//            setGradientColor(startColor, endColor)
//            setDrawValues(false)
//        }
//
//        barChart.renderer = GroupTopRoundedBarChartRenderer(
//            barChart,
//            barChart.animator,
//            barChart.viewPortHandler
//        )
//
//        val barWidth = 0.15f
//        val barSpace = 0.05f
//        val groupSpace = 0.4f
//        val groupCount = terms.size.toFloat()
//
//        val data = BarData(dataSet1, dataSet2, dataSet3).apply {
//            this.barWidth = barWidth
//            groupBars(0f, groupSpace, barSpace)
//        }
//
//        barChart.apply {
//            this.data = data
//
//            xAxis.apply {
//                valueFormatter = IndexAxisValueFormatter(terms)
//                position = XAxis.XAxisPosition.BOTTOM
//                setDrawGridLines(false)
//                granularity = 1f
//                textSize = 12f
//                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
//
//                axisMinimum = 0f  // Start at 0 to align bars correctly
//                axisMaximum = groupCount + (groupCount * (barWidth + barSpace))  // Auto-calculate max value
//                setLabelCount(terms.size, false)
//                setCenterAxisLabels(true)  // Required for proper alignment
//            }
//
//            axisLeft.apply {
//                axisMinimum = 0f
//                axisMaximum = 100f
//                granularity = 20f
//                setLabelCount(6, true)
//                textSize = 11f
//                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
//                valueFormatter = object : ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return "${value.toInt()}%"
//                    }
//                }
//                enableGridDashedLine(10f, 10f, 0f)
//                setDrawGridLines(true)
//                gridColor = Color.GRAY
//            }
//
//            axisRight.isEnabled = false
//            description.isEnabled = false
//            legend.isEnabled = false
//            setTouchEnabled(true)
//
//            val markerView = CustomMarkerView(mActivity)
//            this.marker = markerView
//
//            setDrawBarShadow(false)
//            setDrawValueAboveBar(false)
//
//            animateY(1000)
//            setExtraOffsets(20f, 20f, 20f, 20f)
//
//            invalidate()
//        }
//    }
//    private fun setupBarChart2() {
//        val entries = listOf(
//            BarEntry(0f, 100f),
//            BarEntry(1f, 80f),
//            BarEntry(2f, 60f),
//            BarEntry(3f, 40f),
//            BarEntry(4f, 20f)
//        )
//
//        val subjects = listOf("Tamil", "English", "Maths", "Science", "Social")
//
//        val dataSet = BarDataSet(entries, "").apply {
//            val startColor = Color.parseColor("#ffffff")
//            val endColor = Color.parseColor("#ffffff")
//            colors = listOf(
//                startColor,
//                endColor
//            )
//            setGradientColor(startColor, endColor)
//            setDrawValues(false)
//        }
//
//        barChart2.renderer = TopRoundedBarChartRenderer(
//            barChart2,
//            barChart2.animator,
//            barChart2.viewPortHandler
//        )
//
//        val barData = BarData(dataSet)
//        barData.barWidth = 0.3f
//
//        barChart2.apply {
//            data = barData
//
//            xAxis.valueFormatter = IndexAxisValueFormatter(subjects)
//            xAxis.position = XAxis.XAxisPosition.BOTTOM
//            xAxis.setDrawGridLines(false)
//            xAxis.granularity = 1f
//            xAxis.textSize = 11f
//            xAxis.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
//
//            axisLeft.axisMinimum = 0f
//            axisLeft.axisMaximum = 100f
//            axisLeft.granularity = 20f
//            axisLeft.setLabelCount(5, true)
//            axisLeft.textSize = 11f
//            axisLeft.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
//            axisLeft.valueFormatter = object : ValueFormatter() {
//                override fun getFormattedValue(value: Float): String {
//                    return "${value.toInt()}%"
//                }
//            }
//
//            axisLeft.enableGridDashedLine(10f, 10f, 0f)
//            axisLeft.setDrawGridLines(true)
//            axisLeft.gridColor = Color.GRAY
//
//            axisRight.isEnabled = false
//
//            // Chart styling
//            description.isEnabled = false
//            legend.isEnabled = false
//            setTouchEnabled(true)
//            // Add Marker for Tooltips
//            val markerView = CustomMarkerView(mActivity)
//            barChart2.marker = markerView
//
//            // Add rounded corners to bars
//            setDrawBarShadow(false)
//            setDrawValueAboveBar(false)
//
//            // Animation
//            animateY(1000)
//            setExtraOffsets(10f, 10f, 10f, 10f)
//
//            invalidate()
//        }
//    }
//    private fun setupBarChart3() {
//        val entries = listOf(
//            BarEntry(0f, 100f),
//            BarEntry(1f, 80f),
//            BarEntry(2f, 60f),
//            BarEntry(3f, 40f),
//            BarEntry(4f, 20f)
//        )
//
//        val subjects = listOf("Tamil", "English", "Maths", "Science", "Social")
//
//        val dataSet = BarDataSet(entries, "").apply {
//            val startColor = Color.parseColor("#ffffff")
//            val endColor = Color.parseColor("#ffffff")
//            colors = listOf(
//                startColor,
//                endColor
//            )
//            setGradientColor(startColor, endColor)
//            setDrawValues(false)
//        }
//
//        barChart3.renderer = TopRoundedBarChartRenderer(
//            barChart3,
//            barChart3.animator,
//            barChart3.viewPortHandler
//        )
//
//        val barData = BarData(dataSet)
//        barData.barWidth = 0.3f
//
//        barChart3.apply {
//            data = barData
//
//            xAxis.valueFormatter = IndexAxisValueFormatter(subjects)
//            xAxis.position = XAxis.XAxisPosition.BOTTOM
//            xAxis.setDrawGridLines(false)
//            xAxis.granularity = 1f
//            xAxis.textSize = 11f
//            xAxis.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
//
//            axisLeft.axisMinimum = 0f
//            axisLeft.axisMaximum = 100f
//            axisLeft.granularity = 20f
//            axisLeft.setLabelCount(5, true)
//            axisLeft.textSize = 11f
//            axisLeft.typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
//            axisLeft.valueFormatter = object : ValueFormatter() {
//                override fun getFormattedValue(value: Float): String {
//                    return "${value.toInt()}%"
//                }
//            }
//
//            axisLeft.enableGridDashedLine(10f, 10f, 0f)
//            axisLeft.setDrawGridLines(true)
//            axisLeft.gridColor = Color.GRAY
//
//            axisRight.isEnabled = false
//
//            // Chart styling
//            description.isEnabled = false
//            legend.isEnabled = false
//            setTouchEnabled(true)
//            // Add Marker for Tooltips
//            val markerView = CustomMarkerView(mActivity)
//            barChart3.marker = markerView
//
//            // Add rounded corners to bars
//            setDrawBarShadow(false)
//            setDrawValueAboveBar(false)
//
//            // Animation
//            animateY(1000)
//            setExtraOffsets(10f, 10f, 10f, 10f)
//
//            invalidate()
//        }
//    }
    private fun setupBarChart4() {

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

    barChart4.renderer = GroupTopRoundedBarChartRenderer(
        barChart4,
        barChart4.animator,
        barChart4.viewPortHandler
    )

    val barWidth = 0.15f
    val barSpace = 0.05f
    val groupSpace = 0.4f
    val groupCount = terms.size.toFloat()

    val data = BarData(dataSet1, dataSet2, dataSet3).apply {
        this.barWidth = barWidth
        groupBars(0f, groupSpace, barSpace)
    }

    barChart4.apply {
        this.data = data

        xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(terms)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            textSize = 12f
            typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)

            axisMinimum = 0f  // Start at 0 to align bars correctly
            axisMaximum = groupCount + (groupCount * (barWidth + barSpace))  // Auto-calculate max value
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

    private fun clsTestBarChart(result: ArrayList<SubjectWiseClassExamProResponse.Result>) {
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
        val validResults = result.filter { it.percentage!!.toFloat() ?: 0f > 0f }
            .toCollection(ArrayList())

        val segmentPercentages = FloatArray(validResults.size) { i ->
            validResults[i].percentage?.toFloat() ?: 0f
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

//            marker = CustomMarkerView2(mActivity, validResults)

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)

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

//    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {
//        /* listOfBioFragment.clear()
//         listOfBioFragment.add(listChildFragment1)
//         listOfBioFragment.add(listChildFragment2)
//         listOfBioFragment.add(listChildFragment3)*/
//
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
//        // binding.tabLayout.tabGravity = TabLayout.GRAVITY_START
//        binding.tabLayout.tabGravity = TabLayout.GRAVITY_START
//        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
//
//        val linear0: View = inflater.inflate(R.layout.custom_tab, container, false)
//        val txttab0 = linear0.findViewById<TextView>(R.id.tab)
//        txttab0.text = "My Students"
//        UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
//        txttab0.setTextAppearance(R.style.FontMedium)
////        UiUtils.linearLayoutBgTint(lin0,"#F2D9DA",null)
//        binding.tabLayout.getTabAt(0)!!.customView = linear0
//
//        val linear1: View = inflater.inflate(R.layout.custom_tab, container, false)
//        val txttab1 = linear1.findViewById<TextView>(R.id.tab)
//        txttab1.text = "Attendance"
//        UiUtils.textViewTextColor(txttab1, null, R.color.black_varient3)
//        txttab1.setTextAppearance(R.style.FontMedium)
//        binding.tabLayout.getTabAt(1)!!.customView = linear1
//
//        val linear2: View = inflater.inflate(R.layout.custom_tab, container, false)
//        val txttab2 = linear2.findViewById<TextView>(R.id.tab)
//        txttab2.text = "Fees"
//        UiUtils.textViewTextColor(txttab2, null, R.color.black_varient3)
//        txttab2.setTextAppearance(R.style.FontMedium)
//        binding.tabLayout.getTabAt(2)!!.customView = linear2
//
//        val linear3: View = inflater.inflate(R.layout.custom_tab, container, false)
//        val txttab3 = linear3.findViewById<TextView>(R.id.tab)
//        txttab3.text = "Examination"
//        UiUtils.textViewTextColor(txttab3, null, R.color.black_varient3)
//        txttab3.setTextAppearance(R.style.FontMedium)
//        binding.tabLayout.getTabAt(3)!!.customView = linear3
//
//        val linear4: View = inflater.inflate(R.layout.custom_tab, container, false)
//        val txttab4 = linear4.findViewById<TextView>(R.id.tab)
//        txttab4.text = "Progress"
//        UiUtils.textViewTextColor(txttab4, null, R.color.black_varient3)
//        txttab4.setTextAppearance(R.style.FontMedium)
//        binding.tabLayout.getTabAt(4)!!.customView = linear4
//
//        val linear5: View = inflater.inflate(R.layout.custom_tab, container, false)
//        val txttab5 = linear5.findViewById<TextView>(R.id.tab)
//        txttab5.text = "Classroom Champions"
//        UiUtils.textViewTextColor(txttab5, null, R.color.black_varient3)
//        txttab5.setTextAppearance(R.style.FontMedium)
//        binding.tabLayout.getTabAt(5)!!.customView = linear5
//
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                val linear0: View = tab.customView!!
//                val txttab0 = linear0.findViewById<TextView>(R.id.tab)
//                UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
//                txttab0.setTextAppearance(R.style.FontMedium)
//                if (tab.position == 0) {
//                    binding.pageMyStudents.visibility = View.VISIBLE
//                    binding.tabAttendance.visibility = View.GONE
//                    binding.pageFeeLayout.visibility = View.GONE
//                    binding.pageExamination.visibility = View.GONE
//                    binding.pageProgress.visibility = View.GONE
//                    binding.pageClsRoomChampions.visibility = View.GONE
//                } else if (tab.position == 1) {
//                    binding.pageMyStudents.visibility = View.GONE
//                    binding.tabAttendance.visibility = View.VISIBLE
//                    binding.pageFeeLayout.visibility = View.GONE
//                    binding.pageExamination.visibility = View.GONE
//                    binding.pageProgress.visibility = View.GONE
//                    binding.pageClsRoomChampions.visibility = View.GONE
//                } else if (tab.position == 2) {
//                    binding.pageMyStudents.visibility = View.GONE
//                    binding.tabAttendance.visibility = View.GONE
//                    binding.pageFeeLayout.visibility = View.VISIBLE
//                    binding.pageExamination.visibility = View.GONE
//                    binding.pageProgress.visibility = View.GONE
//                    binding.pageClsRoomChampions.visibility = View.GONE
//                } else if (tab.position == 3) {
//                    binding.pageMyStudents.visibility = View.GONE
//                    binding.tabAttendance.visibility = View.GONE
//                    binding.pageFeeLayout.visibility = View.GONE
//                    binding.pageExamination.visibility = View.VISIBLE
//                    binding.pageProgress.visibility = View.GONE
//                    binding.pageClsRoomChampions.visibility = View.GONE
//                } else if (tab.position == 4) {
//                    binding.pageMyStudents.visibility = View.GONE
//                    binding.tabAttendance.visibility = View.GONE
//                    binding.pageFeeLayout.visibility = View.GONE
//                    binding.pageExamination.visibility = View.GONE
//                    binding.pageProgress.visibility = View.VISIBLE
//                    binding.pageClsRoomChampions.visibility = View.GONE
//                    teacherAttoveral()
//                } else if (tab.position == 5) {
//                    binding.pageMyStudents.visibility = View.GONE
//                    binding.tabAttendance.visibility = View.GONE
//                    binding.pageFeeLayout.visibility = View.GONE
//                    binding.pageExamination.visibility = View.GONE
//                    binding.pageProgress.visibility = View.GONE
//                    binding.pageClsRoomChampions.visibility = View.VISIBLE
//
//                }
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                val linear1: View = tab.customView!!
//                val txttab1 = linear1.findViewById<TextView>(R.id.tab)
//                UiUtils.textViewTextColor(txttab1, null, R.color.black)
//                txttab1.setTextAppearance(R.style.FontMedium)
//            }
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//    }

//    private fun getStudentList(){
//        DialogUtils.showLoader(mActivity)
//        ApiConnection.getInstance().getStudentList(mActivity,"",programId,gender).observe(mActivity){
//            it?.let {
//                DialogUtils.dismissLoader()
//                it.success.let { success ->
//                    if (success){
//                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
//                            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
//                            val adapter = StudentProfileAdapter(mActivity, it.result!!.rows!!,object :
//                                OnClickListener {
//                                override fun onClickItem(pos: Int) {
////                                    assUpdMark(pos)
//                                }
//                            })
//                            binding.studentListRecycler.layoutManager= layoutManager
//                            binding.studentListRecycler.adapter = adapter
//                        }
//                        else{
//                            binding.noData1.root.visibility = View.VISIBLE
//                            binding.studentListRecycler.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root, false)
//                        }
//                    }
//                    else{
//                        binding.noData1.root.visibility = View.VISIBLE
//                        binding.studentListRecycler.visibility = View.GONE
//                        UiUtils.showSnack(it.msg, binding.root, false)
//                    }
//                }
//            }
//        }
//    }


    fun getExam() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().getExam(mActivity,search, examStatus).observe(mActivity) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.noDatas.root.visibility = View.GONE
                            binding.tabparentHomeworkRecyclerView.visibility = View.VISIBLE
                            val layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = ExamListAdapter(mActivity, it.result!!.rows!!)
                            binding.tabparentHomeworkRecyclerView.layoutManager = layoutManager
                            binding.tabparentHomeworkRecyclerView.adapter = adapter
                        }
                        else {
                            binding.noDatas.root.visibility = View.VISIBLE
                            binding.tabparentHomeworkRecyclerView.visibility = View.GONE
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noDatas.root.visibility = View.VISIBLE
                        binding.tabparentHomeworkRecyclerView.visibility = View.GONE
                    }
                }
            }
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
}