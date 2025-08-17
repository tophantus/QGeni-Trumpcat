package com.example.qgeni.ui.screens.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.model.response.UserInfoResponse
import com.example.qgeni.data.preferences.JwtPreferenceManager
import com.example.qgeni.data.repositories.DefaultUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    fun logOut(context: Context) {
        viewModelScope.launch {
            JwtPreferenceManager.clearJwt(context)
        }
    }

    private val _userInfo = MutableStateFlow<UserInfoResponse?>(null)
    val userInfo = _userInfo.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userInfo = DefaultUserRepository.getInfo()
                Log.i("Debug loading userinfo", userInfo.toString())
                _userInfo.value = userInfo
            } catch (e: Exception) {
                Log.e("Error loading userinfo", "Error: ${e.message}", e)
            }
        }
    }

}
