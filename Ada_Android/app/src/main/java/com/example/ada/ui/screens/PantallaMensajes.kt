package com.example.ada.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ada_1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMensajes(navController: NavController, nombreNino: String) {
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

    val mensajesSimulados = listOf(
        MensajeUI("Mensajes/alertas provinientes de la parte del backend", R.drawable.logo_tt)
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_chat),
                        contentDescription = "regresar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = nombreNino,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(baseColor)
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(softGlow))
            Box(modifier = Modifier.fillMaxSize().background(secondaryGlow))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                items(mensajesSimulados) { mensaje ->
                    FilaMensaje(mensaje)
                }
            }
        }
    }
}

@Composable
fun FilaMensaje(mensaje: MensajeUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // icono de la app provinente
        Image(
            painter = painterResource(id = R.drawable.logo_tt),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))

        // burbuja de mensaje
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(Color(0xFFA762DD)) // burbuja
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = mensaje.texto,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

data class MensajeUI(
    val texto: String,
    val appIcon: Int
)
