package com.gdsc.composesafespot.presentation.sign_in.login

import androidx.compose.foundation.Image
import com.gdsc.composesafespot.presentation.components.ButtonComponent
import com.gdsc.composesafespot.presentation.components.ClickableLoginTextComponent
import com.gdsc.composesafespot.presentation.components.DividerTextComponent
import com.gdsc.composesafespot.presentation.components.HeadingTextComponent
import com.gdsc.composesafespot.presentation.components.MyTextField
import com.gdsc.composesafespot.presentation.components.NormalTextComponent
import com.gdsc.composesafespot.presentation.components.PasswordTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gdsc.composesafespot.presentation.navigation.Screen
import com.gdsc.composesafespot.R

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {
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
            Image(
                painter = painterResource(id = R.drawable.safespot),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Text(
                text = "Welcome Back",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            MyTextField(
                labelValue = "Email",
                painterResource(id = R.drawable.email),
                onTextSelected = {
                    loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                },
                errorStatus = loginViewModel.loginUIState.value.emailError
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add spacing between fields
            PasswordTextField(
                labelValue = "Password",
                painterResource(id = R.drawable.lock),
                onTextSelected = {
                    loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                },
                errorStatus = loginViewModel.loginUIState.value.passwordError
            )


                val navigateToHome by loginViewModel.navigateToHome.collectAsState()
                val loginFailed by loginViewModel.loginFailed.collectAsState()

                if (navigateToHome) {
                    // Navigate to the Home screen
                    navController.navigate(Screen.HomeScreen.toString())
                }else if (loginFailed){
                    AlertDialog(
                        onDismissRequest = { loginViewModel.dismissLoginFailed() },
                        title = { Text("Login Failed") },
                        text = { Text("Incorrect email or password. Please try again.") },
                        confirmButton = {
                            Button(
                                onClick = { loginViewModel.dismissLoginFailed() },
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                enabled = loginViewModel.allValidationsPassed.value,
                elevation = ButtonDefaults.elevation(4.dp),
            ) {
                Text(
                    text = "Login",
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
                Text("Don't have an account? ",
                    color = Color.White
                )
                Text(
                    text = "Sign Up",
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.SignUpScreen.toString())
                    }
                )
            }
        }
    }
}
