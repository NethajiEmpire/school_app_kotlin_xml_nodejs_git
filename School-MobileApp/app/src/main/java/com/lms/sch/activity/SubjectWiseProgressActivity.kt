package com.lms.sch.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.adapter.ClassTestProgressAdapter
import com.lms.sch.adapter.ExamProgressAdapter
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.SubjectBasedMarksAdapter
import com.lms.sch.adapter.SubjectListAdapter
import com.lms.sch.adapter.WeekDayAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.databinding.ActivitySubjectWiseProgressBinding
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.ExamResultResponse
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.get
import kotlin.math.roundToInt
import kotlin.text.toInt

class SubjectWiseProgressActivity : BaseActivity() {
    lateinit var binding: ActivitySubjectWiseProgressBinding
    var calendar = Calendar.getInstance()
    var subject = "all"
    var date = ""
    var program = ""
    var adapter : SubjectListAdapter? = null
    var noticeList = ArrayList<NoticeBoardResponse.Result>()
    var selectedDate: Date? = null
    private var currentMonthDays = ArrayList<Date>()
    var isClicked = false
    var eventDate = ""
    var examid = ""
    var mgrExamid = ""
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var selectedYear : Int = 0
    var selectedMonth : Int = 0
    var timeTableDay = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubjectWiseProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.page1.visibility = View.VISIBLE
        binding.page2.visibility = View.GONE
        val today = Calendar.getInstance().time
        selectedDate = today
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today)
        binding.date.text = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(today)
//        loadReport()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfDay = SimpleDateFormat("EEEE", Locale.getDefault())
        val sdfMon = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(currentDate)
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        val currentMonth = SimpleDateFormat("MMM", Locale.getDefault()).format(Date())
        val api = sdfDay.format(currentDate).toLowerCase(Locale.getDefault())
