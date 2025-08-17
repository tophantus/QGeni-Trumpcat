package com.example.qgeni.data.network

import com.example.qgeni.data.model.request.ChatRequest
import com.example.qgeni.data.model.response.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {
    @POST("/api/chatbot")
    suspend fun sendChat(@Body body: ChatRequest): ChatResponse
}