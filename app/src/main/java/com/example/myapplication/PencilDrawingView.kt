// PencilDrawingView.kt
package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PencilDrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint()
    private var currentColor = Color.GRAY // Default color
    private lateinit var bitmap: Bitmap
    private lateinit var canvasBitmap: Canvas

    init {
        setupPaint(currentColor)
    }

    private fun setupPaint(color: Int) {
        paint.color = color
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.alpha = 150
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap)
    }

    override fun onDraw(canvas: Canvas) {
        // Draw ruled lines
        drawRuledLines(canvas)

        // Draw the actual path for drawing on the bitmap
        canvasBitmap.drawPath(path, paint)

        // Draw the bitmap on the canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
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
        bitmap.eraseColor(Color.TRANSPARENT)
        invalidate()
    }

}
