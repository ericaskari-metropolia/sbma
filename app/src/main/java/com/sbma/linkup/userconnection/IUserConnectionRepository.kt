package com.sbma.linkup.userconnection

import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of the item from a given data source.
 */
interface IUserConnectionRepository {
    fun getUserItemsStream(userId: UUID): Flow<List<UserConnection>>
    fun getItemStream(id: String): Flow<UserConnection?>
    suspend fun insertItem(item: UserConnection)
    suspend fun deleteItem(item: UserConnection)

    suspend fun updateItem(item: UserConnection)
}
