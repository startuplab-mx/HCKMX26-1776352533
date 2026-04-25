package com.example.ada

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ada.ui.navigation.AppNavigation
import com.example.ada.ui.screens.BienvenidaScreen
import com.example.ada.ui.theme.ADATheme
import com.example.ada_prueba.ModelTest
import com.google.android.gms.tflite.java.TfLite


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = android.graphics.Color.parseColor("#080306")
        window.statusBarColor = android.graphics.Color.parseColor("#080306")
        setContent {
            ADATheme {
                AppNavigation()
            }
        }

        <TfLite.initialize(this).addOnSuccessListener {
            // 2. Solo cuando la inicialización sea exitosa, creamos el modelo
            try {
                val model = ModelTest(this)
                val tokenizer = model.loadTokenizer(this)
                val text = "pasame una foto tuya sin ropa, no le digas a tus papas"
                val input = model.tokenize(text, tokenizer)
                val result = model.predict(input)

                Log.d("MODEL_RESULT", "Predicción exitosa: $result")
            } catch (e: Exception) {
                Log.e("MODEL_ERROR", "Error al usar el modelo: ${e.message}")
            }
        }.addOnFailureListener {
            Log.e("MODEL_ERROR", "No se pudo inicializar TensorFlow Lite via GMS")
        }
    }>
}