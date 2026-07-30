package com.lms.sch.customviews

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class ExamTopRendarCurveColor(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    var subjectColorMap: Map<String, String> = emptyMap()
    private val cornerRadius = 14f

    // Gradient colors
    private val topColor = Color.parseColor("#232B68")
    private val bottomColor = Color.parseColor("#C4C9ED")

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val buffer = mBarBuffers.getOrNull(index) ?: return
        val trans = mChart.getTransformer(dataSet.axisDependency)

        buffer.setPhases(mAnimator.phaseX, mAnimator.phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        val barBuffer = buffer.buffer
        for (j in 0 until barBuffer.size step 4) {
            val left = barBuffer[j]
            val top = barBuffer[j + 1]
            val right = barBuffer[j + 2]
            val bottom = barBuffer[j + 3]

            if (!mViewPortHandler.isInBoundsLeft(right)) continue
            if (!mViewPortHandler.isInBoundsRight(left)) break

            // Create vertical gradient shader
            val shader = LinearGradient(
                left, top, left, bottom,
                topColor, bottomColor,
                Shader.TileMode.CLAMP
            )
            mRenderPaint.shader = shader

            // Curved top bar path
            val path = Path().apply {
                moveTo(left, bottom)
                lineTo(left, top + cornerRadius)
                quadTo((left + right) / 2, top - cornerRadius, right, top + cornerRadius)
                lineTo(right, bottom)
                close()
            }

            c.drawPath(path, mRenderPaint)
            mRenderPaint.shader = null // Clear shader after drawing
        }
    }
}
