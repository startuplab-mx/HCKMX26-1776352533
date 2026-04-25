package com.example.ada.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC77DFF),
    secondary = Color(0xFF9D4EDD),
    tertiary = Color(0xFF7B2CBF),

    background = Color(0xFF0C0521),
    surface = Color(0xFF14082E),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,

    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun ADATheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AdaTypography, // 👈 tu typography con Poppins
        content = content
    )
}