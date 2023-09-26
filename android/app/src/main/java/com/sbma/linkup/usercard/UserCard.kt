package com.sbma.linkup.usercard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class UserCard(
    @PrimaryKey val id: UUID,
    @ColumnInfo val userId: UUID,
    @ColumnInfo val name: String,
    @ColumnInfo val value: String,
) {

    companion object {

    }
}

