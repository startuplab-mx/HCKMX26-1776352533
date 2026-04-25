package com.example.ada_prueba

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

// Agrega este import
import com.google.android.gms.tflite.java.TfLite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. INICIALIZACIÓN CRÍTICA
        TfLite.initialize(this).addOnSuccessListener {
            // 2. Solo cuando la inicialización sea exitosa, creamos el modelo
            try {
                val model = ModelTest(this)
                val tokenizer = model.loadTokenizer(this)
                val text = "Podemos vernos hoy para realizar la tarea? | Hola, soy uriel, el de la coleta, te acuerdas de mi??"
                val input = model.tokenize(text, tokenizer)
                val result = model.predict(input)

                Log.d("MODEL_RESULT", "Predicción exitosa: $result")
            } catch (e: Exception) {
                Log.e("MODEL_ERROR", "Error al usar el modelo: ${e.message}")
            }
        }.addOnFailureListener {
            Log.e("MODEL_ERROR", "No se pudo inicializar TensorFlow Lite via GMS")
        }
    }
}