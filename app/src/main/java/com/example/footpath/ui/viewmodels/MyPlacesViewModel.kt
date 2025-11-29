package com.example.footpath.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.api.dto.PlaceDto
import com.example.footpath.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class MyPlacesUiState(
    val myPlaces: List<PlaceDto> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class MyPlacesViewModel : ViewModel() {

    private val placesRepository = PlacesRepository() // In a real app, inject this

    private val _uiState = MutableStateFlow(MyPlacesUiState())
    val uiState = _uiState.asStateFlow()

    // In a real app, you would get the current user's ID from an auth service.
    private val currentUserId = "user-123"
    private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    init {
        fetchMyPlaces()
    }

    private fun fetchMyPlaces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // For now, we use mock data. In a real app, you'd fetch from a repository.
            val allPlaces = generateMockUserPlaces()

            val myPlaces = allPlaces
                .filter { it.creatorId == currentUserId }
                .sortedByDescending { isoFormatter.parse(it.createdAt) }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    myPlaces = myPlaces
                )
            }
        }
    }

    private fun generateMockUserPlaces(): List<PlaceDto> {
        val now = Date().time

        return listOf(
            PlaceDto(
                placeId = "mock-1",
                name = "Pending Place by Me",
                description = "This is a place I suggested.",
                coordinates = com.example.footpath.data.api.dto.Coordinates("Point", listOf(37.6273, 55.7658)),
                tagIds = emptyList(),
                status = "pending",
                creatorId = currentUserId,
                createdAt = isoFormatter.format(Date(now - 24 * 3600 * 1000)), // 1 day ago
                updatedAt = isoFormatter.format(Date(now - 24 * 3600 * 1000))
            ),
            PlaceDto(
                placeId = "mock-2",
                name = "Rejected Place by Me",
                description = "This place was rejected.",
                coordinates = com.example.footpath.data.api.dto.Coordinates("Point", listOf(37.6373, 55.7758)),
                tagIds = emptyList(),
                status = "rejected",
                creatorId = currentUserId,
                createdAt = isoFormatter.format(Date(now - 2 * 24 * 3600 * 1000)), // 2 days ago
                updatedAt = isoFormatter.format(Date(now - 2 * 24 * 3600 * 1000))
            ),
             PlaceDto(
                placeId = "mock-4",
                name = "Approved Place by Me",
                description = "This place was approved!",
                coordinates = com.example.footpath.data.api.dto.Coordinates("Point", listOf(37.6573, 55.7958)),
                tagIds = emptyList(),
                status = "approved",
                creatorId = currentUserId,
                createdAt = isoFormatter.format(Date(now - 3 * 24 * 3600 * 1000)), // 3 days ago
                updatedAt = isoFormatter.format(Date(now - 3 * 24 * 3600 * 1000))
            )
        )
    }
}
