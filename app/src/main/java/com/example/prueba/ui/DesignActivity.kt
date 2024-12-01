package com.example.prueba.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba.R
import com.example.prueba.model.PolygonResponse
import com.google.gson.Gson

class DesignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design)

        val canvasView = findViewById<PolygonCanvasView>(R.id.canvasView)
        val polygonJson = intent.getStringExtra("polygon")
        val polygon = Gson().fromJson(polygonJson, PolygonResponse::class.java)
        canvasView.points = polygon.points
    }
}
