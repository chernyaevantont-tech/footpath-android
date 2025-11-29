package com.example.footpath.data.api.dto

import com.google.gson.annotations.SerializedName

data class PlaceDto(
    @SerializedName("id") val placeId: String,
    val name: String,
    val description: String,
    val coordinates: Coordinates,
    val tagIds: List<String>,
    val status: String, // pending, approved, rejected
    val creatorId: String,
    val moderatorId: String? = null,
    val createdAt: String, // ISO 8601 date string
    val updatedAt: String // ISO 8601 date string
) {
    val latitude: Double
        get() = coordinates.coordinates[1]

    val longitude: Double
        get() = coordinates.coordinates[0]
}

data class Coordinates(
    val type: String, // e.g., "Point"
    val coordinates: List<Double> // [longitude, latitude]
)
