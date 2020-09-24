package com.enaz.capsl.main.di

import androidx.lifecycle.ViewModelProvider
import com.enaz.capsl.main.fragment.*
import com.enaz.capsl.main.viewmodel.*
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

    @ContributesAndroidInjector(modules = [InjectMainViewModelModule::class])
    abstract fun bindMainFragment(): MainFragment

    @Module
    class InjectMainViewModelModule {
        @Provides
        internal fun provideMainViewModel(
            factory: ViewModelProvider.Factory,
            target: MainFragment
        ) = ViewModelProvider(target, factory).get(MainViewModel::class.java)
    }

    @ContributesAndroidInjector(modules = [InjectRoleViewModelModule::class])
    abstract fun bindRoleFragment(): RoleFragment

    @Module
    class InjectRoleViewModelModule {
        @Provides
        internal fun provideRoleViewModel(
            factory: ViewModelProvider.Factory,
            target: RoleFragment
        ) = ViewModelProvider(target, factory).get(RoleViewModel::class.java)
    }
}
