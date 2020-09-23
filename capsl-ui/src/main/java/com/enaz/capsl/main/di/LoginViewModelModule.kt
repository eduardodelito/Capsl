package com.enaz.capsl.main.di

import androidx.lifecycle.ViewModel
import com.enaz.capsl.common.viewmodel.ViewModelKey
import com.enaz.capsl.db.repository.LoginRepository
import com.enaz.capsl.main.viewmodel.CreateUserViewModel
import com.enaz.capsl.main.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by eduardo.delito on 9/23/20.
 */
@Module
class LoginViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideLoginViewModel(loginRepository: LoginRepository): ViewModel = LoginViewModel(loginRepository)

    @Provides
    @IntoMap
    @ViewModelKey(CreateUserViewModel::class)
    fun provideCreateUserViewModel(loginRepository: LoginRepository): ViewModel = CreateUserViewModel(loginRepository)
}
