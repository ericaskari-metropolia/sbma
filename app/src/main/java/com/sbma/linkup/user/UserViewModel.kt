package com.sbma.linkup.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbma.linkup.api.ApiService
import com.sbma.linkup.api.apimodels.AssignTagRequest
import com.sbma.linkup.api.apimodels.toCardList
import com.sbma.linkup.api.apimodels.toConnectionCardList
import com.sbma.linkup.api.apimodels.toConnectionList
import com.sbma.linkup.api.apimodels.toUser
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.ICardRepository
import com.sbma.linkup.connection.IConnectionRepository
import com.sbma.linkup.datasource.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class UserViewModel(
    private val userRepository: IUserRepository,
    private val cardRepository: ICardRepository,
    private val userConnectionRepository: IConnectionRepository,
    private val apiService: ApiService,
    private val dataStore: DataStore,

    ) : ViewModel() {

    init {
        // Example code of how Api works.
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.getProfile(authorization)
                    .onSuccess { apiUser ->
                        val user = apiUser.toUser()

                        println("Syncing User Profile")
                        userRepository.insertItem(user)

                        println("Syncing User Cards")
                        val cards: List<Card> = (apiUser.cards ?: listOf()).toCardList()
                        cardRepository.syncUserItems(user.id, cards)

                        println("Syncing User Connections")
                        val apiConnections = apiUser.connections ?: listOf()
                        val connections = apiConnections.toConnectionList()
                        userConnectionRepository.syncUserConnections(user.id, connections)

                        println("Syncing User Reverse Connections")
                        val apiReverseConnections = apiUser.reverseConnections ?: listOf()
                        val reverseConnections = apiReverseConnections.toConnectionList()
                        userConnectionRepository.syncUserReverseConnections(user.id, reverseConnections)

                        println("Syncing Connection Cards")
                        apiConnections.forEach { apiConnection ->
                            val apiConnectionUser = apiConnection.connectedUser
                            val apiConnectionCards = apiConnection.connectionCards ?: listOf()
                            val connectionCards = apiConnectionCards.toConnectionCardList()
                            apiConnectionUser?.let {
                                println("Syncing Connection User")
                                userRepository.insertItem(it.toUser())
                            }

                            userConnectionRepository.syncConnectionCardItems(UUID.fromString(apiConnection.id), connectionCards)

                        }

                        println("Syncing Reverse Connection Cards")
                        apiReverseConnections.forEach { apiConnection ->
                            val apiConnectionCards = apiConnection.connectionCards ?: listOf()
                            val connectionCards = apiConnectionCards.toConnectionCardList()
                            userConnectionRepository.syncConnectionCardItems(UUID.fromString(apiConnection.id), connectionCards)
                        }


                    }.onFailure {
                        println(it)
                    }

            }
        }
    }

    val loggedInUserId = dataStore.getUserId
    val getAccessToken = dataStore.getAccessToken
    val getAccessTokenExpiresAt = dataStore.getAccessTokenExpiresAt
    val shareId = dataStore.getJsonToShare

    val allItemsStream = userRepository.getAllItemsStream()
    fun getItemStream(id: UUID) = userRepository.getItemStream(id)
    val responseStatus: MutableStateFlow<String?> = MutableStateFlow(null)
    val assignTagResponseStatus: MutableStateFlow<String?> = MutableStateFlow(null)


    /**
     * combines two flows together. here it combines userId and list of users and returns the user with that id.
     * we used list of user with one element to distinguish between flow not having data and not having user.
     * (null) = when it is used with collectAsState(initial = null) means that there is no data in the flow yet so we
     * cannot know user status.
     * (Empty list) = there is no user with this id or id is null
     */
    val getLoggedInUserProfile: Flow<List<User>> =
        dataStore.getUserId.combine(userRepository.getAllItemsStream()) { userId, users ->
            userId?.let { id ->
                val user = users.find { it.id == id }
                if (user == null) {
                    listOf<User>()
                } else {
                    listOf<User>(user)
                }
            } ?: listOf<User>()
        }

    suspend fun insertItem(user: User) = userRepository.insertItem(user)

    suspend fun saveLoginData(accessToken: String, expiresAt: String, userId: UUID) =
        dataStore.saveLoginData(accessToken = accessToken, expiresAt = expiresAt, userId = userId)

    suspend fun deleteLoginData() = dataStore.deleteLoginData()

    suspend fun shareCards(cardIDs: List<String>) {
        // Example code of how Api works.
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.share(
                    authorization,
                    cardIDs
                )
                    .onSuccess { response ->
                        println("share new Card")
                        println(response)
                        dataStore.setJsonToShare(response.id)
                    }.onFailure {
                        println(it)
                    }
            }
        }
    }

    suspend fun assignTag(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                dataStore.getJsonToShare.first()?.let { shareId ->
                    apiService.assignTag(
                        authorization,
                        AssignTagRequest(shareId, id)
                    )
                        .onSuccess { response ->
                            println("assignTag")
                            println(response)
                            assignTagResponseStatus.value =
                                "Congratulations! You successfully added your details on the NFC-enabled card."
                        }.onFailure {
                            println(it)
                            assignTagResponseStatus.value = "Oh no! Something went wrong."
                        }
                }
            }
        }
    }

    suspend fun scanTag(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.scanTag(
                    authorization,
                    id
                )
                    .onSuccess { response ->
                        println("receiveTag")
                        println(response)
                        responseStatus.value =
                            "Congratulations! Contact has been successfully added."

                    }.onFailure {
                        println(it)
                        responseStatus.value = "Oh no! Contact retrieval has failed"
                    }
            }
        }
    }

    fun observeNfcStatus(): StateFlow<String?> {
        return responseStatus.asStateFlow()
    }

    fun assignNfcStatus(): StateFlow<String?> {
        return assignTagResponseStatus.asStateFlow()
    }
}



