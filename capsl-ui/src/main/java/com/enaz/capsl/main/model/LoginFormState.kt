package com.enaz.capsl.main.model

/**
 * Data validation state of the login form.
 */
sealed class LoginFormState
data class LoginForm(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
) : LoginFormState()

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
) : LoginFormState()
