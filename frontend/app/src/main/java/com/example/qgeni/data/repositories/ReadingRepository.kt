package com.example.qgeni.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.PracticeItem
import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.model.ReadingPracticeItem
import com.example.qgeni.data.network.ApiClient


interface ReadingRepository : PracticeRepository {

}


object DefaultReadingRepository : ReadingRepository {
    private val api = ApiClient.getReadingPracticeApiService()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllSummaries(): List<PracticeItemSummary> {
        val responseList = api.getReadingPracticeSummaries()
        return responseList.map {
            PracticeItemSummary.fromResponse(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getItem(
        id: Int
    ): PracticeItem {
        val response = api.getReadingPractice(id)
        return ReadingPracticeItem.fromResponse(
            response
        )
    }

}


