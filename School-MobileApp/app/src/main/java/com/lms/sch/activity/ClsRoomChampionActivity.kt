package com.lms.sch.activity

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lms.sch.R
import com.lms.sch.customviews.CustomMarkerView
import com.lms.sch.customviews.TopRoundedBarChart
import com.lms.sch.customviews.TopRoundedBarChartRenderer
import com.lms.sch.databinding.ActivityClsRoomChampionBinding

class ClsRoomChampionActivity : BaseActivity() {
    lateinit var binding: ActivityClsRoomChampionBinding
    lateinit var barChart: BarChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClsRoomChampionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        barChart = binding.barChart
        setupBarChart()
    }

    private fun setupBarChart() {
        val entries = listOf(
            BarEntry(0f, 100f),
            BarEntry(1f, 80f),
            BarEntry(2f, 60f),
            BarEntry(3f, 40f)
        )

        val subjects = listOf("Excellent", "Good", "Average", "Poor")

        val dataSet = BarDataSet(entries, "").apply {
            colors = listOf(
                Color.parseColor("#FF5733"),
                Color.parseColor("#33FF57"),
                Color.parseColor("#3357FF"),
                Color.parseColor("#FF33A1")
            )
            setDrawValues(false)
        }
        barChart.renderer = TopRoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )

        val barData = BarData(dataSet)
        barData.barWidth = 0.3f

        barChart.apply {
            data = barData

            xAxis.valueFormatter = IndexAxisValueFormatter(subjects)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.textSize = 11f
            xAxis.typeface = ResourcesCompat.getFont(this@ClsRoomChampionActivity, R.font.font_regular)

            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 100f
            axisLeft.granularity = 20f
            axisLeft.setLabelCount(5, true)
            axisLeft.textSize = 11f
            axisLeft.typeface = ResourcesCompat.getFont(this@ClsRoomChampionActivity, R.font.font_regular)
            axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}%"
                }
            }

            axisLeft.enableGridDashedLine(10f, 10f, 0f)
            axisLeft.setDrawGridLines(true)
            axisLeft.gridColor = Color.GRAY

            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)

            val markerView = CustomMarkerView(this@ClsRoomChampionActivity)
            barChart.marker = markerView

            // Add rounded corners to bars
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            // Animation
            animateY(1000)
            setExtraOffsets(10f, 10f, 10f, 10f)

            invalidate()
        }
    }

}