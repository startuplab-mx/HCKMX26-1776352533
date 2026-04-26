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
    private val processingScope = CoroutineScope(Dispatchers.Default)
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
            Log.w("ADA_AI", "Modelo aún no inicializado: $contextPrompt")
            return
        }

        processingScope.launch {
            mutex.withLock {
                Log.d("ADA_AI", "Analizando en cola...\nTexto: $contextPrompt")
                try {
                    val result = model?.predictText(contextPrompt, context)
                    Log.d("ADA_AI", "Resultado de inferencia: $result")

                    if (result != null && result <= 0.84f) {
                        AlertSender.send(
                            mensaje   = GlobalContext.formattedContext,
                            appOrigen = GlobalContext.sourceApp
                        )
                    }
                } catch (e: Exception) {
                    Log.e("ADA_AI", "El modelo crasheó: ${e.message}")
                }
            }
        }
    }
}