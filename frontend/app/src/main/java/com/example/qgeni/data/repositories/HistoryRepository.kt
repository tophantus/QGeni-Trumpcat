package com.example.qgeni.data.repositories


import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.data.model.request.CreatePracticeHistoryRequest
import com.example.qgeni.data.model.response.PracticeHistoryResponse
import com.example.qgeni.data.model.response.PracticeHistorySummaryResponse
import com.example.qgeni.data.model.word.WordAccessHistory
import com.example.qgeni.data.network.ApiClient
import com.example.qgeni.ui.screens.profile.DayStat


interface HistoryRepository {
    /**
     * Return the summarized histories of one Reading
     */
    suspend fun getOneReadingHistories(id: Int): List<PracticeHistorySummaryResponse>

    /**
     * Return the summarized histories of one Listening
     */
    suspend fun getOneListeningHistories(id: Int): List<PracticeHistorySummaryResponse>

    /**
     * Return the all the histories of all the Readings
     */
    suspend fun getAllReadingsHistories(): List<PracticeHistoryResponse>

    /**
     * Return all the histories of all the Listening
     */
    suspend fun getAllListeningHistories(): List<PracticeHistoryResponse>


    suspend fun getAllWordAccessHistories(): List<WordAccessHistory>

    suspend fun createWordAccessHistory(wordId: Int)

    suspend fun deleteWordAccessHistory(wordId: Int)

    suspend fun createListeningHistory(id: Int, score: Float, durationSecond: Long): Float

    suspend fun createReadingHistory(id: Int, score: Float, durationSecond: Long): Float

    suspend fun getStats(start: String, end: String): List<DayStat>

    suspend fun getListeningPracticeRanking(id: Int): List<Achievement>

    suspend fun getReadingPracticeRanking(id: Int): List<Achievement>
}

object DefaultHistoryRepository: HistoryRepository {
    private val api = ApiClient.getHistoryApiService()

    override suspend fun getOneReadingHistories(id: Int): List<PracticeHistorySummaryResponse> {
        return api.getOneReadingHistories(id)
    }

    override suspend fun getOneListeningHistories(id: Int): List<PracticeHistorySummaryResponse> {
        return api.getOneListeningHistories(id)
    }

    override suspend fun getAllReadingsHistories(): List<PracticeHistoryResponse> {
        return api.getAllReadingsHistories()
    }

    override suspend fun getAllListeningHistories(): List<PracticeHistoryResponse> {
        return api.getAllListeningHistories()
    }

    override suspend fun getAllWordAccessHistories(): List<WordAccessHistory> {
        return api.getAllWordAccessHistories()
    }

    override suspend fun createWordAccessHistory(wordId: Int) {
        api.createWordAccessHistory(wordId)
    }

    override suspend fun deleteWordAccessHistory(wordId: Int) {
        api.deleteWordAccessHistory(wordId)
    }

    override suspend fun createListeningHistory(id: Int, score: Float, durationSecond: Long): Float {
        return api.createListeningHistory(
            id,
            CreatePracticeHistoryRequest(score, durationSecond)
        ).totalScore
    }

    override suspend fun createReadingHistory(id: Int, score: Float, durationSecond: Long): Float {
        return api.createReadingHistory(
            id,
            CreatePracticeHistoryRequest(score, durationSecond)
        ).totalScore
    }

    override suspend fun getStats(start: String, end: String): List<DayStat> {
        return api.getStats(start, end)
    }

    override suspend fun getListeningPracticeRanking(id: Int): List<Achievement> {
        return api.getListeningPracticeRanking(id)
    }

    override suspend fun getReadingPracticeRanking(id: Int): List<Achievement> {
        return api.getReadingPracticeRanking(id)
    }


}