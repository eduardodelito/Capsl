package com.enaz.capsl.main.di.module

import androidx.lifecycle.ViewModelProvider
import com.enaz.capsl.common.viewmodel.ViewModelFactory
import com.enaz.capsl.main.di.LoginViewModelModule
import com.enaz.capsl.main.di.UIViewModelModule
import dagger.Binds
import dagger.Module

/**
 * Created by eduardo.delito on 9/21/20.
 */
@Module(
    includes = [
        UIViewModelModule::class,
        LoginViewModelModule::class
    ]
)
abstract class ViewModelBindingModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
