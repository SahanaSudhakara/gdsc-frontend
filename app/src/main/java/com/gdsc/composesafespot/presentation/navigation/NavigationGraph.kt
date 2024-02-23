package com.gdsc.composesafespot.presentation.navigation

import HomeScreen
import MapViewModel
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.libraries.places.api.net.PlacesClient
import com.gdsc.composesafespot.presentation.sign_in.login.LoginScreen
import com.gdsc.composesafespot.presentation.sign_in.login.LoginViewModel
import com.gdsc.composesafespot.presentation.sign_in.register.SignInScreen
import com.gdsc.composesafespot.presentation.sign_in.register.SignInViewModel
import com.gdsc.composesafespot.presentation.sign_in.login.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    launcher: ActivityResultLauncher<IntentSenderRequest>,
    placesClient: PlacesClient
) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.toString()) {
        composable(Screen.SplashScreen.toString()) {
            SplashScreen(navController = navController)
        }
        composable(Screen.LoginScreen.toString()){
            val viewModel= LoginViewModel()
            LoginScreen(
                navController = navController,
                viewModel
            )
        }
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
                viewModel = viewModel,navController=navController
            )
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