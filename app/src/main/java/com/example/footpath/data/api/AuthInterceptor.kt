package com.example.footpath.data.api

import com.example.footpath.FootPathApp
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private val accountManager = FootPathApp.accountManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = accountManager.getActiveToken()

        if (token == null) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}