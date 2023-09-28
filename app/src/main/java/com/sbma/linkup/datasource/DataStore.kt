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

/**
 * key value database
 * Currently used for userId of logged in user.
 * And also json string of contacts that user wants to share.
 */
class DataStore(private val context: Context) {

    /**
     * Getter for logged in user id
     */
    val getUserId: Flow<UUID?> = context.dataStore.data
        .map { it[USERID_KEY] }
        .map { it?.let { UUID.fromString(it) } }

    /**
     * Getter for saved Json string.
     */
    val getJsonToShare: Flow<String?> = context.dataStore.data
        .map { it[JSONTOSHARE_KEY] }

    /**
     * Setter for logged in user id
     */
    suspend fun setUserId(userId: UUID) {
        context.dataStore.edit { it[USERID_KEY] = userId.toString() }
    }

    /**
     * Setter for logged in user cards json string
     */
    suspend fun setJsonToShare(json: String) {
        context.dataStore.edit { it[JSONTOSHARE_KEY] = json }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userId")
        val USERID_KEY = stringPreferencesKey("user_id")
        val JSONTOSHARE_KEY = stringPreferencesKey("json_to_share")
    }
}