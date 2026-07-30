package com.lms.sch.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener

class SemiProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 50f
    }

    private var totalProgress = 100f
    private var progress1 = 25f
    private var progress2 = 25f
    private var progress3 = 25f

    private var c1 = "#FD9902"
    private var c2 = "#65D7C9"
    private var c3 = "#826BF8"
//    private var c4 = "#FF8686"

    private var animatedProgress1 = 0f
    private var animatedProgress2 = 0f
    private var animatedProgress3 = 0f
//    private var animatedProgress4 = 0f

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1000 // Duration of the animation in milliseconds
        addUpdateListener(AnimatorUpdateListener { animation ->
            val fraction = animation.animatedFraction
            animatedProgress1 = progress1 * fraction
            animatedProgress2 = progress2 * fraction
            animatedProgress3 = progress3 * fraction
//            animatedProgress4 = progress4 * fraction
            invalidate()
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = paint.strokeWidth / 2
        val width = width.toFloat()
        val height = height.toFloat()
        val radius = (Math.min(width, height) / 2) - padding
        val centerX = width / 2
        val centerY = height - padding

        val oval = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // Background Arc
        paint.color = Color.parseColor("#D3D3D3") // Light gray
        canvas.drawArc(oval, 180f, 180f, false, paint)

        paint.color = Color.parseColor(c1)
        val angle1 = if (totalProgress > 0) (animatedProgress1 / totalProgress) * 180f else 0f
        canvas.drawArc(oval, 180f, angle1, false, paint)

        paint.color = Color.parseColor(c2)
        val angle2 = if (totalProgress > 0) (animatedProgress2 / totalProgress) * 180f else 0f
        canvas.drawArc(oval, 180f + angle1, angle2, false, paint)

        paint.color = Color.parseColor(c3)
        val angle3 = if (totalProgress > 0) (animatedProgress3 / totalProgress) * 180f else 0f
        canvas.drawArc(oval, 180f + angle1 + angle2, angle3, false, paint)

//        paint.color = Color.parseColor(c4)
//        val angle4 = if (totalProgress > 0) (animatedProgress4 / totalProgress) * 180f else 0f
//        canvas.drawArc(oval, 180f + angle1 + angle2 + angle3, angle4, false, paint)
    }

    fun setProgress(total: Float, p1: Float,cl1 :String, p2: Float,cl2 :String, p3: Float,cl3 :String) {
        totalProgress = total
        progress1 = p1
        c1 = cl1
        progress2 = p2
        c2 = cl2
        progress3 = p3
        c3 = cl3
        animator.cancel() // Cancel any ongoing animation
        animator.start() // Start the animation
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val displayMetrics = context.resources.displayMetrics
        val desiredWidth = (230 * displayMetrics.density).toInt()
        val desiredHeight = (230 * displayMetrics.density).toInt()
        val width = MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.EXACTLY)
        val height = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)

        setMeasuredDimension(width, height)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }
}
