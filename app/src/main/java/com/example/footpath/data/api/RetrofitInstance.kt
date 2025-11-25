package com.example.footpath.data.api

import com.example.footpath.data.api.service.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // ВАЖНО: Замените на IP-адрес вашего компьютера, где запущен бэкенд.
    // 10.0.2.2 - это специальный адрес для доступа к localhost хост-машины из эмулятора Android.
    private const val BASE_URL = "http://10.0.2.2:3000/"

    // Логгер для просмотра тел запросов и ответов в Logcat. Очень полезно для отладки.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(AuthInterceptor()) // <--- ДОБАВЬТЕ ЭТУ СТРОКУ
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Создаем ленивый экземпляр нашего API-сервиса
    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}