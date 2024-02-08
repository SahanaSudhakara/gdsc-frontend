package com.plcoding.composegooglesignincleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.plcoding.composegooglesignincleanarchitecture.presentation.navigation.NavigationGraph
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.GoogleAuthUiClient
import com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.SignInViewModel
import com.plcoding.composegooglesignincleanarchitecture.ui.theme.SignInGDSCArchitectureTheme
import kotlinx.coroutines.launch

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
                    val googleAuthUiClient by lazy {
                        GoogleAuthUiClient(
                            context = applicationContext,
                            oneTapClient = Identity.getSignInClient(applicationContext)
                        )
                    }
                    val viewModel = viewModel<SignInViewModel>()

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                navController.currentBackStackEntry?.let { entry ->
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        }
                    )


                    NavigationGraph(
                        navController = navController,
                        googleAuthUiClient = googleAuthUiClient,
                        viewModel = viewModel,
                        launcher = launcher
                    )
                }
            }
        }

    }
}
