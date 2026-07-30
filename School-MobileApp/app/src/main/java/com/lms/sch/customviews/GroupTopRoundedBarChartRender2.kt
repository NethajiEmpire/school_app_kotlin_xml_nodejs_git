package com.lms.sch.customviews
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.renderer.Renderer
import com.github.mikephil.charting.utils.ViewPortHandler

class GroupTopRoundedBarChartRender2 (
    chart: BarChart,
    animator: com.github.mikephil.charting.animation.ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val barRect = RectF()
    private val cornerRadius = 20f

    // Define gradient colors for each dataset
    private val datasetColors = listOf(
        //listOf("#232B68", "#C4C9ED"),  // Dataset 1 colors
        listOf("#32B138", "#E2FFE4"),  // Dataset 2 colors
        listOf("#B22222", "#FADBD8")
      //  listOf("#EA5455", "#FFDDDE")   // Dataset 3 colors
    )

    override fun drawDataSet(c: Canvas?, dataSet: IBarDataSet?, index: Int) {
        super.drawDataSet(c, dataSet, index)
        val paint = mRenderPaint
        val buffer = mBarBuffers[index]

        // Get colors for current dataset (default to first set if index is out of bounds)
        val colors = datasetColors.getOrElse(index) { datasetColors[0] }

        for (j in buffer.buffer.indices step 4) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            // Define the bar's rectangle
            barRect.set(left, top, right, bottom)

            // Apply gradient shader with dataset-specific colors
            paint.shader = LinearGradient(
                barRect.left, barRect.top, barRect.left, barRect.bottom,
                Color.parseColor(colors[0]), // Start color
                Color.parseColor(colors[1]),  // End color
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