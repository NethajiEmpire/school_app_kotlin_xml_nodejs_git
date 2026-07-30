package com.lms.sch.customviews

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class TopRoundedBarChart(
    chart: BarChart,
    animator: com.github.mikephil.charting.animation.ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val barRect = RectF()
    private val cornerRadius = 20f

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val paint = mRenderPaint
        val buffer = mBarBuffers[index]

        for (j in buffer.buffer.indices step 4) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            // Define the bar's rectangle
            barRect.set(left, top, right, bottom)

            // Retrieve the color for this bar from the dataset
            val color = dataSet.getColor(j / 4)
            paint.color = color
            paint.style = Paint.Style.FILL
            Log.d("wsedfgv", "Bar color: ${dataSet.getColor(j / 4)}")
            // Create a Path to draw rounded top corners only
            val radii = floatArrayOf(
                cornerRadius, cornerRadius, // Top-left
                cornerRadius, cornerRadius, // Top-right
                0f, 0f,                     // Bottom-right
                0f, 0f                      // Bottom-left
            )
            val path = Path().apply {
                addRoundRect(barRect, radii, Path.Direction.CW)
            }
            c.drawPath(path, paint)


            // Draw the path with rounded top corners
            c.drawPath(path, paint)
        }
    }
}
