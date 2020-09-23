package com.enaz.capsl.common.viewmodel

import androidx.lifecycle.ViewModel

/**
 * ViewModel base class that will handle shared events/transactions to all ViewModels.
 *
 * Created by eduardo.delito on 9/21/20.
 */
abstract class BaseViewModel : ViewModel() {

    open fun onStart() {
    }

    open fun onResume() {
    }

    open fun onPause() {
    }

    open fun onStop() {
    }

    open fun onDestroy() {
    }
}
