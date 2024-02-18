package com.plcoding.composegooglesignincleanarchitecture.presentation.navigation

import HomeScreen
import MapViewModel
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.libraries.places.api.net.PlacesClient
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.GoogleAuthUiClient
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.login.LoginScreen
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.login.LoginViewModel
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.register.SignInScreen
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.register.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    launcher: ActivityResultLauncher<IntentSenderRequest>,
    placesClient: PlacesClient
) {
    NavHost(navController = navController, startDestination = Screen.SignUpScreen.toString()) {
        composable(Screen.SignUpScreen.toString()) {
            val viewModel=SignInViewModel()

            SignInScreen(
                navController = navController,
               viewModel
            )
        }
        composable(Screen.HomeScreen.toString()) {
           // val userData by remember { mutableStateOf(googleAuthUiClient.getSignedInUser()) }
            val viewModel = MapViewModel(placesClient)
            HomeScreen(
                viewModel = viewModel
               // googleAuthUiClient = googleAuthUiClient
            )
        }
        composable(Screen.LoginScreen.toString()) {
            val viewModel = LoginViewModel()
            LoginScreen(navController, viewModel)
        }

    }
}

fun onLoginSuccess(navController: NavController) {
    navController.navigate(Screen.HomeScreen.toString())
}


/*  LaunchedEffect(key1 = Unit) {
               if (googleAuthUiClient.getSignedInUser() != null) {
                   navController.navigate("Home")
               }
           }*/

/*LaunchedEffect(key1 = state.isSignInSuccessful) {
    if (state.isSignInSuccessful) {
        Toast.makeText(
            navController.context,
            "Sign in successful",
            Toast.LENGTH_LONG
        ).show()

        navController.navigate("Home")
        viewModel.resetState()
    }
}*/