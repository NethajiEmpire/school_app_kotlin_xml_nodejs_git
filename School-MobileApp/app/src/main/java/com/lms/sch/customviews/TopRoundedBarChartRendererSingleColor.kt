
package com.lms.sch.customviews

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class TopRoundedBarChartRendererSingleColor(
    chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val barRect = RectF()
    private val cornerRadius = 20f

    override fun drawDataSet(c: Canvas?, dataSet: IBarDataSet?, index: Int) {
        val buffer = mBarBuffers.getOrNull(index) ?: return
        val paint = mRenderPaint

        for (j in buffer.buffer.indices step 4) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            // Set the bar rectangle
            barRect.set(left, top, right, bottom)

            // Apply vertical gradient
            paint.shader = LinearGradient(
                barRect.left, barRect.top, barRect.left, barRect.bottom,
                Color.parseColor("#232B68"), // Top
                Color.parseColor("#C4C9ED"), // Bottom
                Shader.TileMode.CLAMP
            )

            // Create a rounded rectangle path
            val path = Path().apply {
                addRoundRect(
                    barRect,
                    floatArrayOf(
                        cornerRadius, cornerRadius, // Top-left
                        cornerRadius, cornerRadius, // Top-right
                        0f, 0f, 0f, 0f               // Bottom corners
                    ),
                    Path.Direction.CW
                )
            }

            c?.drawPath(path, paint)

            // Clear shader to avoid leaking it
            paint.shader = null
        }
    }
}
