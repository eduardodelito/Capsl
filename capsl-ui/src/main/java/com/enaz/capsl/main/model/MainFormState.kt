package com.enaz.capsl.main.model

/**
 * Created by eduardo.delito on 9/24/20.
 */
sealed class MainFormState
data class TextWatcherForm(val isEnabled: Boolean = false) :
    MainFormState()

data class MainLogoForm(val mainLogoVisible: Int = 0) :
    MainFormState()