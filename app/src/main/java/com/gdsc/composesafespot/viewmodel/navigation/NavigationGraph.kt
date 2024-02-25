package com.gdsc.composesafespot.view.navigation

import HomeScreen
import com.gdsc.composesafespot.viewmodel.maps.MapViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gdsc.composesafespot.model.DataViewModel
import com.google.android.libraries.places.api.net.PlacesClient
import com.gdsc.composesafespot.view.sign_in.login.LoginScreen
import com.gdsc.composesafespot.viewmodel.login.LoginViewModel
import com.gdsc.composesafespot.view.sign_in.register.SignInScreen
import com.gdsc.composesafespot.viewmodel.sign_in.SignInViewModel
import com.gdsc.composesafespot.view.sign_in.login.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    dataViewModel: DataViewModel,
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
            val viewModel= SignInViewModel()

            SignInScreen(
                navController = navController,
               viewModel
            )
        }
        composable(Screen.HomeScreen.toString()) {
           // val userData by remember { mutableStateOf(googleAuthUiClient.getSignedInUser()) }
            val viewModel = MapViewModel(placesClient)
          //  val viewModel1 = hiltViewModel()
                //DataViewModel(crimeStatusService = crim)

            HomeScreen(
                viewModel = viewModel,navController=navController, dataViewModel = dataViewModel
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