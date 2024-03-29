package com.gdsc.composesafespot.view.sign_in.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gdsc.composesafespot.R
import com.gdsc.composesafespot.view.navigation.Screen
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SplashScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.White)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.safespot), // Replace with your app logo
            contentDescription = "App Logo"
        )
    }

    val SPLASH_SCREEN_DELAY = 2000L

    // Navigate to the login screen after a delay
    LaunchedEffect(Unit) {
        delay(SPLASH_SCREEN_DELAY)
        navController.navigate(Screen.LoginScreen.toString())
    }
}
