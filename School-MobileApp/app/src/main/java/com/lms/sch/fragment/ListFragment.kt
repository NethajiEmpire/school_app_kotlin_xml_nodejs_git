package com.lms.sch.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ViewPortHandler
import com.lms.sch.databinding.FragmentListBinding

class ListFragment : BaseFragment() {
    lateinit var binding: FragmentListBinding
    lateinit var pieChart : PieChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        pieChart = binding.pieChart
        setupPieChart()
        return view
    }

    private fun setupPieChart() {
        val entries = arrayListOf(
            PieEntry(25f), // Pink
            PieEntry(15f), // Orange
            PieEntry(60f)  // Green
        )

        val colors = listOf(
            Color.parseColor("#FF7171"), // Pink
            Color.parseColor("#FF9800"), // Orange
            Color.parseColor("#31A935")  // Green
        )

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 0f  // Space between segments
            selectionShift = 5f
            setDrawValues(false) // Hide percentage values
        }

        val pieData = PieData(dataSet)

        pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            setDrawEntryLabels(false)
            setDrawHoleEnabled(true)
            holeRadius = 80f
            setTouchEnabled(false)
            legend.isEnabled = false

//            renderer = CurvedPieChartRenderer(this, animator, this.viewPortHandler)

            invalidate()
        }
    }


}