//        val date = sdfDate.format(currentDate).toLowerCase(Locale.getDefault())
        val cYear = years.indexOf(currentYear.toString())
        val cMon = months.indexOf(currentMonth.toString())
        if (cYear != -1) {
            selectedYear = cYear
        }
        if (cMon != -1) {
            selectedMonth = cMon
        }
        binding.dtText1.text = sdfMon
        timeTableDay = api
        eventDate = date
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        if (SharedHelper(this).role == "STUDENT"){
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().studentProfileTwo(this, SharedHelper(this).id).observe(this) {
                it?.let {
                    DialogUtils.dismissLoader()
                    if (it.success) {
                        program = it.result!!.program!!
                    }else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }

        }
        else{
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().studentProfileTwo(this, SharedHelper(this).childId).observe(this) {
                it?.let {
                    DialogUtils.dismissLoader()
                    if (it.success) {
                        program = it.result!!.program!!
                    }else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        loadEvents()
        binding.report.setOnClickListener {
            BaseUtils.startActivity(this, ClassTestReportActivity(),null,false)
        }
        binding.date1.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_month_picker)
            val bind: DialogMonthPickerBinding = DialogMonthPickerBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(bind.root)
            dialog.window?.setBackgroundDrawable( ColorDrawable(ContextCompat.getColor(this, R.color.transparent)) )
            var width: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//          var height: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//          val width = ViewGroup.LayoutParams.MATCH_PARENT
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
                selectedYear = sYear
                selectedMonth = sMonth
                currentMonthDays = getCurrentMonthDays(years[sYear].toInt(), selectedMonth)
                binding.dtText1.text = "${months[selectedMonth]} ${years[selectedYear]}"
                val adapter = WeekDayAdapter(this, currentMonthDays, currentDate, noticeList, object : OnClickListener {
                    override fun onClickItem(pos: Int) {
                        selectedDate = currentMonthDays[pos]
                        val dt = sdfDate.format(currentMonthDays[pos])
                        eventDate = dt
                        date = dt
                        binding.date.text = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(selectedDate!!)
                        loadReport(true)
                    }
                })

                val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.dateRecycler.layoutManager = linearLayoutManager
                binding.dateRecycler.adapter = adapter

                // 👇 Fix: Set default selected date to first of the month
                selectedDate = currentMonthDays.firstOrNull()
                if (selectedDate != null) {
                    date = sdfDate.format(selectedDate)
                    binding.date.text = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(selectedDate!!)
                    loadReport(true)
                }

                dialog.dismiss()
            }
            bind.cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnDismissListener {

            }
            dialog.show()
        }
//        binding.date2.setOnClickListener {
//            showCalender{
//                val sdfDate = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
//                val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
//                val dt = sdfDate.format(it)
//                val api = sdfApi.format(it).toLowerCase(Locale.getDefault())
//                binding.dtText1.text = dt
////                timeTableDay = api
//                loadEvents()
//            }
//        }
        binding.tab1.setOnClickListener {
            UiUtils.textViewTextColor(binding.tab1,null, R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tab1, R.drawable.border_line_curve_24dp_primary)
            adapter?.clearSelection()
            binding.classTestResult.text = "All Class test Results"
            subject = "all"
            if (selectedDate != null) {
                val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                date = sdfDate.format(selectedDate)
                loadReport(false)
            } else {
                Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show()
            }
        }
        initAdapter(layoutInflater, binding.root)
//        binding.tabLayout.getTabAt(0)?.select()
    }
    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {
        /* listOfBioFragment.clear()
         listOfBioFragment.add(listChildFragment1)
         listOfBioFragment.add(listChildFragment2)
         listOfBioFragment.add(listChildFragment3)*/

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        // binding.tabLayout.tabGravity = TabLayout.GRAVITY_START
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_START
        binding.tabLayout.tabMode = TabLayout.MODE_FIXED

        val linear0: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab0 = linear0.findViewById<TextView>(R.id.tab)
        txttab0.text = "Class test Report"
        UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
        txttab0.setTextAppearance(R.style.FontMedium)
//        UiUtils.linearLayoutBgTint(lin0,"#F2D9DA",null)
        binding.tabLayout.getTabAt(0)!!.customView = linear0

        val linear1: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab1 = linear1.findViewById<TextView>(R.id.tab)
        txttab1.text = "Examination Report"
        UiUtils.textViewTextColor(txttab1, null, R.color.black_varient3)
        txttab1.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(1)!!.customView = linear1

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val linear0: View = tab.customView!!
                val txttab0 = linear0.findViewById<TextView>(R.id.tab)
                UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                txttab0.setTextAppearance(R.style.FontMedium)
                binding.page1.visibility = View.VISIBLE
                binding.calender.visibility = View.VISIBLE
                when (tab.position) {
                    0 -> {
                        binding.tab1.visibility = View.VISIBLE
                        binding.reportRecycler.visibility = View.VISIBLE
                        binding.reportRecyclerexam.visibility = View.GONE
                        getClassTestRes()
                    }
                    1 ->{
                        binding.tab1.visibility = View.GONE
                        binding.reportRecycler.visibility = View.GONE
                        binding.reportRecyclerexam.visibility = View.VISIBLE
                        getExaminationRes()

                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                val linear1: View = tab.customView!!
                val txttab1 = linear1.findViewById<TextView>(R.id.tab)
                UiUtils.textViewTextColor(txttab1, null, R.color.black)
                txttab1.setTextAppearance(R.style.FontMedium)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.tabLayout.post {
            val defaultTab = binding.tabLayout.getTabAt(0) // Use 1 for Examination Report
            if (defaultTab != null) {
                binding.tabLayout.selectTab(defaultTab)
                getClassTestRes() // This triggers onTabSelected automatically
            }
        }
    }
    fun loadReport(isReportLayout: Boolean){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().classTestReport(this,subject,date).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            binding.noData.root.visibility = View.GONE
                            binding.reportRecycler.visibility = View.VISIBLE
//                            binding.date.text = BaseUtils.getFormattedDate(it.result!![pos].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                            val adapter = ClassTestProgressAdapter(this, isReportLayout,it.result!!)
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                            binding.reportRecycler.layoutManager = layoutManager
                            binding.reportRecycler.adapter = adapter
                        }
                        else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.reportRecycler.visibility = View.GONE
                            binding.reportRecyclerexam.visibility = View.GONE
                        }
                    }
                    else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.reportRecycler.visibility = View.GONE
                        binding.reportRecyclerexam.visibility = View.GONE
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    fun loadEvents() {
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentMonthDays = getCurrentMonthDays()
        val adapter = WeekDayAdapter1(currentMonthDays, currentDate) { selected ->
            selectedDate = selected
            val dt = sdfDate.format(selectedDate)
//            UiUtils.log("kiuygf",dt)
            binding.date.text = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(selectedDate!!)
            date = dt
            loadReport(false)
        }
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler.layoutManager = linearLayoutManager
        binding.dateRecycler.adapter = adapter

        val currentDayIndex = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex != -1) {
            binding.dateRecycler.centerItem(currentDayIndex)
        }
    }
    fun RecyclerView.centerItem(position: Int) {
        post {
            val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
            layoutManager.scrollToPosition(position)
            post {
                val child = layoutManager.findViewByPosition(position) ?: return@post
                val isVertical = layoutManager.orientation == LinearLayoutManager.VERTICAL
                val recyclerSize =
                    if (isVertical) height - paddingTop - paddingBottom else width - paddingStart - paddingEnd
                val itemSize = if (isVertical) child.height else child.width
                val offset = (recyclerSize / 2) - (itemSize / 2)
                layoutManager.scrollToPositionWithOffset(position, offset)
            }
        }
    }
    private fun getCurrentMonthDays(): List<Date> {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val totalItems = daysInMonth + (firstDayOfWeek - calendar.firstDayOfWeek)

        val daysList = mutableListOf<Date>()

        for (i in 1 until firstDayOfWeek) {
            val tempCalendar = Calendar.getInstance()
            tempCalendar.set(Calendar.YEAR, currentYear)
            tempCalendar.set(Calendar.MONTH, currentMonth)
            tempCalendar.set(Calendar.DAY_OF_MONTH, 1)
            tempCalendar.add(Calendar.DAY_OF_MONTH, -(firstDayOfWeek - i))
            daysList.add(tempCalendar.time)
        }
        for (i in 1..daysInMonth) {
            val tempCalendar = Calendar.getInstance()
            tempCalendar.set(Calendar.YEAR, currentYear)
            tempCalendar.set(Calendar.MONTH, currentMonth)
            tempCalendar.set(Calendar.DAY_OF_MONTH, i)
            daysList.add(tempCalendar.time)
        }
        return daysList
    }
    fun showCalender(onDateSelected: (String) -> Unit) {
        var datePickerDialog: DatePickerDialog? = null
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day

        // Date picker dialog
        datePickerDialog = DatePickerDialog(this,
            { view, year, monthOfYear, dayOfMonth ->
                var sDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                sDate = BaseUtils.getFormattedDate(sDate, "dd/MM/yyyy", "yyyy-MM-dd")
                onDateSelected(sDate)
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }
    fun getClassTestRes(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().subjectDropdown(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            adapter = SubjectListAdapter(this,it.result!!,object: OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    binding.classTestResult.text = "${it.result!![pos].label!!} Class Test Report"
//                                  binding.result.root.visibility = View.VISIBLE
                                    subject = it.result!![pos].value!!
                                    UiUtils.textViewTextColor(binding.tab1, null, R.color.black_varient3)
                                    UiUtils.textviewCustomDrawable(binding.tab1, R.drawable.border_line_curve_24dp_grey)
                                    loadReport(true)
                                }
                            })
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
                            binding.subsRecycler.layoutManager = layoutManager
                            binding.subsRecycler.adapter = adapter
//                            binding.tab1.performClick()
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    fun getExaminationRes() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentExamId(this,program).observe(this) {
            DialogUtils.dismissLoader()
            if (it.success == true && !it.result.isNullOrEmpty()) {
                adapter = SubjectListAdapter(this, it.result!!, object : OnClickListener {
                    override fun onClickItem(pos: Int) {
                        Log.d("CLICKED_POS", "User clicked item at position $pos")
                        binding.classTestResult.text = "${it.result!![pos].label!!} Report"
                        mgrExamid = it.result!![pos].value!!
                        studentExamResult()
                    }
                })
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.subsRecycler.layoutManager = layoutManager
                binding.subsRecycler.adapter = adapter
            } else {
                UiUtils.showSnack(it.msg, binding.root, false)
            }
        }
    }
    fun studentExamResult(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentExamRes(this,mgrExamid).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        binding.noData.root.visibility = View.GONE
                        binding.reportRecycler.visibility = View.GONE
                        binding.reportRecyclerexam.visibility = View.VISIBLE
                        if (it.result != null && it.result != null && it.result!!.rows!!.isNotEmpty()){
                            for (i in it.result!!.rows!!) {
                                var std = BaseUtils.getFormattedDate(i.majorExam!!.startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                var end = BaseUtils.getFormattedDate(i.majorExam!!.endDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                binding.date.text = "${std} - ${end}"
                            }
                            val adapter = ExamProgressAdapter(this, it.result!!.rows!!)
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                            binding.reportRecyclerexam.layoutManager = layoutManager
                            binding.reportRecyclerexam.adapter = adapter
                        }else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.reportRecycler.visibility = View.GONE
                            binding.reportRecyclerexam.visibility = View.GONE
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                        binding.noData.root.visibility = View.VISIBLE
                        binding.reportRecycler.visibility = View.GONE
                        binding.reportRecyclerexam.visibility = View.GONE
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
}