package com.lms.sch.customviews

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.*

class CurvedPieChartRenderer(
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

        // Slightly thinner ring stroke (20–22% of radius is ideal)
        val strokeWidth = radius * 0.20f
        paint.strokeWidth = strokeWidth

        val drawAngles = mChart.drawAngles
        var angle = mChart.rotationAngle

        // ↓ Decreased outer radius to move arcs inward
        val curveInset = 16f // Tune this: 10f–20f for more or less inner curve
        val outerRadius = radius - strokeWidth / 2f - curveInset

        val rect = RectF(
            center.x - outerRadius,
            center.y - outerRadius,
            center.x + outerRadius,
            center.y + outerRadius
        )

        for (i in 0 until dataSet.entryCount) {
            val sliceAngle = drawAngles[i] * phaseX
            val sliceSpace = dataSet.sliceSpace * 0.75f

            if (sliceAngle <= 0f) {
                angle += sliceAngle
                continue
            }

            val adjustedAngle = sliceAngle - sliceSpace
            if (adjustedAngle <= 0f) {
                angle += sliceAngle
                continue
            }

            paint.color = dataSet.getColor(i)
            c.drawArc(rect, angle + sliceSpace / 2f, adjustedAngle, false, paint)
            angle += sliceAngle
        }
    }
}
