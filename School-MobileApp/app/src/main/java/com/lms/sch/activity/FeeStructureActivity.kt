package com.lms.sch.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.AdmissionProcessAdapter
import com.lms.sch.adapter.CarouselAdapter
import com.lms.sch.adapter.FeeStructureAdapter
import com.lms.sch.databinding.ActivityFeeStructureBinding
import com.lms.sch.fragment.HomeFragment
import com.lms.sch.models.AdmissionProcess
import com.lms.sch.models.FeeStructure

class FeeStructureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeeStructureBinding
    var fees= ArrayList<FeeStructure>()
    var admProc= ArrayList<AdmissionProcess>()
    var carousel= ArrayList<Int>()
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityFeeStructureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fees.add(FeeStructure("1st Standard"))
        fees.add(FeeStructure("2st Standard"))
        fees.add(FeeStructure("3st Standard"))
        fees.add(FeeStructure("4st Standard"))
        fees.add(FeeStructure("5st Standard"))
        fees.add(FeeStructure("6st Standard"))

        admProc.add(AdmissionProcess("01","Register Your Account", "Register Your Account"))
        admProc.add(AdmissionProcess("02","Registration Fee", "Fee"))
        admProc.add(AdmissionProcess("03","Document Verification", "Doc"))
        admProc.add(AdmissionProcess("04","Application Form", "Form"))
        admProc.add(AdmissionProcess("05","Fees Payment", "Payment"))
        admProc.add(AdmissionProcess("06","School Approval", "Approval"))


        /*val linearLayoutManager = GridLayoutManager(this,2, RecyclerView.VERTICAL,false)
        val adapter = FeeStructureAdapter(this, fees)
        binding.feestructureRecyclerview.layoutManager = linearLayoutManager
        binding.feestructureRecyclerview.adapter = adapter

        val admissionLayoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        val admissionAdapter = AdmissionProcessAdapter(this, admProc)
        binding.admissionRecyclerview.layoutManager = admissionLayoutManager
        binding.admissionRecyclerview.adapter = admissionAdapter
*/
        val carouselImages = arrayListOf(
            R.drawable.face_interaction,
            R.drawable.practice_lab,
            R.drawable.sports_activities,
            R.drawable.classroom_learning
        )


        /*val carouselAdapter = CarouselAdapter(this, carouselImages)
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
        timer?.start()*/

    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}