package com.enaz.capsl.main.fragment

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import com.enaz.capsl.common.fragment.BaseFragment
import com.enaz.capsl.common.util.afterTextChanged
import com.enaz.capsl.common.util.reObserve
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.CreateUserFragmentBinding
import com.enaz.capsl.main.model.CreateUserForm
import com.enaz.capsl.main.model.CreateUserFormState
import com.enaz.capsl.main.model.RegisterResult
import com.enaz.capsl.main.viewmodel.CreateUserViewModel
import kotlinx.android.synthetic.main.create_user_fragment.*
import javax.inject.Inject

class CreateUserFragment : BaseFragment<CreateUserFragmentBinding, CreateUserViewModel>() {

    private var listener: CreateUserFragmentListener? = null

    companion object {
        fun newInstance() = CreateUserFragment()
    }

    @Inject
    override lateinit var viewModel: CreateUserViewModel

    override fun createLayout() = R.layout.create_user_fragment

    override fun getBindingVariable() = BR.createUserViewModel

    override fun initViews() {
        afterTextChangedView(register_firstName)
        afterTextChangedView(register_lastName)
        afterTextChangedView(register_username)

        register_password.apply {
            afterTextChangedView(this)

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.onRegister(
                            register_firstName.text.toString(),
                            register_lastName.text.toString(),
                            register_username.text.toString(),
                            register_password.text.toString()
                        )
                }
                false
            }

            register.setOnClickListener {
                register_loading.visibility = View.VISIBLE
                viewModel.onRegister(
                    register_firstName.text.toString(),
                    register_lastName.text.toString(),
                    register_username.text.toString(),
                    register_password.text.toString()
                )
            }
        }
    }

    private fun afterTextChangedView(view: AppCompatEditText) {
        view.afterTextChanged {
            viewModel.createUserDataChanged(
                register_firstName.text.toString(),
                register_lastName.text.toString(),
                register_username.text.toString(),
                register_password.text.toString()
            )
        }
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getCreateUserLiveData(), ::onRegisterStateChanged)
        }
    }

    private fun onRegisterStateChanged(state: CreateUserFormState?) {
        when(state) {
            is CreateUserForm -> {
                register.isEnabled = state.isDataValid

                if (state.firstNameError != null) {
                    register_firstName.error = getString(state.firstNameError)
                }

                if (state.lastNameError != null) {
                    register_lastName.error = getString(state.lastNameError)
                }

                if (state.usernameError != null) {
                    register_username.error = getString(state.usernameError)
                }

                if (state.passwordError != null) {
                    register_password.error = getString(state.passwordError)
                }
            }

            is RegisterResult -> {
                register_loading.visibility = View.GONE

                if (state.error != null) {
                    showRegisterFailed(state.error)
                }
                if (state.success != null) {
                    if (state.success.isRegister) {
                        listener?.navigateToMainScreen()
                    } else {
                        listener?.navigateToMainScreen()
                    }
                    clearFields()
                }
            }
        }
    }

    private fun clearFields() {
        register_firstName.text?.clear()
        register_lastName.text?.clear()
        register_username.text?.clear()
        register_password.text?.clear()
    }

    private fun showRegisterFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreateUserFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface CreateUserFragmentListener {
        fun navigateToMainScreen()
    }
}
