package com.example.prueba.network

import com.example.prueba.model.PolygonResponse
import retrofit2.http.GET

interface PolygonApi {
    @GET("polygons")
    suspend fun getPolygons(): List<PolygonResponse>
}
