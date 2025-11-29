package com.example.footpath.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.AdminActivity
import com.example.footpath.R
import com.example.footpath.profile.ProfileViewModel
import com.example.footpath.AccountActivity

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val accountActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.refreshData()
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.errorMessage != null) {
            Text(
                text = stringResource(id = R.string.account_data_load_error),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Email: ${uiState.activeAccount?.email}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Role: ${uiState.activeAccount?.role}")
                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.activeAccount?.role == "admin") {
                    Button(onClick = { context.startActivity(Intent(context, AdminActivity::class.java)) }) {
                        Text(stringResource(id = R.string.admin_panel))
                    }
                }

                Button(onClick = { accountActivityLauncher.launch(Intent(context, AccountActivity::class.java)) }) {
                    Text(stringResource(id = R.string.manage_accounts))
                }
            }
        }
    }
}
