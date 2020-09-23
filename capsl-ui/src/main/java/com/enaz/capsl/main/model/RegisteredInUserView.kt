package com.enaz.capsl.main.model

/**
 * User details post authentication that is exposed to the UI
 */
data class RegisteredInUserView(
    val lastName: String,
    val firstName: String,
    val userName: String,
    val password: String,
    val isRegister: Boolean
)