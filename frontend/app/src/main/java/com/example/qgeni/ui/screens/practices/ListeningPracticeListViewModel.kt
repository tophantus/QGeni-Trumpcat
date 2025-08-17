package com.example.qgeni.ui.screens.practices

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.preferences.UserPreferenceManager
import com.example.qgeni.data.repositories.DefaultFavoriteRepository
import com.example.qgeni.data.repositories.DefaultListeningRepository

open class ListeningPracticeListViewModel : PracticeListViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPracticeItemList(): List<PracticeItemSummary> {
        return DefaultListeningRepository.getAllSummaries()
    }

    override suspend fun changeFavorite(id: Int) {
        DefaultFavoriteRepository.changeListeningFavorite(id)
    }

}