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
import com.example.ada.ui.navigation.AppNavigation
import com.example.ada.ui.screens.BienvenidaScreen
import com.example.ada.ui.theme.ADATheme

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
    }
}