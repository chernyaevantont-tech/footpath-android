package com.example.footpath.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.FootPathApp
import com.example.footpath.data.repository.UserRepository
import com.example.footpath.util.StoredAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val activeAccount: StoredAccount? = null,
    val allAccounts: List<StoredAccount> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class ProfileViewModel : ViewModel() {

    private val accountManager = FootPathApp.accountManager

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAccountData()
    }

    private fun loadAccountData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val all = accountManager.getAccounts()
            val active = accountManager.getActiveAccount()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    allAccounts = all,
                    activeAccount = active
                )
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val all = accountManager.getAccounts()
            val active = accountManager.getActiveAccount()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    allAccounts = all,
                    activeAccount = active
                )
            }
        }
    }

    fun switchAccount(account: StoredAccount) {
        accountManager.setActiveAccount(account.userId)
        loadAccountData()
    }

    fun logout(account: StoredAccount) {
        accountManager.logout(account.userId)
        loadAccountData()
    }
}