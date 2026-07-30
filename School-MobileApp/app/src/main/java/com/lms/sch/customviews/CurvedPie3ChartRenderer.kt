package com.lms.sch.customviews

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CurvedPie3ChartRenderer(
    chart: PieChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : PieChartRenderer(chart, animator, viewPortHandler) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    override fun drawDataSet(c: Canvas, dataSet: IPieDataSet) {
        val phaseX = mAnimator.phaseX
        val center = mChart.centerCircleBox
        val radius = mChart.radius

        // Thinner stroke for a sleek appearance
        val strokeWidth = radius * 0.20f
        paint.strokeWidth = strokeWidth

        val drawAngles = mChart.drawAngles
        var angle = mChart.rotationAngle

        // Inner curve adjustment
        val curveInset = 18f // Adjust for a more pronounced inner curve
        val outerRadius = radius - strokeWidth / 2f - curveInset

        val rect = RectF(
            center.x - outerRadius,
            center.y - outerRadius,
            center.x + outerRadius,
            center.y + outerRadius
        )

        for (i in 0 until dataSet.entryCount) {
            val sliceAngle = drawAngles[i] * phaseX
            val sliceSpace = dataSet.sliceSpace * 0.8f // Slightly reduced space for aesthetics

            if (sliceAngle <= 0f) {
                angle += sliceAngle
                continue
            }

            val adjustedAngle = sliceAngle - sliceSpace
            if (adjustedAngle <= 0f) {
                angle += sliceAngle
                continue
            }

            // Set segment color based on entry index
            paint.color = dataSet.getColor(i)
            c.drawArc(rect, angle + sliceSpace / 2f, adjustedAngle, false, paint)
            angle += sliceAngle
        }
    }
}
