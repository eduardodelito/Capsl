package com.enaz.capsl.main.di

import androidx.lifecycle.ViewModel
import com.enaz.capsl.common.viewmodel.ViewModelKey
import com.enaz.capsl.main.viewmodel.*
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by eduardo.delito on 9/21/20.
 */
@Module
class UIViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(LiveStreamViewModel::class)
    fun provideLiveStreamViewModel(): ViewModel = LiveStreamViewModel()

    @Provides
    @IntoMap
    @ViewModelKey(UsersViewModel::class)
    fun provideUsersViewModel(): ViewModel = UsersViewModel()

    @Provides
    @IntoMap
    @ViewModelKey(SupportViewModel::class)
    fun provideSupportViewModel(): ViewModel = SupportViewModel()

    @Provides
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun provideSettingsViewModel(): ViewModel = SettingsViewModel()

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(): ViewModel = MainViewModel()

    @Provides
    @IntoMap
    @ViewModelKey(RoleViewModel::class)
    fun provideRoleViewModel(): ViewModel = RoleViewModel()
}
