package com.example.qgeni.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.response.ReadingPracticeResponse
import java.time.LocalDateTime
import java.util.Date



data class ReadingPracticeItem(
    override val id: Int,
    override val title: String,
    val passage: String,
    override val creationDate: LocalDateTime,
    override val questionList: List<ReadingQuestion>
) : PracticeItem(id, title, creationDate, questionList) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromResponse(response: ReadingPracticeResponse): ReadingPracticeItem {
            return ReadingPracticeItem(
                id = response.id,
                title = response.title,
                passage = response.passage,
                creationDate = LocalDateTime.parse(response.creationDate),
                questionList = response.questionList
            )
        }
    }
}



data class ReadingQuestion(
    val statement: String,
    val answer: String,
    val explanation: String
)





