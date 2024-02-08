package com.plcoding.composegooglesignincleanarchitecture.presentation.navigation

import HomeScreen
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen.MapViewModel
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.GoogleAuthUiClient
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.SignInScreen
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    viewModel: SignInViewModel,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    NavHost(navController = navController, startDestination = "sign_in") {
        composable("sign_in") {
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate("Home")
                }
            }

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        navController.context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate("Home")
                    viewModel.resetState()
                }
            }
            val coroutineScope = rememberCoroutineScope()

            SignInScreen(
                state = state,
                onSignInClick = {
                    navController.currentBackStackEntry?.let { entry ->
                        coroutineScope.launch { // Execute signIn within a coroutine scope
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                }
            )


        }
        composable("Home") {
            val userData by remember { mutableStateOf(googleAuthUiClient.getSignedInUser()) }
            val coroutineScope = rememberCoroutineScope() // Create a new coroutine scope
            val viewModel=MapViewModel()
            HomeScreen(
                viewModel,
                userData = userData
            ) {
                coroutineScope.launch { // Execute the signOut function within a coroutine scope
                    googleAuthUiClient.signOut()
                    Toast.makeText(
                        navController.context,
                        "Signed out",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.popBackStack()
                }
            }
        }
    }
}
