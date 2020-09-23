package com.enaz.capsl.main.di

import androidx.lifecycle.ViewModelProvider
import com.enaz.capsl.main.fragment.CreateUserFragment
import com.enaz.capsl.main.fragment.LoginFragment
import com.enaz.capsl.main.viewmodel.CreateUserViewModel
import com.enaz.capsl.main.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by eduardo.delito on 9/21/20.
 */
@Module
abstract class LoginBindingModule {

    @ContributesAndroidInjector(modules = [InjectLoginViewModelModule::class])
    abstract fun bindLoginFragment(): LoginFragment

    @Module
    class InjectLoginViewModelModule {
        @Provides
        internal fun provideLoginViewModel(
            factory: ViewModelProvider.Factory,
            target: LoginFragment
        ) = ViewModelProvider(target, factory).get(LoginViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectCreateUserViewModelModule::class])
    abstract fun bindCreateUserFragment(): CreateUserFragment

    @Module
    class InjectCreateUserViewModelModule {
        @Provides
        internal fun provideCreateUserViewModel(
            factory: ViewModelProvider.Factory,
            target: CreateUserFragment
        ) = ViewModelProvider(target, factory).get(CreateUserViewModel::class.java)
    }
}
