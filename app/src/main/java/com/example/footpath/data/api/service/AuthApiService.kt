package com.example.footpath.data.api.service

import com.example.footpath.data.api.dto.LoginRequest
import com.example.footpath.data.api.dto.LoginResponse
import com.example.footpath.data.api.dto.RegisterModeratorRequest
import com.example.footpath.data.api.dto.RegisterModeratorResponse
import com.example.footpath.data.api.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getMyProfile(@Header("Authorization") token: String): UserDto

    @POST("auth/register-moderator")
    suspend fun registerModerator(@Body request: RegisterModeratorRequest): RegisterModeratorResponse

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): LoginResponse
}