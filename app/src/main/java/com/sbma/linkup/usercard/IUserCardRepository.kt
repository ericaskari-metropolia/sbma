package com.sbma.linkup.usercard

import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of the item from a given data source.
 */
interface IUserCardRepository {
    fun getUserItemsStream(userId: UUID): Flow<List<UserCard>>
    fun getItemStream(id: UUID): Flow<UserCard?>
    suspend fun insertItem(item: UserCard)
    suspend fun deleteItem(item: UserCard)
    suspend fun updateItem(item: UserCard)
}
