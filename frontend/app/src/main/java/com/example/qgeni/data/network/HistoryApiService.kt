package com.example.qgeni.data.network

import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.data.model.request.CreatePracticeHistoryRequest
import com.example.qgeni.data.model.response.CreatePracticeHistoryResponse
import com.example.qgeni.data.model.response.MsgResponse
import com.example.qgeni.data.model.response.PracticeHistoryResponse
import com.example.qgeni.data.model.response.PracticeHistorySummaryResponse
import com.example.qgeni.data.model.word.WordAccessHistory
import com.example.qgeni.ui.screens.profile.DayStat
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryApiService {
    /**
     * Return the summarized histories of one Reading
     */
    @GET("/api/history/reading/{id}")
    suspend fun getOneReadingHistories(@Path("id") id: Int): List<PracticeHistorySummaryResponse>

    /**
     * Return the summarized histories of one Listening
     */
    @GET("/api/history/listening/{id}")
    suspend fun getOneListeningHistories(@Path("id") id: Int): List<PracticeHistorySummaryResponse>

    /**
     * Return the all the histories of all the Readings
     */
    @GET("/api/history/reading")
    suspend fun getAllReadingsHistories(): List<PracticeHistoryResponse>

    /**
     * Return all the histories of all the Listenings
     */
    @GET("/api/history/listening")
    suspend fun getAllListeningHistories(): List<PracticeHistoryResponse>


    @GET("/api/history/word")
    suspend fun getAllWordAccessHistories(): List<WordAccessHistory>

    @POST("/api/history/word/{id}")
    suspend fun createWordAccessHistory(@Path("id") wordId: Int): Response<Unit>

    @DELETE("/api/history/word/{id}")
    suspend fun deleteWordAccessHistory(@Path("id") wordId: Int): Response<Unit>

    @POST("/api/history/listening/{id}")
    suspend fun createListeningHistory(
        @Path("id") id: Int,
        @Body body: CreatePracticeHistoryRequest
    ): CreatePracticeHistoryResponse

    @POST("/api/history/reading/{id}")
    suspend fun createReadingHistory(
        @Path("id") id: Int, @Body body:
        CreatePracticeHistoryRequest
    ): CreatePracticeHistoryResponse

    @GET("/api/history")
    suspend fun getStats(@Query("start_iso") start: String, @Query("end_iso") end: String): List<DayStat>

    @GET("/api/history/listening/{id}/ranking")
    suspend fun getListeningPracticeRanking(@Path("id") id: Int): List<Achievement>

    @GET("/api/history/reading/{id}/ranking")
    suspend fun getReadingPracticeRanking(@Path("id") id: Int): List<Achievement>
}