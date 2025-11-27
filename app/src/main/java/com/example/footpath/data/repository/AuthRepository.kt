package com.example.footpath.data.repository

import android.util.Log
import com.example.footpath.FootPathApp
import com.example.footpath.data.api.RetrofitInstance
import com.example.footpath.data.api.dto.LoginRequest
import com.example.footpath.data.api.dto.RegisterModeratorRequest
import com.example.footpath.util.StoredAccount
import java.lang.Exception

class AuthRepository {
    private val authApiService = RetrofitInstance.authApi
    private val accountManager = FootPathApp.accountManager

    suspend fun login(email: String, password: String): StoredAccount? {
        return try {
            val loginRequest = LoginRequest(email = email, password = password)
            val loginResponse = authApiService.login(loginRequest)
            val token = loginResponse.token

            if (token.isBlank()) {
                Log.e("AuthRepository", "Login failed: Token is blank")
                return null
            }
            Log.d("AuthRepository", "Step 1: Login successful, got token.")

            val authHeader = "Bearer $token"
            val userProfile = authApiService.getMyProfile(authHeader)

            val finalAccount = StoredAccount(
                userId = userProfile.id,
                email = userProfile.email,
                role = userProfile.role,
                token = token
            )
            accountManager.addOrUpdateAccount(finalAccount)
            accountManager.setActiveAccount(finalAccount.userId)

            finalAccount
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun register(email: String, password: String): StoredAccount? {
        return try {
            val registerRequest = LoginRequest(email = email, password = password)
            val loginResponse = authApiService.register(registerRequest)
            val token = loginResponse.token

            if (token.isBlank()) return null
            Log.d("AuthRepository", "Step 1: Registration successful, got token.")

            val authHeader = "Bearer $token"
            val userProfile = authApiService.getMyProfile(authHeader)
            Log.d("AuthRepository", "Step 2: Fetched profile after registration. Role: ${userProfile.role}")

            val finalAccount = StoredAccount(
                userId = userProfile.id,
                email = userProfile.email,
                role = userProfile.role,
                token = token
            )
            accountManager.addOrUpdateAccount(finalAccount)
            accountManager.setActiveAccount(finalAccount.userId)
            Log.d("AuthRepository", "Step 3: Saved new account and set as active.")

            finalAccount
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception during registration process", e)
            null
        }
    }

    suspend fun registerModerator(request: RegisterModeratorRequest): Boolean {
        return try {
            val response = authApiService.registerModerator(request)
            response.user.id.isNotBlank()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}