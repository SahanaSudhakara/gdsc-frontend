package com.gdsc.composesafespot.view.sign_in.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gdsc.composesafespot.R
import com.gdsc.composesafespot.view.components.DividerTextComponent
import com.gdsc.composesafespot.view.components.MyTextField
import com.gdsc.composesafespot.view.components.PasswordTextField
import com.gdsc.composesafespot.view.navigation.Screen
import com.gdsc.composesafespot.viewmodel.sign_in.SignInViewModel
import com.gdsc.composesafespot.viewmodel.sign_in.SignupUIEvent


@Composable
fun SignInScreen(
    navController: NavController,
    signInViewModel: SignInViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3F51B5),
                        Color(0xFF5C6BC0)
                    ),
                    startY = 0f,
                    endY = 700f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.safespot),
                    contentDescription = "Logo"
                )
            }
            Text(
                text = "Hey there,",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Text(
                text = "Create an Account",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
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

            val navigateToHome by signInViewModel.navigateToHome.collectAsState()

            if (navigateToHome) {
                // Navigate to the Home screen
                navController.navigate(Screen.HomeScreen.toString())
            }

            Button(
                onClick = {
                    signInViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                enabled = signInViewModel.allValidationsPassed.value,
                elevation = ButtonDefaults.elevation(4.dp),
            ) {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.button,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            DividerTextComponent()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ",
                    color = Color.White
                )
                Text(
                    text = "Login",
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.LoginScreen.toString())
                    }
                )
            }
        }
    }
}

