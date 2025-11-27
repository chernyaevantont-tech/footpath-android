package com.example.footpath.map

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footpath.data.api.dto.PlaceDto
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

data class MapUiState(
    val places: List<PlaceDto> = emptyList(),
    val isLoading: Boolean = true,
    val userLocation: GeoPoint? = null,
    val selectedPlace: PlaceDto? = null,
    val errorMessage: String? = null
)

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val placesRepository = PlacesRepository()
    // 2. Создаем клиент для получения геолокации
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchPlaces()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val placesList = placesRepository.getApprovedPlaces()

            if (placesList.isNotEmpty()) {
                _uiState.update {
                    it.copy(isLoading = false, places = placesList)
                }
            } else {
                // Можно обработать и случай, когда список просто пуст,
                // но для простоты будем считать это ошибкой, если бэкенд не вернул ничего.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Could not load places.")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun centerOnUserLocation() {
        viewModelScope.launch {
            try {
                // Запрашиваем ОДНОКРАТНОЕ получение точной геолокации
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await() // .await() позволяет использовать корутины

                location?.let {
                    val userGeoPoint = GeoPoint(it.latitude, it.longitude)
                    _uiState.update { state ->
                        state.copy(userLocation = userGeoPoint)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Можно обновить UI с ошибкой
            }
        }
    }

    fun onMarkerClick(place: PlaceDto) {
        _uiState.update { it.copy(selectedPlace = place) }
    }

    fun onBottomSheetDismiss() {
        _uiState.update { it.copy(selectedPlace = null) }
    }
}