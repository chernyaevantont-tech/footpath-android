package com.example.footpath

import android.app.Application
import com.example.footpath.util.TokenManager

class FootPathApp : Application() {

    companion object {
        lateinit var tokenManager: TokenManager
            private set // Доступ на запись только внутри этого класса
    }

    override fun onCreate() {
        super.onCreate()
        // Инициализируем менеджер токенов при создании приложения
        tokenManager = TokenManager(applicationContext)
    }
}