package com.sbma.linkup.userconnection

import com.sbma.linkup.user.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of the item from a given data source.
 */
interface IUserConnectionRepository {
    fun getUserItemsStream(userId: UUID): Flow<Map<UserConnection, User>>
    fun getItemStream(id: String): Flow<UserConnection?>
    suspend fun insertItem(item: UserConnection)
    suspend fun insertItem(item: List<UserConnection>)

    suspend fun deleteItem(item: UserConnection)

    suspend fun updateItem(item: UserConnection)
    suspend fun syncConnectionCardItems(connectionId: UUID, items: List<ConnectionCard>)
    suspend fun syncUserConnections(userId: UUID, items: List<UserConnection>)
    suspend fun syncUserReverseConnections(userId: UUID, items: List<UserConnection>)

}
