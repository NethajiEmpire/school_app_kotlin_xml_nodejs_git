package com.lms.sch.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lms.sch.R
import com.lms.sch.adapter.SubjectStudentsAdapter
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.databinding.ActivityStudentStatsBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.response.SubmissionProgressResponse
import com.lms.sch.response.SubmissionProgressTeacherSide
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class StudentStatsActivity : BaseActivity() {
    lateinit var binding: ActivityStudentStatsBinding
    lateinit var pieChart: PieChart
    lateinit var pieChart1: PieChart
    var name = ""
    var studentId = ""
    var type = ""
    var resultAtt = GetStudentAttenDanceResponse.Result()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = intent.getStringExtra(Constants.IntentKeys.KEY) ?: ""
        Log.d("hjgdj",name)
        pieChart = binding.pieChart
        pieChart1 = binding.pieChart1
        binding.back.setOnClickListener {
            finish()
        }
        Log.d("hasjdfakdsj",name)
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentProfilethree(this,name).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.applicationForm != null && it.result!!.applicationForm!!.studentInfo != null &&  it.result!!.student != null) {
                            Log.d("hasjdfakdsj",it.result.toString())
                            studentId = it.result!!.student!!._id!!
                            binding.name.text = "${it.result!!.applicationForm!!.studentInfo!!.firstName} ${it.result!!.applicationForm!!.studentInfo!!.lastName}"
                            UiUtils.loadImage(binding.img,it.result!!.student!!!!.img_url)
                            if (it.result!!.student!!.activeStatus!! != null && it.result!!.student!!.activeStatus!!.toString() == "active"){
                                binding.status.text = "Present"
                            }else{
                                binding.status.text = "Absent"
                            }
                            binding.admnbr.text = it.result!!.student!!.lead_id!!
                            if (it.result!!.studentClass!!.name != null &&  it.result!!.section != null){
                                binding.grade.text = "${it.result!!.studentClass!!.name} ${it.result!!.section!!.name}"
                            }else{
                                binding.grade.text = "--/--"
                            }
                            binding.rolenbr.text = it.result!!.rollNo

                            binding.gen.text = it.result!!.student!!.gender!!
                            binding.dob.text = BaseUtils.getFormattedDate(it.result!!.student!!.dob!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                            binding.bg.text = it.result!!.applicationForm!!.studentInfo!!.blood_group!!
                            binding.address.text = it.result!!.applicationForm!!.studentInfo!!.address!!
//                            getStudentAttendance()
                            binding.homeworkTap.post {
                                binding.homeworkTap.performClick()
                            }
                            Log.d("hgfhggggggggggggggggggg", it.msg)
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                            Log.d("hgfh", it.msg)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                        Log.d("hgfh", it.msg)
                    }
                }
            }
        }
        binding.homeworkTap.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.homeworkTap,  R.drawable.border_line_curve_24dp_primary )
            UiUtils.textviewCustomDrawable( binding.attendanceTap,  R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable( binding.feeInsightTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable( binding.assignmentTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable( binding.projectTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.black_varient6)
            binding.subOverView.visibility = View.VISIBLE
            binding.attendanceProgress.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            type = "homework"
            submissionProgress()
        }
        binding.assignmentTap.setOnClickListener {
            UiUtils.textviewCustomDrawable(  binding.assignmentTap,   R.drawable.border_line_curve_24dp_primary )
            UiUtils.textviewCustomDrawable( binding.attendanceTap,  R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable(binding.feeInsightTap,  R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable( binding.homeworkTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(  binding.projectTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.black_varient6)
            binding.subOverView.visibility = View.VISIBLE
            binding.attendanceProgress.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            type = "assignment"
            submissionProgress()
        }
        binding.projectTap.setOnClickListener {
            UiUtils.textviewCustomDrawable(  binding.projectTap,  R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(  binding.attendanceTap,  R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable( binding.feeInsightTap,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable(binding.homeworkTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable( binding.assignmentTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.colorPrimary)
            binding.subOverView.visibility = View.VISIBLE
            binding.attendanceProgress.visibility = View.GONE
            type = "project"
            submissionProgress()
        }
    }
    fun loadProgressBar(result: GetStudentAttenDanceResponse.Result) {
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

    private fun submissionProgress(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().singlestudentSubmission(this,type,studentId).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        val result = it.result!!
                        if (result != null ) {
                            setupPieChart2(result)
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
//    private fun setupPieChart(result: SubmissionProgressResponse.Result) {
////        val ontime = result.percentage?.onTime?.toFloat() ?: 0f
////        val missed = result.percentage?.missed?.toFloat() ?: 0f
////        val late = result.percentage?.late?.toFloat() ?: 0f
//
//        val total = result!!.total?.toFloat()?.coerceAtLeast(1f) ?: 1f
//        val ontime = result.percentage!!.onTime?.toFloat() ?: 0f
//        val missed = result.percentage!!.missed?.toFloat() ?: 0f
//        val late = result.percentage?.late?.toFloat() ?: 0f
////            val halfDayCount = result.count!!.halfDay?.toFloat() ?: 0f
//
//        val isAllZero = ontime == 0f && missed == 0f && late == 0f
//
//        val entries = if (isAllZero) {
//            arrayListOf(
//                PieEntry(100f, "No Data")
//            )
//        } else {
//            arrayListOf(
//                PieEntry((ontime / total) * 100f, "Present"),  // Green
//                PieEntry((missed / total) * 100f, "Absent"),
//                PieEntry((late / total) * 100f, "late"),// Red
////                    PieEntry((halfDayCount / total) * 100f, "Half Day") // Blue
//            )
//        }
//
//        val colors = if (isAllZero) {
//            listOf(Color.parseColor("#f5f5f5")) // Grey for no data
//        } else {
//            listOf(
//                Color.parseColor("#32B138"),  // Green for Present
//                Color.parseColor("#FF7475")  // Red for Absent
////                    Color.parseColor("#1170E4")   // Blue for Half Day
//            )
//        }
//
//        val dataSet = PieDataSet(entries, "").apply {
//            setColors(colors)
//            sliceSpace = 12f
//            selectionShift = 5f
//            setDrawValues(false)
//        }
//
//        val pieData = PieData(dataSet)
//
//        pieChart.apply {
//            data = pieData
//            description.isEnabled = false
//            isRotationEnabled = false
//            setDrawEntryLabels(false)
//            setDrawHoleEnabled(true)
//            holeRadius = 60f
//            transparentCircleRadius = 0f
//            setTouchEnabled(false)
//            legend.isEnabled = false
//            setHoleColor(Color.TRANSPARENT)
//            renderer = CurvedPieChartRenderer(this, animator, viewPortHandler)
//
//            // Animate the chart
//            animateY(1000, Easing.EaseInOutCubic)
//
//            // Refresh the chart
//            invalidate()
//        }
//    }
    @SuppressLint("SuspiciousIndentation")
    private fun setupPieChart2(result: SubmissionProgressResponse.Result) {
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
            pieData.setDrawValues(false)
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
