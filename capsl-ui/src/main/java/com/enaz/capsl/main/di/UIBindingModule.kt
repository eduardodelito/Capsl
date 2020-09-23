package com.enaz.capsl.main.di

import androidx.lifecycle.ViewModelProvider
import com.enaz.capsl.main.fragment.LiveStreamFragment
import com.enaz.capsl.main.fragment.SettingsFragment
import com.enaz.capsl.main.fragment.SupportFragment
import com.enaz.capsl.main.fragment.UsersFragment
import com.enaz.capsl.main.viewmodel.LiveStreamViewModel
import com.enaz.capsl.main.viewmodel.SettingsViewModel
import com.enaz.capsl.main.viewmodel.SupportViewModel
import com.enaz.capsl.main.viewmodel.UsersViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by eduardo.delito on 9/21/20.
 */
@Module
abstract class UIBindingModule {

    @ContributesAndroidInjector(modules = [InjectLiveStreamViewModelModule::class])
    abstract fun bindLiveStreamFragment(): LiveStreamFragment

    @Module
    class InjectLiveStreamViewModelModule {
        @Provides
        internal fun provideLiveStreamViewModel(
            factory: ViewModelProvider.Factory,
            target: LiveStreamFragment
        ) = ViewModelProvider(target, factory).get(LiveStreamViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectUsersViewModelModule::class])
    abstract fun bindUsersFragment(): UsersFragment

    @Module
    class InjectUsersViewModelModule {
        @Provides
        internal fun provideUsersViewModel(
            factory: ViewModelProvider.Factory,
            target: UsersFragment
        ) = ViewModelProvider(target, factory).get(UsersViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectSupportViewModelModule::class])
    abstract fun bindSupportFragment(): SupportFragment

    @Module
    class InjectSupportViewModelModule {
        @Provides
        internal fun provideSupportViewModel(
            factory: ViewModelProvider.Factory,
            target: SupportFragment
        ) = ViewModelProvider(target, factory).get(SupportViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectSettingsViewModelModule::class])
    abstract fun bindSettingsFragment(): SettingsFragment

    @Module
    class InjectSettingsViewModelModule {
        @Provides
        internal fun provideSettingsViewModel(
            factory: ViewModelProvider.Factory,
            target: SettingsFragment
        ) = ViewModelProvider(target, factory).get(SettingsViewModel::class.java)
    }
}
