package com.example.prueba.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba.R
import com.example.prueba.model.Point
import com.example.prueba.model.PolygonResponse
import com.example.prueba.repository.PolygonRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectionActivity : AppCompatActivity() {
    private lateinit var repository: PolygonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        repository = PolygonRepository(applicationContext)
        val listView = findViewById<ListView>(R.id.listView)
        val btnGeneratePolygon = findViewById<Button>(R.id.btnGeneratePolygon)

        CoroutineScope(Dispatchers.IO).launch {
            val polygons = repository.fetchPolygons().toMutableList()

            val sharedPreferences = getSharedPreferences("PolygonPrefs", Context.MODE_PRIVATE)
            val savedPolygonJson = sharedPreferences.getString("savedPolygon", null)
            if (savedPolygonJson != null) {
                val savedPolygon = Gson().fromJson(savedPolygonJson, PolygonResponse::class.java)
                polygons.add(0, savedPolygon)
            }

            val names = polygons.map { it.name }

            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(
                    this@SelectionActivity,
                    android.R.layout.simple_list_item_1,
                    names
                )
                listView.adapter = adapter

                listView.setOnItemClickListener { _, _, position, _ ->
                    val selectedPolygon = polygons[position]
                    val intent = Intent(this@SelectionActivity, DesignActivity::class.java)
                    intent.putExtra("polygon", Gson().toJson(selectedPolygon))
                    startActivity(intent)
                }
            }
        }

        btnGeneratePolygon.setOnClickListener {
            val inputDialog = AlertDialog.Builder(this)
            inputDialog.setTitle("Número de lados")
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            inputDialog.setView(input)

            inputDialog.setPositiveButton("Aceptar") { _, _ ->
                val sides = input.text.toString().toIntOrNull() ?: 0
                if (sides >= 3) {
                    val polygon = generateRegularPolygon(sides)
                    val intent = Intent(this, DesignActivity::class.java)
                    intent.putExtra("polygon", Gson().toJson(polygon))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Debe tener al menos 3 lados", Toast.LENGTH_SHORT).show()
                }
            }
            inputDialog.setNegativeButton("Cancelar", null)
            inputDialog.show()
        }
    }

    private fun generateRegularPolygon(sides: Int): PolygonResponse {
        val points = mutableListOf<Point>()
        val angleIncrement = 2 * Math.PI / sides
        for (i in 0 until sides) {
            val x = 0.5 + 0.4 * Math.cos(i * angleIncrement)
            val y = 0.5 + 0.4 * Math.sin(i * angleIncrement)
            points.add(Point(x, y))
        }
        return PolygonResponse("Polígono Regular", points)
    }
}
