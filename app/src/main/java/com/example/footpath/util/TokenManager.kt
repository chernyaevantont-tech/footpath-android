package com.example.footpath.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    // Создаем или получаем главный ключ для шифрования
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Инициализируем EncryptedSharedPreferences
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_token_prefs", // Имя файла для SharedPreferences
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val JWT_TOKEN_KEY = "jwt_token"
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(JWT_TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(JWT_TOKEN_KEY, null)
    }

    fun deleteToken() {
        sharedPreferences.edit().remove(JWT_TOKEN_KEY).apply()
    }
}