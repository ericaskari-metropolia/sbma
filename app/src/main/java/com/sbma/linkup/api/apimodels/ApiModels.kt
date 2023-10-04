package com.sbma.linkup.api.apimodels

import com.sbma.linkup.card.Card
import com.sbma.linkup.user.User
import com.sbma.linkup.userconnection.UserConnection
import java.util.UUID

data class ApiUser(
    val id: String,
    val email: String,
    val name: String,
    val picture: String?,
    val cards: List<ApiCard>?,
    val connections: List<ApiConnection>?,
    val connectedUsers: List<ApiConnection>?,
    val shares: List<ApiShare>?
)
fun ApiUser.toUser(): User = User(UUID.fromString(id), name, "", picture)


data class ApiCard(
    val id: String,
    val title: String,
    val value: String,
    val ownerId: String,
    val owner: ApiUser?,
    val connectionCards: List<ApiConnectionCard>?,
    val shareCards: List<ApiShareCard>?
)
fun ApiCard.toConnection(): Card = Card(UUID.fromString(id), UUID.fromString(ownerId), title, value)
fun List<ApiCard>.toConnectionList(): List<Card> = this.map { it.toConnection() }

data class ApiConnection(
    val id: String,
    val userId: String,
    val user: ApiUser?,
    val connectedUserId: String,
    val connectedUser: ApiUser?,
    val connectionCards: List<ApiConnectionCard>?
)
fun ApiConnection.toConnection(): UserConnection = UserConnection(UUID.fromString(id), UUID.fromString(userId), UUID.fromString(connectedUserId))
fun List<ApiConnection>.toConnectionList(): List<UserConnection> = this.map { it.toConnection() }

data class ApiConnectionCard(
    val id: String,
    val cardId: String,
    val card: ApiCard?,
    val connectionId: String,
    val connection: ApiConnection?
)

data class ApiShare(
    val id: String,
    val userId: String,
    val user: ApiUser?,
    val cards: List<ApiShareCard>?,
    val tags: List<ApiTag>?
)

data class ApiShareCard(
    val id: String,
    val shareId: String,
    val share: ApiShare?,
    val cardId: String,
    val card: ApiCard?
)

data class ApiTag(
    val id: String,
    val shareId: String,
    val share: ApiShare?,
    val tagId: String
)

data class AssignTagRequest (
    val shareId: String,
    val tagId: String
)


data class NewCardRequest(
    val title: String,
    val value: String
)