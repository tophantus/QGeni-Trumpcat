package com.example.qgeni.data.repositories

import com.example.qgeni.data.model.request.ForgotPasswordRequest
import com.example.qgeni.data.model.request.LoginRequest
import com.example.qgeni.data.model.request.RegisterRequest
import com.example.qgeni.data.model.request.UserInfoRequest
import com.example.qgeni.data.model.response.ForgotPasswordResponse
import com.example.qgeni.data.model.response.MsgResponse
import com.example.qgeni.data.model.response.UserInfoResponse
import com.example.qgeni.data.network.ApiClient

interface UserRepository {
    suspend fun login(request: LoginRequest): MsgResponse
    suspend fun register(request: RegisterRequest): MsgResponse
    suspend fun getInfo(): UserInfoResponse
    suspend fun updateInfo(body: UserInfoRequest) : UserInfoResponse
    suspend fun forgotPassword(body: ForgotPasswordRequest) : ForgotPasswordResponse
}

object DefaultUserRepository : UserRepository {
    private val authApi = ApiClient.getAuthApiService()
    private val userApi = ApiClient.getUserApiService()
    override suspend fun login(request: LoginRequest): MsgResponse {
        return authApi.login(request)
    }
    override suspend fun register(request: RegisterRequest): MsgResponse {
        return authApi.register(request)
    }

    override suspend fun getInfo(): UserInfoResponse {
        return userApi.getInfo()
    }

    override suspend fun updateInfo(body: UserInfoRequest): UserInfoResponse {
        return userApi.updateInfo(body)
    }

    override suspend fun forgotPassword(body: ForgotPasswordRequest): ForgotPasswordResponse {
        return userApi.forgotPassword(body)
    }


}
/*
class MainActivity : AppCompatActivity() {

    private val apiManager = ApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            try {
                // GET
                val posts = apiManager.getAllPosts()
                Log.d("GET", "Tổng post: ${posts.size}")

                // POST
                val newPost = apiManager.createNewPost(
                    userId = 1,
                    title = "Tiêu đề Coroutine",
                    body = "Nội dung Coroutine"
                )
                Log.d("POST", "Tạo thành công ID: ${newPost.id}")

                // DELETE
                apiManager.deletePost(newPost.id)
                Log.d("DELETE", "Đã xoá post ID: ${newPost.id}")

            } catch (e: Exception) {
                Log.e("API_ERROR", "Lỗi: ${e.message}")
            }
        }
    }
}
*/
