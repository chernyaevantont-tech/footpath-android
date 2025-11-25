package com.example.footpath.data.repository

import com.example.footpath.FootPathApp
import com.example.footpath.data.api.RetrofitInstance
import com.example.footpath.data.api.dto.LoginRequest
import java.lang.Exception

class AuthRepository {

    private val authApiService = RetrofitInstance.authApi
    // Получаем доступ к нашему синглтону
    private val tokenManager = FootPathApp.tokenManager

    // Теперь метод возвращает true в случае успеха
    suspend fun login(email: String, password: String): Boolean {
        return try {
            val request = LoginRequest(email = email, password = password)
            val response = authApiService.login(request)

            if (response.token.isNotBlank()) {
                // Сохраняем токен
                tokenManager.saveToken(response.token)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}