package com.example.qgeni.data.model.request

data class ChatRequest(
    val prompt: String,
    val isNew: Boolean = true,
    val chatId: Int? = null
) {
}