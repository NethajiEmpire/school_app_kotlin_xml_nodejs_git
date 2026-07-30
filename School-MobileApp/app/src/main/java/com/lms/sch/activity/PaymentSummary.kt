package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lms.sch.databinding.ActivityPaymentSummeryBinding

class PaymentSummary : AppCompatActivity() {
    lateinit var binding: ActivityPaymentSummeryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentSummeryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.onlineChk.setOnClickListener{
            binding.onlinePay.visibility = View.VISIBLE
            binding.offlinePay.visibility = View.GONE
        }
        binding.offlineChk.setOnClickListener{
            binding.onlinePay.visibility = View.GONE
            binding.offlinePay.visibility = View.VISIBLE
        }
    }
}