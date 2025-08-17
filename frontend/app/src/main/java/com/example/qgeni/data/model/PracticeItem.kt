package com.example.qgeni.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.response.PracticeSummaryResponse
import java.time.LocalDateTime
import java.util.Date

data class PracticeItemSummary(
    val id: Int,
    val title: String,
    val creationDate: LocalDateTime,
    var isFavorite: Boolean,
    val isNew: Boolean,
    val highestScore: Float?,
    val highestDoneAt: LocalDateTime?,
    val latestScore: Float?,
    val latestDoneAt: LocalDateTime?
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromResponse(response: PracticeSummaryResponse): PracticeItemSummary {
            return PracticeItemSummary(
                id = response.id,
                title = response.title,
                creationDate = LocalDateTime.parse(response.creationDate),
                isFavorite = response.isFavorite,
                isNew = response.isNew,
                highestScore = response.highestScore,
                highestDoneAt = if (response.highestDoneAt != null) {
                    LocalDateTime.parse(response.highestDoneAt)
                } else { null },
                latestScore = response.latestScore,
                latestDoneAt = if (response.latestDoneAt != null) {
                    LocalDateTime.parse(response.latestDoneAt)
                } else { null }
            )
        }
    }
}

open class PracticeItem(
    open val id: Int,
    open val title: String,
    open val creationDate: LocalDateTime,
    open val questionList: List<Any>
)

