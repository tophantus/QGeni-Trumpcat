package com.example.qgeni.ui.screens.dictionary

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.AppContextHolder
import com.example.qgeni.data.model.word.Word
import com.example.qgeni.data.repositories.DefaultWordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Locale

class DictionaryViewModel: ViewModel(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val voiceMap = mapOf(
        Locale.UK to "en-gb-x-rjs-local",
        Locale.US to "en-us-x-sfg-local"
    )

    private val _uiState = MutableStateFlow(DictionaryUIState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        tts = TextToSpeech(AppContextHolder.appContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsReady = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {}
            })
        }
    }

    fun speak(accent: Locale) {
        if (_uiState.value.curWord == null) {
            println("Curword is null")
            return
        }
        val text = _uiState.value.curWord!!.text.trim()
        if (!isTtsReady || text.isEmpty()) {
            println("Tst is not ready or text is empty")
            return
        }

        tts?.language = accent

        voiceMap[accent]?.let { voiceName ->
            val voice = tts?.voices?.find { it.name == voiceName }
            if (voice != null) {
                tts?.voice = voice
            }
        }

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "tts_id")
        }

        // QUEUE_FLUSH sẽ tự dừng đoạn đọc trước đó
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "tts_id")
        println("Speak")
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }

    fun onTextChanged(text: String) {

        _uiState.update {
            it.copy(
                text = text
            )
        }
        updateExpandDropdown()
        searchJob?.cancel()
        println("textChange")

        searchJob = viewModelScope.launch {
            delay(300)
            try {
                _uiState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                println("fetch: " + text)
                val response: Response<List<String>> = withContext(Dispatchers.IO) {
                    DefaultWordRepository.getSuggestions(text)
                }
                val words: List<String> = response.body()!!
                if (response.isSuccessful) {
                    val body = response.body()
                    println("Body: " + body)
                } else {
                    println("Error: " + response.errorBody()?.string())
                }
                println(words)
                _uiState.update {
                    it.copy(
                        recommendedWord = words,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                println("error: " + e.message)
                _uiState.update {
                    it.copy(
                        error = e.message,
                        loading = false
                    )
                }
            }

        }
        //fetchRecommendedWords(text)

    }

    private fun fetchRecommendedWords(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            loading = true,
                            error = null
                        )
                    }
                }
                println("fetch")
                val words: List<String> = DefaultWordRepository.getSuggestions(query).body()!!
                println(words)
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            recommendedWord = words,
                            loading = false
                        )
                    }
                }
            } catch (e: Exception) {
                println("error: " + e.message)
                _uiState.update {
                    it.copy(
                        error = e.message,
                        loading = false
                    )
                }
            }

        }
    }

    fun selectWord(text: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        loading = true,
                        error = null
                    )
                }
                val word: Word = withContext(Dispatchers.IO) {
                    DefaultWordRepository.getWord(text)
                }
                _uiState.update {
                    it.copy(
                        curWord = word,
                        showWord = true,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                println("getWordError: " + e.message)
                _uiState.update {
                    it.copy(
                        showWord = false,
                        loading = false,
                        error = e.message
                    )
                }
            }
        }

    }

    private fun updateExpandDropdown() {
        println("update")
        if (_uiState.value.text.isNotEmpty() && _uiState.value.text.isNotBlank() && !_uiState.value.expandDropdown) {
            _uiState.update {
                it.copy(
                    expandDropdown = true
                )
            }
        } else if (_uiState.value.expandDropdown && (_uiState.value.text.isEmpty() || _uiState.value.text.isBlank())) {
            _uiState.update {
                it.copy(
                    expandDropdown = false
                )
            }
        }

        println("done")
    }

    fun hideWord() {
        _uiState.update {
            it.copy(
                showWord = false
            )
        }
    }

    fun setDropdownExpanded(expanded: Boolean) {
        _uiState.update {
            it.copy(
                expandDropdown = expanded
            )
        }
    }

}

data class DictionaryUIState(
    val text: String = "",
    val curWord: Word? = null,
    val recommendedWord: List<String> = emptyList(),
    val expandDropdown: Boolean = false,
    val showWord: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)