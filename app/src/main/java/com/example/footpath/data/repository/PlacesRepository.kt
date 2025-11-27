package com.example.footpath.data.repository

import com.example.footpath.data.api.RetrofitInstance
import com.example.footpath.data.api.dto.PlaceDto

class PlacesRepository {

    private val placesApiService = RetrofitInstance.placesApi

    suspend fun getApprovedPlaces(): List<PlaceDto> {
        return try {
            placesApiService.getApprovedPlaces()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}