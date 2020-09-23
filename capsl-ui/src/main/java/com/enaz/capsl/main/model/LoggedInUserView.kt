package com.enaz.capsl.main.model

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val userName: String,
    val password: String,
    val isRegister: Boolean
)