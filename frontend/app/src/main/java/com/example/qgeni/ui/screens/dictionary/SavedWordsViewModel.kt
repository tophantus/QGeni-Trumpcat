package com.example.qgeni.ui.screens.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.data.model.word.Word
import com.example.qgeni.data.model.word.WordAccessHistory
import com.example.qgeni.data.model.word.WordMockData
import com.example.qgeni.data.repositories.DefaultFavoriteRepository
import com.example.qgeni.data.repositories.DefaultHistoryRepository
import com.example.qgeni.data.repositories.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SavedWordsViewModel : ViewModel() {
    private val _savedWordListUIState = MutableStateFlow(SavedWordsUIState())
    open val savedWordListUIState = _savedWordListUIState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _savedWordListUIState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                withContext(Dispatchers.IO) {
                    updateAccessHistoryWords()
                }
                _savedWordListUIState.update {
                    it.copy(
                        loading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                println("Error: " + e.message)
                _savedWordListUIState.update {
                    it.copy(
                        loading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun reload() {
        viewModelScope.launch {
            try {
                _savedWordListUIState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                withContext(Dispatchers.IO) {
                    updateAccessHistoryWords()
                }
                _savedWordListUIState.update {
                    it.copy(
                        loading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                println("Error: " + e.message)
                _savedWordListUIState.update {
                    it.copy(
                        loading = false,
                        error = e.message
                    )
                }
            }
        }
    }
    private suspend fun updateAccessHistoryWords() {
        val words: List<WordAccessHistory> = DefaultHistoryRepository.getAllWordAccessHistories()
        println("Word " + words.toString())

        _savedWordListUIState.update {
            it.copy(
                savedWordList = words,
                error = null
            )
        }


    }

    private fun toggleDialog(show: Boolean) {
        _savedWordListUIState.update { it.copy(showDialog = show) }
    }

    fun onSelectedWordsChange(id: Int, selected: Boolean) {
        println("selectedWord viewmodel")
        if (!selected) {
            val newList = _savedWordListUIState.value.selectedWordIdList.filterNot {
                it == id
            }
            _savedWordListUIState.update {
                it.copy(
                    selectedWordIdList = newList
                )
            }
            println("selected: " + _savedWordListUIState.value.selectedWordIdList)
        } else {
            val newList = _savedWordListUIState.value.selectedWordIdList + id
            _savedWordListUIState.update {
                it.copy(
                    selectedWordIdList = newList
                )
            }
            println("selected: " + _savedWordListUIState.value.selectedWordIdList)
        }
        if (_savedWordListUIState.value.selectedWordIdList.isNotEmpty()) {
            toggleDialog(true)
            println("showDialog")
        } else {
            toggleDialog(false)
            println("hideDialog")
        }
    }

    fun isSelected(id: Int): Boolean {
        return id in _savedWordListUIState.value.selectedWordIdList
    }

    fun onFavoriteWordsChange(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    DefaultFavoriteRepository.changeWordFavorite(id)
                }
                val words: List<WordAccessHistory> = withContext(Dispatchers.IO) {
                    DefaultHistoryRepository.getAllWordAccessHistories()
                }
                _savedWordListUIState.update {
                    it.copy(
                        savedWordList = words,
                        loading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _savedWordListUIState.update {
                    it.copy(
                        error = e.message
                    )
                }
            }

        }
    }

    fun removeAllSelectedWordId() {
        _savedWordListUIState.update{
            it.copy(
                selectedWordIdList = emptyList(),
                showDialog = false,
            )
        }
    }

    fun removeError() {
        _savedWordListUIState.update{
            it.copy(
                error = null
            )
        }
    }

    fun deleteAllSelectedWords() {
        println("deleteAll")
        val filteredList = _savedWordListUIState.value.selectedWordIdList
        viewModelScope.launch {
            try {
                _savedWordListUIState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                withContext(Dispatchers.IO) {
                    filteredList.forEach {id ->
                        DefaultHistoryRepository.deleteWordAccessHistory(id)
                    }
                    updateAccessHistoryWords()
                }
                _savedWordListUIState.update {
                    it.copy(
                        loading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                println("Errordelete: ${e.message}")
                _savedWordListUIState.update {
                    it.copy(
                        error = e.message,
                        loading = false
                    )
                }
            }
        }

        removeAllSelectedWordId()
    }

    fun addAllSelectedWordToFavorite() {
        println("addAllFavorite")
        val filteredList = _savedWordListUIState.value.selectedWordIdList.filterNot {id ->
            _savedWordListUIState.value.savedWordList.any {w ->
                w.isFavorite && w.id == id
            }
        }
        viewModelScope.launch {
            try {
                _savedWordListUIState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                withContext(Dispatchers.IO) {
                    filteredList.forEach {id ->
                        DefaultFavoriteRepository.changeWordFavorite(id)
                    }
                    updateAccessHistoryWords()
                }

                _savedWordListUIState.update {
                    it.copy(
                        loading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _savedWordListUIState.update {
                    it.copy(
                        error = e.message,
                        loading = false
                    )
                }
            }
        }
        removeAllSelectedWordId()
    }
}


data class SavedWordsUIState(
    val savedWordList: List<WordAccessHistory> = emptyList(),
    val selectedWordIdList: List<Int> = emptyList(),
    val showDialog: Boolean = false,
    val selectedIdx: Int? = null,
    val loading: Boolean = false,
    val error: String? = null,
)