package com.example.footpath.data.repository

import com.example.footpath.data.api.RetrofitInstance
import com.example.footpath.data.api.dto.PlaceResponseDto

class PlacesRepository {

    private val placesApiService = RetrofitInstance.placesApi

    suspend fun findPlaces(status: String? = null): List<PlaceResponseDto> {
        return try {
            placesApiService.findPlaces(status = status).data
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}