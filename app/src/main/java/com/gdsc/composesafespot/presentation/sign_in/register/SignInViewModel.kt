package com.gdsc.composesafespot.presentation.sign_in.register

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gdsc.composesafespot.presentation.navigation.NavigationAppRouter
import com.gdsc.composesafespot.presentation.navigation.Screen
import com.gdsc.composesafespot.presentation.sign_in.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SignInViewModel : ViewModel() {
    private val _navigateToHome = MutableStateFlow(false)

    var navigateToHome: StateFlow<Boolean> = _navigateToHome

    var registrationUIState = mutableStateOf(RegistrationUIState())

    var allValidationsPassed = mutableStateOf(false)


    fun onEvent(event: SignupUIEvent) {
        when (event) {
            is SignupUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
                printState()
            }

            is SignupUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
                printState()
            }

            is SignupUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
                printState()

            }


            is SignupUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
                printState()

            }

            is SignupUIEvent.RegisterButtonClicked -> {
                signUp()
            }
        }
        validateDataWithRules()
    }


    private fun signUp() {
        Log.d(TAG, "Inside_signUp")
        printState()
        validateDataWithRules()
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password
        )

    }
    private fun createUserInFirebase(email: String, password: String) {
FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener()
{
    Log.d(TAG, "Successful")
    //signUpInProgress.value = false
    if (it.isSuccessful) {
        NavigationAppRouter.navigateTo(Screen.HomeScreen)
        _navigateToHome.value = true
      //  signUpDone.value=true
    }}.addOnFailureListener(){
    Log.d(TAG, "Error in FailureListener")
    Log.d(TAG, "Error in FailureListener: ${it.message}")
}
    }


    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(
            fName = registrationUIState.value.firstName
        )

        val lNameResult = Validator.validateLastName(
            lName = registrationUIState.value.lastName
        )

        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )
        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "fNameResult= $fNameResult")
        Log.d(TAG, "lNameResult= $lNameResult")
        Log.d(TAG, "emailResult= $emailResult")
        Log.d(TAG, "passwordResult= $passwordResult")

        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )


        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status

    }
    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registrationUIState.value.toString())
    }
}