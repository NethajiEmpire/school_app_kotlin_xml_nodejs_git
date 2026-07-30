package com.lms.sch.customviews

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class GradientPieChartRenderer(
    chart: PieChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val colors: List<Int>
) : PieChartRenderer(chart, animator, viewPortHandler) {

    private val greyColor = Color.parseColor("#EFEFEF")

    override fun drawDataSet(c: Canvas, dataSet: IPieDataSet) {
        val pie = mChart
        val rotation = pie.rotationAngle
        val drawAngles = pie.drawAngles
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val circleBox = pie.circleBox
        val center = pie.centerCircleBox

        // Convert MPPointF to PointF
        val centerPointF = PointF(center.x, center.y)

        // Define different thicknesses for each segment
        val fullThickness = circleBox.width() / 1.4f
        val halfDayThickness = fullThickness * 0.8f
        val absentThickness = fullThickness * 0.7f

        // Calculate total value and check if all values are zero
        var totalValue = 0f
        var allValuesZero = true
        var nonZeroCount = 0

        for (i in 0 until dataSet.entryCount) {
            val entry = dataSet.getEntryForIndex(i)
            totalValue += entry.y
            if (entry.y > 0) {
                allValuesZero = false
                nonZeroCount++
            }
        }

        // Case 1: All values are zero - draw full grey circle
        if (allValuesZero) {
            drawFullGreyCircle(c, centerPointF, fullThickness, pie.holeRadius)
            return
        }

        var angle = 0f
        val hasSingleNonZeroEntry = nonZeroCount == 1

        for (i in 0 until dataSet.entryCount) {
            val entry = dataSet.getEntryForIndex(i) as PieEntry

            // Skip if value is zero (unless it's the only non-zero entry)
            if (entry.y == 0f && !(hasSingleNonZeroEntry && nonZeroCount == 1)) {
                angle += drawAngles[i] * phaseX
                continue
            }

            val sweepAngle = drawAngles[i] * phaseY
            if (sweepAngle <= 0f) {
                angle += drawAngles[i] * phaseX
                continue
            }

            val thickness = when (i) {
                0 -> fullThickness
                1 -> halfDayThickness
                2 -> absentThickness
                else -> fullThickness
            }

            val outerRadius = pie.holeRadius + thickness
            val customCircleBox = RectF(
                centerPointF.x - outerRadius,
                centerPointF.y - outerRadius,
                centerPointF.x + outerRadius,
                centerPointF.y + outerRadius
            )

            // Draw the segment
            mRenderPaint.color = if (hasSingleNonZeroEntry && entry.y > 0) {
                colors[i] // Use the segment color for single non-zero entry
            } else if (entry.y > 0) {
                colors[i] // Normal case
            } else {
                Color.TRANSPARENT // Skip zero values in multi-segment case
            }

            if (mRenderPaint.color == Color.TRANSPARENT) {
                angle += drawAngles[i] * phaseX
                continue
            }

            mRenderPaint.style = Paint.Style.FILL

            val startAngle = rotation + angle
            val actualSweepAngle = if (hasSingleNonZeroEntry && entry.y > 0) {
                360f * phaseY // Full circle for single non-zero entry
            } else {
                sweepAngle
            }

            val path = Path().apply {
                moveTo(centerPointF.x, centerPointF.y)
                arcTo(customCircleBox, startAngle, actualSweepAngle)
                close()
            }

            c.drawPath(path, mRenderPaint)
            angle += drawAngles[i] * phaseX
        }
    }

    private fun drawFullGreyCircle(c: Canvas, center: PointF, thickness: Float, holeRadius: Float) {
        val outerRadius = holeRadius + thickness
        val circleBox = RectF(
            center.x - outerRadius,
            center.y - outerRadius,
            center.x + outerRadius,
            center.y + outerRadius
        )

        mRenderPaint.color = greyColor
        mRenderPaint.style = Paint.Style.FILL

        val path = Path().apply {
            moveTo(center.x, center.y)
            arcTo(circleBox, 0f, 360f)
            close()
        }

        c.drawPath(path, mRenderPaint)
    }

}