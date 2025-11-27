package com.example.footpath.data.repository

import com.example.footpath.data.api.RetrofitInstance
import com.example.footpath.data.api.dto.UserDto


class UserRepository {

    private val authApiService = RetrofitInstance.authApi

    suspend fun getMyProfile(): UserDto? {
        return try {
//            authApiService.getMyProfile()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}