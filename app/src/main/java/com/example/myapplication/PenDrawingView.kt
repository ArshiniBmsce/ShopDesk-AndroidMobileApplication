// PenDrawingView.kt
package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PenDrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint()
    private var currentColor = Color.BLACK // Default color

    init {
        setupPaint(currentColor)
    }

    private fun setupPaint(color: Int) {
        paint.color = color
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        // Draw ruled lines
        drawRuledLines(canvas)

        // Draw the actual path for drawing
        canvas.drawPath(path, paint)
    }

    private fun drawRuledLines(canvas: Canvas) {
        val lineSpacing = 40f
        val startY = 0f
        val endY = height.toFloat()

        paint.color = Color.LTGRAY
        paint.strokeWidth = 1f

        var currentY = startY
        while (currentY < endY) {
            canvas.drawLine(0f, currentY, width.toFloat(), currentY, paint)
            currentY += lineSpacing
        }

        // Restore the original paint settings
        setupPaint(currentColor)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
            else -> return false
        }

        // Trigger a redraw
        invalidate()
        return true
    }

    fun clearDrawing() {
        path.reset()
        invalidate()
    }

    fun setColor(color: Int) {
        currentColor = color
        setupPaint(currentColor)
    }
}
