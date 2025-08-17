package com.example.qgeni.ui.screens.mascot


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.model.mascot.Mascot
import com.example.qgeni.data.model.mascot.MascotRepository
import com.example.qgeni.data.model.request.ChatRequest
import com.example.qgeni.data.repositories.DefaultChatRepository
import com.example.qgeni.ui.screens.mascot.MascotPreferences.saveMascotVisibility
import com.example.qgeni.ui.screens.mascot.MascotPreferences.shouldShowMascot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MascotViewModel(application: Application) : AndroidViewModel(application) {

    private val _mascotUiState = MutableStateFlow(MascotUiState())
    val mascotUiState = _mascotUiState.asStateFlow()
    private val clickTimestamps = ArrayDeque<Long>()

    init {
        viewModelScope.launch {
            _mascotUiState.update {
                it.copy(
                    curMascot = MascotRepository.mascot,
                    showMascot = shouldShowMascot()
                )
            }
        }
        observeIdleBubble()
    }

    fun toggleMascotVisibility(show: Boolean) {
        _mascotUiState.update {
            it.copy(showMascot = show)
        }
        saveMascotVisibility(show)
    }

    fun restoreLastOpenState(context: Context) {
        viewModelScope.launch {
            val prefs = context.getSharedPreferences("mascot_prefs", Context.MODE_PRIVATE)
            val lastOpen = prefs.getLong("last_open", 0)
            val now = System.currentTimeMillis()
            if (now - lastOpen > 2 * 86400000L) {
                val cur = _mascotUiState.value.curMascot
                _mascotUiState.update {
                    it.copy(
                        animationState = cur.sadLottie,
                        bubbleText = "Bỏ mình hơn 2 ngày rồi đó 😢",
                        showBubble = true
                    )
                }
            }
        }
    }

    fun updateOffset(dx: Float, dy: Float) {
        _mascotUiState.update {
            it.copy(
                offsetX = it.offsetX + dx,
                offsetY = it.offsetY + dy
            )
        }
    }

    fun showBubble(message: String) {
        _mascotUiState.update {
            it.copy(
                showBubble = true,
                bubbleText = message
            )
        }
    }

    private fun observeIdleBubble() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                val msg = getRandomBubbleContent()
                _mascotUiState.update {
                    it.copy(showBubble = true, bubbleText = msg)
                }
                delay(5000)
                _mascotUiState.update {
                    it.copy(showBubble = false)
                }
            }
        }
    }

    fun onMascotClicked() {
        val now = System.currentTimeMillis()
        clickTimestamps.add(now)

        // Xoá click cũ hơn 2s
        while (clickTimestamps.isNotEmpty() && now - clickTimestamps.first() > 2000) {
            clickTimestamps.removeFirst()
        }

        val state = _mascotUiState.value
        if (!state.showBubble && !state.isAngry && clickTimestamps.size >= 5) {
            clickTimestamps.clear()
            triggerAngry()
        }
    }

    fun hideBubble() {
        _mascotUiState.update { it.copy(showBubble = false) }
    }

    fun setDialogVisible(visible: Boolean) {
        _mascotUiState.update { it.copy(showDialog = visible) }
    }

    fun setAnimationState(resId: Int) {
        _mascotUiState.update { it.copy(animationState = resId) }
    }

    private fun triggerAngry() {
        _mascotUiState.update {
            it.copy(
                isAngry = true,
                animationState = it.curMascot.angryLottie,
                showBubble = true,
                bubbleText = "Bình tĩnh đi nào 😠"
            )
        }

        viewModelScope.launch {
            delay(3000)
            _mascotUiState.update {
                it.copy(
                    isAngry = false,
                    animationState = it.curMascot.idleLottie,
                    showBubble = false
                )
            }
        }
    }

    fun openChat(showChat: Boolean) {
        _mascotUiState.update {
            it.copy(showChat = showChat)
        }
    }

    fun onUserInputChange(input: String) {
        _mascotUiState.update {
            it.copy(
                userInput = input
            )
        }
    }

    fun sendUserInput() {
        val userInput = _mascotUiState.value.userInput
        if (userInput.trim().isEmpty()) {
            return
        }

        _mascotUiState.update {
            val oldChatHistories = it.chatHistories.toMutableList()
            oldChatHistories.add(Pair(userInput, true))

            it.copy(
                chatHistories = oldChatHistories,
                userInput = ""
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val chatId = _mascotUiState.value.chatId
            try {
                val res = DefaultChatRepository.sendChat(
                    ChatRequest(
                        prompt = userInput,
                        isNew = chatId == null,
                        chatId = chatId
                    )
                )

                println("Res__ $res")
                _mascotUiState.update {
                    val chatHistories = it.chatHistories.toMutableList()
                    chatHistories.add(Pair(res.response.toString(), false))

                    it.copy(
                        chatHistories = chatHistories,
                        chatId = res.chatId
                    )
                }

                println(_mascotUiState.value.chatHistories)
            } catch (e: Exception) {
                _mascotUiState.update {
                    val chatHistories = it.chatHistories.toMutableList()
                    chatHistories.add(Pair("⚠\uFE0F Lỗi Server \uD83D\uDE1E", false))

                    it.copy(
                        chatHistories = chatHistories
                    )
                }
                e.printStackTrace()
            }
        }
    }

    fun newChat() {
        _mascotUiState.update {
            it.copy(
                chatId = null,
                chatHistories = listOf(),
                userInput = ""
            )
        }
    }
}

data class MascotUiState(
    var curMascot: Mascot = MascotRepository.mascot,
    val offsetX: Float = 200f,
    val offsetY: Float = 800f,
    val showBubble: Boolean = false,
    val bubbleText: String = "",
    val showDialog: Boolean = false,
    val animationState: Int = MascotRepository.mascot.idleLottie,
    val isAngry: Boolean = false,
    val showMascot: Boolean = false,

    val chatId: Int? = null,
    val showChat: Boolean = false,
    val chatHistories: List<Pair<String, Boolean>> = listOf(),
    val userInput: String = ""
)

private fun getRandomBubbleContent(): String {
    val messages = listOf(
        "'abandon' nghĩa là 'từ bỏ'",
        "'I love learning English!'",
        "Học 15 từ mới mỗi ngày nhé!",
        "'benevolent' = nhân hậu",
        "Bạn có biết? 'Run' là 'chạy'",
        "Bạn có thể ẩn tôi ở trong cài đặt",
    )
    return messages.random()
}