package com.example.footpath.data.api.dto

import com.google.gson.annotations.SerializedName

// This file now contains all DTOs related to Places, based on the Swagger spec.

data class CoordinatesDto(
    val longitude: Double,
    val latitude: Double
)

data class CreatePlaceDto(
    val name: String,
    val description: String?,
    val coordinates: CoordinatesDto,
    val tagIds: List<String>?
)

data class TagResponseDto(
    val id: String,
    val name: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class PlaceResponseDto(
    val id: String,
    val name: String,
    val description: String?,
    val coordinates: String, // Well-known text
    val status: String,
    val creatorId: String?,
    val moderatorId: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    val tags: List<TagResponseDto>
)

data class LocationFilterDto(
    val latitude: Double,
    val longitude: Double,
    val radius: Double
)

data class PlaceFilterResponseDto(
    val data: List<PlaceResponseDto>,
    val meta: PageMeta
)

data class PageMeta(
    val page: Int,
    val limit: Int,
    val total: Int,
    val pages: Int
)

data class UpdateCoordinatesDto(
    val latitude: Double,
    val longitude: Double
)

data class UpdatePlaceDto(
    val name: String?,
    val description: String?,
    val coordinates: UpdateCoordinatesDto?,
    val tagIds: List<String>?
)

data class ApprovePlaceDto(
    val reason: String?
)
