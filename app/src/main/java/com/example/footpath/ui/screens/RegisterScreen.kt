package com.example.footpath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.auth.RegisterViewModel
import com.example.footpath.util.StoredAccount

@Composable
fun RegisterScreen(
    onRegistrationSuccess: (StoredAccount) -> Unit,
    onBackToLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registeredAccount) {
        uiState.registeredAccount?.let {
            onRegistrationSuccess(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = uiState.email, onValueChange = viewModel::onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = { Text("Пароль") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = uiState.confirmPassword, onValueChange = viewModel::onConfirmPasswordChange, label = { Text("Подтвердите пароль") }, modifier = Modifier.fillMaxWidth())

        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = viewModel::onRegisterClicked, enabled = !uiState.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text("Зарегистрироваться")
        }

        TextButton(onClick = onBackToLogin) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}