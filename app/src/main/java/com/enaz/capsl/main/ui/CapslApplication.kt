package com.enaz.capsl.main.ui

import com.enaz.capsl.common.di.CommonModule
import com.enaz.capsl.db.di.DBModule
import com.enaz.capsl.main.di.component.DaggerCapslComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.agora.rtc.RtcEngine

/**
 * Created by eduardo.delito on 9/21/20.
 */
class CapslApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerCapslComponent.builder()
            .application(this)
            .database(DBModule(this))
            .common(CommonModule(applicationContext, this))
            .build()
    }

    override fun onTerminate() {
        super.onTerminate()
        RtcEngine.destroy()
    }
}