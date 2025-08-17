package com.example.qgeni.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.model.request.UserInfoRequest
import com.example.qgeni.data.repositories.DefaultUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeInfoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChangeInfoUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val userInfo = withContext(Dispatchers.IO) {
                    DefaultUserRepository.getInfo()
                }
                _uiState.update {
                    it.copy(
                        username = userInfo.username,
                        phoneNumber = userInfo.phoneNumber,
                        email = userInfo.email,
                    )
                }

            } catch (e: Exception) {
                Log.e("Error loading userinfo", "Error: ${e.message}", e)
            }
        }
    }
    private fun loadUserInfo() {
        viewModelScope.launch {
            try {
                val userInfo = withContext(Dispatchers.IO) {
                    DefaultUserRepository.getInfo()
                }
                _uiState.update {
                    it.copy(
                        username = userInfo.username,
                        phoneNumber = userInfo.phoneNumber,
                        email = userInfo.email,
                    )
                }
                
            } catch (e: Exception) {
                Log.e("Error loading userinfo", "Error: ${e.message}", e)
            }
        }
    }

    fun updateUserInfo(username: String?, phoneNumber: String?, email: String?) {
        viewModelScope.launch {
            try {
                val userInfoRequest = UserInfoRequest(
                    username = username ?: "",
                    phoneNumber = phoneNumber ?: "",
                    email = email ?: ""
                )
                val isUpdated = withContext(Dispatchers.IO) {
                    DefaultUserRepository.updateInfo(userInfoRequest)
                }
                if (isUpdated != null) {
                    // Nếu cập nhật thành công, tải lại thông tin người dùng
                    loadUserInfo()
                } else {
                    // Xử lý nếu cập nhật không thành công
                }
            } catch (e: Exception) {
                println("E: ${e.message}")
            }
        }
    }
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

    fun updateGender(gender: String) {
        _uiState.update {
            it.copy(
                gender = gender
            )
        }
    }

}

data class ChangeInfoUIState(
    val username: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = ""
)