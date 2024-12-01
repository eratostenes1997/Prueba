package com.example.prueba.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba.R
import com.example.prueba.model.PolygonResponse
import com.google.gson.Gson

class DesignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design)

        val canvasView = findViewById<PolygonCanvasView>(R.id.canvasView)
        val seekBar = findViewById<SeekBar>(R.id.seekBarScale)
        val btnSave = findViewById<Button>(R.id.btnSaveFigure)

        val polygonJson = intent.getStringExtra("polygon")
        val polygon = Gson().fromJson(polygonJson, PolygonResponse::class.java)
        canvasView.points = polygon.points.toMutableList()

        seekBar.max = 100
        seekBar.progress = 50
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                canvasView.scale = progress / 50.0f
                canvasView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnSave.setOnClickListener {
            val sharedPreferences = getSharedPreferences("PolygonPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val json = Gson().toJson(PolygonResponse("Figura Guardada", canvasView.points))
            editor.putString("savedPolygon", json)
            editor.apply()
            finish()
        }
    }
}
