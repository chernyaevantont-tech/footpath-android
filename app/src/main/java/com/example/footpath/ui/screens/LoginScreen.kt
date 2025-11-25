package com.example.footpath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.auth.LoginViewModel
import com.example.footpath.ui.theme.FootPathTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()

    // 1. Используем LaunchedEffect для навигации
    // Этот блок выполнится, когда uiState.loginSuccess станет true
    LaunchedEffect(key1 = uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to FootPath",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                // 2. Блокируем поля во время загрузки
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (uiState.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (uiState.isPasswordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { loginViewModel.onTogglePasswordVisibility() }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                // Блокируем поля во время загрузки
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Отображаем ошибку, если она есть
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { loginViewModel.onLoginClicked() },
                modifier = Modifier.fillMaxWidth(),
                // Кнопка заблокирована во время загрузки или если поля пустые
                enabled = !uiState.isLoading && uiState.email.isNotBlank() && uiState.password.isNotBlank()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { /* TODO */ }, enabled = !uiState.isLoading) {
                Text("Don't have an account? Register")
            }
            TextButton(onClick = { /* TODO */ }, enabled = !uiState.isLoading) {
                Text("Forgot Password?")
            }
        }

        // 4. Показываем индикатор загрузки поверх всего
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FootPathTheme {
        LoginScreen(onLoginSuccess = {})
    }
}