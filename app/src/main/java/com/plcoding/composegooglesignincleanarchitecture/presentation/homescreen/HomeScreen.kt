import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen.MapViewModel
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.UserData

@Composable
fun HomeScreen(
    viewModel: MapViewModel= androidx.lifecycle.viewmodel.compose.viewModel(),
    userData: UserData?,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile tab
            ProfileTab(
                userData = userData,
                onSignOut = onSignOut,
                onClick = { /* Handle profile tab click */ }
            )


            // Search bar
            TextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Search") },
                modifier = Modifier.weight(1f)
            )
        }

        // Google Map
        GoogleMap(
            modifier = Modifier.weight(1f),
            // properties = viewModel.state.properties, // Adjust this based on your ViewModel
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
        )
    }
}
