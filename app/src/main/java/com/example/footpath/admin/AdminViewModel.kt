package com.example.footpath.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.api.dto.RegisterModeratorRequest
import com.example.footpath.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val email: String = "",
    val password: String = "",
    val role: String = "moderator", // По умолчанию создаем модератора
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class AdminViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } }
    fun onRoleChange(role: String) { _uiState.update { it.copy(role = role) } }

    fun registerModerator() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, successMessage = null, errorMessage = null) }
            val request = RegisterModeratorRequest(
                email = _uiState.value.email,
                password = _uiState.value.password,
                role = _uiState.value.role
            )
            val isSuccess = authRepository.registerModerator(request)
            if (isSuccess) {
                _uiState.update { it.copy(isLoading = false, successMessage = "Пользователь ${request.email} успешно создан!") }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Ошибка. Возможно, email уже занят.") }
            }
        }
    }
}