package com.example.qgeni.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUIState())
    val uiState = _uiState.asStateFlow()

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updateRePassword(reenteredPassword: String) {
        _uiState.update {
            it.copy(
                rePassword = reenteredPassword
            )
        }
    }

    fun changePasswordVisibility() {
        _uiState.update {
            it.copy(
                passwordVisible = !it.passwordVisible
            )
        }
    }

    fun changeRePasswordVisibility() {
        _uiState.update {
            it.copy(
                rePasswordVisible = !it.rePasswordVisible
            )
        }
    }

    fun resetPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            //DefaultAccountRepository.resetPassword(_uiState.value.password)
        }
    }
}

data class ResetPasswordUIState(
    val password: String = "",
    val rePassword: String = "",
    val passwordVisible: Boolean = false,
    val rePasswordVisible: Boolean = false,
) {
    val isPasswordSame: Boolean
        get() = password == rePassword
}