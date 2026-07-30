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
import com.lms.sch.adapter.AdminFinanceFeesAdapter
import com.lms.sch.databinding.ActivityAdminFinanceBinding
import com.lms.sch.databinding.ActivityAdminTimeTableBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.AdminFinanceFee
import com.lms.sch.models.AdminLeaveRequest
import com.lms.sch.utils.UiUtils

class AdminFinanceActivity : BaseActivity() {
    lateinit var binding: ActivityAdminFinanceBinding
    var isFinance = true
    var finance = ArrayList<AdminFinanceFee>()
    var clickedDialog = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminFinanceBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initAdapter(layoutInflater, binding.root)

        binding.backarrow.setOnClickListener {
            onBackPressed()
        }

        binding.showDetails.setOnClickListener{
            binding.showDetails.visibility = View.GONE
            binding.feeDetails.visibility = View.VISIBLE
        }
        binding.upWard.setOnClickListener{
            binding.showDetails.visibility = View.VISIBLE
            binding.feeDetails.visibility = View.GONE
        }

        binding.overDueReceipt.root.setOnClickListener{
            binding.dialogFeeReceipt.root.visibility = View.VISIBLE
        }
        binding.paidReceipt.root.setOnClickListener{
            binding.dialogFeeReceipt.root.visibility = View.VISIBLE
        }
        binding.dialogFeeReceipt.cancel.setOnClickListener {
            binding.dialogFeeReceipt.root.visibility = View.GONE
        }

        finance.add(AdminFinanceFee("Nethaji Hada", "Pending"))
        finance.add(AdminFinanceFee("Karthick", "OverDue"))
        finance.add(AdminFinanceFee("Supriya", "Paid"))

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        val adapter = AdminFinanceFeesAdapter(this, finance, object : OnClickListener {
            override fun onClickItem(pos: Int) {
                getProject(pos)
            }
        })
        binding.financeRecyclerView.layoutManager = linearLayoutManager
        binding.financeRecyclerView.adapter = adapter
        binding.financeRecyclerView.visibility = View.VISIBLE

    }
    fun getProject(pos: Int){

        if (finance[pos].statusType == "OverDue"){
            binding.dialogFeeReceipt.root.visibility = View.VISIBLE
            binding.dialogFeeReceipt.studentName.text = "Karthick"
            binding.dialogFeeReceipt.status.text = "Overdue"
            UiUtils.textViewBgTint(binding.dialogFeeReceipt.status, "#FFE0E1",null)
            UiUtils.textViewTextColor(binding.dialogFeeReceipt.status, "#FF7C00",null)
            binding.dialogFeeReceipt.paidAmt.visibility = View.GONE

        }

       else if(finance[pos].statusType == "Paid"){
            binding.dialogFeeReceipt.root.visibility = View.VISIBLE
            binding.dialogFeeReceipt.studentName.text = "Supriya"
        }

    }
    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_START
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        val linear0: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab0 = linear0.findViewById<TextView>(R.id.tab)
        txttab0.text = "Term 1"
        UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
        txttab0.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(0)!!.customView = linear0

        val linear1: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab1 = linear1.findViewById<TextView>(R.id.tab)
        txttab1.text = "Term 2"
        UiUtils.textViewTextColor(txttab1, null, R.color.black_varient3)
        txttab1.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(1)!!.customView = linear1

        val linear2: View = inflater.inflate(R.layout.custom_tab, container, false)
        val txttab2 = linear2.findViewById<TextView>(R.id.tab)
        txttab2.text = "Term 3"
        UiUtils.textViewTextColor(txttab2, null, R.color.black_varient3)
        txttab2.setTextAppearance(R.style.FontMedium)
        binding.tabLayout.getTabAt(2)!!.customView = linear2


        /*binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val linear0: View = tab.customView!!
                val txttab0 = linear0.findViewById<TextView>(R.id.tab)
                UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                txttab0.setTextAppearance(R.style.FontMedium)
                if (tab.position == 0) {
                    binding.term1Page.visibility = View.VISIBLE
                    isFinance = true
                } else if (tab.position == 1) {
                    binding.term1Page.visibility = View.GONE
                    isFinance = false
                   // binding.tabToday.performClick()
                } else if (tab.position == 2) {
                    binding.term1Page.visibility = View.GONE
                    isFinance = false
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val linear1: View = tab.customView!!
                val txttab1 = linear1.findViewById<TextView>(R.id.tab)
                UiUtils.textViewTextColor(txttab1, null, R.color.black)
                txttab1.setTextAppearance(R.style.FontMedium)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })*/
    }
}