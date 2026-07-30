package com.lms.sch.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.adapter.AdminTimeTableAdapter
import com.lms.sch.adapter.TimeTableAdapter
import com.lms.sch.adapter.TimetableFilterAdapter
import com.lms.sch.adapter.TimetableFilterRemoveAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.databinding.ActivityAdminTimeTableBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.BatchDropdownResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdminTimeTableActivity : BaseActivity() {
    lateinit var binding: ActivityAdminTimeTableBinding
    var calendar = Calendar.getInstance()
    var boardRes =  ArrayList<StudentBoardResponse.Result>()
    var batchRes =  ArrayList<BatchDropdownResponse.Result>()
    var classRes =  ArrayList<DropdownResponse.Result>()
    var timeTableDay = ""
    var selectedTab = ""
    var program = ""
    var batchId = ""
    var batchName = ""
    var classId = ""
    var classname = ""
    var section = ""
    var search = ""
    var boardId = ""
    var filterArr = ArrayList<String>()
    var selectedPos = -1
    var selectedPos1 = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTimeTableBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.page1.visibility = View.VISIBLE
        binding.page2.visibility = View.GONE
        loadEvents()
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search.text.toString()
                program()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                program()
            }
            false
        })

        binding.dialogFilter.tabBatch.setOnClickListener {
            selectedTab = "batch"
            UiUtils.textviewCustomDrawable(binding.dialogFilter.tabBatch,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(binding.dialogFilter.tabBatch,"#F7FBFE",null)
            UiUtils.textviewCustomDrawable(binding.dialogFilter.tabClass,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(binding.dialogFilter.tabClass,"#FFFFFF",null)
            binding.dialogFilter.title.text = "Select Batch"
            loadBatch()
        }
        binding.dialogFilter.tabClass.setOnClickListener {
            selectedTab = "classes"
            UiUtils.textviewCustomDrawable(binding.dialogFilter.tabClass,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(binding.dialogFilter.tabClass,"#F7FBFE",null)
            UiUtils.textviewCustomDrawable(binding.dialogFilter.tabBatch,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(binding.dialogFilter.tabBatch,"#FFFFFF",null)
            binding.dialogFilter.title.text = "Select Student Class"
            loadClass()
        }

        binding.filter.setOnClickListener {
            if (selectedTab == "classes"){
                binding.dialogFilter.tabClass.performClick()
            }
            else {
                binding.dialogFilter.tabBatch.performClick()
            }
            binding.dialogFilter.root.visibility = View.VISIBLE
            UiUtils.animation(this,binding.dialogFilter.root,R.anim.slide_in_from_bottom,true)
        }
        binding.dialogFilter.cancel.setOnClickListener {
            binding.dialogFilter.root.visibility = View.GONE
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
        }
        binding.dialogFilter.close.setOnClickListener {
            binding.dialogFilter.root.visibility = View.GONE
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
        }
        binding.dialogFilter.apply.setOnClickListener {
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
            binding.dialogFilter.root.visibility = View.GONE
            program()
        }

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentBoard(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            boardRes = it.result!!
                            initAdapter(layoutInflater, binding.root)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().batchDropdown(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            batchRes = it.result!!
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentClsDropdown(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            classRes = it.result!!
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }

        binding.date.setOnClickListener {
            showCalender { selectedDateString ->
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val outputApiFormat = SimpleDateFormat("EEEE", Locale.getDefault())

                try {
                    val selectedDate = inputDateFormat.parse(selectedDateString)

                    val dt = outputDateFormat.format(selectedDate)
                    val api = outputApiFormat.format(selectedDate).lowercase(Locale.getDefault())

                    binding.dtText.text = dt
                    timeTableDay = api
                    getTimeTable()
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }

        binding.calender.setOnClickListener {
            showCalender { selectedDateString ->
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val outputApiFormat = SimpleDateFormat("EEEE", Locale.getDefault())

                try {
                    val selectedDate = inputDateFormat.parse(selectedDateString)

                    val dt = outputDateFormat.format(selectedDate)
                    val api = outputApiFormat.format(selectedDate).lowercase(Locale.getDefault())

                    binding.dtText.text = dt
                    timeTableDay = api
                    getTimeTable()
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }


        binding.backarrow.setOnClickListener{
            onBackPressed()
        }

    }

    fun loadFilter(){
        val adapter = TimetableFilterRemoveAdapter(this,filterArr,object : OnClickListener {
            override fun onClickItem(pos: Int) {
                val value = filterArr[pos]
                if (value == batchName){
                    batchId = ""
                    batchName = ""
                }
                else if (value == classname){
                    classId = ""
                    classname = ""
                }
                program()
            }
        })
        val layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        binding.filterRecycler.layoutManager = layoutManager
        binding.filterRecycler.adapter = adapter
    }

    override fun onBackPressed() {
        if (binding.dialogFilter.root.visibility == View.VISIBLE){
            binding.dialogFilter.root.visibility = View.GONE
        }
        else if (binding.page2.visibility == View.VISIBLE){
            binding.page2.visibility = View.GONE
            binding.page1.visibility = View.VISIBLE
        }
        else {
            super.onBackPressed()
        }
    }

    fun loadBatch(){
        if (batchRes.isNotEmpty()){
            val adapter = TimetableFilterAdapter(this,selectedTab,batchRes,classRes,object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    if (selectedTab == "batch"){
                        batchId = batchRes[pos].value!!
                        val selected = batchRes[pos].label!!
                        val previousName = batchName
                        batchName = selected
                        for (i in filterArr.size - 1 downTo 0) {
                            if (filterArr[i] == previousName) {
                                filterArr.removeAt(i)
                                break
                            }
                        }
                        filterArr.add(selected)
                    }
                }
            })
            val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            binding.dialogFilter.recycler.layoutManager = layoutManager
            binding.dialogFilter.recycler.adapter = adapter
        }
        else {
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().batchDropdown(this).observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.result != null && it.result!!.isNotEmpty()){
                                batchRes = it.result!!
                                val adapter = TimetableFilterAdapter(this,selectedTab,batchRes,classRes,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        if (selectedTab == "batch"){
                                            batchId = batchRes[pos].value!!
                                            val selected = batchRes[pos].label!!
                                            val previousName = batchName
                                            batchName = selected
                                            for (i in filterArr.size - 1 downTo 0) {
                                                if (filterArr[i] == previousName) {
                                                    filterArr.removeAt(i)
                                                    break
                                                }
                                            }
                                            filterArr.add(selected)
                                        }
                                    }
                                })
                                val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                                binding.dialogFilter.recycler.layoutManager = layoutManager
                                binding.dialogFilter.recycler.adapter = adapter
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                }
            }
        }
    }

    fun loadClass(){
        if (classRes.isNotEmpty()){
            val adapter = TimetableFilterAdapter(this,selectedTab,batchRes,classRes,object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    if (selectedTab == "classes"){
                        classId = classRes[pos].value!!
                        val selected = classRes[pos].label!!
                        val pName = classname
                        classname = UiUtils.getOrdinalSuffix(selected.toInt())
                        for (i in filterArr.size - 1 downTo 0) {
                            if (filterArr[i] == pName) {
                                filterArr.removeAt(i)
                                break
                            }
                        }
                        val res = UiUtils.getOrdinalSuffix(selected.toInt())
                        filterArr.add(res)
                    }
                }
            })
            val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            binding.dialogFilter.recycler.layoutManager = layoutManager
            binding.dialogFilter.recycler.adapter = adapter
        }
        else {
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().batchDropdown(this).observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success){
                            if (it.result != null && it.result!!.isNotEmpty()){
                                batchRes = it.result!!
                                val adapter = TimetableFilterAdapter(this,selectedTab,batchRes,classRes,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        if (selectedTab == "classes"){
                                            classId = classRes[pos].value!!
                                            val selected = classRes[pos].label!!
                                            val pName = classname
                                            classname = UiUtils.getOrdinalSuffix(selected.toInt())
                                            for (i in filterArr.size - 1 downTo 0) {
                                                if (filterArr[i] == pName) {
                                                    filterArr.removeAt(i)
                                                    break
                                                }
                                            }
                                            val res = UiUtils.getOrdinalSuffix(selected.toInt())
                                            filterArr.add(res)
                                        }
                                    }
                                })
                                val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                                binding.dialogFilter.recycler.layoutManager = layoutManager
                                binding.dialogFilter.recycler.adapter = adapter
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                }
            }
        }
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

    fun program(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().program(this,search,boardId,batchId,classId).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.programRecycler.visibility = View.VISIBLE
                            val adapter = AdminTimeTableAdapter(this,it.result!!.rows!!,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    program = it.result!!.rows!![pos]._id!!
                                    if (it.result!!.rows!![pos].classTeacher != null){
                                        binding.incharge.text = it.result!!.rows!![pos].classTeacher!!.firstName + " " + it.result!!.rows!![pos].classTeacher!!.lastName
                                    }
                                    else {
                                        binding.incharge.text = "--/--"
                                    }
                                    if (it.result!!.rows!![pos].studentClass != null && it.result!!.rows!![pos].section != null){
                                        binding.standard.text = "${UiUtils.getOrdinalSuffix(it.result!!.rows!![pos].studentClass!!.name!!.toInt())} - ${it.result!!.rows!![pos].section!!.name} Sec"
                                    }
                                    else {
                                        binding.standard.text = "--/--"
                                    }
                                    getTimeTable()
                                    binding.page2.visibility = View.VISIBLE
                                    binding.page1.visibility = View.GONE
                                }
                            })
                            val layoutManager = GridLayoutManager(this,2,RecyclerView.VERTICAL,false)
                            binding.programRecycler.layoutManager = layoutManager
                            binding.programRecycler.adapter = adapter
                        } else {
                            binding.noData.txt.text = "No Program Available"
                            binding.noData.root.visibility = View.VISIBLE
                            binding.programRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noData.txt.text = "No Program Available"
                        binding.noData.root.visibility = View.VISIBLE
                        binding.programRecycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun getTimeTable(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getTimetable(this,"",timeTableDay,program).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.periods!!.isNotEmpty()){
                            binding.noData01.root.visibility = View.GONE
                            binding.timetableRecycler.visibility = View.VISIBLE
                            val adapter = TimeTableAdapter(this,false,it.result!!.periods!!,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
//                                    program = it.result!!.
                                }
                            })
                            val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                            binding.timetableRecycler.layoutManager = layoutManager
                            binding.timetableRecycler.adapter = adapter
                        } else {
                            binding.noData01.txt.text = "No Tabletable Available"
                            binding.noData01.root.visibility = View.VISIBLE
                            binding.timetableRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noData01.txt.text = "No Tabletable Available"
                        binding.noData01.root.visibility = View.VISIBLE
                        binding.timetableRecycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    fun loadEvents() {
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        val currentMonthDays = getCurrentMonthDays()
        val currentDayIndex = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }

        val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
            val dt = sdfApi.format(selectedDate).toLowerCase(Locale.getDefault())
            timeTableDay = dt
            getTimeTable()
        }
        val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler1.layoutManager = linearLayoutManager1
        binding.dateRecycler1.adapter = adapter1

        val currentDayIndex1 = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex1 != -1) {
            binding.dateRecycler1.centerItem(currentDayIndex1)
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

    fun RecyclerView.centerItem(position: Int) {
        post {
            val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
            layoutManager.scrollToPosition(position)
            post {
                val child = layoutManager.findViewByPosition(position) ?: return@post
                val isVertical = layoutManager.orientation == LinearLayoutManager.VERTICAL
                val recyclerSize = if (isVertical) height - paddingTop - paddingBottom else width - paddingStart - paddingEnd
                val itemSize = if (isVertical) child.height else child.width
                val offset = (recyclerSize / 2) - (itemSize / 2)
                layoutManager.scrollToPositionWithOffset(position, offset)
            }
        }
    }

    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {
        if (binding.tabLayout.tabCount == 0) {
            for (i in 0 until boardRes.size) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
                val tabView: View = inflater.inflate(R.layout.custom_tab, container, false)
                val tabText = tabView.findViewById<TextView>(R.id.tab)

                tabText.text = boardRes[i].name
                UiUtils.textViewTextColor(tabText, null, R.color.colorPrimary)
                tabText.setTextAppearance(R.style.FontMedium)

                binding.tabLayout.getTabAt(i)?.customView = tabView
            }

            binding.tabLayout.tabGravity = TabLayout.GRAVITY_CENTER
            binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val linear0: View = tab.customView!!
                    val txttab0 = linear0.findViewById<TextView>(R.id.tab)
                    UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                    txttab0.setTextAppearance(R.style.FontMedium)
                    if (tab.position < boardRes.size) {
                        boardId = boardRes[tab.position]._id!!
                        program()
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
        }
        else {
            binding.tabLayout.removeAllTabs()
            binding.tabLayout.clearOnTabSelectedListeners()
            initAdapter(inflater, container)
        }
        if (boardRes.isNotEmpty()){
            boardId = boardRes[0]._id!!
            program()
        }
    }

}
