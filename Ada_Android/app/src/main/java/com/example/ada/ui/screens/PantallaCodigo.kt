package com.example.ada.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ada.R
import com.example.ada.data.model.CodigoVincular
import com.example.ada.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCodigo(navController: NavController) {
    var codigoTexto by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var mensajeExito by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
        Box(modifier = Modifier.fillMaxSize().background(softGlow))
        Box(modifier = Modifier.fillMaxSize().background(secondaryGlow))

        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 20.dp, start = 18.dp)
                .size(45.dp)
                .scale(scale)
                .shadow(
                    elevation = if (isPressed) 12.dp else 4.dp,
                    shape = RoundedCornerShape(50)
                )
                .background(Color(0xFF1E0D41).copy(alpha = 0.6f), RoundedCornerShape(50))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    navController.popBackStack()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "Regresar",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_ada),
                contentDescription = "Logo Ada",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Ingresar código",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                value = codigoTexto,
                onValueChange = {
                    codigoTexto = it.uppercase().take(6)
                    mensajeError = null
                    mensajeExito = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .shadow(10.dp, RoundedCornerShape(25.dp)),
                shape = RoundedCornerShape(25.dp),
                placeholder = {
                    Text("Código de vinculación")
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )

            if (mensajeError != null) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = mensajeError!!,
                    color = Color(0xFFFF4D6D),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (mensajeExito != null) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = mensajeExito!!,
                    color = Color(0xFF7CFFB2),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        mensajeError = null
                        mensajeExito = null

                        try {
                            val prefs = context.getSharedPreferences(
                                "ADA_PREFS",
                                Context.MODE_PRIVATE
                            )

                            val tutorId = prefs.getString("TUTOR_ID", null)

                            if (tutorId == null) {
                                mensajeError = "No se encontró el ID del tutor"
                                return@launch
                            }

                            val request = CodigoVincular(
                                codigo = codigoTexto.trim().uppercase()
                            )

                            val response = RetrofitClient.api.vincularSupervisor(
                                id = tutorId,
                                request = request
                            )

                            if (response.isSuccessful && response.body()?.ok == true) {
                                Log.d("VINCULAR", "Vinculación exitosa")
                                mensajeExito = "Vinculación exitosa"

                                // Después, si quieres navegar:
                                // navController.navigate("pantalla_chat")
                            } else {
                                val error = response.errorBody()?.string()
                                Log.e("VINCULAR", "Error backend: $error")
                                mensajeError = "Código inválido o expirado"
                            }

                        } catch (e: Exception) {
                            Log.e("VINCULAR", "Error: ${e.message}")
                            mensajeError = "Error de conexión"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = codigoTexto.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(65.dp)
                    .padding(bottom = 20.dp)
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = if (!isLoading && codigoTexto.isNotBlank()) {
                                    listOf(Color(0xFF6219D7), Color(0xFFA762DD))
                                } else {
                                    listOf(Color(0xFF2A2A2A), Color(0xFF1F1F1F))
                                }
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isLoading) "ENVIANDO..." else "ENVIAR",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}