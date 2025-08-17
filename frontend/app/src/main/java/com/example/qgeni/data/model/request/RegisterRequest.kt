package com.example.qgeni.data.model.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String
)
