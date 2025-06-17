package com.example.smsSender.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val SETTINGS_NAME = "user_prefs.preferences_pb" // nazwa pliku gdzie DataStore bedzie trzymal nasze preferencje (pary klucz wartosc)
val Context.dataStore by preferencesDataStore(name = SETTINGS_NAME) // daje dostep do DataStore<Preferences> z każdego miejsca gdzie jest context