package com.sbma.linkup.userconnection

import java.util.UUID

class UserConnectionRepository(private val dao: UserConnectionDao, private val connectionCardDao: ConnectionCardDao) : IUserConnectionRepository {
    override suspend fun insertItem(item: UserConnection) = dao.insert(item)
    override suspend fun insertItem(item: List<UserConnection>) = dao.insert(item)
    override suspend fun syncUserConnections(userId: UUID, items: List<UserConnection>) = dao.syncUserConnections(userId, items)
    override suspend fun syncUserReverseConnections(userId: UUID, items: List<UserConnection>) = dao.syncReverseConnections(userId, items)
    override suspend fun deleteItem(item: UserConnection) = dao.delete(item)
    override suspend fun updateItem(item: UserConnection) = dao.update(item)
    override fun getUserItemsStream(userId: UUID) = dao.getUserItems(userId)
    override fun getItemStream(id: String) = dao.getItem(id)
    override suspend fun syncConnectionCardItems(connectionId: UUID, items: List<ConnectionCard>) = connectionCardDao.syncConnectionCardItems(connectionId, items)

}