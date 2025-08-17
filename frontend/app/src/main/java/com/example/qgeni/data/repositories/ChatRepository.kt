package com.example.qgeni.data.repositories

import com.example.qgeni.data.model.request.ChatRequest
import com.example.qgeni.data.model.response.ChatResponse
import com.example.qgeni.data.network.ApiClient

interface ChatRepository {
    suspend fun sendChat(request: ChatRequest): ChatResponse
}
object DefaultChatRepository : ChatRepository {
    private val apiService = ApiClient.getChatApiService()
    override suspend fun sendChat(request: ChatRequest): ChatResponse {
        return apiService.sendChat(request)
    }

}