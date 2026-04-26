package com.example.ada.data.model

import com.example.ada.data.Resumen

data class ResumenResponse(
    val total: Int,
    val data: List<Resumen>
)
