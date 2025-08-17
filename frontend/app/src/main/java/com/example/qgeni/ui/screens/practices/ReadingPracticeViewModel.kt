package com.example.qgeni.ui.screens.practices

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.qgeni.data.model.ReadingPracticeItem
import com.example.qgeni.data.repositories.DefaultHistoryRepository
import com.example.qgeni.data.repositories.DefaultReadingRepository
import com.example.qgeni.utils.formatFloat2Decimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
open class ReadingPracticeViewModel(private val id: Int) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadingPracticeUIState())
    open val uiState = _uiState.asStateFlow()

    private lateinit var readingPracticeItem: ReadingPracticeItem
    init {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                readingPracticeItem = withContext(Dispatchers.IO) {
                    DefaultReadingRepository.getItem(id) as ReadingPracticeItem
                }
                _uiState.update {
                    it.copy(
                        readingPracticeItem = readingPracticeItem,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                println("E: ${e.message}")
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = e.message
                    )
                }
            }
        }

        viewModelScope.launch {
            if (
                _uiState.value.readingPracticeItem != null
            ) {
                while (!_uiState.value.isComplete) {
                    updateTime()
                    kotlinx.coroutines.delay(1000)
                }
            }
        }

    }

    fun reload() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                readingPracticeItem = withContext(Dispatchers.IO) {
                    DefaultReadingRepository.getItem(id) as ReadingPracticeItem
                }
                _uiState.update {
                    it.copy(
                        readingPracticeItem = readingPracticeItem,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                println("E: ${e.message}")
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun removeError() {
        _uiState.update {
            it.copy(
                error = null
            )
        }
    }
    fun toggleHighlightEnabled() {
        _uiState.update { it.copy(isHighlightEnabled = !it.isHighlightEnabled) }
    }

    fun toggleHighlightMode() {
        _uiState.update { it.copy(isHighlightMode = !it.isHighlightMode) }
    }

    fun updateTime() {
        if (!_uiState.value.isComplete) {
            _uiState.update {
                it.copy(time = it.time + 1000L)
            }
        }
    }

    fun updateHighlightedIndices(index: Int, isHighlightMode: Boolean) {
        _uiState.update {
            val newIndices = it.highlightedIndices.toMutableList()
            if(isHighlightMode) {
                if(!newIndices.contains(index)) {
                    newIndices.add(index)
                }
            } else
                newIndices.remove(index)
            it.copy(
                highlightedIndices = newIndices
            )
        }
    }

    fun updateTextLayoutResult(result: TextLayoutResult?) {
        _uiState.update { it.copy(textLayoutResult = result) }
    }

    fun updateCurrentQuestionIndex(index: Int) {
        _uiState.update {
            it.copy(
                currentQuestionIndex = index
            )
        }
    }

    fun toggleSubmitConfirmDialog(show: Boolean) {
        _uiState.update { it.copy(showSubmitConfirmDialog = show) }
    }

    fun toggleScoreDialog(show: Boolean) {
        _uiState.update { it.copy(showScoreDialog = show) }
    }

    fun updateSelectAnswer(selectAnswer: String?) {
        _uiState.update {
            it.copy(
                selectAnswer = selectAnswer
            )
        }
    }

    fun updateAnsweredQuestions(questionIndex: Int, answer: String?) {
        _uiState.update {
            val currentAnswer = it.answeredQuestions.toMutableMap()
            currentAnswer[questionIndex] = answer
            it.copy(
                answeredQuestions = currentAnswer
            )
        }
    }

    fun updateIsComplete(isComplete: Boolean) {
        _uiState.update {
            it.copy(
                isComplete = isComplete
            )
        }
    }

    fun submit() {
        var numCorrect = 0
        val correctAnswerQgs = mutableListOf<Boolean>()

        for (qIdx in 0..<readingPracticeItem.questionList.size) {
            val ans = _uiState.value.answeredQuestions[qIdx]
            val correctAns = readingPracticeItem.questionList[qIdx].answer
            if (correctAns == ans) {
                numCorrect++
                correctAnswerQgs.add(true)
            } else {
                correctAnswerQgs.add(false)
            }
        }

        val score = numCorrect.toFloat() / readingPracticeItem.questionList.size.toFloat() * 10

        val time = _uiState.value.time / 1000

        _uiState.update {
            it.copy(
                isComplete = true,
                correctAnswerList = correctAnswerQgs
            )
        }


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalScore = DefaultHistoryRepository.createReadingHistory(
                    readingPracticeItem.id,
                    score,
                    time
                )

                println("Total Score $totalScore")

                _uiState.update {
                    it.copy(
                        showScoreDialog = true,
                        scoreMessage = "Kết quả: $score/10\nThời gian: $time giây\nTổng điểm: ${formatFloat2Decimal(totalScore)}"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    companion object {
        fun factory(id: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ReadingPracticeViewModel(id)
            }
        }
    }


}


data class ReadingPracticeUIState(
    val readingPracticeItem: ReadingPracticeItem? = null,
    var isHighlightEnabled: Boolean = false,
    var isHighlightMode: Boolean = true,
    var time: Long = 0L,
    val highlightedIndices: List<Int> = listOf(),
    val textLayoutResult: TextLayoutResult? = null,
    val currentQuestionIndex: Int = 0,
    val showSubmitConfirmDialog: Boolean = false,
    val showScoreDialog: Boolean = false,
    val selectAnswer: String? = null,
    val answeredQuestions: MutableMap<Int, String?> = mutableMapOf(),
    val isComplete: Boolean = false,

    val scoreMessage: String = "",
    val correctAnswerList: List<Boolean> = listOf(false, false),

    val correctAnswerQgs: List<Boolean> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null

)
