package com.example.qgeni.ui.screens.reminder

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.qgeni.AppContextHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ReminderViewModel: ViewModel() {

    private val _state = MutableStateFlow(ReminderUiState())
    val state = _state.asStateFlow()

    init {
        loadPreferences()
    }

    fun setEnabled(enabled: Boolean) {
        _state.update { it.copy(enabled = enabled) }
        savePreferences()
    }

    fun setTime(hour: Int, minute: Int) {
        _state.update { it.copy(hour = hour, minute = minute) }
        savePreferences()
    }

    fun setInterval(days: Int) {
        _state.update { it.copy(intervalDays = days) }
        savePreferences()
    }

    private fun loadPreferences() {
        val prefs = AppContextHolder.appContext.getSharedPreferences("reminder", Context.MODE_PRIVATE)
        _state.update {
            it.copy(
                enabled = prefs.getBoolean("enabled", false),
                hour = prefs.getInt("hour", 20),
                minute = prefs.getInt("minute", 0),
                intervalDays = prefs.getInt("interval", 1)
            )
        }
    }

    private fun savePreferences() {
        val context = AppContextHolder.appContext
        val prefs = context.getSharedPreferences("reminder", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean("enabled", _state.value.enabled)
            putInt("hour", _state.value.hour)
            putInt("minute", _state.value.minute)
            putInt("interval", _state.value.intervalDays)
            apply()
        }
    }
}

data class ReminderUiState(
    val enabled: Boolean = false,
    val hour: Int = 20,
    val minute: Int = 0,
    val intervalDays: Int = 1
)
