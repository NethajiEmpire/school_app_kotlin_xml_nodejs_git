package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lms.sch.R
import com.lms.sch.databinding.ActivityClassesBinding
import com.lms.sch.databinding.ActivityExamBinding
import com.lms.sch.utils.UiUtils

class ClassesActivity : BaseActivity() {
    lateinit var binding: ActivityClassesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityClassesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        binding.tabcompletedlay.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabcompletedlay, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.tabnotcompletedlay, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.Completed, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.ongoing,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.upcoming,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.Completedtab,null, R.color.black_varient6)
        }
        binding.tabnotcompletedlay.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabcompletedlay, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabnotcompletedlay, R.drawable.border_line_curve_24dp_primary)
            UiUtils.linearLayoutBgDrawable(binding.Completed, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.ongoing,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.upcoming,null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.Completedtab,null, R.color.black_varient6)
        }
        binding.Completed.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.tabcompletedlay, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.tabnotcompletedlay, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.Completed, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.ongoing,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.upcoming,null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.Completedtab,null, R.color.colorPrimary)
        }
         binding.scheduledOn.setOnClickListener {
             if (binding.academicdetails.visibility == View.VISIBLE) {
                 binding.academicdetails.visibility = View.GONE
             } else {
                 binding.academicdetails.visibility = View.VISIBLE
             }
        }
    }
}