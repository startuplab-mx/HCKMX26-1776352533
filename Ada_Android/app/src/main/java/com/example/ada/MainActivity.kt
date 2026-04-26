package com.example.ada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ada.ui.navigation.AppNavigation
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