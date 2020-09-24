package com.enaz.capsl.main.model

/**
 * Created by eduardo.delito on 9/24/20.
 */
sealed class MainFormState
data class MainForm(val isEnabled: Boolean = false) : MainFormState()