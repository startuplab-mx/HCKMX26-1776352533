package com.example.ada.data

data class Supervisado(
    val id: String,
    val supervisor_id: String?, //Puede ser null
    val nombre: String,
    val appat: String,
    val apmat: String,
    val fecha_nacimiento: String,
    val created_at: String,
    val curp: String
)