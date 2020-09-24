package com.enaz.capsl.main.di.component

import android.app.Application
import com.enaz.capsl.common.di.CommonModule
import com.enaz.capsl.db.di.DBModule
import com.enaz.capsl.main.di.module.ActivityBindingModule
import com.enaz.capsl.main.di.module.CapslModule
import com.enaz.capsl.main.di.module.ViewModelBindingModule
import com.enaz.capsl.main.ui.CapslApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by eduardo.delito on 9/21/20.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBindingModule::class,
    ViewModelBindingModule::class,
    CapslModule::class
])
interface CapslComponent : AndroidInjector<CapslApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun database(dbModule: DBModule): Builder
        fun common(commonModule: CommonModule): Builder
        fun build(): CapslComponent
    }
}
