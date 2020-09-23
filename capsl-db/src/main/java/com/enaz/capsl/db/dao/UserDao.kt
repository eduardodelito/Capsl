package com.enaz.capsl.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enaz.capsl.db.entity.UserEntity
import com.enaz.capsl.db.model.LoggedInUser

/**
 * Created by eduardo.delito on 9/23/20.
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(userEntity: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE userName = :userName LIMIT 1")
    fun isUserExist(userName: String) : Boolean

    @Query("SELECT * FROM UserEntity WHERE userName = :userName LIMIT 1")
    fun loggedInUser(userName: String) : LoggedInUser

    @Query("SELECT * FROM UserEntity WHERE userName = :userName AND password = :password LIMIT 1")
    fun isUsenamePasswordValid(userName: String, password: String) : Boolean

    @Query("DELETE FROM UserEntity")
    fun deleteAccount()
}
