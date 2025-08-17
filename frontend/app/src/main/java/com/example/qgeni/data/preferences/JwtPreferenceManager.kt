package com.example.qgeni.data.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object JwtPreferenceManager {
    private const val JWT_KEY = "jwt"
    private const val PREFERENCE_NAME = "jwt_preference"

    // Save jwt to DataStore
    fun saveJwt(context: Context, jwt: String) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(JWT_KEY, jwt)
        }
    }

    // Read theme from DataStore
    fun getJwtFlow(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(JWT_KEY, "")

    }

    fun clearJwt(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            remove(JWT_KEY)
        }
    }

}