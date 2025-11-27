package com.example.footpath.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } }
    fun onConfirmPasswordChange(password: String) { _uiState.update { it.copy(confirmPassword = password) } }

    fun onRegisterClicked() {
        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Пароли не совпадают.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val newAccount = authRepository.register(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            if (newAccount != null) {
                _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Ошибка регистрации. Возможно, email уже занят.") }
            }
        }
    }
}