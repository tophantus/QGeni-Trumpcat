package com.example.qgeni.data.repositories

import com.example.qgeni.data.network.ApiClient

interface FileRepository {
    suspend fun getFileStream(fileName: String)
    fun getAbsolutePath(fileName: String): String
}

object DefaultFileRepository: FileRepository {
    private val api = ApiClient.getFileApiService()
    override suspend fun getFileStream(fileName: String) {

    }

    override fun getAbsolutePath(fileName: String): String {
        return "${ApiClient.BASE_URL}/api/file/${fileName}"
    }

}