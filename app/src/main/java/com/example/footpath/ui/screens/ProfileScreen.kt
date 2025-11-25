package com.example.footpath.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.footpath.AuthActivity
import com.example.footpath.FootPathApp

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile Screen")
        Button(onClick = {
            // Очищаем токен
            FootPathApp.tokenManager.deleteToken()

            // Перезапускаем приложение, отправляя пользователя на AuthActivity
            val intent = Intent(context, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
            // Завершаем текущую Activity, чтобы пользователь не мог на нее вернуться
            (context as? Activity)?.finish()
        }) {
            Text(text = "Logout")
        }
    }
}