package com.example.smsSender.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys { // przechowuje zestaw wszystkich kluczy ktore sa uzywane w DataStore
    val USER_TEXT = stringPreferencesKey("user_text") // zwraca Preferences.Key<String>
    // ten klucz jest Identyfikatorem wpisu w DataStore pod nazwą "user_text"
}