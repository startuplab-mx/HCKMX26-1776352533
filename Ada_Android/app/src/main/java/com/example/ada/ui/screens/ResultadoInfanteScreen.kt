package com.example.ada.ui.screens


import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ada.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
@Composable
fun ResultadoInfanteScreen(
    onBackClick: () -> Unit
) {
    var codigo by remember { mutableStateOf("") }
    var mostrarCodigo by remember { mutableStateOf(false) }

    fun generarCodigo(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }
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

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(),
        label = "arrowScale"
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

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(top = 20.dp, start = 18.dp)
                .size(56.dp)
                .scale(scale)
                .shadow(
                    elevation = if (isPressed) 18.dp else 8.dp,
                    shape = RoundedCornerShape(50)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onBackClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.flecha_salir),
                contentDescription = "Regresar",
                modifier = Modifier.size(44.dp)
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(70.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_ada),
                contentDescription = "Logo Ada",
                modifier = Modifier.size(250.dp)
            )
            Text(
                text = "Registro realizado correctamente.",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "¿Quieres vincularte con un tutor?",
                color = Color(0xFFD8CCFF),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            val lockScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.08f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 900,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "lockPulse"
            )

            val lockGlow by infiniteTransition.animateFloat(
                initialValue = 10f,
                targetValue = 28f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 900,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "lockGlow"
            )

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .scale(lockScale)
                    .shadow(
                        elevation = lockGlow.dp,
                        shape = RoundedCornerShape(75.dp),
                        ambientColor = Color(0xFFC77DFF),
                        spotColor = Color(0xFF7B2CBF)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.candado),
                    contentDescription = "Candado",
                    modifier = Modifier.size(125.dp)
                )

            }
            Spacer(modifier = Modifier.height(40.dp))

            RegistroActionButton(
                text = if (mostrarCodigo) "Ocultar código" else "Vincular",
                modifier = Modifier.fillMaxWidth(0.6f),
                onClick = {
                    if (codigo.isEmpty()) {
                        codigo = generarCodigo()
                    }

                    mostrarCodigo = !mostrarCodigo
                }
            )
            if (mostrarCodigo && codigo.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Tu código de vinculación",
                    color = Color(0xFFB8A9FF),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 22.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Color(0xFF7B2CBF),
                            spotColor = Color(0xFFC77DFF)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF5A189A),
                                    Color(0xFF7B2CBF),
                                    Color(0xFF9D4EDD)
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(horizontal = 28.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = codigo,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            letterSpacing = 4.sp
                        )
                    )
                }
            }
        }

    }
}

@Composable
fun RegistroLabel(
    text: String
) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun RegistroActionButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(),
        label = "registroButtonScale"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 12.dp else 22.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0xFF7B2CBF),
                spotColor = Color(0xFF9D4EDD)
            )
            .background(
                brush = if (enabled) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFC77DFF),
                            Color(0xFF9D4EDD),
                            Color(0xFF7B2CBF),
                            Color(0xFF5A189A)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2A2A2A),
                            Color(0xFF1F1F1F)
                        )
                    )
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = Color.White.copy(alpha = 0.35f)
                ),
                enabled = enabled
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) Color.White else Color.Gray,
            style = MaterialTheme.typography.labelLarge.copy(
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )
        )
    }
}