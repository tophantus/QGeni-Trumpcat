package com.example.qgeni.data.repositories

import com.example.qgeni.data.model.word.Word
import com.example.qgeni.data.network.ApiClient
import retrofit2.Response

interface WordRepository {
    suspend fun getWords(): List<Word>
    suspend fun getWord(text: String): Word
    suspend fun getSuggestions(query: String): Response<List<String>>
}

object DefaultWordRepository : WordRepository{
    private val api = ApiClient.getWordApiService()

    override suspend fun getWords(): List<Word> {
        return api.getWords()
    }

    override suspend fun getWord(text: String): Word {
        return api.getWord(text)
    }

    override suspend fun getSuggestions(query: String): Response<List<String>> {
        return api.getSuggestions(query)
    }


}