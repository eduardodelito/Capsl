package com.enaz.capsl.main.di.module

import com.enaz.capsl.common.di.CommonModule
import com.enaz.capsl.db.di.DBModule
import dagger.Module

/**
 * Created by eduardo.delito on 9/23/20.
 */
@Module(
    includes = [
        DBModule::class,
        CommonModule::class
    ]
)
class CapslModule