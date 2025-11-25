package com.example.footpath.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,       // Для индикатора загрузки
    val loginSuccess: Boolean = false,    // Флаг успешного входа
    val errorMessage: String? = null      // Для сообщения об ошибке
)

class LoginViewModel : ViewModel() {

    // 1. Создаем экземпляр репозитория
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { currentState ->
            // Сбрасываем ошибку, как только пользователь начинает печатать
            currentState.copy(email = newEmail, errorMessage = null)
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { currentState ->
            // Сбрасываем ошибку, как только пользователь начинает печатать
            currentState.copy(password = newPassword, errorMessage = null)
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { currentState ->
            currentState.copy(isPasswordVisible = !currentState.isPasswordVisible)
        }
    }

    // 2. Обновляем логику нажатия на кнопку
    fun onLoginClicked() {
        if (_uiState.value.isLoading) return

        val email = _uiState.value.email
        val password = _uiState.value.password

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Теперь получаем Boolean
            val isSuccess = authRepository.login(email, password)

            if (isSuccess) {
                // Успех!
                _uiState.update {
                    it.copy(isLoading = false, loginSuccess = true)
                }
            } else {
                // Ошибка
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Invalid email or password."
                    )
                }
            }
        }
    }
}