package com.example.qgeni.data.model.response

import com.example.qgeni.data.model.ListeningQuestion

data class ListeningPracticeResponse(
    val id: Int,
    val title: String,
    val creationDate: String,
    val questionList: List<ListeningQuestion>
)