package com.example.qgeni.ui.screens.practices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.model.PracticeItemSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class PracticeListViewModel : ViewModel() {
    private val _practiceListUIState = MutableStateFlow(PracticeListUIState())
    open val practiceListUIState = _practiceListUIState.asStateFlow()

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

    protected abstract suspend fun getPracticeItemList(): List<PracticeItemSummary>
    protected abstract suspend fun changeFavorite(id: Int)

    fun changeFavoriteState(idx: Int) {
        val oldItem = _practiceListUIState.value.practiceItemList[idx]
        val newItem = oldItem.copy(isFavorite = !oldItem.isFavorite)
        val newList = _practiceListUIState.value.practiceItemList.toMutableList()
        newList[idx] = newItem
        _practiceListUIState.update {
            it.copy(
                practiceItemList = newList
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                changeFavorite(oldItem.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleOpenDialog(show: Boolean) {
        _practiceListUIState.update { it.copy(showOpenDialog = show) }
    }

    fun selectItemIdx(index: Int) {
        _practiceListUIState.update { it.copy(selectedIdx = index) }
    }

}


data class PracticeListUIState(
    val practiceItemList: List<PracticeItemSummary> = emptyList(),
    val showOpenDialog: Boolean = false,
    val selectedIdx: Int? = null,
    val loading: Boolean = false,
    val error: String? = null
) {
    val selectedItemId: Int?
        get() = selectedIdx?.let { practiceItemList[it].id }
}
