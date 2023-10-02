package com.sbma.linkup.intents.login

import com.sbma.linkup.user.User
import java.util.UUID

data class LoginUserResponse(
    val id: String,
    val name: String,
    val description: String,
    val googleProfile: LoginGoogleProfileResponse,

    ) {

    fun toUser(): User {
        return User(UUID.fromString(id), name ?: "", description ?: "", picture = googleProfile.picture)
    }
}
