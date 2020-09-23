package com.enaz.capsl.main.di.module

import com.enaz.capsl.main.di.LoginBindingModule
import com.enaz.capsl.main.di.UIBindingModule
import com.enaz.capsl.main.ui.LoginActivity
import com.enaz.capsl.main.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by eduardo.delito on 9/21/20.
 */
@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [
        LoginBindingModule::class
    ])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [
        UIBindingModule::class
    ])
    abstract fun bindMainActivity(): MainActivity
}
