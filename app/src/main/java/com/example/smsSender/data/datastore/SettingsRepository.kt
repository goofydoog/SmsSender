package com.example.smsSender.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor (private val dataStore: DataStore<Preferences>) {

    // Flow zwracający aktualny zapisany tekst (pusty, jeśli nie ma)
    val userTextFlow: Flow<String> = dataStore.data
        .map { prefs ->
            prefs[SettingsKeys.USER_TEXT] ?: ""
        }

    // Funkcja do zapisania tekstu
    suspend fun saveUserText(text: String) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.USER_TEXT] = text
        }
    }
}