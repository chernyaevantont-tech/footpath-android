package com.example.footpath.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.api.dto.PlaceResponseDto
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
    val placesToModerate: List<PlaceResponseDto> = emptyList(),
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

            val pendingPlaces = placesRepository.findPlaces(status = "pending")

            _uiState.update {
                it.copy(
                    isLoading = false,
                    placesToModerate = pendingPlaces
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

    private fun updatePlaceStatus(placeId: String, newStatus: String) {
        // This is a mock implementation. In a real app, you would call the repository.
        _uiState.update { state ->
            val updatedPlaces = state.placesToModerate.filterNot { it.id == placeId }
            state.copy(placesToModerate = updatedPlaces)
        }
    }
}
