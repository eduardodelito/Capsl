package com.enaz.capsl.db.di

import android.app.Application
import androidx.room.Room
import com.enaz.capsl.db.CapslDB
import com.enaz.capsl.db.dao.UserDao
import com.enaz.capsl.db.repository.LoginRepository
import com.enaz.capsl.db.repository.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by eduardo.delito on 9/23/20.
 */
@Module
class DBModule(private var application: Application) {
    private var capslDB: CapslDB

    init {
        synchronized(this) {
            val instance = Room.databaseBuilder(
                application,
                CapslDB::class.java,
                CapslDB.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
            capslDB = instance
            instance
        }
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): CapslDB {
        return capslDB
    }

    @Singleton
    @Provides
    fun providesUserDao(capslDB: CapslDB): UserDao {
        return capslDB.userDao()
    }

    @Singleton
    @Provides
    fun providesLoginRepository(userDao: UserDao): LoginRepository = LoginRepositoryImpl(userDao)
}
