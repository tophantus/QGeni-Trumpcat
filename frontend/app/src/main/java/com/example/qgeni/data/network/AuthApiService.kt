package com.example.qgeni.data.network

import com.example.qgeni.data.model.request.LoginRequest
import com.example.qgeni.data.model.request.RegisterRequest
import com.example.qgeni.data.model.response.MsgResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body body: LoginRequest) : MsgResponse

    @POST("/api/auth/register")
    suspend fun register(@Body body: RegisterRequest) : MsgResponse
}