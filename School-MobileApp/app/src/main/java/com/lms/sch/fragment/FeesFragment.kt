package com.lms.sch.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lms.sch.R
import com.lms.sch.activity.PaymentHistoryActivity
import com.lms.sch.activity.ProfileActivity
import com.lms.sch.adapter.PaymentHistoryAdapter
import com.lms.sch.adapter.StudentFeesAdapter
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.GroupTopRoundedBarChartRenderer
import com.lms.sch.databinding.FragmentFeesBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetAdminStatsReponse
import com.lms.sch.response.StudentFeeResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class FeesFragment : BaseFragment() {
    lateinit var binding: FragmentFeesBinding
    lateinit var barChart: BarChart
    //var fees = ArrayList<StudentFeeResponse.Result>()
    var fees : StudentFeeResponse.Result? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFeesBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(2)
        barChart = binding.barChart
        setupBarChart()
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, ProfileActivity(),null,false)
        }
        binding.feeViewAll.setOnClickListener {
            BaseUtils.startActivity(mActivity, PaymentHistoryActivity(),null,false)
        }
        studentFees()
        paymentHistory()
        getStudentFeeStats()
        return view

    }

    private fun setupBarChart() {

        val entries1 = listOf(
            BarEntry(0f, 70f),
            BarEntry(1f, 85f),
            BarEntry(2f, 15f)
        )

        val entries2 = listOf(
            BarEntry(0f, 60f),
            BarEntry(1f, 75f),
            BarEntry(2f, 25f)
        )

        val entries3 = listOf(
            BarEntry(0f, 90f),
            BarEntry(1f, 65f),
            BarEntry(2f, 35f)
        )

        val terms = listOf("1st Term", "2nd Term", "3rd Term")

        val dataSet1 = BarDataSet(entries1, "Set 1").apply {
            val startColor = Color.parseColor("#ffffff")
            val endColor = Color.parseColor("#ffffff")
            colors = listOf(
                startColor,
                endColor
            )
            setGradientColor(startColor, endColor)
            setDrawValues(false)
        }
        val dataSet2 = BarDataSet(entries2, "Set 2").apply {
            val startColor = Color.parseColor("#ffffff")
            val endColor = Color.parseColor("#ffffff")
            colors = listOf(
                startColor,
                endColor
            )
            setGradientColor(startColor, endColor)
            setDrawValues(false)
        }
        val dataSet3 = BarDataSet(entries3, "Set 3").apply {
            val startColor = Color.parseColor("#ffffff")
            val endColor = Color.parseColor("#ffffff")
            colors = listOf(
                startColor,
                endColor
            )
            setGradientColor(startColor, endColor)
            setDrawValues(false)
        }

        barChart.renderer = GroupTopRoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )

        val barWidth = 0.15f
        val barSpace = 0.05f
        val groupSpace = 0.4f
        val groupCount = terms.size.toFloat()

        val data = BarData(dataSet1, dataSet2, dataSet3).apply {
            this.barWidth = barWidth
            groupBars(0f, groupSpace, barSpace)
        }

        barChart.apply {
            this.data = data

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(terms)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 12f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)

                axisMinimum = 0f  // Start at 0 to align bars correctly
                axisMaximum = groupCount + (groupCount * (barWidth + barSpace))  // Auto-calculate max value
                setLabelCount(terms.size, false)
                setCenterAxisLabels(true)  // Required for proper alignment
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 100f
                granularity = 20f
                setLabelCount(6, true)
                textSize = 11f
                typeface = ResourcesCompat.getFont(mActivity, R.font.font_regular)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}%"
                    }
                }
                enableGridDashedLine(10f, 10f, 0f)
                setDrawGridLines(true)
                gridColor = Color.GRAY
            }

            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            val markerView = CustomMarkerView(mActivity)
            this.marker = markerView

            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            animateY(1000)
            setExtraOffsets(20f, 20f, 20f, 20f)

            invalidate()
        }
    }
    private fun getStudentFeeStats(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentFees(mActivity).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){

                            if (it.result!!.paidAmount != null){
                                binding.paidFee.text = it.result!!.paidAmount.toString()
                            }
                            else{
                                binding.paidFee.text = "--/--"
                            }
                            if (it.result!!.pendingAmount != null){
                                binding.blncAmt.text = it.result!!.pendingAmount.toString()
                            }
                            else{
                                binding.blncAmt.text = "--/--"
                            }
                            if (it.result!!.overdueAmount != null){
                                binding.overdueFee.text = it.result!!.overdueAmount.toString()
                            }
                            else{
                                binding.overdueFee.text = "--/--"
                            }
                            if (it.result!!.feesAmount != null){
                                binding.overAllFee.text = it.result!!.feesAmount.toString()
                            }
                            else{
                                binding.overAllFee.text = "--/--"
                            }

                        }
                        else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun studentFees() {
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentFees(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            binding.feesRecycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                            val adapter = StudentFeesAdapter(mActivity,it.result!!.terms!!)
                            binding.feesRecycler.layoutManager = linearLayoutManager
                            binding.feesRecycler.adapter = adapter
                        } else {
                            binding.feesRecycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.feesRecycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun paymentHistory() {
        DialogUtils.showLoader(mActivity)
        Log.d("lkjhgfhjklyui", "Calling API...")

        ApiConnection.getInstance().paymentHistory(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            Log.d("lkjhgfhjklyui", "API response received")
            it?.let {
                Log.d("lkjhgfhjklyui", "$it")
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {

                            Log.d("lkjhgfhjklyui", it.result!!.toString())

                            binding.paymentHistoryRecycler.visibility = View.VISIBLE
                            binding.noData1.root.visibility = View.GONE
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = PaymentHistoryAdapter(mActivity,true,it.result!!.rows!!,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    getPaymentHistory(pos)
                                }
                            })
                            binding.paymentHistoryRecycler.layoutManager = linearLayoutManager
                            binding.paymentHistoryRecycler.adapter = adapter
                        } else {
                            binding.paymentHistoryRecycler.visibility = View.GONE
                            binding.noData1.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.paymentHistoryRecycler.visibility = View.GONE
                        binding.noData1.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun getPaymentHistory(pos: Int) {
        binding.historyDetails.root.visibility = View.VISIBLE
        binding.historyDetails.close.setOnClickListener {
            binding.historyDetails.root.visibility = View.GONE
        }
    }
}