package com.example.footpath.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.footpath.map.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiState(
    val role: Role = Role.USER // Default role
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // In a real app, fetch the user's role from a repository.
        // For demonstration, we can switch the role here.
        // _uiState.value = MainUiState(role = Role.MODERATOR)
        // _uiState.value = MainUiState(role = Role.ADMIN)
    }
}
