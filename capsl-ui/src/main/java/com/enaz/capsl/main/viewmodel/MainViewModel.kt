package com.enaz.capsl.main.viewmodel

import android.animation.Animator
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.enaz.capsl.common.viewmodel.BaseViewModel
import com.enaz.capsl.main.model.MainFormState
import com.enaz.capsl.main.model.MainLogoForm
import com.enaz.capsl.main.model.TextWatcherForm

class MainViewModel : BaseViewModel() {

    private val mainFormState = MediatorLiveData<MainFormState>()
    internal fun getMainLiveData(): LiveData<MainFormState> = mainFormState

    fun topicEditTextChanged(text: String) {
        mainFormState.postValue(TextWatcherForm(isEnabled = text.isNotEmpty()))
    }

    val mLogoAnimListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {
            // Do nothing
        }

        override fun onAnimationEnd(animator: Animator) {
            mainFormState.postValue(MainLogoForm(mainLogoVisible = View.VISIBLE))
        }

        override fun onAnimationCancel(animator: Animator) {
            mainFormState.postValue(MainLogoForm(mainLogoVisible = View.VISIBLE))
        }

        override fun onAnimationRepeat(animator: Animator) {
            // Do nothing
        }
    }
}