package com.gdsc.composesafespot


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.gdsc.composesafespot.model.DataViewModel
import com.google.android.libraries.places.api.Places
import com.gdsc.composesafespot.view.navigation.NavigationGraph
import com.gdsc.composesafespot.ui.theme.SignInGDSCArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInGDSCArchitectureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            // ...
                        }
                    )

                    Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
                    val placesClient = Places.createClient(this) // Use 'this' for context

                    // Retrieve the DataViewModel using hiltViewModel
                    val dataViewModel: DataViewModel = hiltViewModel()

                    NavigationGraph(
                        navController = navController,
                        dataViewModel = dataViewModel,
                        placesClient = placesClient
                    )
                }
            }
        }
    }
}

