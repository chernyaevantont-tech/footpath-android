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

data class MyPlacesUiState(
    val myPlaces: List<PlaceResponseDto> = emptyList(),
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

            val allPlaces = placesRepository.findPlaces()

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
}
