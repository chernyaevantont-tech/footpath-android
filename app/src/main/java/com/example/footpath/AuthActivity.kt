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
import com.example.footpath.ui.screens.RegisterScreen
import com.example.footpath.ui.theme.FootPathTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAddNewAccountMode = intent.getBooleanExtra("ADD_NEW_ACCOUNT_MODE", false)

        if (!isAddNewAccountMode && FootPathApp.accountManager.getActiveAccount() != null) {
            navigateToMainApp()
            return
        }

        setContent {
            FootPathTheme {
                AuthNavHost()
            }
        }
    }

    @Composable
    fun AuthNavHost() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Screen.Login.route) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = { navigateToMainApp() },
                    // Используем новый централизованный маршрут
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }

            // Используем новый централизованный маршрут
            composable(Screen.Register.route) {
                RegisterScreen (
                    onRegistrationSuccess = { navigateToMainApp() },
                    onBackToLogin = { navController.popBackStack() }
                )
            }

        }
    }



    private fun navigateToMainApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("LOGIN_SUCCESS", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)

        finish()
    }
}