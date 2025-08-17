package com.example.qgeni.data.model.response

data class ChatResponse(
    val response: String?,
    val msg: String?,
    val chatId: Int?,
    val status: String
) {
}