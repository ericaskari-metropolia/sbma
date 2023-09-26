package com.sbma.linkup.usercard

import java.util.UUID

class UserCardRepository(private val dao: UserCardDao) : IUserCardRepository {
    override suspend fun insertItem(item: UserCard) = dao.insert(item)
    override suspend fun deleteItem(item: UserCard) = dao.delete(item)
    override suspend fun updateItem(item: UserCard) = dao.update(item)
    override fun getUserItemsStream(userId: UUID) = dao.getUserItems(userId)
    override fun getItemStream(id: UUID) = dao.getItem(id)

}