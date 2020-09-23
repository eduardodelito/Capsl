package com.enaz.capsl.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enaz.capsl.db.dao.UserDao
import com.enaz.capsl.db.entity.UserEntity

/**
 * Created by eduardo.delito on 9/23/20.
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CapslDB : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "CapslDB"
    }
}
