import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Switch
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment.Horizontal
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen.MapEvent
import com.plcoding.composegooglesignincleanarchitecture.presentation.profile.ProfileTab
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(viewModel:MapViewModel) {
    val currentLatLongState by viewModel.currentLatLong.collectAsState()// Santa Clara University coordinates
    val sanJose = remember { LatLng(currentLatLongState.latitude, currentLatLongState.longitude) }
    var cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sanJose, 10f)
    }
    var previousText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Set up GoogleMap
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition
        ) {
            createMarker(MarkerData(currentLatLongState.latitude, currentLatLongState.longitude, "Marker"))
        }

        Surface(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                var text by remember { mutableStateOf("") }

                AnimatedVisibility(
                    viewModel.locationAutofill.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.locationAutofill) { autocompleteResult ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {
                                        text = autocompleteResult.address
                                        viewModel.locationAutofill.clear()
                                        viewModel.getCoordinates(autocompleteResult)
                                    }
                            ) {
                                Text(autocompleteResult.address)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }



                // Observe map events
                val mapEvent by viewModel.mapEvent.observeAsState()

                // Effect to handle map update event
                LaunchedEffect(mapEvent) {
                    mapEvent?.let { event ->
                        when (event) {
                            is MapEvent.UpdateLocation -> {
                                // Update map with the new location
                                updateMapWithLocation(cameraPosition, viewModel)
                            }
                        }
                    }
                }

                // Text field
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        viewModel.searchPlaces(it)
                        if (it.isEmpty() && previousText.isNotEmpty()) {
                            // Trigger update map event
                            viewModel.triggerMapEvent(MapEvent.UpdateLocation(0.0, 0.0)) // Pass default values or appropriate values if available
                        }
                        previousText = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

            }
        }
    }
}

fun updateMapWithLocation(cameraPosition: CameraPositionState, viewModel: MapViewModel) {
    // Update the map to the new location
    cameraPosition.move(CameraUpdateFactory.newLatLng(viewModel.getCurrentLocation()))
}

@Composable
fun createMarker(markerData: MarkerData) {
    val position = remember { LatLng(markerData.latitude, markerData.longitude) }
    val markerState = remember { MarkerState(position = position) }
    Marker(
        state = markerState,
        title = markerData.title,
        snippet = markerData.snippet,
    )
}

