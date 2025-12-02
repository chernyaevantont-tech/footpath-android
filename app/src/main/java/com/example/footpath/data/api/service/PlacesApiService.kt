package com.example.footpath.data.api.service

import com.example.footpath.data.api.dto.ApprovePlaceDto
import com.example.footpath.data.api.dto.CreatePlaceDto
import com.example.footpath.data.api.dto.PlaceFilterResponseDto
import com.example.footpath.data.api.dto.PlaceResponseDto
import com.example.footpath.data.api.dto.UpdatePlaceDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesApiService {

    @POST("places")
    suspend fun createPlace(@Body createPlaceDto: CreatePlaceDto): PlaceResponseDto

    @GET("places")
    suspend fun findPlaces(
        @Query("name") name: String? = null,
        @Query("tagIds") tagIds: List<String>? = null,
        @Query("status") status: String? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
        @Query("radius") radius: Double? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): PlaceFilterResponseDto

    @GET("places/{id}")
    suspend fun getPlace(@Path("id") id: String): PlaceResponseDto

    @PUT("places/{id}")
    suspend fun updatePlace(@Path("id") id: String, @Body updatePlaceDto: UpdatePlaceDto): PlaceResponseDto

    @PUT("places/{id}/approve")
    suspend fun approvePlace(@Path("id") id: String, @Body approvePlaceDto: ApprovePlaceDto): PlaceResponseDto

    @PUT("places/{id}/reject")
    suspend fun rejectPlace(@Path("id") id: String, @Body approvePlaceDto: ApprovePlaceDto): PlaceResponseDto
}