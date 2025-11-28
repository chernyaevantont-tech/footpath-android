// file: app/src/main/java/com/footpath/mobile/AccountActivity.kt
package com.footpath.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.footpath.AuthActivity
import com.example.footpath.profile.ProfileViewModel
import com.example.footpath.ui.components.AccountAction
import com.example.footpath.ui.components.AccountSwitcherSheet
import com.example.footpath.ui.theme.FootPathTheme

class AccountActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()

    private val authActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // New account was added, refresh the list
            profileViewModel.refreshData()
            // And also signal the previous screen to refresh
            setResult(Activity.RESULT_OK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FootPathTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AccountManagementScreen(viewModel = profileViewModel)
                }
            }
        }
    }

    @Composable
    fun AccountManagementScreen(viewModel: ProfileViewModel) {
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(uiState.allAccounts) {
            if (uiState.allAccounts.isEmpty() && !uiState.isLoading) {
                // This will likely be hit after logging out of the last account.
                // We should just finish this activity and let the previous one handle it.
                setResult(Activity.RESULT_OK) // To signal a change
                finish()
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            AccountSwitcherSheet(
                accounts = uiState.allAccounts,
                activeAccount = uiState.activeAccount,
                onAccountAction = { action ->
                    when (action) {
                        is AccountAction.Select -> {
                            viewModel.switchAccount(action.account)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        is AccountAction.Logout -> {
                            viewModel.logout(action.account)
                            // The LaunchedEffect will handle finishing if it's the last account
                        }
                    }
                },
                onAddAccountClicked = {
                    val intent = Intent(this@AccountActivity, AuthActivity::class.java).apply {
                        putExtra("ADD_NEW_ACCOUNT_MODE", true)
                    }
                    authActivityLauncher.launch(intent)
                }
            )
        }
    }
}
