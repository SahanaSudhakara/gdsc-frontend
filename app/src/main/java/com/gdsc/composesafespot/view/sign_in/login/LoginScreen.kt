package com.gdsc.composesafespot.view.sign_in.login

import androidx.compose.foundation.Image
import com.gdsc.composesafespot.view.components.DividerTextComponent
import com.gdsc.composesafespot.view.components.MyTextField
import com.gdsc.composesafespot.view.components.PasswordTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gdsc.composesafespot.view.navigation.Screen
import com.gdsc.composesafespot.R
import com.gdsc.composesafespot.viewmodel.login.LoginUIEvent
import com.gdsc.composesafespot.viewmodel.login.LoginViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material.Surface

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color(0xFF3F51B5))
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection()
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                LoginSection(loginViewModel, navController)
                Spacer(modifier = Modifier.height(16.dp))
                BottomSection(navController)
            }
        }

    }
}

@Composable
private fun BottomSection(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Don't have an account? ",
            color = Color.Black
        )
        Text(
            text = "Sign Up",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate(Screen.SignUpScreen.toString())
            }
        )
    }
}

@Composable
private fun LoginSection(loginViewModel: LoginViewModel, navController: NavController) {
    MyTextField(
        labelValue = "Email",
        painterResource(id = R.drawable.email),
        onTextSelected = {
            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
        },
        errorStatus = loginViewModel.loginUIState.value.emailError
    )
    Spacer(modifier = Modifier.height(16.dp))
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
    val isLoginInProgress = loginViewModel.loginInProgress.value
    Spacer(modifier = Modifier.height(24.dp))
    if (isLoginInProgress) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Black)
        }
    } else {
        Button(
            onClick = {
                loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp),
            enabled = loginViewModel.allValidationsPassed.value,
            elevation = ButtonDefaults.elevation(4.dp),
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.button,
                color = Color.Black
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    DividerTextComponent()
}

@Composable
private fun TopSection() {

    Box(
        contentAlignment = Alignment.TopCenter
    ) {

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

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
            text = "Login",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(alignment = Alignment.BottomCenter)
        )
    }
}
