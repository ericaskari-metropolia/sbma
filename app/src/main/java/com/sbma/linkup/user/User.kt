package com.sbma.linkup.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class User(
    @PrimaryKey val id: UUID,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val picture: String?,
) {

    companion object {

    }
}

