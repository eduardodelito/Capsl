package com.enaz.capsl.db.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userName: String,
    val firstName: String,
    val lastName: String
)