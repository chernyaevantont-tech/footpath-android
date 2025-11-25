package com.example.footpath.data.api.service

import com.example.footpath.data.api.dto.LoginRequest
import com.example.footpath.data.api.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}