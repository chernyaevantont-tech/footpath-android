// file: app/src/main/java/com/footpath/mobile/AccountActivity.kt

package com.footpath.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.footpath.AuthActivity
import com.example.footpath.profile.ProfileViewModel
import com.example.footpath.ui.components.AccountSwitcherSheet
import com.example.footpath.ui.theme.FootPathTheme

class AccountActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FootPathTheme() {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AccountManagementScreen(viewModel = profileViewModel)
                }
            }
        }
    }

    @Composable
    fun AccountManagementScreen(viewModel: ProfileViewModel) {
        val uiState by viewModel.uiState.collectAsState()

        Column(modifier = Modifier.padding(16.dp)) {

            AccountSwitcherSheet (
                accounts = uiState.allAccounts,
                activeAccount = uiState.activeAccount,
                onAccountSelected = { selectedAccount ->

                    viewModel.switchAccount(selectedAccount)

                    setResult(Activity.RESULT_OK)

                    finish()
                },
                onAddAccountClicked = {
                    val intent = Intent(this@AccountActivity, AuthActivity::class.java).apply {
                        putExtra("ADD_NEW_ACCOUNT_MODE", true)
                    }
                    startActivity(intent)
                    finishAffinity()
                },
                onLoginAsExistingClicked = { /* TODO */ }
            )
        }
    }
}