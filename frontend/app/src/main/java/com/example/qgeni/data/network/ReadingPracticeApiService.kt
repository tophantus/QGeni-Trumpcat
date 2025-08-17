package com.example.qgeni.data.network

import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.model.ReadingPracticeItem
import com.example.qgeni.data.model.response.PracticeSummaryResponse
import com.example.qgeni.data.model.response.ReadingPracticeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReadingPracticeApiService {
    @GET("/api/practice/reading")
    suspend fun getReadingPracticeSummaries(): List<PracticeSummaryResponse>

    @GET("/api/practice/reading/{id}")
    suspend fun getReadingPractice(@Path("id") id: Int): ReadingPracticeResponse
}