package com.lms.sch.customviews

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class RoundedBarChartRenderer(
    chart: BarChart,
    animator: com.github.mikephil.charting.animation.ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val barRect = RectF()
    private val cornerRadius = 20f

    override fun drawDataSet(c: Canvas?, dataSet: IBarDataSet?, index: Int) {
        super.drawDataSet(c, dataSet, index)
        val paint = mRenderPaint
        val buffer = mBarBuffers[index]

        for (j in buffer.buffer.indices step 4) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            // Define the bar's rectangle
            barRect.set(left, top, right, bottom)

            // Apply gradient shader
            paint.shader = LinearGradient(
                barRect.left, barRect.top, barRect.left, barRect.bottom,
                Color.parseColor("#232B68"), // Start color
                Color.parseColor("#C4C9ED"), // End color
                Shader.TileMode.CLAMP
            )

            // Create a Path to draw rounded top corners only
            val path = Path().apply {
                addRoundRect(
                    barRect,
                    floatArrayOf(
                        cornerRadius, cornerRadius, // Top-left
                        cornerRadius, cornerRadius, // Top-right
                        0f, 0f,                     // Bottom-right
                        0f, 0f                      // Bottom-left
                    ),
                    Path.Direction.CW
                )
            }

            // Draw the path with rounded top corners
            c?.drawPath(path, paint)

            // Reset shader to avoid affecting other elements
            paint.shader = null
        }

    }
}
