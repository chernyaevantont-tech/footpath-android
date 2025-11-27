package com.example.footpath.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.profile.ProfileViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import com.example.footpath.AdminActivity

import com.footpath.mobile.AccountActivity


@Composable
fun ProfileScreen(
    // Снова получаем ViewModel через делегат, так как проброс больше не нужен
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by profileViewModel.uiState.collectAsState()

    val accountSwitcherLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            profileViewModel.refreshData()
        }
    }

    LaunchedEffect (key1 = Unit) {
        profileViewModel.refreshData()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            val activeAccount = uiState.activeAccount
            if (activeAccount == null) {
                Text("Ошибка: не удалось загрузить данные аккаунта.")
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Активный аккаунт", style = MaterialTheme.typography.titleMedium)
                    Text(
                        activeAccount.email,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(onClick = {
                        // Запускаем AccountActivity через наш лаунчер
                        val intent = Intent(context, AccountActivity::class.java)
                        accountSwitcherLauncher.launch(intent)
                    }) {
                        Text("Управление аккаунтами")
                    }

                    if (activeAccount.role == "admin") {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            context.startActivity(Intent(context, AdminActivity::class.java))
                        }) {
                            Text("Админ-панель")
                        }
                    }
                }
            }
        }
    }
}