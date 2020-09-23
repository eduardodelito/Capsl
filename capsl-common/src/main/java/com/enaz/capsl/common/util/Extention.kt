package com.enaz.capsl.common.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Created by eduardo.delito on 9/23/20.
 */
/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

/**
 * Method to simplified how to set an Observer by just passing the [body] to be executed inside the observer.
 */
inline fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, crossinline body: (T?) -> Unit) =
    liveData.observe(this, Observer<T?> { t -> body(t) })

fun <T : Any, L : LiveData<T>> LifecycleOwner.unObserve(liveData: L) = liveData.removeObservers(this)
/**
 * We need to restart the observer because it stays observing until the LifecycleOwner is destroyed.
 * In the case of Fragments, they are not destroyed when the fragment is detached/reattached, and a new
 * observer could be added every time the Fragment is shown and onActivityCreated() is executed.
 * By doing this, we make sure we only have ONE observer at a time.
 * Refer to: https://medium.com/@BladeCoder/architecture-components-pitfalls-part-1-9300dd969808
 */
inline fun <T : Any, L : LiveData<T>> LifecycleOwner.reObserve(liveData: L, crossinline body: (T?) -> Unit) {
    unObserve(liveData)
    observe(liveData, body)
}
