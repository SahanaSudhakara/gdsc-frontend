package com.gdsc.composesafespot.view.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {

    object SignUpScreen : Screen()
    object Report : Screen()
    object LoginScreen : Screen()
    object SplashScreen : Screen()
    object HomeScreen : Screen()
}

object NavigationAppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpScreen)

    fun navigateTo(destination : Screen){
        currentScreen.value = destination
    }


}