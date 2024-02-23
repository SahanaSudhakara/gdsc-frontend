
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.gdsc.composesafespot.presentation.homescreen.MapEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.google.maps.android.compose.MapUiSettings
import com.gdsc.composesafespot.presentation.components.AppToolbar
import com.gdsc.composesafespot.presentation.navigation.Screen
import com.gdsc.composesafespot.ui.theme.Primary

//
@Composable
fun HomeScreen(viewModel: MapViewModel, navController: NavController) {
    val state by viewModel.mapState
    var text by remember { mutableStateOf("") }
    val selectedItem by remember { mutableStateOf<AutocompleteResult?>(null) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var updateLatLng by remember { mutableStateOf(false) }

    // MutableState to hold the camera position
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.markerState, 15f)
    }
    Scaffold(
        topBar = {
            AppToolbar(toolbarTitle = "Safe Spot", logoutButtonClicked = { viewModel.logout() }) {
            }
        }
    )
    {paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val localFocusManager=LocalFocusManager.current
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
                keyboardActions = KeyboardActions(onAny = {
                    viewModel.onEvent(MapEvent.SearchName(text))
                    // localFocusManager.clearFocus()
                }),
                shape = RoundedCornerShape(4.dp)
            )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(paddingValues)
        ) {
            GoogleMap(
                properties = state.properties,
                modifier = Modifier
                    .fillMaxWidth(),
                cameraPositionState = cameraPosition,
                uiSettings = MapUiSettings()
            ) {
                // Add Marker inside GoogleMap
                markerPosition?.let { position ->
                    Marker(
                        state = MarkerState(position = position),
                        title = "title",
                        draggable = true
                    )
                }
            }

                LazyColumnWithSelection(
                    items = viewModel.locationAutofill,
                    text = text,
                    onItemSelected = { selectedItem ->
                        text = selectedItem.address
                        viewModel.getCoordinates(selectedItem) { latLng ->
                            if (latLng != null) {
                                cameraPosition.position = CameraPosition.fromLatLngZoom(latLng, 10f)
                                viewModel.newLatLng=latLng
                                // Update the marker position by setting updateLatLng to true
                                updateLatLng = true

                            } else {
                                // Handle failure
                            }
                        }
                    }, viewModel

                )
                // Observe changes in updateLatLng state variable
                LaunchedEffect(updateLatLng) {
                    // Update the marker position based on the latest value in the viewmodel
                    markerPosition = viewModel.newLatLng
                    updateLatLng=false

                }
                val navigateToLogin by viewModel.navigateToLogin.collectAsState()

                if (navigateToLogin) {
                    // Navigate to the Home screen
                    navController.navigate(Screen.LoginScreen.toString())
                }
            }
            if (selectedItem?.address != null) {
                BottomCardView(location = text)
            }
        }

    }


}


@Composable
fun LazyColumnWithSelection(
    items: MutableList<AutocompleteResult>,
    text: String,
    onItemSelected: (AutocompleteResult) -> Unit,viewModel: MapViewModel
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

@Composable
fun BottomCardView(location: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            // Expand the Card to fill the available width
            backgroundColor = Color.White,
            shape = RoundedCornerShape(8.dp), // Rounded corners with an 8dp radius
            elevation = 8.dp // Add elevation for a shadow effect
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = location)
                Button(onClick = { /* Launch Google Maps with directions */ }) {
                    Text("Directions")
                }
            }
        }
    }
}