package com.example.ada.data.model

import com.example.ada.data.Supervisado
import java.util.Date

data class CrearSupervisadoResponse(
    val message: String,
    val data: List<Supervisado>
)
