package com.example.ada_prueba

import android.content.Context
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.InterpreterFactory // IMPORTANTE
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ModelTest(context: Context) {

    private var interpreter: InterpreterApi

    init {
        val model = loadModelFile(context, "model.tflite")

        val options = InterpreterApi.Options().apply {
            setNumThreads(4)
            // La constante correcta es esta:
            setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
        }

        interpreter = InterpreterFactory().create(model, options)
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

    fun predict(inputArray: IntArray): Float {
        // Formato [1, 100]
        val input = arrayOf(inputArray)
        val output = Array(1) { FloatArray(1) }

        // InterpreterApi soporta el método run directamente
        interpreter.run(input, output)

        return output[0][0]
    }

    fun loadTokenizer(context: Context): Map<String, Int> {
        val json = context.assets.open("tokenizer.json").bufferedReader().use { it.readText() }
        val wordIndex = mutableMapOf<String, Int>()
        val regex = "\"(.*?)\":(\\d+)".toRegex()
        val matches = regex.findAll(json)
        for (match in matches) {
            wordIndex[match.groupValues[1]] = match.groupValues[2].toInt()
        }
        return wordIndex
    }

    fun tokenize(text: String, wordIndex: Map<String, Int>): IntArray {
        val tokens = text.lowercase().split(" ")
        val sequence = tokens.map { wordIndex[it] ?: 1 }
        val padded = IntArray(100)
        val start = 100 - sequence.size
        for (i in sequence.indices) {
            if (start + i < 100 && start + i >= 0) {
                padded[start + i] = sequence[i]
            }
        }
        return padded
    }
}