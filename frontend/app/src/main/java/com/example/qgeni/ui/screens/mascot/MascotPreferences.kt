package com.example.qgeni.ui.screens.mascot

import android.content.Context
import androidx.core.content.edit
import com.example.qgeni.AppContextHolder

object MascotPreferences {
    fun saveMascotVisibility(show: Boolean) {
        AppContextHolder.appContext.getSharedPreferences("mascot_prefs", Context.MODE_PRIVATE)
            .edit() {
                putBoolean("show_mascot", show)
            }
    }


    fun shouldShowMascot(): Boolean {
        return AppContextHolder.appContext.getSharedPreferences("mascot_prefs", Context.MODE_PRIVATE)
            .getBoolean("show_mascot", true)
    }
}

