package com.sbma.linkup.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: User)

    @Update
    suspend fun update(item: User)

    @Delete
    suspend fun delete(item: User)

    @Query("SELECT * from User WHERE id = :id")
    fun getItem(id: String): Flow<User?>

    @Query("SELECT * from User ORDER BY name DESC")
    fun getAllItems(): Flow<List<User>>
}