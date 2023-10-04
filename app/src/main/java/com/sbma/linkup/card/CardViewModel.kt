package com.sbma.linkup.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbma.linkup.api.ApiService
import com.sbma.linkup.api.apimodels.NewCardRequest
import com.sbma.linkup.datasource.DataStore
import com.sbma.linkup.presentation.screens.CreateCardData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class CardViewModel(
    private val repository: ICardRepository,
    private val dataStore: DataStore,
    private val apiService: ApiService
) : ViewModel() {
    var phoneNumber: String = ""
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
    suspend fun insertItem(item: Card) = repository.insertItem(item)

    suspend fun saveItem(createCardData: CreateCardData) {
        // Example code of how Api works.
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.createNewCard(
                    authorization,
                    NewCardRequest(createCardData.title, createCardData.value)
                )
                    .onSuccess { response ->
                        println("create new Card")
                        println(response)
                        insertItem(
                            Card(
                                UUID.fromString(response.id),
                                UUID.fromString(response.ownerId),
                                response.title,
                                response.value
                            )
                        )
                    }.onFailure {
                        println(it)
                    }
            }
        }
    }

    suspend fun saveItems(items: List<CreateCardData>) {
        viewModelScope.launch {
            items.forEach {
                saveItem(it)
            }
        }
    }


}