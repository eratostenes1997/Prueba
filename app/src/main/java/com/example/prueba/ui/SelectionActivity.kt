package com.example.prueba.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
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
import kotlin.math.cos
import kotlin.math.sin

class SelectionActivity : AppCompatActivity() {
    private lateinit var repository: PolygonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        repository = PolygonRepository(applicationContext)
        val listView = findViewById<ListView>(R.id.listView)
        val btnGeneratePolygon = findViewById<Button>(R.id.btnGeneratePolygon)

        // Cargar polígonos desde la API
        CoroutineScope(Dispatchers.IO).launch {
            val polygons = repository.fetchPolygons().toMutableList()

            // Carga el polígono guardado desde SharedPreferences
            val sharedPreferences = getSharedPreferences("PolygonPrefs", Context.MODE_PRIVATE)
            val savedPolygonJson = sharedPreferences.getString("savedPolygon", null)
            if (savedPolygonJson != null) {
                val savedPolygon = Gson().fromJson(savedPolygonJson, PolygonResponse::class.java)
                polygons.add(0, savedPolygon) // Agrega el polígono guardado al inicio de la lista
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

        // Botón para generar polígonos regulares
        btnGeneratePolygon.setOnClickListener {
            showPolygonSidesDialog { sides ->
                val generatedPolygon = generateRegularPolygon(sides)
                val intent = Intent(this@SelectionActivity, DesignActivity::class.java)
                val polygonResponse = PolygonResponse("Polígono Regular", generatedPolygon)
                intent.putExtra("polygon", Gson().toJson(polygonResponse))
                startActivity(intent)
            }
        }
    }

    private fun showPolygonSidesDialog(onSidesSelected: (Int) -> Unit) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Generar Polígono Regular")
        dialog.setMessage("Ingresa el número de lados:")

        val input = android.widget.EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        dialog.setView(input)

        dialog.setPositiveButton("Aceptar") { _, _ ->
            val sides = input.text.toString().toIntOrNull()
            if (sides != null && sides > 2) {
                onSidesSelected(sides)
            } else {
                showMessage("Número de lados inválido. Debe ser mayor a 2.")
            }
        }

        dialog.setNegativeButton("Cancelar") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        dialog.show()
    }

    private fun generateRegularPolygon(sides: Int): MutableList<Point> {
        val points = mutableListOf<Point>()
        val centerX = 0.5
        val centerY = 0.5
        val radius = 0.4

        for (i in 0 until sides) {
            val angle = 2.0 * Math.PI * i / sides
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            points.add(Point(x, y))
        }

        return points
    }

    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
