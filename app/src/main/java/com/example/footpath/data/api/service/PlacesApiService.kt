package com.example.footpath.data.api.service

import com.example.footpath.data.api.dto.PlaceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {

    @GET("places")
    suspend fun getApprovedPlaces(@Query("status") status: String = "approved"): List<PlaceDto>
}