package com.sbma.linkup.usercard

import androidx.lifecycle.ViewModel
import com.sbma.linkup.datasource.DataStore
import java.util.UUID

class UserCardViewModel(
    private val repository: IUserCardRepository,
    private val dataStore: DataStore,
) : ViewModel() {
    /**
     * Flow of user cards json string that user wants to share.
     */
    val jsonToShare = dataStore.getJsonToShare

    /**
     * Setter for logged in user cards json string
     */
    suspend fun setJsonToShare(json: String) = dataStore.setJsonToShare(json)

    fun allItemsStream(userId: UUID) = repository.getUserItemsStream(userId)
    fun getItemStream(id: UUID) = repository.getItemStream(id)
    suspend fun insertItem(item: UserCard) = repository.insertItem(item)

}