package com.example.qgeni.data.repositories

import com.example.qgeni.data.model.PracticeItem
import com.example.qgeni.data.model.PracticeItemSummary

interface PracticeRepository {
    suspend fun getAllSummaries() : List<PracticeItemSummary>

    suspend fun getItem(id: Int): PracticeItem
}