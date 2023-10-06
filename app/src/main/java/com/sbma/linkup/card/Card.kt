package com.sbma.linkup.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Card(
    @PrimaryKey val id: UUID,
    @ColumnInfo val userId: UUID,
    @ColumnInfo val title: String,
    @ColumnInfo val value: String,
    @ColumnInfo val picture: String?,
) {

    companion object {

    }
}

