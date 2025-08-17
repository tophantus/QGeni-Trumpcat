package com.example.qgeni.ui.screens.practices

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.repositories.DefaultFavoriteRepository
import com.example.qgeni.data.repositories.DefaultReadingRepository

open class ReadingPracticeListViewModel : PracticeListViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPracticeItemList(): List<PracticeItemSummary> {
        return DefaultReadingRepository.getAllSummaries()
    }

    override suspend fun changeFavorite(id: Int) {
        DefaultFavoriteRepository.changeReadingFavorite(id)
    }
}