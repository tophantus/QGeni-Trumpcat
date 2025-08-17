package com.example.qgeni.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.model.request.RegisterRequest
import com.example.qgeni.data.repositories.DefaultUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }
    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun togglePasswordVisible() {
        _uiState.update {
            it.copy(
                passwordVisible = !it.passwordVisible
            )
        }
    }

    fun toggleTermsAccepted() {
        _uiState.update {
            it.copy(
                termsAccepted = !it.termsAccepted
            )
        }
    }

    fun showSuccessDialog(isShow: Boolean) {
        _uiState.update {
            it.copy(
                showSuccessDialog = isShow
            )
        }
    }

    fun signUp() {

        viewModelScope.launch {
            val request = RegisterRequest(
                username = _uiState.value.username,
                password = _uiState.value.password,
                email = _uiState.value.email.trim().ifEmpty {
                    null
                } ?: "",
                phoneNumber = _uiState.value.phoneNumber.trim().ifEmpty {
                    null
                } ?: ""
            )
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                val res = withContext(Dispatchers.IO) {
                    DefaultUserRepository.register(request)
                }
                if (res.status == "SUCCESS") {
                    _uiState.update {
                        it.copy(
                            showSuccessDialog = true,
                        )
                    }
                }
                if (res.status == "FAILED") {
                    println("SignUpError: " + res.msg)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            } catch (e : Exception) {
                println("SignUpError: " + e.message)
                _uiState.update {
                    it.copy(
                        showSuccessDialog = true,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun reset() {
        _uiState.update {
            it.copy(
                isAccountError = false,
                isPasswordError = false,
                isEmailError = false,
                isPhoneNumberError = false
            )
        }
    }

    fun checkEmpty(): Boolean {
        return !_uiState.value.isAccountError && !_uiState.value.isPasswordError && !_uiState.value.isEmailError && !_uiState.value.isPhoneNumberError
    }

    fun updateConstraint() {
        _uiState.update {
            it.copy(
                isAccountError = it.username == "",
                isPasswordError = it.password == "",
                isEmailError = it.email == "",
                isPhoneNumberError = it.phoneNumber == ""
            )
        }
    }
}

data class SignUpUIState(
    val username: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val termsAccepted: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val isAccountError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isEmailError: Boolean = false,
    val isPhoneNumberError: Boolean = false,
    val isLoading: Boolean = false
)