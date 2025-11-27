package com.example.footpath.data.api.dto

data class PlaceDto(
    val placeId: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
)