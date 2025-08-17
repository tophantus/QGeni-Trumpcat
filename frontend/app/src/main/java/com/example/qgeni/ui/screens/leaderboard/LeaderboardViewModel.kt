package com.example.qgeni.ui.screens.leaderboard

import androidx.lifecycle.ViewModel
import com.example.qgeni.AppContextHolder
import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.data.preferences.UserPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LeaderboardUiState(
    val achievements: List<Achievement> = emptyList(),
    val currentUserId: Int = 0,
    val error: String? = null
)

class LeaderboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Fake data demo
        val id = UserPreferenceManager.loadUserId(AppContextHolder.appContext)
        if (id != null) {
            _uiState.update {
                it.copy(
                    achievements = emptyList(),
                    currentUserId = id
                )
            }
        } else {
            println("null")
        }

    }

    fun updateError(e: String?) {
        _uiState.update {
            it.copy(
                error = e
            )
        }
    }
    fun updateAchievement(list: List<Achievement>) {
        println("update + ${list.toString()}")
        val id = UserPreferenceManager.loadUserId(AppContextHolder.appContext)
        if (id != null) {
            _uiState.update {
                it.copy(
                    achievements = list,
                    currentUserId = id
                )
            }
        } else {
            println("null")
        }
    }
}