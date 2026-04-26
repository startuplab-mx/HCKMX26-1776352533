package com.example.ada.uriel

import android.content.Context
import android.util.Log
import com.example.ada_prueba.ModelTest
import com.google.android.gms.tflite.java.TfLite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AdaModelManager(private val context: Context) {

    private var isInitialized = false
    private var model: ModelTest? = null

    // CoroutineScope independiente para no bloquear el hilo principal
    private val processingScope = CoroutineScope(Dispatchers.Default)

    // El "cadenero" que crea la cola secuencial
    private val mutex = Mutex()

    init {
        TfLite.initialize(context).addOnSuccessListener {
            model = ModelTest(context)
            model?.loadTokenizer(context)
            isInitialized = true
            Log.d("ADA_AI", "Motor encendido y listo para recibir contexto.")
        }.addOnFailureListener {
            Log.e("ADA_AI", "Fallo fatal al inicializar TFLite via GMS")
        }
    }

    fun analyzeContext(contextPrompt: String) {
        if (!isInitialized || model == null) {
            Log.w("ADA_AI", "Modelo aún no inicializado, el mensaje se perderá: $contextPrompt")
            return
        }

        // Se lanza la corrutina inmediatamente, pero el mutex las forma en fila
        processingScope.launch {
            mutex.withLock {
                Log.d("ADA_AI", "Analizando en cola...\nTexto: $contextPrompt")

                try {
                    // Aquí la IA se toma su tiempo sin colisionar
                    val result = model?.predictText(contextPrompt, context)
                    Log.d("ADA_AI", "Resultado de inferencia: $result")

                    // Aquí puedes disparar una notificación local si el resultado es alerta

                } catch (e: Exception) {
                    Log.e("ADA_AI", "El modelo crasheó en la inferencia: ${e.message}")
                }
            } // Al salir de este bloque, el mutex deja pasar al siguiente mensaje
        }
    }
}