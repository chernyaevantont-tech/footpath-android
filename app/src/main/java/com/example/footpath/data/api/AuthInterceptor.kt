package com.example.footpath.data.api

import com.example.footpath.FootPathApp
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    // Получаем доступ к нашему менеджеру токенов
    private val tokenManager = FootPathApp.tokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        // Получаем оригинальный запрос
        val originalRequest = chain.request()

        // Пытаемся получить токен
        val token = tokenManager.getToken()

        // Если токена нет (например, для запроса на /login),
        // просто выполняем оригинальный запрос.
        if (token == null) {
            return chain.proceed(originalRequest)
        }

        // Если токен есть, создаем новый запрос с заголовком Authorization
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        // Выполняем новый, аутентифицированный запрос
        return chain.proceed(newRequest)
    }
}