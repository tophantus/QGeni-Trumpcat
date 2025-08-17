package com.example.qgeni.data.network

import com.example.qgeni.AppContextHolder
import com.example.qgeni.data.preferences.JwtPreferenceManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = JwtPreferenceManager.getJwtFlow(AppContextHolder.appContext)

        val requestWithToken = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .header("Connection", "close")
            .build()
        return chain.proceed(requestWithToken)
    }
}
