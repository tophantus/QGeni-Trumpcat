package com.example.qgeni.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.AppContextHolder
import com.example.qgeni.data.model.request.LoginRequest
import com.example.qgeni.data.preferences.JwtPreferenceManager
import com.example.qgeni.data.preferences.UserPreferenceManager
import com.example.qgeni.data.repositories.DefaultUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUIState())
    val uiState = _uiState.asStateFlow()

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

    fun reset() {
        _uiState.update {
            it.copy(
                isFailure = false,
                isAccountError = false,
                isPasswordError = false,
            )
        }
    }
    fun hideError() {
        _uiState.update {
            it.copy(
                signInEvent = SignInEvent.IDLE
            )
        }
    }

    fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            val loginRequest = LoginRequest(
                email = _uiState.value.email.trim(),
                password = _uiState.value.password
            )

            println("login " + uiState.value.email.trim() + "  " + uiState.value.password)
            try {
                _uiState.update {
                    it.copy(
                        signInEvent = SignInEvent.LOADING
                    )
                }
                val res = DefaultUserRepository.login(loginRequest)


                val jwt = res.accessToken
                println(jwt)
                if (jwt != null) {
                    JwtPreferenceManager.saveJwt(AppContextHolder.appContext, jwt)

                    println("Token: $jwt")

                    val info = DefaultUserRepository.getInfo()

                    UserPreferenceManager.saveUserId(AppContextHolder.appContext, info.id)

                    _uiState.update {
                        it.copy(
                            signInEvent = SignInEvent.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            signInEvent = SignInEvent.FAILURE
                        )
                    }
                }
            } catch (e : Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        signInEvent = SignInEvent.FAILURE
                    )
                }
            }
        }
    }

    fun checkEmpty(): Boolean {
        return !_uiState.value.isAccountError and !_uiState.value.isPasswordError
    }

    fun updateConstraint() {
        _uiState.update {
            it.copy(
                isAccountError = (it.email == ""),
                isPasswordError = (it.password == "")
            )
        }
    }
}

data class SignInUIState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val signInEvent: SignInEvent = SignInEvent.IDLE,
    val isAccountError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isFailure: Boolean = false
)

sealed interface SignInEvent {
    data object IDLE : SignInEvent
    data object SUCCESS : SignInEvent
    data object FAILURE : SignInEvent
    data object LOADING: SignInEvent
}