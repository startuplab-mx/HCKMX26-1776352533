package com.example.ada.data.remote

import com.example.ada.data.model.CrearSupervisadoRequest
import com.example.ada.data.model.CrearSupervisadoResponse
import com.example.ada.data.model.LoginRequest
import com.example.ada.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    //SUPERVISADOS

    @POST("supervisor/supervisado/registrado/")
    suspend fun crearSupervisado(
        @Body request: CrearSupervisadoRequest
    ): Response<CrearSupervisadoResponse>


    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}