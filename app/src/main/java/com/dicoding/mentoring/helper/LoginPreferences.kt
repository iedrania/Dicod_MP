package com.dicoding.mentoring.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val token = stringPreferencesKey("token")

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }

    suspend fun setToken(newToken: String) {
        dataStore.edit { preferences ->
            preferences[token] = newToken
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}