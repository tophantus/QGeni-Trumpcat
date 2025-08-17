package com.example.qgeni.utils

import android.content.Context
import java.io.File

object ContextConstants {
    lateinit var cacheDir: File

    private var otp = List(4) { (0..9).random() }
    private var currentEmail = ""

    fun init(context: Context) {
        cacheDir = context.cacheDir
    }

    fun setOtp(otp: List<Int>) {
        this.otp = otp
    }

    fun checkOtp(otp: List<Int>): Boolean {
        return this.otp == otp
    }

    fun setCurrentEmail(email: String) {
        currentEmail = email
    }

    fun getEmailForReset(): String {
        return currentEmail
    }
}