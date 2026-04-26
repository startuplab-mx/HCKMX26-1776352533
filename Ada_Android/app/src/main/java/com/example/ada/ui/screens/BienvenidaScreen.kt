package com.example.ada.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ada.R

@Composable
fun BienvenidaScreen(
    onTutorClick: () -> Unit,
    onInfanteClick: () -> Unit
) {
    var mostrarTerminos by remember { mutableStateOf(false) }
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
                onClick = onTutorClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            ImagenBoton(
                imageRes = R.drawable.logo_infante,
                texto = "Infante",
                onClick = onInfanteClick
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Terminos y condiciones",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { mostrarTerminos = true }
                    .padding(bottom = 16.dp)
            )
        }

        if (mostrarTerminos) {
            TerminosYCondicionesDialog(onDismiss = { mostrarTerminos = false })
        }
    }
}

@Composable
fun TerminosYCondicionesDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = Color(0xFF1A1233)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Cerrar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    text = "Terminos y condiciones",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Recopilacion de datos y el trabajo de la aplicacion\n\n" +
                                "Ada es una aplicación diseñada para la protección de menores. " +
                                "Al utilizar este servicio, usted acepta la recopilación de datos " +
                                "necesarios para la detección de contenido inapropiado o riesgoso.",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFA762DD), Color(0xFF6219D7))
                                ),
                                shape = RoundedCornerShape(25.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancelar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
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
