package com.example.prueba.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba.R
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

        CoroutineScope(Dispatchers.IO).launch {
            val polygons = repository.fetchPolygons()
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
    }
}
