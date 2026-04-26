package com.example.ada.data

data class Resumen(
    val supervisado_id: String,
    val nombre: String,
    val ultima_alerta: String?,
    val aplicacion: String?,
    val fecha_hora: String?
)
