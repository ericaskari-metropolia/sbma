package com.sbma.linkup.application.data

import android.content.Context
import com.sbma.linkup.datasource.AppDatabase
import com.sbma.linkup.user.IUserRepository
import com.sbma.linkup.user.UserRepository


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val userRepository: IUserRepository
}

class AppDataContainer(private val context: Context) :
    AppContainer {

    override val userRepository: IUserRepository by lazy {
        UserRepository(AppDatabase.getInstance(context).userDao())
    }
}