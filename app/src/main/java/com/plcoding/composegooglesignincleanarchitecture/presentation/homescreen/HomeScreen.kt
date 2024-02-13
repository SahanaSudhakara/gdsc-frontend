import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment.Horizontal
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.plcoding.composegooglesignincleanarchitecture.presentation.profile.ProfileTab
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.GoogleAuthUiClient
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(viewModel: MapViewModel,googleAuthUiClient:GoogleAuthUiClient) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Set up variables for map center and initial zoom level
        val sanJose = remember { LatLng(37.3496, -121.9381) } // Santa Clara University coordinates
        var cameraPosition = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(sanJose, 10f)
        }

        // Set up GoogleMap
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition
        ) {
            Marker(
                state = MarkerState(position = sanJose),
                title = "San Jose"
            )
        }
        Button(
            onClick = {
                cameraPosition.move(CameraUpdateFactory.newLatLng(viewModel.getCurrentLocation()))
            }
        ) {
            Text("Animate to current position")
        }
        }
        Surface(
            modifier = Modifier
              //  .align(Alignment.TopEnd)
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
                ProfileTab(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                    },
                    onClick = {

                    }
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        viewModel.searchPlaces(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }

