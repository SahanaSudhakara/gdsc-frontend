package com.plcoding.composegooglesignincleanarchitecture.presentation.sign_in.register

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)
