package com.example.ada.data

data class ChatNinoUI(
    val id: String,
    val nombreCompleto: String,
    val ultimoMensaje: String,
    val hora: String,
    val appIcon: Int? = null
)
