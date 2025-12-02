package com.example.footpath.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.footpath.data.api.dto.PlaceResponseDto
import com.example.footpath.map.MapViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = viewModel()
) {
    val uiState by mapViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = uiState.selectedPlace != null

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                mapViewModel.centerOnUserLocation()
            } else {

            }
        }
    )

    val mapView = remember { MapView(context) }

    val locationOverlay = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
    }

    Scaffold (
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = {
                        val mapCenter = mapView.mapCenter
                        mapViewModel.addPlace(GeoPoint(mapCenter.latitude, mapCenter.longitude))
                    },
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Place")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = { mapViewModel.toggleShowPendingAndRejected() },
                    containerColor = if (uiState.showPendingAndRejected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Show/Hide Pending/Rejected",
                        tint = if (uiState.showPendingAndRejected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "My Location")
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    mapView.apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(15.0)
                        controller.setCenter(GeoPoint(55.7558, 37.6173)) // Москва

                        locationOverlay.enableMyLocation()
                        overlays.add(locationOverlay)
                    }
                },
                update = { mapView ->
                    mapView.overlays.removeAll { it is Marker }

                    uiState.places.forEach { place ->
                        val marker = Marker(mapView).apply {
                            position = parseCoordinates(place.coordinates)
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = place.name

                            val alpha = if (place.status == "approved") 255 else 128
                            val iconDrawable = this.icon.mutate()
                            iconDrawable.alpha = alpha
                            this.icon = iconDrawable

                            setOnMarkerClickListener { clickedMarker, _ ->
                                mapViewModel.onMarkerClick(place)
                                true
                            }
                        }
                        mapView.overlays.add(marker)
                    }
                    mapView.invalidate()
                }
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    LaunchedEffect(uiState.userLocation) {
        uiState.userLocation?.let {
            mapView.controller.animateTo(it)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet (
            onDismissRequest = { mapViewModel.onBottomSheetDismiss() },
            sheetState = sheetState
        ) {
            // TODO: Show place details here
            uiState.selectedPlace?.let {
                PlaceDetailsBottomSheet(place = it, onDismiss = { mapViewModel.onBottomSheetDismiss() })
            }
        }
    }
}

@Composable
fun PlaceDetailsBottomSheet(place: PlaceResponseDto, onDismiss: () -> Unit) {
    // Dummy implementation
    Box(modifier = Modifier.padding(16.dp)) {
        Text(text = "Selected place: ${place.name}")
    }
}

private fun parseCoordinates(wkt: String): GeoPoint? {
    val pattern = Pattern.compile("POINT \\(([-\\d.]+) ([-\\d.]+)\\)")
    val matcher = pattern.matcher(wkt)
    return if (matcher.find()) {
        val lon = matcher.group(1).toDouble()
        val lat = matcher.group(2).toDouble()
        GeoPoint(lat, lon)
    } else {
        null
    }
}
