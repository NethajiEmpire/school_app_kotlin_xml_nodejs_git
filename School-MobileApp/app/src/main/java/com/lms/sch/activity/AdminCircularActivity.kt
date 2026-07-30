package com.lms.sch.activity

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.NoticeBoardAdapter
import com.lms.sch.adapter.WeekDayAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.databinding.ActivityAdminCircularBinding
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class AdminCircularActivity : BaseActivity() {
    lateinit var binding: ActivityAdminCircularBinding
    var noticeList = ArrayList<NoticeBoardResponse.Result>()
    var eventDate = ""
    var monDate = ""
    var dayDate = ""
    var type = ""
    var selectedMonth = 0
    var calendar = Calendar.getInstance()
    var selectedYear = 0
    private var currentMonthDays = ArrayList<Date>()
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCircularBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(this, ProfileActivity(),null,false)
        }
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        val date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val mon = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        currentMonthDays = getCurrentMonthDays()
        val currentDate = calendar.time
        dayDate = date.format(currentDate)
        monDate = mon.format(currentDate)
        loadDates()
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
                val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
                selectedYear = sYear
                selectedMonth = sMonth
                currentMonthDays = getCurrentMonthDays(years[sYear].toInt(),selectedMonth)
                binding.date.text = "${months[selectedMonth]} ${years[selectedYear]}"
                val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
                    UiUtils.log("gfhjk",""+selectedDate)
                    val dt = sdfDate.format(selectedDate)
                    eventDate = dt
                    getEvents()
                    val date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                    val mon = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    if (type == "day"){
                        binding.cDate.text = date.format(selectedDate)
                        dayDate = binding.cDate.text.toString()
                    }
                    else {
                        binding.cDate.text = mon.format(selectedDate)
                        monDate = binding.cDate.text.toString()
                    }
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

        ApiConnection.getInstance().getNoticeBoard(this,"","").observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result.isNotEmpty()){
                            noticeList = it.result
                            loadDates()
                        }
                        else {
                            loadDates()
                        }
                    }
                    else{
                        
                    }
                }
            }
        }

        binding.backarrow.setOnClickListener{
            onBackPressed()
        }

        binding.tabAnnouncement.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabAnnouncement,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(binding.tabEvents,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.tabEvents, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.tabAnnouncement, null, R.color.colorPrimary)
            
        }
        binding.tabEvents.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabAnnouncement,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(binding.tabEvents,R.drawable.border_line_curve_24dp_primary )
            UiUtils.textViewTextColor(binding.tabAnnouncement, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.tabEvents, null, R.color.colorPrimary)
        }

        binding.tabDay.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabDay,R.drawable.border_curve_24dp )
            UiUtils.textviewCustomDrawable(binding.tabWeek,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.tabDay, null,R.color.white)
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.black_varient3)
            type = "day"
            binding.title.text = "Today Announcement"
            binding.cDate.text = dayDate
            getEvents()
        }
        binding.tabWeek.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.tabWeek, R.drawable.border_curve_24dp)
            UiUtils.textviewCustomDrawable(binding.tabDay, R.drawable.border_curve_6dp)
            UiUtils.textViewBgTint(binding.tabWeek, null, R.color.white)
            UiUtils.textViewTextColor(binding.tabWeek, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabDay, null, R.color.black_varient3)
            type = "month"
            binding.title.text = "Monthly Announcement"
            binding.cDate.text = monDate
            getEvents()
        }
        binding.tabDay.performClick()

    }
    fun getEvents(){
        if (type == "month"){
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().getNoticeBoard(this,type,"").observe(this){
                it?.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success){
                            if (it.result != null && it.result.isNotEmpty()){
                                binding.noData.root.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE
                                val adapter = NoticeBoardAdapter(this,"",it.result)
                                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                                binding.recycler.layoutManager = layoutManager
                                binding.recycler.adapter = adapter
                            }
                            else {
                                binding.noData.txt.text = "No events present today!"
                                binding.noData.root.visibility = View.VISIBLE
                                binding.recycler.visibility = View.GONE
                            }
                        }
                        else{
                            binding.noData.txt.text = "No events present today!"
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                }
            }
        }
        else {
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().getNoticeBoard(this,type,eventDate).observe(this){
                it?.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success){
                            if (it.result != null && it.result.isNotEmpty()){
                                binding.noData.root.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE
                                val adapter = NoticeBoardAdapter(this,"",it.result)
                                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                                binding.recycler.layoutManager = layoutManager
                                binding.recycler.adapter = adapter
                            }
                            else {
                                binding.noData.txt.text = "No events present today!"
                                binding.noData.root.visibility = View.VISIBLE
                                binding.recycler.visibility = View.GONE
                            }
                        }
                        else{
                            binding.noData.txt.text = "No events present today!"
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                }
            }
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

    fun loadDates(){
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())
        binding.date.text = date
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
            UiUtils.log("gfhjk",""+selectedDate)
            val dt = sdfDate.format(selectedDate).toLowerCase(Locale.getDefault())
            eventDate = dt
            getEvents()
            val date1 = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val mon = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            if (type == "day"){
                binding.cDate.text = date1.format(selectedDate)
                dayDate = binding.cDate.text.toString()
            }
            else {
                binding.cDate.text = mon.format(selectedDate)
                monDate = binding.cDate.text.toString()
            }
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

}