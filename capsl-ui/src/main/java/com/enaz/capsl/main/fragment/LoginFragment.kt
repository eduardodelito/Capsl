package com.enaz.capsl.main.fragment

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.enaz.capsl.common.fragment.PreMainBaseFragment
import com.enaz.capsl.common.util.afterTextChanged
import com.enaz.capsl.common.util.hideKeyboard
import com.enaz.capsl.common.util.reObserve
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.LoginFragmentBinding
import com.enaz.capsl.main.model.LoginForm
import com.enaz.capsl.main.model.LoginFormState
import com.enaz.capsl.main.model.LoginResult
import com.enaz.capsl.main.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject

class LoginFragment : PreMainBaseFragment<LoginFragmentBinding, LoginViewModel>() {

    private var listener: LoginFragmentListener? = null

    companion object {
        fun newInstance() = LoginFragment()
    }

    @Inject
    override lateinit var viewModel: LoginViewModel

    override fun createLayout() = R.layout.login_fragment

    override fun getBindingVariable() = BR.loginViewModel

    override fun initViews() {
        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.onLogin(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewModel.onLogin(username.text.toString(), password.text.toString())
            }
        }
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getLoginLiveData(), ::onLoginStateChanged)
        }
    }

    private fun onLoginStateChanged(state: LoginFormState?) {
        when (state) {
            is LoginForm -> {
                login.isEnabled = state.isDataValid

                if (state.usernameError != null) {
                    username.error = getString(state.usernameError)
                }

                if (state.passwordError != null) {
                    password.error = getString(state.passwordError)
                }
            }

            is LoginResult -> {
                loading.visibility = View.GONE

                if (state.error != null) {
                    showLoginFailed(state.error)
                }
                if (state.success != null) {
                    if (state.success.isRegister) {
                        listener?.navigateToMainScreen()
                    } else {
                        registrationDialog(state.success.userName)
                    }
                    clearFields()
                    hideKeyboard()
                }
            }
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        username.text?.clear()
        password.text?.clear()
    }

    private fun registrationDialog(userName: String) {
        // build alert dialog
        val dialogBuilder = context?.let { AlertDialog.Builder(it) }

        // set message of alert dialog
        dialogBuilder?.setMessage("$userName not yet registered. Do you want to continue?")
            // if the dialog is cancelable
            ?.setCancelable(false)
            // positive button text and action
            ?.setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, _ ->
                listener?.navigateToCreateUserScreen(requireView())
                dialog.dismiss()
            })
            // negative button text and action
            ?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder?.create()
        // show alert dialog
        alert?.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface LoginFragmentListener {
        fun navigateToMainScreen()

        fun navigateToCreateUserScreen(view: View)
    }
}
