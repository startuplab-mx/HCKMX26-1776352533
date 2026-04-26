package com.example.ada.data.remote

import com.example.ada.data.model.CrearCodigoResponse
import com.example.ada.data.model.CrearEquipoRequest
import com.example.ada.data.model.CrearEquipoResponse
import com.example.ada.data.model.CrearSupervisadoRequest
import com.example.ada.data.model.CrearSupervisadoResponse
import com.example.ada.data.model.CrearSupervisorRequest
import com.example.ada.data.model.CrearSupervisorResponse
import com.example.ada.data.model.LoginRequest
import com.example.ada.data.model.LoginResponse
import com.example.ada.data.model.ResponseCodigo
import com.example.ada.data.model.consultarEquipos
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.ada.data.model.CodigoVincular
interface ApiService {

    //SUPERVISADOS


    //EQUIPO
    //REGISTRO
    @POST("supervisado/equipo/crear/{id}")
    suspend fun crearEquipo(
        @Path("id") id: String,
        @Body request: CrearEquipoRequest
    ): Response<CrearEquipoResponse>

    //CONSULTAR EQUIPO
    @GET("supervisado/equipo/{id}")
    suspend fun consultarEquipo(
        @Path("id") id: String
    ): Response<consultarEquipos>

    @POST("supervisor/supervisado/registrado/")
    suspend fun crearSupervisado(
        @Body request: CrearSupervisadoRequest
    ): Response<CrearSupervisadoResponse>

    @POST("supervisor/registro/")

    suspend fun crearSupervisor(
        @Body request: CrearSupervisorRequest
    ): Response<CrearSupervisorResponse>


    @POST("supervisor/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

//VINCULAR CODIGO
@POST("supervisor/vincular/{id}")
suspend fun vincularSupervisor(
    @Path("id") id: String,
    @Body request: CodigoVincular
): Response<ResponseCodigo>


    @POST("supervisado/codigo/vinculacion/{id}")
    suspend fun crearCodigo(
        @Path("id") id: String,
    ): Response<CrearCodigoResponse>
}