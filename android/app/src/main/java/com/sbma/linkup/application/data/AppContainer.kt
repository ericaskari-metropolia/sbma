package com.sbma.linkup.application.data

import android.content.Context
import com.sbma.linkup.datasource.AppDatabase
import com.sbma.linkup.user.IUserRepository
import com.sbma.linkup.user.UserRepository
import com.sbma.linkup.usercard.IUserCardRepository
import com.sbma.linkup.usercard.UserCardRepository
import com.sbma.linkup.userconnection.IUserConnectionRepository
import com.sbma.linkup.userconnection.UserConnectionRepository


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val userRepository: IUserRepository
    val userCardRepository: IUserCardRepository
    val userConnectionRepository: IUserConnectionRepository
}

class AppDataContainer(private val context: Context) :
    AppContainer {

    override val userRepository: IUserRepository by lazy {
        UserRepository(AppDatabase.getInstance(context).userDao())
    }
    override val userCardRepository: IUserCardRepository by lazy {
        UserCardRepository(AppDatabase.getInstance(context).userCardDao())
    }
    override val userConnectionRepository: IUserConnectionRepository by lazy {
        UserConnectionRepository(AppDatabase.getInstance(context).userConnectionDao())
    }
}