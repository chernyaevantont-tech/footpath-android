package com.example.footpath.data.api.dto

import com.google.gson.annotations.SerializedName

// Класс для тела запроса на /auth/login
data class LoginRequest(
    val email: String,
    val password: String
)

// Класс для ответа от /auth/login
// Предполагаем, что сервер возвращает объект с JWT токеном
data class LoginResponse(
    @SerializedName("token") // Указываем точное имя поля в JSON
    val token: String
)