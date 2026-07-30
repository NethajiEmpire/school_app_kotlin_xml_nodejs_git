package com.lms.sch.activity

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.MySubjectsAdapter
import com.lms.sch.adapter.TeacherTimeTableAdapter
import com.lms.sch.adapter.TimeTableAdapter
import com.lms.sch.adapter.TimeTableTeacherAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.databinding.ActivityKidsTimeTableBinding
import com.lms.sch.databinding.ActivityTeacherTimeTableBinding
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetClassTimeTableResponse
import com.lms.sch.response.GetTeacherScheduleResponse
import com.lms.sch.response.TeacherTimeTableResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import io.grpc.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class TeacherTimeTableActivity : BaseActivity() {
    private lateinit var binding: ActivityTeacherTimeTableBinding
    var calendar = Calendar.getInstance()
    var timeTableRes = ArrayList<TeacherTimeTableResponse.Result.Periods>()
    var timeTableDay = ""
    var selectedMonth = 0
    var selectedYear = 0
    var programId = ""
    var subId = ""
    private var currentMonthDays = ArrayList<Date>()
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTeacherTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        currentMonthDays = getCurrentMonthDays()
        loadDates()
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        binding.date.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_month_picker)
            val bind: DialogMonthPickerBinding = DialogMonthPickerBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(bind.root)
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this, R.color.transparent))
            )
            var width: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//        var height: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setGravity(Gravity.CENTER)
            var sMonth = selectedMonth
            var sYear = selectedYear
            val yearAdapter = YearAdapter(this, years,selectedYear, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    sYear = pos
                    bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                }
            })
            bind.yearRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
            val monthAdapter = MonthsAdapter(this, months,years[selectedYear].toInt(),selectedMonth, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    sMonth = pos
                    bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                }
            })
            bind.monthRecycler.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
            bind.monthRecycler.adapter = monthAdapter

            bind.select.setOnClickListener {
                calendar = Calendar.getInstance()
                val currentDate = calendar.time
                val date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
                selectedYear = sYear
                selectedMonth = sMonth
                currentMonthDays = getCurrentMonthDays(years[sYear].toInt(),selectedMonth)
                binding.dayName.text = "${sdfApi.format(currentDate)} Time Table"
                binding.sDate.text = date.format(currentDate)
                binding.date.text = "${months[selectedMonth]} ${years[selectedYear]}"
                val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
                    UiUtils.log("gfhjk",""+selectedDate)
                    val dt = sdfApi.format(selectedDate).toLowerCase(Locale.getDefault())
                    binding.dayName.text = "${sdfApi.format(selectedDate)} Time Table"
                    binding.sDate.text = date.format(selectedDate)
                    timeTableDay = dt
                    getClassTimeTableHome()
                }
                val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.dateRecycler1.layoutManager = linearLayoutManager1
                binding.dateRecycler1.adapter = adapter1

                val currentDayIndex1 = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
                if (currentDayIndex1 != -1) {
                    val itemWidth = resources.getDimensionPixelSize(R.dimen._55dp)
                    binding.dateRecycler1.centerItem(currentDayIndex1, itemWidth)
                }
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
        getClassTimeTableHome()
    }
    fun loadDates(){
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        binding.sDate.text = date.format(currentDate)
        binding.dayName.text = "${sdfApi.format(currentDate)} Time Table"
        val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
            UiUtils.log("gfhjk",""+selectedDate)
            val dt = sdfApi.format(selectedDate).toLowerCase(Locale.getDefault())
            binding.dayName.text = "${sdfApi.format(selectedDate)} Time Table"
            binding.sDate.text = date.format(selectedDate)
            timeTableDay = dt
            getClassTimeTableHome()
        }
        val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler1.layoutManager = linearLayoutManager1
        binding.dateRecycler1.adapter = adapter1

        val currentDayIndex1 = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex1 != -1) {
            val itemWidth = resources.getDimensionPixelSize(R.dimen._55dp)
            binding.dateRecycler1.centerItem(currentDayIndex1, itemWidth)
        }
    }
    fun getClassTimeTableHome() {
        if (timeTableRes.isEmpty()) {
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().teacherTimeTable(this).observe(this) {
                it?.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null && it.result!!.periods!!.isNotEmpty()) {
                                binding.noData01.root.visibility = View.GONE
                                binding.timetableRecycler.visibility = View.VISIBLE
                                timeTableRes = it.result!!.periods!!
                                val adapter = TeacherTimeTableAdapter( this,it.result!!.periods!!,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        binding.timeTableDialog.root.visibility = View.VISIBLE
                                    }
                                })
                                val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
                                binding.timetableRecycler.layoutManager = layoutManager
                                binding.timetableRecycler.adapter = adapter
                            } else {
                                binding.noData01.txt.text = "No Time table Available"
                                binding.noData01.root.visibility = View.VISIBLE
                                binding.timetableRecycler.visibility = View.GONE
                            }
                        } else {
                            binding.noData01.txt.text = "No Time table Available"
                            binding.noData01.root.visibility = View.VISIBLE
                            binding.timetableRecycler.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
        } else {
            binding.noData01.root.visibility = View.GONE
            binding.timetableRecycler.visibility = View.VISIBLE
            val adapter =TeacherTimeTableAdapter(this, timeTableRes, object : OnClickListener {
                    override fun onClickItem(pos: Int) {
                        binding.timeTableDialog.root.visibility = View.VISIBLE
                    }
                })
            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.timetableRecycler.layoutManager = layoutManager
            binding.timetableRecycler.adapter = adapter
        }
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
    fun RecyclerView.centerItem(position: Int, itemWidth: Int) {
        post {
            val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
            val visibleWidth = width - paddingLeft - paddingRight
            val exactCenter = (visibleWidth / 2) - (itemWidth / 2)
            layoutManager.scrollToPositionWithOffset(position, exactCenter)
        }
    }
}