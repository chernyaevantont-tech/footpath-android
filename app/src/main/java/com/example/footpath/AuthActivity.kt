package com.example.footpath

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.footpath.ui.navigation.Screen
import com.example.footpath.ui.screens.LoginScreen
import com.example.footpath.ui.theme.FootPathTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FootPathTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavHost()
                }
            }
        }
    }

    @Composable
    fun AuthNavHost() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.Login.route) {
            composable(Screen.Login.route) {
                LoginScreen (
                    onLoginSuccess = {
                        // При успешном входе запускаем MainActivity
                        navigateToMainApp()
                    }
                )
            }
            // Здесь в будущем можно будет добавить экран регистрации
            // composable("register_screen") { ... }
        }
    }

    private fun navigateToMainApp() {
        // Создаем Intent для запуска MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            // Флаги, чтобы пользователь не мог вернуться на экран входа кнопкой "назад"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        // Завершаем AuthActivity
        finish()
    }
}