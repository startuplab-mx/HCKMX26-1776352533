package com.example.ada.uriel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object AlertSender {

    private fun resolveAppId(appOrigen: String): String = when (appOrigen) {
        "com.whatsapp"             -> "04149a6a-93a8-482c-a595-ba753cc96527"
        "com.zhiliaoapp.musically" -> "579c1b82-c6e2-4215-8da6-78977320caec"
        "com.instagram.android"    -> "e031e1f4-f8d2-4078-9fe0-05fb558127d1"
        else                       -> "04149a6a-93a8-482c-a595-ba753cc96527"
    }

    fun send(mensaje: String, appOrigen: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = JSONObject().apply {
                    put("aplicacion_id",    resolveAppId(appOrigen))
                    put("tipo_alerta",      "b1c4b0da-fba5-4a20-9a09-fa5cab37c58c")
                    put("mensaje_detectado", mensaje)
                    put("lat",              "0")
                    put("lon",              "0")
                    put("colonia",          "Granada")
                    put("municipio",        "Miguel Hidalgo")
                    put("estado",           "CDMX")
                }

                android.util.Log.d("ADA_ALERT", "Enviando: $body")

                val url = URL("https://ada-server-ybnq.onrender.com/supervisado/alerta/crear/17f15b51-951d-4e68-a4db-50c39065c3e0/09e809c3-b541-4ced-adfc-5089cdeeaad0")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.outputStream.write(body.toString().toByteArray())

                val code = conn.responseCode
                android.util.Log.d("ADA_ALERT", "Respuesta del servidor: $code")

                conn.disconnect()

            } catch (e: Exception) {
                android.util.Log.e("ADA_ALERT", "Error enviando alerta: ${e.message}")
            }
        }
    }
}