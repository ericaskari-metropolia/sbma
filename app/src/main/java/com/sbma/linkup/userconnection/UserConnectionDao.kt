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
    suspend fun deleteNotInTheListUserItems(userId: UUID, idList: List<UUID>)

    @Query("DELETE FROM UserConnection WHERE connectedUserId = :connectedUserId AND id NOT IN (:idList)")
    suspend fun deleteNotInTheListConnectedUserItems(connectedUserId: UUID, idList: List<UUID>)

    @Upsert
    suspend fun upsert(item: UserConnection)

    @Delete
    suspend fun delete(item: UserConnection)

    @Query("SELECT * from UserConnection WHERE id = :id")
    fun getItem(id: String): Flow<UserConnection?>

    @Query("SELECT * from UserConnection JOIN User ON user.id = UserConnection.connectedUserId where userId = :userId")
    fun getUserItems(userId: UUID): Flow<Map<UserConnection, User>>

    @Transaction
    suspend fun syncUserItems(userId: UUID, items: List<UserConnection>) {
        val userItems = items.filter { it.userId == userId }
        insert(userItems)
        deleteNotInTheListUserItems(userId, userItems.map { it.id })
    }

    @Transaction
    suspend fun syncConnectedUserItems(userId: UUID, items: List<UserConnection>) {
        val userItems = items.filter { it.connectedUserId == userId }
        insert(userItems)
        deleteNotInTheListConnectedUserItems(userId, userItems.map { it.id })
    }

}
