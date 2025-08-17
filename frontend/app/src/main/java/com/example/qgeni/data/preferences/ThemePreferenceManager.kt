package com.example.qgeni.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "theme_preferences")

class ThemePreferenceManager(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    // Save theme to DataStore
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    // Read theme from DataStore
    val themeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name
        }

}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}
