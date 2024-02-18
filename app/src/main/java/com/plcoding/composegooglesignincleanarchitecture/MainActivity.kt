package com.plcoding.composegooglesignincleanarchitecture

import MapViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.libraries.places.api.Places
import com.plcoding.composegooglesignincleanarchitecture.presentation.navigation.NavigationGraph
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.GoogleAuthUiClient
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.login.LoginViewModel
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.register.SignInViewModel
import com.plcoding.composegooglesignincleanarchitecture.ui.theme.SignInGDSCArchitectureTheme

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

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

                    NavigationGraph(
                        navController = navController,
                        googleAuthUiClient = googleAuthUiClient,
                        launcher = launcher,
                        placesClient = placesClient
                    )

                }
            }
        }
    }
}
