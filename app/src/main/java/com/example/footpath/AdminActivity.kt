package com.example.footpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.footpath.admin.AdminViewModel
import com.example.footpath.ui.theme.FootPathTheme

class AdminActivity : ComponentActivity() {
    private val viewModel: AdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FootPathTheme() {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AdminScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Создание модератора", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = uiState.email, onValueChange = viewModel::onEmailChange, label = { Text("Email") })
        OutlinedTextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = { Text("Пароль") })

        Button(onClick = viewModel::registerModerator, enabled = !uiState.isLoading) {
            Text("Зарегистрировать")
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
        uiState.successMessage?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}