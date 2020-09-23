package com.enaz.capsl.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.enaz.capsl.common.viewmodel.BaseViewModel
import com.enaz.capsl.db.repository.LoginRepository
import com.enaz.capsl.main.R
import com.enaz.capsl.main.mapper.modelToUserEntity
import com.enaz.capsl.main.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val loginFormState = MediatorLiveData<LoginFormState>()
    internal fun getLoginLiveData(): LiveData<LoginFormState> = loginFormState


    fun onLogin(userName: String, password: String) {
        // can be launched in a separate asynchronous job
        launch {
            login(userName, password)
        }
    }

    /**
     * Suspend method to validate username and password into database,
     * if exist continue to login if not return error message.
     * @param userName
     * @param password
     */
    private suspend fun login(userName: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                if (loginRepository.isUsernamePasswordValid(userName, password)) {
                    loginFormState.postValue(
                        LoginResult(
                            success = LoggedInUserView(
                                userName,
                                password,
                                true
                            )
                        )
                    )
                } else {
                    loginFormState.postValue(
                        LoginResult(
                            success = LoggedInUserView(
                                userName,
                                password,
                                false
                            )
                        )
                    )
                }
            } catch (e: IOException) {
                loginFormState.postValue(LoginResult(error = R.string.login_failed))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isValueValid(username)) {
            loginFormState.postValue(LoginForm(usernameError = R.string.invalid_username))
        } else if (!isValueValid(password)) {
            loginFormState.postValue(LoginForm(passwordError = R.string.invalid_password))
        } else {
            loginFormState.postValue(LoginForm(isDataValid = true))
        }
    }

    // A placeholder value validation check
    private fun isValueValid(value: String): Boolean {
        return value.length > 5
    }

    fun insertUser(username: String, password: String) {
        launch {
            insertUserToDB(User("", "", username, password))
        }
    }

    private suspend fun insertUserToDB(user: User) {
        withContext(Dispatchers.IO) {
            try {
                loginRepository.insertUser(user.modelToUserEntity())
                loginFormState.postValue(
                    LoginResult(
                        success = LoggedInUserView(
                            user.userName,
                            user.password,
                            true
                        )
                    )
                )
            } catch (e: Exception) {
                loginFormState.postValue(LoginResult(error = R.string.insert_invalid))
            }
        }
    }
}
