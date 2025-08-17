package com.example.qgeni.ui.screens.leaderboard

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.data.repositories.DefaultFavoriteRepository
import com.example.qgeni.data.repositories.DefaultHistoryRepository
import com.example.qgeni.data.repositories.DefaultListeningRepository
import com.example.qgeni.ui.screens.practices.PracticeListViewModel

open class LeaderboardListeningListViewModel: LeaderboardListViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPracticeItemList(): List<PracticeItemSummary> {
        return DefaultListeningRepository.getAllSummaries()
    }

    override suspend fun getAchievementItems(id: Int): List<Achievement> {
        return DefaultHistoryRepository.getListeningPracticeRanking(id)
    }
}
