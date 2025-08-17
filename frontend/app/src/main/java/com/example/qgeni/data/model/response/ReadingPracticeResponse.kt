package com.example.qgeni.data.model.response

import com.example.qgeni.data.model.ReadingQuestion

data class ReadingPracticeResponse(
    val id: Int,
    val title: String,
    val passage: String,
    val creationDate: String,
    val questionList: List<ReadingQuestion>
) {
}