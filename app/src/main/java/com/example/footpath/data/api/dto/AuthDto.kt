package com.example.footpath.data.api.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("token")
    val token: String
)

data class RegisterModeratorRequest(
    val email: String,
    val password: String,
    val role: String
)