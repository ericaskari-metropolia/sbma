package com.sbma.linkup.userconnection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sbma.linkup.user.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserConnectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: UserConnection)

    @Update
    suspend fun update(item: UserConnection)

    @Delete
    suspend fun delete(item: UserConnection)

    @Query("SELECT * from UserConnection WHERE id = :id")
    fun getItem(id: String): Flow<UserConnection?>

    @Query("SELECT * from UserConnection JOIN User ON user.id = UserConnection.connectedUserId where userId = :userId")
    fun getUserItems(userId: UUID): Flow<Map<UserConnection, User>>
}
