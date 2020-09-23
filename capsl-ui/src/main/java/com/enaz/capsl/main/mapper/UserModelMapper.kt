package com.enaz.capsl.main.mapper

import com.enaz.capsl.db.entity.UserEntity
import com.enaz.capsl.main.model.User

/**
 * Created by eduardo.delito on 9/23/20.
 */
fun User.modelToUserEntity() : UserEntity {
    return UserEntity(
        id = 0,
        firstName = firstName,
        lastName = lastName,
        userName = userName,
        password = password
    )
}
