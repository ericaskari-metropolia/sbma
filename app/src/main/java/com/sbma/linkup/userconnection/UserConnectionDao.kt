package com.sbma.linkup.userconnection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.sbma.linkup.user.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserConnectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: UserConnection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: List<UserConnection>)

    @Update
    suspend fun update(item: UserConnection)

    @Query("DELETE FROM UserConnection WHERE userId = :userId AND id NOT IN (:idList)")
    suspend fun deleteNotInTheListUserConnections(userId: UUID, idList: List<UUID>)

    @Query("DELETE FROM UserConnection WHERE connectedUserId = :userId AND id NOT IN (:idList)")
    suspend fun deleteNotInTheListReverseUserConnections(userId: UUID, idList: List<UUID>)

    @Upsert
    suspend fun upsert(item: UserConnection)

    @Delete
    suspend fun delete(item: UserConnection)

    @Query("SELECT * from UserConnection WHERE id = :id")
    fun getItem(id: String): Flow<UserConnection?>

    @Query("SELECT * from UserConnection JOIN User ON user.id = UserConnection.connectedUserId where UserConnection.userId = :userId")
    fun getUserItems(userId: UUID): Flow<Map<UserConnection, User>>

    @Query("SELECT * from UserConnection where UserConnection.userId = :userId")
    fun getUserConnections(userId: UUID): Flow<List<UserConnection>>

    @Transaction
    suspend fun syncUserConnections(userId: UUID, items: List<UserConnection>) {
        val userItems = items.filter { it.userId == userId }
        insert(userItems)
        deleteNotInTheListUserConnections(userId, userItems.map { it.id })
    }

    @Transaction
    suspend fun syncReverseConnections(userId: UUID, items: List<UserConnection>) {
        val userItems = items.filter { it.connectedUserId == userId }
        insert(userItems)
        deleteNotInTheListReverseUserConnections(userId, userItems.map { it.id })
    }

}
