package com.sbma.linkup.userconnection

import java.util.UUID

class UserConnectionRepository(private val dao: UserConnectionDao) : IUserConnectionRepository {
    override suspend fun insertItem(item: UserConnection) = dao.insert(item)
    override suspend fun deleteItem(item: UserConnection) = dao.delete(item)
    override suspend fun updateItem(item: UserConnection) = dao.update(item)
    override fun getUserItemsStream(userId: UUID) = dao.getUserItems(userId)
    override fun getItemStream(id: String) = dao.getItem(id)

}