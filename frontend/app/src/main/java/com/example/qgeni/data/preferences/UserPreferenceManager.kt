package com.example.qgeni.data.preferences

import android.content.Context
import android.content.SharedPreferences

object UserPreferenceManager {

    private const val ID_KEY = "user_id"
    private const val PREFERENCE_NAME = "id_preferences"

    private var userId: Int? = null

    fun loadUserId(context: Context): Int? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt(ID_KEY, 1)?.let {
            it
        }
        println("ID: $id")
        return id
    }

    fun getUserId(): Int? {
        return this.userId
    }

    fun saveUserId(context: Context, userId: Int) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(ID_KEY, userId).apply()
        this.userId = userId
        println("Save Id: $userId")
    }

    fun removeUserId(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(ID_KEY).apply()
        this.userId = null
    }

}