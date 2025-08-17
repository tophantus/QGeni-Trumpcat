package com.example.qgeni.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FileApiService {
    @GET("/api/file/<name>")
    suspend fun getFileStream(@Path("name") name: String): Response<ResponseBody>
}