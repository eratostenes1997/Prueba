package com.example.prueba.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.prueba.model.Point

class PolygonCanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 5f
    }
    var points: MutableList<Point> = mutableListOf()
    var scale = 1.0f

    // Variables para manejar los gestos
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var activePoint: Point? = null

    init {
        // Establece un OnTouchListener para manejar eventos táctiles directamente
        setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> handleActionDown(event)
                MotionEvent.ACTION_MOVE -> handleActionMove(event)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> handleActionUp()
            }
            true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points.isEmpty()) return

        // Dibuja las líneas entre los puntos
        for (i in points.indices) {
            val start = points[i]
            val end = points[(i + 1) % points.size]
            canvas.drawLine(
                start.x.toFloat() * width * scale,
                start.y.toFloat() * height * scale,
                end.x.toFloat() * width * scale,
                end.y.toFloat() * height * scale,
                paint
            )
        }
    }

    private fun handleActionDown(event: MotionEvent) {
        lastTouchX = event.x
        lastTouchY = event.y

        // Encuentra el punto más cercano al toque
        activePoint = points.minByOrNull { point ->
            val px = (point.x * width).toFloat()
            val py = (point.y * height).toFloat()
            Math.hypot((px - event.x).toDouble(), (py - event.y).toDouble())
        }
    }

    private fun handleActionMove(event: MotionEvent) {
        activePoint?.let { point ->
            // Calcula el cambio de posición
            val dx = (event.x - lastTouchX) / width
            val dy = (event.y - lastTouchY) / height

            // Actualiza las coordenadas del punto activo
            point.x += dx
            point.y += dy

            // Actualiza las coordenadas para el siguiente movimiento
            lastTouchX = event.x
            lastTouchY = event.y

            invalidate() // Redibuja el canvas
        }
    }

    private fun handleActionUp() {
        activePoint = null // Libera el punto activo
    }
}
