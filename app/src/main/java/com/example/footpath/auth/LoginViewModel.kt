package com.example.footpath.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.repository.AuthRepository
import com.example.footpath.util.StoredAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val loggedInAccount: StoredAccount? = null,
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newEmail, errorMessage = null)
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newPassword, errorMessage = null)
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { currentState ->
            currentState.copy(isPasswordVisible = !currentState.isPasswordVisible)
        }
    }

    fun onLoginClicked() {
        if (_uiState.value.isLoading) return

        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val finalAccount = authRepository.loginAndGetAccount(email, password)

            if (finalAccount != null) {
                _uiState.update {
                    it.copy(isLoading = false, loggedInAccount = finalAccount)
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Неверный адрес электронной почты или пароль."
                    )
                }
            }
        }
    }
}