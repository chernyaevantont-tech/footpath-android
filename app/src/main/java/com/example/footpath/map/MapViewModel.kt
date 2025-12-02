package com.example.footpath.map

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.api.dto.CoordinatesDto
import com.example.footpath.data.api.dto.PlaceResponseDto
import com.example.footpath.data.repository.PlacesRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

enum class Role {
    USER, MODERATOR, ADMIN
}

data class User(val id: String, val role: Role)

data class MapUiState(
    val allPlaces: List<PlaceResponseDto> = emptyList(),
    val places: List<PlaceResponseDto> = emptyList(), // Filtered places
    val isLoading: Boolean = true,
    val userLocation: GeoPoint? = null,
    val selectedPlace: PlaceResponseDto? = null,
    val errorMessage: String? = null,
    val currentUser: User? = null,
    val showPendingAndRejected: Boolean = true
)

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val placesRepository = PlacesRepository()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    init {
        // Mock current user. In a real app, this would come from an auth repository.
        val currentUser = User(id = "user-123", role = Role.USER)
        _uiState.update { it.copy(currentUser = currentUser) }
        fetchPlaces()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // In a real app, you would fetch all places and filter them here.
            // For now, we continue to use the existing repository method and add some mock data.
            val approvedPlaces = placesRepository.findPlaces(status = "approved")
            val mockPlaces = generateMockPlaces()
            val allPlaces = approvedPlaces + mockPlaces

            if (allPlaces.isNotEmpty()) {
                _uiState.update {
                    it.copy(isLoading = false, allPlaces = allPlaces)
                }
                filterPlaces()
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Could not load places.")
                }
            }
        }
    }

    private fun filterPlaces() {
        _uiState.update { state ->
            val currentUser = state.currentUser ?: return@update state

            val filtered = state.allPlaces.filter { place ->
                when (place.status) {
                    "approved" -> true
                    "pending", "rejected" -> {
                        if (!state.showPendingAndRejected) return@filter false
                        when (currentUser.role) {
                            Role.ADMIN, Role.MODERATOR -> true
                            Role.USER -> place.creatorId == currentUser.id
                        }
                    }
                    else -> false
                }
            }
            state.copy(places = filtered)
        }
    }

    @SuppressLint("MissingPermission")
    fun centerOnUserLocation() {
        viewModelScope.launch {
            try {
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()

                location?.let {
                    val userGeoPoint = GeoPoint(it.latitude, it.longitude)
                    _uiState.update { state ->
                        state.copy(userLocation = userGeoPoint)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addPlace(point: GeoPoint) {
        val currentUser = _uiState.value.currentUser ?: return
        val now = isoFormatter.format(Date())

        val newPlace = PlaceResponseDto(
            id = UUID.randomUUID().toString(),
            name = "New Place",
            description = "A new place to be reviewed.",
            coordinates = "POINT (${point.longitude} ${point.latitude})",
            tags = emptyList(),
            status = "pending",
            creatorId = currentUser.id,
            moderatorId = null,
            createdAt = now,
            updatedAt = now
        )

        _uiState.update { state ->
            val updatedPlaces = state.allPlaces + newPlace
            state.copy(allPlaces = updatedPlaces)
        }
        filterPlaces()
    }

    fun toggleShowPendingAndRejected() {
        _uiState.update { it.copy(showPendingAndRejected = !it.showPendingAndRejected) }
        filterPlaces()
    }

    fun onMarkerClick(place: PlaceResponseDto) {
        _uiState.update { it.copy(selectedPlace = place) }
    }

    fun onBottomSheetDismiss() {
        _uiState.update { it.copy(selectedPlace = null) }
    }

    private fun generateMockPlaces(): List<PlaceResponseDto> {
        val currentUser = _uiState.value.currentUser!!
        val now = Date().time

        return listOf(
            PlaceResponseDto(
                id = "mock-1",
                name = "Pending Place by Me",
                description = "This is a place I suggested.",
                coordinates = "POINT (37.6273 55.7658)",
                tags = emptyList(),
                status = "pending",
                creatorId = currentUser.id,
                moderatorId = null,
                createdAt = isoFormatter.format(Date(now - 3600 * 1000)), // 1 hour ago
                updatedAt = isoFormatter.format(Date(now - 3600 * 1000))
            ),
            PlaceResponseDto(
                id = "mock-2",
                name = "Rejected Place by Me",
                description = "This place was rejected.",
                coordinates = "POINT (37.6373 55.7758)",
                tags = emptyList(),
                status = "rejected",
                creatorId = currentUser.id,
                moderatorId = null,
                createdAt = isoFormatter.format(Date(now - 24 * 3600 * 1000)), // 1 day ago
                updatedAt = isoFormatter.format(Date(now - 24 * 3600 * 1000))
            ),
            PlaceResponseDto(
                id = "mock-3",
                name = "Pending by Other",
                description = "Someone else suggested this.",
                coordinates = "POINT (37.6473 55.7858)",
                tags = emptyList(),
                status = "pending",
                creatorId = "another-user",
                moderatorId = null,
                createdAt = isoFormatter.format(Date(now - 5 * 3600 * 1000)), // 5 hours ago
                updatedAt = isoFormatter.format(Date(now - 5 * 3600 * 1000))
            )
        )
    }
}
