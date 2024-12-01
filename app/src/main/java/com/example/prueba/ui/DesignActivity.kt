package com.example.prueba.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba.R
import com.example.prueba.model.Point
import com.example.prueba.model.PolygonResponse
import com.google.gson.Gson

class DesignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design)

        val canvasView = findViewById<PolygonCanvasView>(R.id.canvasView)
        val seekBar = findViewById<SeekBar>(R.id.seekBarScale)
        val btnSave = findViewById<Button>(R.id.btnSaveFigure)

        // Obtén el JSON del polígono desde el Intent
        val polygonJson = intent.getStringExtra("polygon")
        if (polygonJson != null) {
            try {
                // Convierte el JSON a un objeto PolygonResponse
                val polygon = Gson().fromJson(polygonJson, PolygonResponse::class.java)
                canvasView.points = polygon.points.toMutableList()
            } catch (e: Exception) {
                // Maneja errores de deserialización
                showMessage("Error al cargar el polígono, se usará un polígono por defecto.")
                canvasView.points = getDefaultPolygon()
            }
        } else {
            // Maneja el caso en que el Intent no contenga el extra "polygon"
            showMessage("No se recibió un polígono, se usará un polígono por defecto.")
            canvasView.points = getDefaultPolygon()
        }

        // Configura el SeekBar para ajustar la escala
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

        // Implementa la funcionalidad del botón "Guardar"
        btnSave.setOnClickListener {
            val sharedPreferences = getSharedPreferences("PolygonPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Convierte los datos actuales del canvas a JSON
            val savedPolygon = PolygonResponse("Figura Guardada", canvasView.points)
            val json = Gson().toJson(savedPolygon)

            // Guarda el JSON en SharedPreferences
            editor.putString("savedPolygon", json)
            editor.apply()

            showMessage("Figura guardada correctamente.")
        }
    }

    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun getDefaultPolygon(): MutableList<Point> {
        return mutableListOf(
            Point(0.2, 0.2),
            Point(0.8, 0.2),
            Point(0.5, 0.8)
        )
    }
}
