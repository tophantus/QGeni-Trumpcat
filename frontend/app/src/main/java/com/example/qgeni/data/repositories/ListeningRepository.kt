package com.example.qgeni.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.ListeningPracticeItem
import com.example.qgeni.data.model.PracticeItem
import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.network.ApiClient


interface ListeningRepository : PracticeRepository {

}

object DefaultListeningRepository : ListeningRepository {
    private val api = ApiClient.getListeningPracticeApiService()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getItem(id: Int): PracticeItem {
        val response = api.getListeningPractice(id)
        return ListeningPracticeItem.fromResponse(
            response
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllSummaries(): List<PracticeItemSummary> {
        val responseList = api.getListeningPracticeSummaries()
        println("Done")
        return responseList.map {
            PracticeItemSummary.fromResponse(it)
        }
    }
}