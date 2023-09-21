package com.sbma.linkup.user

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface IUserRepository {
    fun getAllItemsStream(): Flow<List<User>>
    fun getItemStream(id: String): Flow<User?>
    suspend fun insertItem(item: User)
    suspend fun deleteItem(item: User)

    suspend fun updateItem(item: User)
}
