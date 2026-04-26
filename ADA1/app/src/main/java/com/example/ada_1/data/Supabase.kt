package com.example.ada_1.data


import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.Serializable

val supabaseUrl = "https://TU_PROYECTO.supabase.co"
val supabaseKey = "TU_KEY_PUBLICA_AQUI"

val supabase: SupabaseClient = createSupabaseClient(
    supabaseUrl = supabaseUrl,
    supabaseKey = supabaseKey
) {
    install(Postgrest)
}

@Serializable
data class Supervisado(
    val id: String,
    val nombre: String,
    val appat: String
)

@Serializable
data class Alerta(
    val mensaje_detectado: String,
    val fecha_hora: String
)

data class ChatNinoUI(
    val id: String,
    val nombreCompleto: String,
    val ultimoMensaje: String,
    val hora: String,
    val appIcon: Int? = null
)