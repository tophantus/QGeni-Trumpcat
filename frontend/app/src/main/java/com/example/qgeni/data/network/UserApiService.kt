package com.example.qgeni.data.network


import com.example.qgeni.data.model.request.ForgotPasswordRequest
import com.example.qgeni.data.model.request.UserInfoRequest
import com.example.qgeni.data.model.response.ForgotPasswordResponse
import com.example.qgeni.data.model.response.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {
    @GET("/api/user/info")
    suspend fun getInfo() : UserInfoResponse

    @PATCH("/api/user/info")
    suspend fun updateInfo(@Body body: UserInfoRequest) : UserInfoResponse

    @POST("/api/user/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): ForgotPasswordResponse
}