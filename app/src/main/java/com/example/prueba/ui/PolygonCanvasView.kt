package com.example.prueba.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.prueba.model.Point

class PolygonCanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 5f
    }
    var points: List<Point> = emptyList()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points.isEmpty()) return

        for (i in points.indices) {
            val start = points[i]
            val end = points[(i + 1) % points.size]
            canvas.drawLine(
                start.x.toFloat() * width,
                start.y.toFloat() * height,
                end.x.toFloat() * width,
                end.y.toFloat() * height,
                paint
            )
        }
    }
}
