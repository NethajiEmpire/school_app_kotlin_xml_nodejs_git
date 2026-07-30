package com.lms.sch.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.MySubjectsAdapter
import com.lms.sch.adapter.ProjectAdapter
import com.lms.sch.adapter.TeacherAssignmentAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.databinding.ActivityTeacherProjectBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TeacherProjectActivity : BaseActivity() {
            private lateinit var binding: ActivityTeacherProjectBinding
            var result = ArrayList<GetHomeworkResponse.Result>()
            var programId = ""
            var selectedMonth = 0
            var selectedYear = 0
            var filterDate = ""
            var type = ""
            var search = ""
            var subId = ""
            var projectStatus = ""
            var calendar = Calendar.getInstance()
            private var currentMonthDays = ArrayList<Date>()
            val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
            val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
            override fun onCreate(savedInstanceState: Bundle?) {
                binding = ActivityTeacherProjectBinding.inflate(layoutInflater)
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContentView(binding.root)
                binding.backarrow.setOnClickListener {
                    onBackPressed()
                }
                val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
                currentMonthDays = getCurrentMonthDays()
                loadDates()

                DialogUtils.showLoader(this)
                ApiConnection.getInstance().teacherProgram(this).observe(this) {
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success->
                            if (success){
                                if (it.result != null && it.result!!.isNotEmpty()){
                                    val adapter = MySubjectsAdapter(this,it.result!!,object: OnClickListener{
                                        override fun onClickItem(pos: Int) {
                                            programId = it.result!![pos]._id!!
                                            if (!it.result!![pos].myClass!!){
                                                subId = it.result!![pos].subject!!.subjectId!!._id!!
                                            }
                                            else {
                                                subId = it.result!![pos].subject!!.subjectId!!._id!!
                                            }
                                            getProject()
                                        }
                                    })
                                    val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
                binding.search.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {
                        search = binding.search.text.toString()
                        getProject()
                    }
                    override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                })

                binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        search = binding.search.text.toString()
                        getProject()
                    }
                    false
                })
                binding.filter.setOnClickListener {
                    val inflater = LayoutInflater.from(this)
                    val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
                    val popupView : View = bind.root

                    val widthInDp = 120
                    val density = resources.displayMetrics.density
                    val widthInPx = (widthInDp * density).toInt()

                    val popupWindow = PopupWindow(popupView,widthInPx,ViewGroup.LayoutParams.WRAP_CONTENT,true)
                    popupWindow.isOutsideTouchable = true
                    popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    popupWindow.elevation = 8f
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        popupWindow.setElevation(8f)
                    }
                    if (projectStatus == "today"){
                        UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                        UiUtils.textviewImgDrawable(bind.all,null,"start")
                        UiUtils.textviewImgDrawable(bind.pending,null,"start")
                        UiUtils.textviewImgDrawable(bind.completed,null,"start")
                    }
                    else if (projectStatus == "pending"){
                        UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                        UiUtils.textviewImgDrawable(bind.all,null,"start")
                        UiUtils.textviewImgDrawable(bind.today,null,"start")
                        UiUtils.textviewImgDrawable(bind.completed,null,"start")
                    }
                    else if (projectStatus == "completed"){
                        UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                        UiUtils.textviewImgDrawable(bind.all,null,"start")
                        UiUtils.textviewImgDrawable(bind.today,null,"start")
                        UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    }
                    else {
                        UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                        UiUtils.textviewImgDrawable(bind.pending,null,"start")
                        UiUtils.textviewImgDrawable(bind.today,null,"start")
                        UiUtils.textviewImgDrawable(bind.completed,null,"start")
                    }
                    bind.all.setOnClickListener {
                        projectStatus = ""
                        getProject()
                        popupWindow.dismiss()
                    }
                    bind.pending.setOnClickListener {
                        projectStatus = "pending"
                        getProject()
                        popupWindow.dismiss()
                    }
                    bind.today.setOnClickListener {
                        projectStatus = "today"
                        getProject()
                        popupWindow.dismiss()
                    }
                    bind.completed.setOnClickListener {
                        projectStatus = "completed"
                        getProject()
                        popupWindow.dismiss()
                    }
                    val anchorView = binding.filter
                    val location = IntArray(2)
                    anchorView.getLocationOnScreen(location)

                    val endGapDp = 8
                    val topGapDp = 8
                    val endGapPx = (endGapDp * density).toInt()
                    val topGapPx = (topGapDp * density).toInt()
                    val xPos = location[0] + anchorView.width - widthInPx - endGapPx
                    val yPos = location[1] + anchorView.height + topGapPx

                    popupWindow.showAtLocation(
                        anchorView,
                        Gravity.NO_GRAVITY,
                        xPos,
                        yPos
                    )
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
            val dt = sdfApi.format(selectedDate).toLowerCase(Locale.getDefault())
            filterDate = dt
//            studentHomework()
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
    fun RecyclerView.centerItem(position: Int, itemWidth: Int) {
        post {
            val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
            val visibleWidth = width - paddingLeft - paddingRight
            val exactCenter = (visibleWidth / 2) - (itemWidth / 2)
            layoutManager.scrollToPositionWithOffset(position, exactCenter)
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
    private fun getProject() {
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getTeacherProject(this,search,programId,"",projectStatus).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.noData.root.visibility = View.GONE
                            binding.prRecycler.visibility = View.VISIBLE
                            val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                            val adapter = ProjectAdapter(this, false, it.result!!.rows!!)
                            binding.prRecycler.layoutManager = linearLayoutManager
                            binding.prRecycler.adapter = adapter
                        } else {
                            binding.prRecycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.prRecycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

}