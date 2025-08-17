package com.example.qgeni.data.repositories

import com.example.qgeni.data.network.ApiClient

interface FavoriteRepository {
    suspend fun changeReadingFavorite(id: Int)

    suspend fun changeListeningFavorite(id: Int)

    suspend fun changeWordFavorite(id: Int)
}

object DefaultFavoriteRepository : FavoriteRepository {
    private val api = ApiClient.getFavoriteApiService()

    override suspend fun changeReadingFavorite(id: Int) {
        api.changeReadingFavorite(id)
    }

    override suspend fun changeListeningFavorite(id: Int) {
        api.changeListeningFavorite(id)
    }

    override suspend fun changeWordFavorite(id: Int) {
        api.changeWordFavorite(id)
    }

}