package com.lms.sch.customviews

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class TopRendarCurveChartbar(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val cornerRadius = 14f

    private val colorPalette = listOf(
        Color.parseColor("#232B68"), // Collected
        Color.parseColor("#32B138"), // Pending
        Color.parseColor("#EA5455")  // Overdue
    )

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        if (index >= mBarBuffers.size) return
        val buffer = mBarBuffers[index]
        buffer?.apply {
            setPhases(mAnimator.phaseX, mAnimator.phaseY)
            setDataSet(index)
            setInverted(mChart.isInverted(dataSet.axisDependency))
            setBarWidth(mChart.barData.barWidth)
            feed(dataSet)
        } ?: return

        val barBuffer = buffer.buffer ?: return

        val trans = mChart.getTransformer(dataSet.axisDependency)
        trans.pointValuesToPixel(barBuffer)

        for (j in 0 until barBuffer.size step 4) {
            if (j + 3 >= barBuffer.size) continue // Safety guard

            val left = barBuffer[j]
            val top = barBuffer[j + 1]
            val right = barBuffer[j + 2]
            val bottom = barBuffer[j + 3]

            if (!mViewPortHandler.isInBoundsLeft(right)) continue
            if (!mViewPortHandler.isInBoundsRight(left)) break

            // Get stacked values for the current bar
            val entry = dataSet.getEntryForIndex(j / 4)
            val values = entry.yVals ?: continue

            var currentBottom = bottom
            values.forEachIndexed { stackIndex, value ->
                if (value == 0f) return@forEachIndexed // Skip zero values

                val stackHeight = (value / 100f) * (bottom - top) // Normalize to chart height
                val stackTop = currentBottom - stackHeight

                mRenderPaint.color = colorPalette[stackIndex % colorPalette.size]

                val path = Path().apply {
                    moveTo(left, currentBottom)
                    lineTo(left, stackTop + cornerRadius)
                    quadTo((left + right) / 2, stackTop - cornerRadius, right, stackTop + cornerRadius)
                    lineTo(right, currentBottom)
                    close()
                }

                c.drawPath(path, mRenderPaint)
                currentBottom = stackTop // Update bottom for next stack
            }
        }
    }
}