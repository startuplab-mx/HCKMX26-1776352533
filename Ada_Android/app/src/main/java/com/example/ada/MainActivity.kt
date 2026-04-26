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
import com.cloudinary.android.MediaManager
import com.example.ada.utils.CloudinaryConfig

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = mapOf(
            "cloud_name" to CloudinaryConfig.CLOUD_NAME
        )

        try {
            MediaManager.init(this, config)
        } catch (e: Exception) {
            // Evita crash si ya estaba inicializado
        }
        window.navigationBarColor = android.graphics.Color.parseColor("#080306")
        window.statusBarColor = android.graphics.Color.parseColor("#080306")
        setContent {
            ADATheme {
                AppNavigation()
            }
        }

        TfLite.initialize(this).addOnSuccessListener {

            lifecycleScope.launch {

                try {
                    val result = withContext(Dispatchers.Default) {
                        val model = ModelTest(this@MainActivity)
                        val tokenizer = model.loadTokenizer(this@MainActivity)

                        model.predictText(
                            "baby vamos a vernos",
                            this@MainActivity)
                    }

                    Log.d("MODEL_RESULT", "Predicción exitosa: $result")

                } catch (e: Exception) {
                    Log.e("MODEL_ERROR", "Error: ${e.message}")
                }
            }

        }.addOnFailureListener {
            Log.e("MODEL_ERROR", "No se pudo inicializar TensorFlow Lite via GMS")
        }

    }
}