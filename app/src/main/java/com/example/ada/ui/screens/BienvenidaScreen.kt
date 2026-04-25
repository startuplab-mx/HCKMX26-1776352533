package com.example.ada.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Brush
import com.example.ada.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.MaterialTheme
@Composable
fun BienvenidaScreen(
    onTutorClick: () -> Unit,
    onInfanteClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 8500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "softGradientOffset"
    )

    val baseColor = Color(0xFF0C0521)

    val softGlow = Brush.radialGradient(
        colors = listOf(
            Color(0xFF7B2CBF).copy(alpha = 0.28f),
            Color(0xFF3C096C).copy(alpha = 0.14f),
            Color.Transparent
        ),
        center = Offset(offset, offset * 0.75f),
        radius = 950f
    )

    val secondaryGlow = Brush.radialGradient(
        colors = listOf(
            Color(0xFFC77DFF).copy(alpha = 0.12f),
            Color.Transparent
        ),
        center = Offset(900f - offset, 1200f - offset),
        radius = 850f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(baseColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(softGlow)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(secondaryGlow)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_ada),
                contentDescription = "Logo_ada",
                modifier = Modifier.size(250.dp)
            )


            Text(
                text = "Bienvenido",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium

            )

            Spacer(modifier = Modifier.height(32.dp))

            ImagenBoton(
                imageRes = R.drawable.logo_tutor,
                texto = "Tutor",

                onClick = {
                    onTutorClick()
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            ImagenBoton(
                imageRes = R.drawable.logo_infante,
                texto = "Infante",
                onClick = {
                    onInfanteClick()
                }
            )
        }
    }
}
@Composable
fun ImagenBoton(
    imageRes: Int,
    texto: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(),
        label = "scaleAnimation"
    )

    Column(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = texto,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = texto,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
    }
}