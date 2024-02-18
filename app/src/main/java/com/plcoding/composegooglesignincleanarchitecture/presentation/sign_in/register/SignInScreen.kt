package com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.plcoding.composegooglesignincleanarchitecture.R
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.ButtonComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.ClickableLoginTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.DividerTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.HeadingTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.MyTextField
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.NormalTextComponent
import com.plcoding.composegooglesignincleanarchitecture.presentation.components.PasswordTextField
import com.plcoding.composegooglesignincleanarchitecture.presentation.navigation.Screen

@Composable
fun SignInScreen(
    navController: NavController,
    signInViewModel: SignInViewModel,
) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(value = "Hey there,")
            HeadingTextComponent(value = "Create an Account")
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField(
                labelValue = "First Name",
                painter = painterResource(id = R.drawable.profile),
                onTextSelected = {
                    signInViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                },
                errorStatus = signInViewModel.registrationUIState.value.firstNameError
            )
            MyTextField(
                labelValue = "Last Name",
                painter = painterResource(id = R.drawable.profile),
                onTextSelected = {
                    signInViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                },
                errorStatus = signInViewModel.registrationUIState.value.lastNameError
            )
            MyTextField(
                labelValue = "Email",
                painter = painterResource(id = R.drawable.email),
                onTextSelected = {
                    signInViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                },
                errorStatus = signInViewModel.registrationUIState.value.emailError
            )
            PasswordTextField(
                labelValue = "Password",
                painter = painterResource(id = R.drawable.lock),
                onTextSelected = {
                    signInViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                },
                errorStatus = signInViewModel.registrationUIState.value.passwordError
            )
            Spacer(modifier = Modifier.height(20.dp))
            // Click listener for the register button
            val onRegisterButtonClick: () -> Unit = {
                signInViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
            }

            val navigateToHome by signInViewModel.navigateToHome.collectAsState()

            if (navigateToHome) {
                // Navigate to the Home screen
                navController.navigate(Screen.HomeScreen.toString())
            }


            ButtonComponent(
                value = "Register",
                onButtonClicked = onRegisterButtonClick,
                isEnabled = signInViewModel.allValidationsPassed.value
            )
            DividerTextComponent()
            ClickableLoginTextComponent(
                tryingToLogin = true,
                onTextSelected = {
                    navController.navigate(Screen.LoginScreen.toString())
                }
            )
        }
    }
}



/*
@Preview
@Composable
fun SignInScreenPreview(
    state: RegistrationUIState = RegistrationUIState(isSignInSuccessful = true, signInError = null),
    onSignInClick: () -> Unit = {},
    navController: NavHostController = rememberNavController(),

) {
    SignInScreen(
        state = state,
        onSignInClick = onSignInClick,
        navController = navController )
}
*/
