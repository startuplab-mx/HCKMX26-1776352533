package com.example.ada.repository

import com.example.ada.data.model.LoginRequest
import com.example.ada.data.model.LoginResponse
import com.example.ada.data.remote.RetrofitClient
import retrofit2.Response

class AuthRepository {

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return RetrofitClient.api.login(
            LoginRequest(email, password)
        )
    }
}