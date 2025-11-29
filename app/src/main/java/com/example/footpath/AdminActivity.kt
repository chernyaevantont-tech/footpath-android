package com.example.footpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.admin.AdminViewModel
import com.example.footpath.ui.theme.FootPathTheme

class AdminActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FootPathTheme {
                AdminScreen()
            }
        }
    }
}

@Composable
fun AdminScreen(viewModel: AdminViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(id = R.string.register_moderator), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = uiState.email, onValueChange = viewModel::onEmailChange, label = { Text(stringResource(id = R.string.email)) }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = { Text(stringResource(id = R.string.password)) }, modifier = Modifier.fillMaxWidth())

        if (uiState.errorMessage != null) {
            Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

        if (uiState.successMessage != null) {
            Text(text = uiState.successMessage!!, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = viewModel::registerModerator, enabled = !uiState.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(id = R.string.register))
        }
    }
}
