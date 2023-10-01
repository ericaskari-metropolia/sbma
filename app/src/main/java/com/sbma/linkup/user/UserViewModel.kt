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
    val getAccessToken = dataStore.getAccessToken
    val getAccessTokenExpiresAt = dataStore.getAccessTokenExpiresAt

    val allItemsStream = repository.getAllItemsStream()
    fun getItemStream(id: UUID) = repository.getItemStream(id)

    /**
     * combines two flows together. here it combines userId and list of users and returns the user with that id.
     * we used list of user with one element to distinguish between flow not having data and not having user.
     * (null) = when it is used with collectAsState(initial = null) means that there is no data in the flow yet so we
     * cannot know user status.
     * (Empty list) = there is no user with this id or id is null
     */
    val getLoggedInUserProfile: Flow<List<User>> = dataStore.getUserId.combine(repository.getAllItemsStream()) { userId, users ->
        userId?.let { id ->
            val user = users.find { it.id == id }
            if (user == null) {
                listOf<User>()
            } else {
                listOf<User>(user)
            }
        } ?: listOf<User>()
    }

    suspend fun insertItem(user: User) = repository.insertItem(user)

    suspend fun saveLoginData(accessToken: String, expiresAt: String, userId: UUID) =
        dataStore.saveLoginData(accessToken = accessToken, expiresAt = expiresAt, userId = userId)

    suspend fun deleteLoginData() = dataStore.deleteLoginData()
}
