package com.example.qgeni.data.model.response

data class PracticeSummaryResponse(
    val id: Int,
    val title: String,
    val creationDate: String,
    val isFavorite: Boolean,
    val isNew: Boolean,
    val highestScore: Float?,
    val highestDoneAt: String?,
    val latestScore: Float?,
    val latestDoneAt: String?
)