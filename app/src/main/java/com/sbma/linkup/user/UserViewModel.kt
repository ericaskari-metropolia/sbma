package com.sbma.linkup.user

import androidx.lifecycle.ViewModel
import com.sbma.linkup.datasource.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class UserViewModel(
    private val repository: IUserRepository,
    private val dataStore: DataStore
) : ViewModel() {

    val loggedInUserId = dataStore.getUserId
    suspend fun setLoggedInUserId(userId: UUID) = dataStore.setUserId(userId)

    val allItemsStream = repository.getAllItemsStream()
    fun getItemStream(id: UUID) = repository.getItemStream(id)

    val getLoggedInUserProfile: Flow<User?> = dataStore.getUserId.combine(repository.getAllItemsStream()) { userId, users ->
        userId?.let { id -> users.find { it.id == id } }
    }

    suspend fun insertItem(user: User) = repository.insertItem(user)
}