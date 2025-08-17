package com.example.qgeni.data.network

import retrofit2.http.PATCH
import retrofit2.http.Path

interface FavoriteApiService {
    @PATCH("/api/favorite/reading/{id}")
    suspend fun changeReadingFavorite(@Path("id") id: Int)

    @PATCH("/api/favorite/listening/{id}")
    suspend fun changeListeningFavorite(@Path("id") id: Int)

    @PATCH("/api/favorite/word/{id}")
    suspend fun changeWordFavorite(@Path("id") id: Int)
}