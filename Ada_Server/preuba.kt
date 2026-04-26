package com.example.prueba_modelo

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TFLiteModel(context: Context) {

    private var interpreter: Interpreter

    init {
        val model = loadModelFile(context, "model.tflite")
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(context: Context, filename: String): ByteBuffer {
        val fileDescriptor = context.assets.openFd(filename)
        val inputStream = fileDescriptor.createInputStream()
        val bytes = inputStream.readBytes()

        val buffer = ByteBuffer.allocateDirect(bytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(bytes)
        buffer.rewind()

        return buffer
    }

    // =========================
    // PREDICCIÓN
    // =========================
    fun predict(inputArray: IntArray): Float {

        // Convertir a formato [1,100]
        val input = arrayOf(inputArray)

        val output = Array(1) { FloatArray(1) }

        interpreter.run(input, output)

        return output[0][0]
    }
}