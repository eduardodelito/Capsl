package com.enaz.capsl.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.enaz.capsl.common.viewmodel.BaseViewModel
import com.enaz.capsl.main.model.MainForm
import com.enaz.capsl.main.model.MainFormState

class MainViewModel : BaseViewModel() {

    private val mainFormState = MediatorLiveData<MainFormState>()
    internal fun getMainLiveData(): LiveData<MainFormState> = mainFormState

    fun topicEditTextChanged(text: String) {
        mainFormState.postValue(MainForm(isEnabled = text.isNotEmpty()))
    }
}