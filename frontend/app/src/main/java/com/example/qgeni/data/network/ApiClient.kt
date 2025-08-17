package com.example.qgeni.data.network

import com.example.qgeni.BuildConfig
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {
    const val BASE_URL = BuildConfig.BASE_URL

    fun getAuthApiService(): AuthApiService {
        val retrofit =  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthApiService::class.java)
    }

    private fun <T> initServiceWithInterceptor(service: Class<T> ): T {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .connectionPool(ConnectionPool(0,1, TimeUnit.NANOSECONDS))
            .retryOnConnectionFailure(true)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // dùng client có interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(service)
    }


    fun getWordApiService(): WordApiService {
        return initServiceWithInterceptor(WordApiService::class.java)
    }

    fun getListeningPracticeApiService(): ListeningPracticeApiService {
        return initServiceWithInterceptor(ListeningPracticeApiService::class.java)
    }

    fun getReadingPracticeApiService(): ReadingPracticeApiService {
        return initServiceWithInterceptor(ReadingPracticeApiService::class.java)
    }

    fun getUserApiService(): UserApiService {
        return initServiceWithInterceptor(UserApiService::class.java)
    }

    fun getFavoriteApiService(): FavoriteApiService {
        return initServiceWithInterceptor(FavoriteApiService::class.java)
    }

    fun getHistoryApiService(): HistoryApiService {
        return initServiceWithInterceptor(HistoryApiService::class.java)
    }

    fun getFileApiService(): FileApiService {
        return initServiceWithInterceptor(FileApiService::class.java)
    }

    fun getChatApiService(): ChatApiService {
        return initServiceWithInterceptor(ChatApiService::class.java)
    }
}
