package com.example.qgeni.data.network

import com.example.qgeni.data.model.response.ListeningPracticeResponse
import com.example.qgeni.data.model.response.PracticeSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ListeningPracticeApiService {

    @GET("/api/practice/listening")
    suspend fun getListeningPracticeSummaries(): List<PracticeSummaryResponse>

    @GET("api/practice/listening/{id}")
    suspend fun getListeningPractice(@Path("id") id: Int): ListeningPracticeResponse


}