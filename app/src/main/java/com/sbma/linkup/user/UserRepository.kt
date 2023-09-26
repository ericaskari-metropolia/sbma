package com.sbma.linkup.user

class UserRepository(private val dao: UserDao) : IUserRepository {
    override suspend fun insertItem(item: User) = dao.insert(item)
    override suspend fun deleteItem(item: User) = dao.delete(item)
    override suspend fun updateItem(item: User) = dao.update(item)
    override fun getAllItemsStream() = dao.getAllItems()
    override fun getItemStream(id: String) = dao.getItem(id)

}