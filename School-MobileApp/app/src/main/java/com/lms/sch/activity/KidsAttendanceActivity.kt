package com.lms.sch.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lms.sch.R
import com.lms.sch.adapter.KidsAttendanceAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.customviews.GradientPieChartRenderer
import com.lms.sch.databinding.ActivityKidsAttendanceBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class KidsAttendanceActivity : BaseActivity() {
    lateinit var binding : ActivityKidsAttendanceBinding
    var resultAtt = GetStudentAttenDanceResponse.Result()
    lateinit var attendanceChart : PieChart
    var attendanceFilters = ArrayList<String>()
    var dateFilter = ""
    var status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKidsAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        attendanceChart = binding.attenDanceProgress
        binding.backarrow.setOnClickListener {
            finish()
        }
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(this, ProfileActivity(),null,false)
        }
        attendanceFilters.clear()
        attendanceFilters.add("All")
        attendanceFilters.add("Today")
        attendanceFilters.add("Yesterday")
        attendanceFilters.add("Last 7 days")
        attendanceFilters.add("This Week")
        attendanceFilters.add("Last Week")
        attendanceFilters.add("This Month")
        attendanceFilters.add("Last Month")

        val adapter = SpinnerAdapter(this, attendanceFilters)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val value = attendanceFilters[position]
                if (value == "All"){
                    dateFilter = ""
                }
                else if (value == "Today"){
                    dateFilter = "today"
                }
                else if (value == "Yesterday"){
                    dateFilter = "yesterday"
                }
                else if (value == "Last 7 days"){
                    dateFilter = "last7days"
                }
                else if (value == "This Week"){
                    dateFilter = "thisweek"
                }
                else if (value == "Last Week") {
                    dateFilter = "lastweek"
                }
                else if (value == "This Month"){
                    dateFilter = "thismonth"
                }
                else if (value == "Last Month") {
                    dateFilter = "lastmonth"
                }
                else{
                    dateFilter = ""
                }
                binding.all.performClick()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.all.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.all,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.present,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.absent,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.pending,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.all,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.present,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.absent,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.pending,null,R.color.black_varient3)
            status = ""
            getStudentAttendance()
        }
        binding.present.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.all,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.present,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.absent,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.pending,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.all,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.present,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.absent,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.pending,null,R.color.black_varient3)
            status = "present"
            getStudentAttendance()
        }
        binding.absent.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.all,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.present,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.absent,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.pending,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.all,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.present,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.absent,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.pending,null,R.color.black_varient3)
            status = "absent"
            getStudentAttendance()
        }
        binding.pending.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.all,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.present,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.absent,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.pending,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.all,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.present,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.absent,null,R.color.black_varient3)
            UiUtils.textViewTextColor(binding.pending,null,R.color.colorPrimary)
            status = "pending"
            getStudentAttendance()
        }

    }

    private fun getStudentAttendance(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getStudentAttendance(this,status,dateFilter).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        resultAtt = it.result!!
                        if (it.result != null ) {
                            loadProgressBar(resultAtt)
                            if (resultAtt.streaks != null){
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
                            if (resultAtt.attendance != null && resultAtt.attendance!!.isNotEmpty()){
                                binding.attendanceRecycler.visibility = View.VISIBLE
                                binding.noData.root.visibility = View.GONE
                                val adapter = KidsAttendanceAdapter(this,resultAtt.attendance!!)
                                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                                binding.attendanceRecycler.layoutManager = layoutManager
                                binding.attendanceRecycler.adapter = adapter
                            }
                            else {
                                binding.attendanceRecycler.visibility = View.GONE
                                binding.noData.root.visibility = View.VISIBLE
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
            sliceSpace = 2f // Reduced space between segments for better visibility
            this.colors = colors // Assign colors to entries
        }

        val pieData = PieData(dataSet)

        attendanceChart.apply {
            data = pieData
            description.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            setDrawHoleEnabled(true)
            holeRadius = 90f // Adjusted for better proportion within 300dp height
            transparentCircleRadius = 65f
            setHoleColor(Color.TRANSPARENT)
            legend.isEnabled = false
            isRotationEnabled = false
            setTouchEnabled(false)

            setMaxAngle(360f) // Full circle
            rotationAngle = 270f // Start from the top

            setCenterText("") // Let the TextView in the layout display the percentage
            //  setCenterTextColor(Color.WHITE)
            //  setCenterTextSize(0f)

            // Add padding to ensure the chart doesn't touch the edges
            setExtraOffsets(30f, 30f, 30f, 30f)
            dataSet.selectionShift = 0f // No shift on selection

            renderer = GradientPieChartRenderer(
                this, animator, viewPortHandler,
                colors
            )

            animateY(1000, Easing.EaseInOutCubic)
            invalidate()
        }
    }

}