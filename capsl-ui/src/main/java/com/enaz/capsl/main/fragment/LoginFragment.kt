package com.enaz.capsl.main.fragment

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import com.enaz.capsl.common.fragment.BaseFragment
import com.enaz.capsl.common.util.afterTextChanged
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

class LoginFragment : BaseFragment<LoginFragmentBinding, LoginViewModel>() {

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
                        clearFields()
                        listener?.navigateToCreateUserScreen(requireView())
                    }
                }
            }
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        username.text.clear()
        password.text.clear()
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
