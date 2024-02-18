package com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.login

data class LoginUIState(
    var email  :String = "",
    var password  :String = "",

    var emailError :Boolean = false,
    var passwordError : Boolean = false

)