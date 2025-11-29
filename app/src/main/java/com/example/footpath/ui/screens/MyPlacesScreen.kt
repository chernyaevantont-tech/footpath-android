package com.example.footpath.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.data.api.dto.PlaceDto
import com.example.footpath.ui.viewmodels.MyPlacesViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun MyPlacesScreen(myPlacesViewModel: MyPlacesViewModel = viewModel()) {
    val uiState by myPlacesViewModel.uiState.collectAsState()

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
                    Text("My Suggested Places", style = MaterialTheme.typography.headlineMedium)
                }
                items(uiState.myPlaces) {
                    MyPlaceCard(place = it)
                }
            }
        }
    }
}

@Composable
fun MyPlaceCard(place: PlaceDto) {
    val isoFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
    val displayFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = place.name, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Status: ${place.status.replaceFirstChar { it.uppercase() }}",
                color = when (place.status) {
                    "approved" -> Color.Green
                    "rejected" -> Color.Red
                    else -> Color.Gray
                }
            )
            val date = isoFormatter.parse(place.createdAt)
            val formattedDate = date?.let { displayFormatter.format(it) } ?: ""
            Text(text = "Suggested on: $formattedDate")
        }
    }
}
