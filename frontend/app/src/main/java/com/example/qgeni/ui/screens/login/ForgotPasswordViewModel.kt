package com.example.qgeni.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.model.request.ForgotPasswordRequest
import com.example.qgeni.data.repositories.DefaultUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel: ViewModel() {
    private val _forgotPasswordUIState = MutableStateFlow(ForgotPasswordUIState())
    val forgotPasswordUIState = _forgotPasswordUIState.asStateFlow()

    fun updateEmail(email: String) {
        _forgotPasswordUIState.update {
            it.copy(
                email = email,
                emailStatus = if (email.isEmpty()) {
                    EmailStatus.EMPTY
                } else {
                    EmailStatus.IDLE
                }
            )
        }
    }

    fun updateConstraint() {
        if (_forgotPasswordUIState.value.email == "") {
            _forgotPasswordUIState.update {
                it.copy(
                    emailStatus = EmailStatus.EMPTY
                )
            }
        }
    }


    fun checkAndSend() {
        _forgotPasswordUIState.update {
            it.copy(
                loading = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val forgotPasswordRequest = ForgotPasswordRequest(
                email = _forgotPasswordUIState.value.email
            )
            try {
                val res = DefaultUserRepository.forgotPassword(forgotPasswordRequest)
                if (res.status == "SUCCESS") {
                    _forgotPasswordUIState.update {
                        it.copy(
                            emailStatus = EmailStatus.VALID,
                            loading = false
                        )
                    }
                } else {
                    _forgotPasswordUIState.update {
                        it.copy(
                            emailStatus = EmailStatus.INVALID,
                            loading = false
                        )
                    }
                }
            } catch (e : Exception) {
                print("ForgotPasswordError: " + e.message)
            }

        }
    }
}

data class ForgotPasswordUIState(
    val email: String = "",
    val emailStatus: EmailStatus = EmailStatus.EMPTY,
    val loading: Boolean = false
)

enum class EmailStatus {
    EMPTY,
    INVALID,
    VALID,
    IDLE
}