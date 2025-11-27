package com.example.footpath.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.footpath.util.StoredAccount
import androidx.compose.material3.ListItem

@Composable
fun AccountSwitcherSheet(
    accounts: List<StoredAccount>,
    activeAccount: StoredAccount?,
    onAccountSelected: (StoredAccount) -> Unit,
    onAddAccountClicked: () -> Unit,
    onLoginAsExistingClicked: (StoredAccount) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Выберите аккаунт",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        accounts.forEach { account ->
            ListItem(
                modifier = Modifier.clickable {
                    if (account.userId != activeAccount?.userId) {
                        onAccountSelected(account)
                    }
                },
                headlineContent = { Text(account.email) },
                trailingContent = {
                    if (account.userId == activeAccount?.userId) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Активный аккаунт",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        ListItem(
            modifier = Modifier.clickable { onAddAccountClicked() },
            headlineContent = { Text("Добавить аккаунт") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить аккаунт"
                )
            }
        )
    }
}