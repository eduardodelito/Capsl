package com.enaz.capsl.common.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
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
inline fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(
    liveData: L,
    crossinline body: (T?) -> Unit
) =
    liveData.observe(this, Observer<T?> { t -> body(t) })

fun <T : Any, L : LiveData<T>> LifecycleOwner.unObserve(liveData: L) = liveData.removeObservers(this)
/**
 * We need to restart the observer because it stays observing until the LifecycleOwner is destroyed.
 * In the case of Fragments, they are not destroyed when the fragment is detached/reattached, and a new
 * observer could be added every time the Fragment is shown and onActivityCreated() is executed.
 * By doing this, we make sure we only have ONE observer at a time.
 * Refer to: https://medium.com/@BladeCoder/architecture-components-pitfalls-part-1-9300dd969808
 */
inline fun <T : Any, L : LiveData<T>> LifecycleOwner.reObserve(
    liveData: L,
    crossinline body: (T?) -> Unit
) {
    unObserve(liveData)
    observe(liveData, body)
}

/**
 * Hide Keyboard in the Fragment.
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/**
 * Hide Keyboard in the Activity.
 */
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * Hide keyboard method.
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun hideWindowStatusBar(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

fun getSystemStatusBarHeight(context: Context): Int {
    val id = context.resources.getIdentifier(
        "status_bar_height", "dimen", "android"
    )
    return if (id > 0) context.resources.getDimensionPixelSize(id) else id
}

fun getPreferences(context: Context, prefName: String): SharedPreferences {
    return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
}
