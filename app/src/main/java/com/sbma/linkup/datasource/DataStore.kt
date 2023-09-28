package com.sbma.linkup.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class DataStore(private val context: Context) {

    val getUserId: Flow<UUID?> = context.dataStore.data
        .map { it[USERID] }
        .map { it?.let{ UUID.fromString(it) } }

    suspend fun setUserId(userId: UUID) {
        context.dataStore.edit { it[USERID] = userId.toString() }
    }
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userId")
        val USERID = stringPreferencesKey("user_id")
    }
}