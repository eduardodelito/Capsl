package com.enaz.capsl.main.viewmodel

import com.enaz.capsl.common.viewmodel.BaseViewModel
import com.enaz.capsl.db.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CreateUserViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}