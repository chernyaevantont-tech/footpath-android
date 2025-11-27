package com.example.footpath.data.api.dto

data class UserDto(
    val id: String,
    val email: String,
    val role: String
)

data class RegisterModeratorResponse(
    val user: UserDto,
    val token: String
)