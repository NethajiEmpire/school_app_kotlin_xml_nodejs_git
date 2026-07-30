package com.lms.sch.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.AdmissionProcessAdapter
import com.lms.sch.adapter.CarouselAdapter
import com.lms.sch.adapter.GuestLandingAdapter
import com.lms.sch.databinding.ActivityGuestLandingBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.AdmissionProcess
import com.lms.sch.models.GuestLanding
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils

class GuestLandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuestLandingBinding
    private lateinit var sharedHelper: SharedHelper

    var fees= ArrayList<GuestLanding>()
    var admProc= ArrayList<AdmissionProcess>()
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuestLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
       sharedHelper = SharedHelper(this)

        binding.registerhere.setOnClickListener {
            sharedHelper.isGuestLandingOpen = true
            BaseUtils.startActivity(this,OtpActivity(),null,false)
        }
        /*binding.register.setOnClickListener {
            sharedHelper.isGuestLandingOpen = true
            BaseUtils.startActivity(this,OtpActivity(),null,false)
        }*/


        /*binding.loginhere.setOnClickListener {
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra(Constants.IntentKeys.KEY, 2)
            intent.putExtra(Constants.IntentKeys.KEY1, "login")
            startActivity(intent)
        }*/



        fees.add(GuestLanding("GroupProfile","Smart Classrooms","Equipped with interactive boards and tech-enabled "))
        fees.add(GuestLanding("Library","Library","Wide collection of books and digital resources"))
        fees.add(GuestLanding("ScienceLab","Science Labs","Modern labs for Physics, Chemistry, and Biology"))
        fees.add(GuestLanding("ComputerLab","Computer Labs","High-speed internet and latest software"))
        fees.add(GuestLanding("Sports","Sports Facilities","Basketball, football, cricket, indoor games"))
        fees.add(GuestLanding("Transport","Transport","safe and GPS-enabled transport services"))

        admProc.add(AdmissionProcess("01","Register Your Account", "Register Your Account"))
        admProc.add(AdmissionProcess("02","Registration \nFee", "Fee"))
        admProc.add(AdmissionProcess("03","Document Verification", "Doc"))
        admProc.add(AdmissionProcess("04","Application \nForm", "Form"))
        admProc.add(AdmissionProcess("05","Fees \nPayment", "Payment"))
        admProc.add(AdmissionProcess("06","School \nApproval", "Approval"))


        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        val adapter = GuestLandingAdapter(this, fees)
        binding.GuestLandingRecyclerview.layoutManager = linearLayoutManager
        binding.GuestLandingRecyclerview.adapter = adapter

        val admissionLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val admissionAdapter = AdmissionProcessAdapter(this, admProc)
        binding.admissionRecyclerview.layoutManager = admissionLayoutManager
        binding.admissionRecyclerview.adapter = admissionAdapter

        /*val carouselImages = arrayListOf(
            R.drawable.registerboys,
            R.drawable.face_interaction,
            R.drawable.practice_lab,
            R.drawable.sports_activities,
            R.drawable.classroom_learning
        )
        val carouselAdapter = CarouselAdapter(this, carouselImages)
        binding.viewPager.adapter = carouselAdapter
        binding.indicator.setViewPager(binding.viewPager)
        timer = object : CountDownTimer(Long.MAX_VALUE, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                val currentPage = binding.viewPager.currentItem
                if (currentPage < carouselImages.size - 1) {
                    binding.viewPager.setCurrentItem(currentPage + 1, true)
                } else {
                    binding.viewPager.setCurrentItem(0, false)
                }
            }

            override fun onFinish() { }
        }
        timer?.start()
        */

        val carouselImages1 = arrayListOf(
            R.drawable.registerboy2,
            R.drawable.practice_lab1,
            R.drawable.sports_activities1,
            R.drawable.classroom_learning1,
            R.drawable.face_interaction
        )
        val carouselAdapter1 = CarouselAdapter(this, carouselImages1, object : OnClickListener {
            override fun onClickItem(position: Int) {
                sharedHelper.isGuestLandingOpen = true
                val intent = Intent(this@GuestLandingActivity, OtpActivity::class.java)
                startActivity(intent)
            }
        })
        binding.viewPager1.adapter = carouselAdapter1
        binding.indicator1.setViewPager(binding.viewPager1)
        timer = object : CountDownTimer(Long.MAX_VALUE, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                val currentPage = binding.viewPager1.currentItem
                if (currentPage < carouselImages1.size - 1) {
                    binding.viewPager1.setCurrentItem(currentPage + 1, true)
                } else {
                    binding.viewPager1.setCurrentItem(0, false)
                }
            }

            override fun onFinish() { }
        }
        timer?.start()
    }



    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}