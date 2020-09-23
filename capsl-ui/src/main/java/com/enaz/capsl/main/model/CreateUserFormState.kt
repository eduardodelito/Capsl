package com.enaz.capsl.main.model

/**
 * Created by eduardo.delito on 9/23/20.
 */
sealed class CreateUserFormState
data class CreateUserForm(
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
) : CreateUserFormState()

data class RegisterResult(
    val success: RegisteredInUserView? = null,
    val error: Int? = null
) : CreateUserFormState()