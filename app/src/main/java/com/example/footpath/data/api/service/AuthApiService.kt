package com.example.footpath.data.api.service

import com.example.footpath.data.api.dto.LoginDto
import com.example.footpath.data.api.dto.LoginResponseDto
import com.example.footpath.data.api.dto.RegisterDto
import com.example.footpath.data.api.dto.RegisterResponseDto
import com.example.footpath.data.api.dto.RegisterModeratorDto
import com.example.footpath.data.api.dto.UserResponseDto
import com.example.footpath.data.api.dto.UserProfileDto
import com.example.footpath.data.api.dto.RequestPasswordResetDto
import com.example.footpath.data.api.dto.ResetPasswordDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginDto): LoginResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterDto): RegisterResponseDto

    @GET("auth/me")
    suspend fun getMyProfile(@Header("Authorization") token: String): UserResponseDto

    @PUT("auth/profile")
    suspend fun updateProfile(@Body request: UserProfileDto): UserResponseDto

    @POST("auth/request-password-reset")
    suspend fun requestPasswordReset(@Body request: RequestPasswordResetDto)

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordDto)

    @POST("auth/register-moderator")
    suspend fun registerModerator(@Body request: RegisterModeratorDto): RegisterResponseDto

}