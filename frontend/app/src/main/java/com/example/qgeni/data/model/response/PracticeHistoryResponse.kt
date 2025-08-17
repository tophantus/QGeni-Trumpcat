package com.example.qgeni.data.model.response

import java.time.LocalDateTime

data class PracticeHistorySummaryResponse(
    val score: Float,
    val doneAt: LocalDateTime
) {
}

data class PracticeHistoryResponse(
    val id: Int,
    val title: String,
    val score: Float,
    val doneAt: LocalDateTime
)