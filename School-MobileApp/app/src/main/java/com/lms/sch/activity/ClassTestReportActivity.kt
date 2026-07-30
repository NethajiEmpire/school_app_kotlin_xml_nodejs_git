package com.lms.sch.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.databinding.ActivityClassTestReportBinding
import com.lms.sch.databinding.ActivitySubjectWiseProgressBinding
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ClassTestReportActivity : BaseActivity() {
    lateinit var binding: ActivityClassTestReportBinding
    var calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClassTestReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            finish()
        }
        binding.tab1.setOnClickListener {
            UiUtils.textViewTextColor(binding.tab1,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tab1,R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tab1,null,R.color.white)
            UiUtils.textViewTextColor(binding.tab2,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab2,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab3,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab3,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab4,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab4,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab5,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab5,R.drawable.border_curve_6dp)
            binding.layAll.visibility = View.VISIBLE
            binding.layTamil.visibility = View.GONE
        }
        binding.tab2.setOnClickListener {
            UiUtils.textViewTextColor(binding.tab2,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tab2,R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tab2,null,R.color.white)
            UiUtils.textViewTextColor(binding.tab1,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab1,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab3,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab3,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab4,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab4,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab5,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab5,R.drawable.border_curve_6dp)
            binding.layTamil.visibility = View.VISIBLE
            binding.layAll.visibility = View.GONE
        }
        binding.tab3.setOnClickListener {
            UiUtils.textViewTextColor(binding.tab3,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tab3,R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tab3,null,R.color.white)
            UiUtils.textViewTextColor(binding.tab1,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab1,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab2,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab2,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab4,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab4,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab5,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab5,R.drawable.border_curve_6dp)
        }
        binding.tab4.setOnClickListener {
            UiUtils.textViewTextColor(binding.tab4,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tab4,R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tab4,null,R.color.white)
            UiUtils.textViewTextColor(binding.tab1,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab1,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab2,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab2,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab3,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab3,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab5,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab5,R.drawable.border_curve_6dp)
        }
        binding.tab5.setOnClickListener {
            UiUtils.textViewTextColor(binding.tab5,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tab5,R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.tab5,null,R.color.white)
            UiUtils.textViewTextColor(binding.tab1,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab1,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab2,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab2,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab3,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab3,R.drawable.border_curve_6dp)
            UiUtils.textViewTextColor(binding.tab4,null,R.color.black_varient3)
            UiUtils.textviewCustomDrawable(binding.tab4,R.drawable.border_curve_6dp)
        }
        binding.tab1.performClick()
        loadEvents()
    }

    fun loadEvents() {
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentMonthDays = getCurrentMonthDays()
        val adapter = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate -> }
        val linearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler.layoutManager = linearLayoutManager
        binding.dateRecycler.adapter = adapter

        val currentDayIndex =
            currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
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

}