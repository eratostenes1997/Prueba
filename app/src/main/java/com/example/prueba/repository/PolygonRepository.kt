package com.example.prueba.repository

import android.content.Context
import com.example.prueba.model.PolygonResponse
import com.example.prueba.network.PolygonApi
import com.example.prueba.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PolygonRepository(private val context: Context) {
    private val api = RetrofitClient.retrofit.create(PolygonApi::class.java)
    private val sharedPreferences = context.getSharedPreferences("PolygonPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    suspend fun fetchPolygons(): List<PolygonResponse> {
        val cachedData = sharedPreferences.getString("cachedPolygons", null)
        return if (cachedData != null) {
            val type = object : TypeToken<List<PolygonResponse>>() {}.type
            gson.fromJson(cachedData, type)
        } else {
            val polygons = api.getPolygons()
            savePolygons(polygons)
            polygons
        }
    }

    private fun savePolygons(polygons: List<PolygonResponse>) {
        val json = gson.toJson(polygons)
        sharedPreferences.edit().putString("cachedPolygons", json).apply()
    }
}
