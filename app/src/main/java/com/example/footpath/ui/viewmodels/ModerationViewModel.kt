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

data class ModerationUiState(
    val placesToModerate: List<PlaceDto> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class ModerationViewModel : ViewModel() {

    private val placesRepository = PlacesRepository() // In a real app, inject this

    private val _uiState = MutableStateFlow(ModerationUiState())
    val uiState = _uiState.asStateFlow()

    private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    init {
        fetchPlacesForModeration()
    }

    private fun fetchPlacesForModeration() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // In a real app, this would fetch from a remote source.
            // For now, we will simulate by creating a list of places.
            val allPlaces = generateMockPlacesForModeration()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    placesToModerate = allPlaces.filter { p -> p.status == "pending" }
                )
            }
        }
    }

    fun approvePlace(placeId: String) {
        updatePlaceStatus(placeId, "approved")
    }

    fun rejectPlace(placeId: String) {
        updatePlaceStatus(placeId, "rejected")
    }

    fun deletePlace(placeId: String) {
        _uiState.update { state ->
            val updatedPlaces = state.placesToModerate.filterNot { it.placeId == placeId }
            state.copy(placesToModerate = updatedPlaces)
        }
    }

    private fun updatePlaceStatus(placeId: String, newStatus: String) {
        _uiState.update { state ->
            val updatedPlaces = state.placesToModerate.map {
                if (it.placeId == placeId) it.copy(status = newStatus) else it
            }
            state.copy(placesToModerate = updatedPlaces.filter { p -> p.status == "pending" })
        }
    }

    private fun generateMockPlacesForModeration(): List<PlaceDto> {
        val now = Date().time

        return listOf(
            PlaceDto(
                placeId = "mock-1",
                name = "Pending Place by Me",
                description = "This is a place I suggested.",
                coordinates = com.example.footpath.data.api.dto.Coordinates("Point", listOf(37.6273, 55.7658)),
                tagIds = emptyList(),
                status = "pending",
                creatorId = "user-123",
                createdAt = isoFormatter.format(Date(now - 2 * 3600 * 1000)), // 2 hours ago
                updatedAt = isoFormatter.format(Date(now - 2 * 3600 * 1000))
            ),
             PlaceDto(
                placeId = "mock-3",
                name = "Pending by Other",
                description = "Someone else suggested this.",
                coordinates = com.example.footpath.data.api.dto.Coordinates("Point", listOf(37.6473, 55.7858)),
                tagIds = emptyList(),
                status = "pending",
                creatorId = "another-user",
                createdAt = isoFormatter.format(Date(now - 3 * 24 * 3600 * 1000)), // 3 days ago
                updatedAt = isoFormatter.format(Date(now - 3 * 24 * 3600 * 1000))
            )
        )
    }
}
