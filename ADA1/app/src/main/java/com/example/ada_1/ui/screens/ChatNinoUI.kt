package com.example.ada.ui.screens

data class ChatNinoUI(
    val id: String,
    val nombreCompleto: String,
    val ultimoMensaje: String,
    val hora: String,
    val appIcon: Int? = null
)
