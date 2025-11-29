package com.example.footpath.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.footpath.R
import com.example.footpath.util.StoredAccount

sealed class AccountAction {
    data class Select(val account: StoredAccount) : AccountAction()
    data class Logout(val account: StoredAccount) : AccountAction()
}

@Composable
fun AccountSwitcherSheet(
    accounts: List<StoredAccount>,
    activeAccount: StoredAccount?,
    onAccountAction: (AccountAction) -> Unit,
    onAddAccountClicked: () -> Unit
) {

    val showDialog = remember { mutableStateOf<StoredAccount?>(null) }

    if (showDialog.value != null) {
        AlertDialog(
            onDismissRequest = { showDialog.value = null },
            title = { Text(stringResource(id = R.string.logout_from_account)) },
            text = { Text(stringResource(id = R.string.logout_confirmation, showDialog.value?.email ?: "")) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value?.let { onAccountAction(AccountAction.Logout(it)) }
                        showDialog.value = null
                    }
                ) {
                    Text(stringResource(id = R.string.logout))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = null }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.select_account),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        accounts.forEach { account ->
            ListItem(
                modifier = Modifier.clickable {
                    if (account.userId != activeAccount?.userId) {
                        onAccountAction(AccountAction.Select(account))
                    }
                },
                headlineContent = { Text(account.email) },
                trailingContent = {
                    if (account.userId == activeAccount?.userId) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(id = R.string.active_account),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        IconButton(onClick = { showDialog.value = account }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.logout_from_account)
                            )
                        }
                    }
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        ListItem(
            modifier = Modifier.clickable { onAddAccountClicked() },
            headlineContent = { Text(stringResource(id = R.string.add_account)) },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_account)
                )
            }
        )
    }
}