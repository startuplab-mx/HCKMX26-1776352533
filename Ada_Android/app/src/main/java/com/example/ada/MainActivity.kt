package com.example.ada

import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.example.ada.ui.navigation.AppNavigation
import com.example.ada.ui.theme.ADATheme
import com.cloudinary.android.MediaManager
import com.example.ada.utils.CloudinaryConfig
import com.example.ada_prueba.ModelTest
import com.google.android.gms.tflite.java.TfLite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    }
}
