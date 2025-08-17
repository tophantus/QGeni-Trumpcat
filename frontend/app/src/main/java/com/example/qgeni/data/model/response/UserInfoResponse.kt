package com.example.qgeni.data.model.response

import java.time.LocalDateTime

data class UserInfoResponse (
    val id: Int,
    val username: String,
    val email: String,
    val phoneNumber: String,
    //val createdAt: LocalDateTime
)