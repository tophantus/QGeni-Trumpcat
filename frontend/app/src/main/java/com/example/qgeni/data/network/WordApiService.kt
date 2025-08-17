package com.example.qgeni.data.network

import com.example.qgeni.data.model.word.Word
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordApiService {
    @GET("/api/word")
    suspend fun getWords(
        @Query("pageIdx") pageIdx: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): List<Word>

    @GET("/api/word/{id}")
    suspend fun getWordById(@Path("id") id: Int): Word

    @DELETE("/api/word/{id}")
    suspend fun deleteWord(@Path("id") id: Int)

    @GET("/api/word/{text}")
    suspend fun getWord(@Path("text") text: String): Word

    @GET("/api/word/suggest/{query}")
    suspend fun getSuggestions(@Path("query") query: String): Response<List<String>>
}