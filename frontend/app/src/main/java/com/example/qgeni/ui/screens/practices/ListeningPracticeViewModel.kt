package com.example.qgeni.ui.screens.practices

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.qgeni.data.model.ListeningPracticeItem
import com.example.qgeni.data.model.McqQuestion
import com.example.qgeni.data.repositories.DefaultHistoryRepository
import com.example.qgeni.data.repositories.DefaultListeningRepository
import com.example.qgeni.utils.AudioPlayer
import com.example.qgeni.utils.formatFloat2Decimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.O)
class ListeningPracticeViewModel(private val id: Int): ViewModel() {
    private val _uiState = MutableStateFlow(ListeningPracticeUIState())
    val uiState = _uiState.asStateFlow()

    private lateinit var currentAudioPlayer: AudioPlayer
    private lateinit var practiceItem: ListeningPracticeItem


    init {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        showLoadingDialog = true,
                        error = null
                    )
                }

                practiceItem = withContext(Dispatchers.IO) {
                    DefaultListeningRepository.getItem(id) as ListeningPracticeItem
                }

                _uiState.update {
                    it.copy(
                        showLoadingDialog = false,
                        imageList = practiceItem.questionList[0].imageList,
                        questionList = practiceItem.questionList.map { question ->
                            McqQuestion(
                                question = "Choose the correct picture",
                                answerList = List(question.imageList.size) { index ->
                                    ('A' + index).toString()
                                },
                                explanation = question.caption
                            )
                        }
                    )
                }

                updateAudioPlayer(0)

                while (true) {
                    delay(1000)
                    updateTime()
                }
            } catch (e: Exception) {
                println("E: ${e.message}")
                _uiState.update {
                    it.copy(
                        showLoadingDialog = false,
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

    fun reload() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        showLoadingDialog = true,
                        error = null
                    )
                }

                practiceItem = withContext(Dispatchers.IO) {
                    DefaultListeningRepository.getItem(id) as ListeningPracticeItem
                }

                _uiState.update {
                    it.copy(
                        showLoadingDialog = false,
                        imageList = practiceItem.questionList[0].imageList,
                        questionList = practiceItem.questionList.map { question ->
                            McqQuestion(
                                question = "Choose the correct picture",
                                answerList = List(question.imageList.size) { index ->
                                    ('A' + index).toString()
                                },
                                explanation = question.caption
                            )
                        }
                    )
                }

                updateAudioPlayer(0)

                while (!_uiState.value.isCompleted) {
                    delay(1000)
                    updateTime()
                }

            } catch (e: Exception) {
                println("E: ${e.message}")
                _uiState.update {
                    it.copy(
                        showLoadingDialog = false,
                        error = e.message
                    )
                }
            }

        }
    }



    private fun updateAudioPlayer(index: Int) {
        if (::currentAudioPlayer.isInitialized) {
            currentAudioPlayer.release()
        }

        currentAudioPlayer = AudioPlayer(
            audioUrl = practiceItem.questionList[index].mp3File,
            onCompletion = {
                _uiState.update {
                    it.copy(
                        playbackState = PlaybackState.FINISHED
                    )
                }
            }
        )

        _uiState.update {
            it.copy(
                audioDuration = currentAudioPlayer.getDurationSecond()
            )
        }
    }

    fun updateCurrentQuestionIndex(index: Int) {
        updateAudioPlayer(index)
        _uiState.update {
            it.copy(
                currentQuestionIndex = index,
                imageList = practiceItem.questionList[index].imageList,
                playbackState = PlaybackState.PAUSED,
                audioSliderPos = 0f,
                audioDuration = currentAudioPlayer.getDurationSecond()
            )
        }

    }

    fun updateAnsweredQuestions(questionIndex: Int, answer: Int) {
        _uiState.update {
            val currentAnswer = it.answeredQuestions.toMutableMap()

            if (currentAnswer[questionIndex] == answer) {
                currentAnswer[questionIndex] = -1
            } else {
                currentAnswer[questionIndex] = answer
            }

            it.copy(
                answeredQuestions = currentAnswer
            )
        }
    }


    private fun updateTime() {
        if (!_uiState.value.isCompleted) {
            _uiState.update {
                it.copy(
                    time = it.time + 1000L
                )
            }
        }
    }

    fun toggleSubmitConfirmDialog(show: Boolean) {
        _uiState.update { it.copy(showSubmitConfirmDialog = show) }
    }

    fun toggleScoreDialog(show: Boolean) {
        if (_uiState.value.isCompleted) {
            _uiState.update { it.copy(showScoreDialog = show) }
        } else {
            val correctAnswerIds = practiceItem.questionList.map { it.answerIndex }
            val numCorrect = _uiState.value.answeredQuestions.filter {
                it.value == correctAnswerIds[it.key]
            }.size

            val score = numCorrect.toFloat() / practiceItem.questionList.size.toFloat() * 10F
            val time = _uiState.value.time / 1000

            _uiState.update {
                it.copy(
                    isCompleted = true,
                    correctAnswerIds = correctAnswerIds,
                    score = score
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val totalScore = DefaultHistoryRepository.createListeningHistory(id, score, time)
                    _uiState.update {
                        it.copy(
                            showScoreDialog = show,
                            showSubmitConfirmDialog = false,
                            scoreMessage = "Kết quả: $score/10\nThời gian: $time giây\nTổng điểm: ${formatFloat2Decimal(totalScore)}"
                        )
                    }
                    Log.e("ListeningPracticeViewModel", "Score changed to $score")
                } catch (e: Exception) {
                    println("E: ${e.message}")
                }
            }
        }
    }

    fun play() {
        val playbackState = _uiState.value.playbackState
        if (playbackState == PlaybackState.PAUSED || playbackState == PlaybackState.FINISHED) {
            _uiState.update {
                it.copy(
                    playbackState = PlaybackState.PLAYING
                )
            }
            viewModelScope.launch {
                currentAudioPlayer.play()
                while (_uiState.value.playbackState == PlaybackState.PLAYING) {
                    _uiState.update {
                        it.copy(
                            audioSliderPos = currentAudioPlayer.getPositionSecond()
                        )
                    }
                    delay(100)
                }
            }

        } else if (playbackState == PlaybackState.PLAYING) {
            _uiState.update {
                it.copy(
                    playbackState = PlaybackState.PAUSED
                )
            }
            viewModelScope.launch {
                currentAudioPlayer.pause()
            }
        }
    }

    fun seekTo() {
        viewModelScope.launch {
            currentAudioPlayer.moveTo(
                (_uiState.value.audioSliderPos * 1000).toInt()
            )
        }
    }

    fun updateAudioSliderPos(pos: Float) {
        _uiState.update {
            it.copy(
                audioSliderPos = pos
            )
        }
    }


    companion object {
        fun factory(id: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ListeningPracticeViewModel(id)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentAudioPlayer.release()

    }
}


data class ListeningPracticeUIState(
    val currentQuestionIndex: Int = 0,
    val imageList: List<String> = emptyList(),
    val questionList: List<McqQuestion> = emptyList(),

    val showLoadingDialog: Boolean = false,
    val showSubmitConfirmDialog: Boolean = false,
    val showScoreDialog: Boolean = false,
    val scoreMessage: String = "",

    // Key: questionIndex, Value: answerIndex in answerList
    val answeredQuestions: Map<Int, Int?> = emptyMap(),

    val time: Long = 0L,

    val audioSliderPos: Float = 0f,
    val audioDuration: Float = 0f,
    val playbackState: PlaybackState = PlaybackState.PAUSED,

    val isCompleted: Boolean = false,
    val score: Float = 0F,
    val correctAnswerIds: List<Int> = emptyList(),

    val error: String? = null
)

enum class PlaybackState {
    PLAYING,
    PAUSED,
    FINISHED
}
