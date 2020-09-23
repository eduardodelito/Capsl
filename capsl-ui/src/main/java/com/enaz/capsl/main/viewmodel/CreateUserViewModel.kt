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

class CreateUserViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val createUserFormState = MediatorLiveData<CreateUserFormState>()
    internal fun getCreateUserLiveData(): LiveData<CreateUserFormState> = createUserFormState

    fun onRegister(firstName: String, lastName: String, userName: String, password: String) {
        // can be launched in a separate asynchronous job
        launch {
            register(firstName, lastName, userName, password)
        }
    }

    private suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        password: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                if (loginRepository.isUserExist(userName)) {
                    createUserFormState.postValue(
                        RegisterResult(
                            success = RegisteredInUserView(
                                firstName,
                                lastName,
                                userName,
                                password,
                                false
                            )
                        )
                    )
                } else {
                    loginRepository.insertUser(
                        User(
                            firstName,
                            lastName,
                            userName,
                            password
                        ).modelToUserEntity()
                    )
                    createUserFormState.postValue(
                        RegisterResult(
                            success = RegisteredInUserView(
                                firstName,
                                lastName,
                                userName,
                                password,
                                true
                            )
                        )
                    )
                }
            } catch (e: IOException) {
                createUserFormState.postValue(RegisterResult(error = R.string.registration_failed))
            }
        }
    }

    fun createUserDataChanged(
        firstName: String,
        lastName: String,
        username: String,
        password: String
    ) {
        if (!isValueValid(firstName)) {
            createUserFormState.postValue(CreateUserForm(firstNameError = R.string.invalid_first_name))
        } else if (!isValueValid(lastName)) {
            createUserFormState.postValue(CreateUserForm(lastNameError = R.string.invalid_last_name))
        } else if (!isValueValid(username)) {
            createUserFormState.postValue(CreateUserForm(usernameError = R.string.invalid_username))
        } else if (!isValueValid(password)) {
            createUserFormState.postValue(CreateUserForm(passwordError = R.string.invalid_password))
        } else {
            createUserFormState.postValue(CreateUserForm(isDataValid = true))
        }
    }

    // A placeholder value validation check
    private fun isValueValid(value: String): Boolean {
        return value.length > 5
    }
}
