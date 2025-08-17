package com.example.qgeni.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VerificationViewModel: ViewModel() {
    private val _verificationUIState = MutableStateFlow(VerificationUIState())
    val verificationUIState = _verificationUIState.asStateFlow()

    fun updateOtp(otp: String) {
        _verificationUIState.update {
            it.copy(
                otp = otp
            )
        }
    }

    fun verifyOtp(): Boolean {
        val enteredOtp = _verificationUIState.value.otp
        val otp = enteredOtp.map { chr -> chr.toString().toInt() }
        Log.e("VerificationViewModel", "OTP: $otp")
        val isCorrect = enteredOtp.length != 4
        _verificationUIState.update {
            it.copy(
                isOtpError = !isCorrect
            )
        }
        return isCorrect
    }
}

data class VerificationUIState(
    val otp: String = "",
    val isOtpError: Boolean = false,
)