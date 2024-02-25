import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gdsc.composesafespot.model.DataViewModel
import com.gdsc.composesafespot.view.components.AppToolbar
import com.gdsc.composesafespot.view.navigation.Screen
import com.gdsc.composesafespot.view.utils.addHeatmapLayerToMap
import com.gdsc.composesafespot.view.utils.convertDataToLatLngList

import com.gdsc.composesafespot.view.utils.rememberMapViewWithLifecycle
import com.gdsc.composesafespot.viewmodel.maps.AutocompleteResult
import com.gdsc.composesafespot.viewmodel.maps.MapEvent
import com.gdsc.composesafespot.viewmodel.maps.MapViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun HomeScreen(
    viewModel: MapViewModel,
    navController: NavController,
    dataViewModel: DataViewModel = hiltViewModel()
) {
    val crimeStatusList by dataViewModel.crimeStatusList.collectAsStateWithLifecycle(emptyList())
    val mapView = rememberMapViewWithLifecycle()
    var text by remember { mutableStateOf("") }
    var updateLatLng by remember { mutableStateOf(false) }
    val state by viewModel.mapState
    val selectedItem by remember { mutableStateOf<AutocompleteResult?>(null) }
    var markerPosition by remember { mutableStateOf<LatLng?>(viewModel.newLatLng) }
    // MutableState to hold the camera position
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.markerState, 10f)
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color(0xFF3F51B5))
    // Listen for changes in fetchingCrimeStatus
    LaunchedEffect(crimeStatusList) {
        // If crimeStatusList is not empty
        // Add the heatmap layer to the map
        if (crimeStatusList.isNotEmpty()) {
            mapView.getMapAsync { googleMap ->
                addHeatmapLayerToMap(
                    googleMap,
                    convertDataToLatLngList(crimeStatusList),
                    emptyList()
                )
            }
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(toolbarTitle = "Safe Spot", logoutButtonClicked = { viewModel.logout() }) {
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = { text = it },
                label = { Text("Search") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.onEvent(MapEvent.SearchName(text))
                    // localFocusManager.clearFocus()
                }),
                shape = RoundedCornerShape(4.dp)
            )
            LazyColumnWithSelection(
                items = viewModel.locationAutofill,
                text = text,
                onItemSelected = { selectedItem ->
                    text = selectedItem.address
                    viewModel.getCoordinates(selectedItem) { latLng ->
                        if (latLng != null) {
                            cameraPosition.position = CameraPosition.fromLatLngZoom(latLng, 10f)
                            viewModel.newLatLng = latLng
                            // Update the marker position by setting updateLatLng to true
                            updateLatLng = true
                        }
                    }
                }, viewModel

            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(paddingValues)
            ) {
                val markerOptionsDestination = MarkerOptions()
                    .position(markerPosition!!)

                AndroidView({ mapView }) { mapView ->
                    mapView.getMapAsync { googleMap ->
                        // Set camera position to San Francisco (SF) or any default location
                        googleMap.uiSettings.isZoomControlsEnabled = true
                        //    val sfLatLng = LatLng(37.7749, -122.4194)
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition.position))
                        // Add markers to the map

                        googleMap.addMarker(markerOptionsDestination)

                    }
                }

            }

            // Observe changes in updateLatLng state variable
            LaunchedEffect(updateLatLng) {
                // Update the marker position based on the latest value in the viewmodel
                markerPosition = viewModel.newLatLng
                updateLatLng = false
            }

            val navigateToLogin by viewModel.navigateToLogin.collectAsState()

            if (navigateToLogin) {
                // Navigate to the Home screen
                navController.navigate(Screen.LoginScreen.toString())
            }
        }
    }
}



@Composable
fun LazyColumnWithSelection(
    items: MutableList<AutocompleteResult>,
    text: String,
    onItemSelected: (AutocompleteResult) -> Unit, viewModel: MapViewModel
) {
    // MutableState to hold the index of the selected item
    val selectedItemIndex = remember { mutableStateOf(-1) }

    LazyColumn {
        itemsIndexed(items) { index, item ->
            // Background color based on selection
            val backgroundColor = if (index == selectedItemIndex.value) {
                Color.LightGray
            } else {
                Color.White
            }

            // Clickable item
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .clickable {
                        // Update selected item index
                        selectedItemIndex.value = index
                        // Invoke the callback to handle item selection
                        onItemSelected(item)
                        viewModel.locationAutofill.clear()
                    }
            ) {
                Text(
                    text = item.address,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    // Reset LazyColumn when text changes
    LaunchedEffect(text) {
        selectedItemIndex.value = -1
    }

}
