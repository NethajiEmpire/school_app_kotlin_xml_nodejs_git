package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lms.sch.R
import com.lms.sch.databinding.ActivityExamBinding
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ExamActivity : BaseActivity() {
    lateinit var binding : ActivityExamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityExamBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener{
            onBackPressed()
        }
        binding.tabcompletedlay.setOnClickListener{
            UiUtils.linearLayoutBgDrawable(binding.tabnotcompletedlay, R.drawable.border_line_curve_24dp_grey )
            UiUtils.linearLayoutBgDrawable( binding.tabcompletedlay, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabcompleted, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabpending, null, R.color.black_varient6)
            binding.pendingcards.visibility = View.VISIBLE
            binding.completedcards.visibility = View.GONE
        }
        binding.tabnotcompletedlay.setOnClickListener{
            UiUtils.linearLayoutBgDrawable(binding.tabcompletedlay, R.drawable.border_line_curve_24dp_grey )
            UiUtils.linearLayoutBgDrawable( binding.tabnotcompletedlay, R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabpending, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabcompleted, null, R.color.black_varient6)
            binding.pendingcards.visibility = View.GONE
            binding.completedcards.visibility = View.VISIBLE
        }
        binding.compledscards.root.setOnClickListener{
              binding.markSheet.root.visibility = View.VISIBLE
           //   binding.createhomework.visibility = View.GONE
              binding.head.visibility = View.VISIBLE
        }
       /* binding.markSheet.close.setOnClickListener{
            binding.markSheet.root.visibility = View.GONE
        }*/
        binding.showdetails.setOnClickListener {
            binding.allDetails.visibility = View.VISIBLE
            binding.showdetails.visibility = View.GONE
            binding.closeAll.visibility = View.VISIBLE
        }
        binding.closeAll.setOnClickListener {
            binding.allDetails.visibility = View.GONE
            binding.showdetails.visibility = View.VISIBLE
            binding.closeAll.visibility = View.GONE
        }

    }
}