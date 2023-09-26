package com.sbma.linkup.usercard

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: UserCard)

    @Update
    suspend fun update(item: UserCard)

    @Delete
    suspend fun delete(item: UserCard)

    @Query("SELECT * from UserCard WHERE id = :id")
    fun getItem(id: UUID): Flow<UserCard?>

    @Query("SELECT * from UserCard WHERE userId = :userId ORDER BY name DESC")
    fun getUserItems(userId: UUID): Flow<List<UserCard>>
}
