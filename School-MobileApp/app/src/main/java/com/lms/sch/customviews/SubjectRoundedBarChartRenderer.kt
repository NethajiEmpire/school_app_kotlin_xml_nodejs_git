package com.lms.sch.charts

import android.graphics.*
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class SubjectRoundedBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarRect = RectF()
    private val mPath = Path()
    private val mCornerRadius = 30f // Corner radius for top/bottom cap and bottom segment

    private val colorPalette = listOf(
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
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        // Exit if buffer or dataset is invalid
        if (buffer.size() < 4 || dataSet.entryCount == 0) return

        val left = buffer.buffer[0]
        val right = buffer.buffer[2]
        val bottomBase = buffer.buffer[3]

        // Get the single BarEntry for stacked values
        val entry = dataSet.getEntryForIndex(0)
        val values = entry.yVals?.filter { it != 0f } ?: emptyList()

        if (values.isEmpty()) return

        var bottom = bottomBase
        values.forEachIndexed { segmentIndex, value ->
            // Calculate top of the current segment
            val top = bottom - (value * mAnimator.phaseY)
            val isBottomSegment = segmentIndex == 0
            val isTopSegment = segmentIndex == values.size - 1

            // Assign color from palette
            mRenderPaint.color = colorPalette[segmentIndex % colorPalette.size]
            mRenderPaint.style = Paint.Style.FILL

            mPath.reset()

            if (isTopSegment) {
                // Draw topmost segment with semi-circular cap
                val capHeight = mCornerRadius // Height of the semi-circular cap
                val adjustedTop = top - capHeight // Extend top to include cap
                mBarRect.set(left, adjustedTop + capHeight, right, bottom)

                // Bottom corners are flat to align with segment below
                val bottomRadii = if (isBottomSegment) mCornerRadius else 0f
                mPath.addRoundRect(
                    mBarRect,
                    floatArrayOf(
                        0f, 0f, // Top-left
                        0f, 0f, // Top-right
                        bottomRadii, bottomRadii, // Bottom-right
                        bottomRadii, bottomRadii // Bottom-left
                    ),
                    Path.Direction.CW
                )

                // Draw semi-circular cap
                val capRect = RectF(left, adjustedTop, right, adjustedTop + 2 * capHeight)
                mPath.addArc(capRect, 180f, 180f) // Top semi-circle
                mPath.lineTo(right, adjustedTop + capHeight)
                mPath.lineTo(left, adjustedTop + capHeight)
                mPath.close()
            } else {
                // Draw non-top segments with flat top and appropriate bottom radii
                mBarRect.set(left, top, right, bottom)
                val bottomRadii = if (isBottomSegment) mCornerRadius else 0f
                mPath.addRoundRect(
                    mBarRect,
                    floatArrayOf(
                        0f, 0f, // Top-left
                        0f, 0f, // Top-right
                        bottomRadii, bottomRadii, // Bottom-right
                        bottomRadii, bottomRadii // Bottom-left
                    ),
                    Path.Direction.CW
                )
            }

            c.drawPath(mPath, mRenderPaint)

            // Update bottom for the next segment
            bottom = top
        }
    }
}


/*

class SubjectRoundedBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarRect = RectF()
    private val mPath = Path()
    private val mCornerRadius = 30f // Corner radius for top/bottom of the entire bar

    private val colorPalette = listOf(
        Color.parseColor("#3B3BBF"),
        Color.parseColor("#F85F73"),
        Color.parseColor("#F9B233"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#E91E63"),
        Color.parseColor("#673AB7")
    )

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        val buffer = mBarBuffers[index]
        buffer.setPhases(mAnimator.phaseX, mAnimator.phaseY)
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        // Exit if buffer or dataset is invalid
        if (buffer.size() < 4 || dataSet.entryCount == 0) return

        val left = buffer.buffer[0]
        val right = buffer.buffer[2]
        val bottomBase = buffer.buffer[3]

        // Get the single BarEntry for stacked values
        val entry = dataSet.getEntryForIndex(0)
        val values = entry.yVals?.filter { it != 0f } ?: emptyList()

        if (values.isEmpty()) return

        var bottom = bottomBase
        values.forEachIndexed { segmentIndex, value ->
            // Calculate top of the current segment
            val top = bottom - (value * mAnimator.phaseY)
            // Add slight offset to topmost segment to ensure top radius visibility
            val adjustedTop = if (segmentIndex == values.size - 1) top - 2f else top
            mBarRect.set(left, adjustedTop, right, bottom)

            // Assign color from palette
            mRenderPaint.color = colorPalette[segmentIndex % colorPalette.size]
            mRenderPaint.style = Paint.Style.FILL

            // Determine corner radii based on segment position
            val isBottomSegment = segmentIndex == 0
            val isTopSegment = segmentIndex == values.size - 1

            val cornerRadii = floatArrayOf(
                if (isTopSegment) mCornerRadius else 0f, // Top-left x
                if (isTopSegment) mCornerRadius else 0f, // Top-left y
                if (isTopSegment) mCornerRadius else 0f, // Top-right x
                if (isTopSegment) mCornerRadius else 0f, // Top-right y
                if (isBottomSegment) mCornerRadius else 0f, // Bottom-right x
                if (isBottomSegment) mCornerRadius else 0f, // Bottom-right y
                if (isBottomSegment) mCornerRadius else 0f, // Bottom-left x
                if (isBottomSegment) mCornerRadius else 0f // Bottom-left y
            )

            // Draw segment with appropriate rounded corners
            mPath.reset()
            mPath.addRoundRect(mBarRect, cornerRadii, Path.Direction.CW)
            c.drawPath(mPath, mRenderPaint)

            // Update bottom for the next segment
            bottom = top
        }
    }
}

*/
