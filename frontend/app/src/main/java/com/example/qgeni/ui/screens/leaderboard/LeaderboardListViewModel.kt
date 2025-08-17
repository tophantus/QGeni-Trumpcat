package com.example.qgeni.ui.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.data.repositories.DefaultHistoryRepository
import com.example.qgeni.data.repositories.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class LeaderboardListViewModel : ViewModel() {
    private val _practiceListUIState = MutableStateFlow(LeaderboardListUIState())
    open val leaderboardListUIState = _practiceListUIState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _practiceListUIState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                val practiceList = withContext(Dispatchers.IO) {
                    getPracticeItemList()
                }
                _practiceListUIState.update {
                    it.copy(
                        practiceItemList = practiceList,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                println("E: ${e.message}")
                _practiceListUIState.update {
                    it.copy(
                        loading = false,
                        error = e.message
                    )
                }
            }

        }
    }

    fun removeError() {
        _practiceListUIState.update {
            it.copy(
                error = null
            )
        }
    }
    fun reload() {
        viewModelScope.launch {
            try {
                _practiceListUIState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                val practiceList = withContext(Dispatchers.IO) {
                    getPracticeItemList()
                }
                _practiceListUIState.update {
                    it.copy(
                        practiceItemList = practiceList,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _practiceListUIState.update {
                    it.copy(
                        loading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun getAchievementItem(id: Int) {
        viewModelScope.launch {
            try {
                _practiceListUIState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                val list = withContext(Dispatchers.IO) {
                    getAchievementItems(id)
                }
                println(list.toString())
                _practiceListUIState.update {
                    it.copy(
                        leaderboardList = list,
                        loading = false
                    )
                }
            } catch(e: Exception) {
                e.printStackTrace()
                _practiceListUIState.update {
                    it.copy(
                        loading = false,
                        error = e.message
                    )
                }
            }
        }
    }
    suspend fun getLeaderboard(id: Int): List<Achievement> {
        return getAchievementItems(id)
    }

    fun setError(e: String) {
        _practiceListUIState.update {
            it.copy(
                error = e
            )
        }
    }

    protected abstract suspend fun getPracticeItemList(): List<PracticeItemSummary>
    protected abstract suspend fun getAchievementItems(id: Int): List<Achievement>

    fun selectItemIdx(index: Int) {
        _practiceListUIState.update { it.copy(selectedIdx = index) }
    }

}


data class LeaderboardListUIState(
    val practiceItemList: List<PracticeItemSummary> = emptyList(),
    val leaderboardList: List<Achievement> = emptyList(),
    val showOpenDialog: Boolean = false,
    val selectedIdx: Int? = null,
    val loading: Boolean = false,
    val error: String? = null
) {
    val selectedItemId: Int?
        get() = selectedIdx?.let { practiceItemList[it].id }
}