package com.tomislav0.roomer.dataAccess

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
    }
    val getEmail: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[EMAIL] ?: ""
        }
    val getPassword: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PASSWORD] ?: ""
        }

    suspend fun saveData(email: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL] = email
            preferences[PASSWORD] = password
        }
    }
}