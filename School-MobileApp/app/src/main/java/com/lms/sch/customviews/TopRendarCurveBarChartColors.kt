package com.lms.sch.customviews

import android.graphics.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class TopRendarCurveBarChartColors(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val cornerRadius = 14f

    private  val colorPalette = listOf(
        Color.parseColor("#7A7ADC"), // Muted indigo
        Color.parseColor("#F67280"), // Soft coral pink
        Color.parseColor("#FBC16D"), // Warm gold
        Color.parseColor("#B085D9"), // Dusty lavender
        Color.parseColor("#81C784"), // Soft leafy green
        Color.parseColor("#64B5F6"), // Light but rich blue
        Color.parseColor("#FFB74D"), // Muted orange
        Color.parseColor("#F48FB1"), // Light mauve-pink
        Color.parseColor("#9575CD"), // Mid lilac
        Color.parseColor("#90A4AE")  // Cool muted slate
    )

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        val buffer = mBarBuffers[index]

        buffer.setPhases(mAnimator.phaseX, mAnimator.phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        var colorIndex = 0

        val barBuffer = buffer.buffer
        for (j in 0 until barBuffer.size step 4) {
            val left = barBuffer[j]
            val top = barBuffer[j + 1]
            val right = barBuffer[j + 2]
            val bottom = barBuffer[j + 3]

            if (!mViewPortHandler.isInBoundsLeft(right)) continue
            if (!mViewPortHandler.isInBoundsRight(left)) break

            mRenderPaint.color = colorPalette[colorIndex % colorPalette.size]
            colorIndex++

            val path = Path().apply {
                moveTo(left, bottom)
                lineTo(left, top + cornerRadius)
                quadTo((left + right) / 2, top - cornerRadius, right, top + cornerRadius)
                lineTo(right, bottom)
                close()
            }
            c.drawPath(path, mRenderPaint)
        }
    }
}
/*
class TopRendarCurveBarChartColors(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    var subjectColorMap: Map<String, String> = emptyMap()
    var defaultColor: Int = Color.GRAY
    private val cornerRadius = 14f

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        val buffer = mBarBuffers[index]

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

            val entry = dataSet.getEntryForIndex(j / 4)
            val subject = (entry.data as? String)?.trim() ?: ""
            val colorHex = subjectColorMap[subject] ?: subjectColorMap[normalizeSubject(subject)] ?: String.format("#%06X", 0xFFFFFF and defaultColor)
            mRenderPaint.color = Color.parseColor(colorHex)

            val path = Path().apply {
                moveTo(left, bottom)
                lineTo(left, top + cornerRadius)
                quadTo((left + right) / 2, top - cornerRadius, right, top + cornerRadius)
                lineTo(right, bottom)
                close()
            }

            c.drawPath(path, mRenderPaint)
        }
    }

    private fun normalizeSubject(subject: String): String {
        return subject.lowercase().replace(" ", "")
    }
}*/
