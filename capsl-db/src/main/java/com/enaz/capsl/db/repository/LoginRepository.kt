package com.enaz.capsl.db.repository

import com.enaz.capsl.db.dao.UserDao
import com.enaz.capsl.db.entity.UserEntity

/**
 * Created by eduardo.delito on 9/23/20.
 */
interface LoginRepository {
    /**
     * Method for to check account if exist.
     * @param userName
     */
    fun isUserExist(userName: String): Boolean

    /**
     * Method for validating userName and Password.
     * @param userName
     * @param password
     */
    fun isUsernamePasswordValid(userName: String, password: String): Boolean

    fun insertUser(userEntity: UserEntity)
}

class LoginRepositoryImpl(private val userDao: UserDao) : LoginRepository {

    /**
     * Method for to check account if exist.
     * @param userName
     */
    override fun isUserExist(userName: String) = userDao.isUserExist(userName)

    /**
     * Method for validating userName and Password.
     * @param userName
     * @param password
     */
    override fun isUsernamePasswordValid(userName: String, password: String) =
        userDao.isUsenamePasswordValid(userName, password)

    override fun insertUser(userEntity: UserEntity) {
        userDao.insertAccount(userEntity)
    }
}
