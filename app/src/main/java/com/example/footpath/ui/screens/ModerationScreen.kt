package com.example.footpath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.data.api.dto.PlaceResponseDto
import com.example.footpath.ui.viewmodels.ModerationViewModel

@Composable
fun ModerationScreen(moderationViewModel: ModerationViewModel = viewModel()) {
    val uiState by moderationViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!, 
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text("Places Awaiting Moderation", style = MaterialTheme.typography.headlineMedium)
                }
                items(uiState.placesToModerate) {
                    PlaceModerationCard(
                        place = it, 
                        onApprove = moderationViewModel::approvePlace,
                        onReject = moderationViewModel::rejectPlace,
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceModerationCard(
    place: PlaceResponseDto,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = place.name, style = MaterialTheme.typography.titleLarge)
            place.description?.let { Text(text = it) }
            Text(text = "Status: ${place.status}")
            place.creatorId?.let { Text(text = "Suggested by: $it") }

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                if (place.status == "pending") {
                    IconButton(onClick = { onApprove(place.id) }) {
                        Icon(Icons.Default.Check, contentDescription = "Approve")
                    }
                    IconButton(onClick = { onReject(place.id) }) {
                        Icon(Icons.Default.Close, contentDescription = "Reject")
                    }
                }
            }
        }
    }
}
