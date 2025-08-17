package com.example.qgeni.ui.screens.leaderboard

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.qgeni.data.model.PracticeItemSummary
import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.data.repositories.DefaultHistoryRepository
import com.example.qgeni.data.repositories.DefaultListeningRepository
import com.example.qgeni.data.repositories.DefaultReadingRepository

open class LeaderboardReadingListViewModel: LeaderboardListViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPracticeItemList(): List<PracticeItemSummary> {
        return DefaultReadingRepository.getAllSummaries()
    }

    override suspend fun getAchievementItems(id: Int): List<Achievement> {
        return DefaultHistoryRepository.getReadingPracticeRanking(id)
    }
}