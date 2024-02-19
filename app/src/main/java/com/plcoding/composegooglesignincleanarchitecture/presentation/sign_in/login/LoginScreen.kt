package com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.login

import com.plcoding.composegooglesignincleanarchitecture.R
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.ButtonComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.ClickableLoginTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.DividerTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.HeadingTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.MyTextField
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.NormalTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.PasswordTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.plcoding.composegooglesignincleanarchitecture.presentation.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel ) {
//loginViewModel: LoginViewModel = viewModel()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                NormalTextComponent(value = "Login")
                HeadingTextComponent(value = "Welcome Back")
                Spacer(modifier = Modifier.height(20.dp))
                MyTextField(labelValue = "Email",
                    painterResource(id = R.drawable.email),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                   errorStatus = loginViewModel.loginUIState.value.emailError
                )

                PasswordTextField(
                    labelValue ="Password",
                    painterResource(id = R.drawable.lock),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                   errorStatus = loginViewModel.loginUIState.value.passwordError
                )

                Spacer(modifier = Modifier.height(40.dp))
                val navigateToHome by loginViewModel.navigateToHome.collectAsState()

                if (navigateToHome) {
                    // Navigate to the Home screen
                    navController.navigate(Screen.HomeScreen.toString())
                }

                ButtonComponent(
                    value = "Login",
                    onButtonClicked = {
                       loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    isEnabled =loginViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    navController.navigate(Screen.SignUpScreen.toString())
                })
            }
        }


     //   if(loginViewModel.loginInProgress.value) {
        //    CircularProgressIndicator()
       // }
    }

    }
   // SystemBackButtonHandler {
     //   PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)
   // }


/*
@Preview
@Composable
fun LoginScreenPreview( navController: NavHostController = rememberNavController()) {
    LoginScreen(navController )
}*/