package com.example.footpath.data.api.dto

import com.google.gson.annotations.SerializedName

data class RegisterDto(
    val email: String,
    val password: String
)

data class LoginDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val user: UserResponseDto,
    val token: String
)

data class RegisterResponseDto(
    val user: UserResponseDto,
    val token: String
)

data class UserResponseDto(
    val id: String,
    val email: String,
    val role: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class UserProfileDto(
    val email: String,
    val name: String
)

data class RequestPasswordResetDto(
    val email: String
)

data class ResetPasswordDto(
    val password: String,
    val token: String
)

data class RegisterModeratorDto(
    val email: String,
    val password: String,
    val role: String
)
