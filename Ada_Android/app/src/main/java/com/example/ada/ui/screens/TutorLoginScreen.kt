package com.example.ada.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ada.R
import kotlin.math.roundToInt
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.ripple
import com.example.ada.data.remote.RetrofitClient
import com.example.ada.data.model.LoginRequest
import android.util.Log

@Composable
fun TutorLoginScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    val correoValido = android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val scope = rememberCoroutineScope()


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

    val shakeOffset by animateFloatAsState(
        targetValue = if (mostrarError) 1f else 0f,
        animationSpec = repeatable(
            iterations = 4,
            animation = tween(durationMillis = 60),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeLogin"
    )

    val baseColor = Color(0xFF0C0521)

    val softGlow = Brush.radialGradient(
        colors = listOf(
            Color(0xFF7B2CBF).copy(alpha = 0.45f),
            Color(0xFF3C096C).copy(alpha = 0.14f),
            Color.Transparent
        ),
        center = Offset(offset, offset * 0.75f),
        radius = 950f
    )

    val secondaryGlow = Brush.radialGradient(
        colors = listOf(
            Color(0xFFC77DFF).copy(alpha = 0.25f),
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

        BackArrowButton(
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_ada),
                contentDescription = "Logo Ada",
                modifier = Modifier.size(170.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Inicio de Sesion",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium

            )

            Spacer(modifier = Modifier.height(40.dp))
            LoginInputLabel(text = "Correo electronico:")

            Spacer(modifier = Modifier.height(10.dp))

            PremiumTextInput(
                value = correo,
                onValueChange = { correo = it },
                placeholder = "ejemplo@correo.com",
                isError = correo.isNotEmpty() && !correoValido,
                shakeOffset = shakeOffset,
                keyboardType = KeyboardType.Email
            )

            if (correo.isNotEmpty() && !correoValido) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Correo no válido",
                    color = Color(0xFFFF4D6D),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LoginInputLabel(text = "Contraseña:")

            Spacer(modifier = Modifier.height(10.dp))

            PremiumTextInput(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    mostrarError = false
                },
                placeholder = "Ingresa tu contraseña",
                isError = mostrarError,
                shakeOffset = shakeOffset,
                isPassword = true
            )

            if (mostrarError || mensajeError != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mensajeError ?: "Correo o contraseña incorrectos",
                    color = Color(0xFFFF4D6D),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(if (mostrarError) 48.dp else 60.dp))

            PremiumLoginButton(
                text = if (isLoading) "ENTRANDO..." else "INICIAR SESION",
                enabled = correo.isNotBlank() &&
                        contrasena.isNotBlank() &&
                        correoValido &&
                        !isLoading,
                onClick = {
                    scope.launch {
                        isLoading = true
                        mostrarError = false
                        mensajeError = null

                        try {
                            val request = LoginRequest(
                                email = correo,
                                contrasena = contrasena
                            )

                            val response = RetrofitClient.api.login(request)

                            if (response.isSuccessful) {
                                val body = response.body()

                                Log.d("LOGIN", "Login exitoso: ${body?.message}")
                                Log.d("LOGIN", "ID recibido: ${body?.id}")

                                onLoginClick()
                            } else {
                                val error = response.errorBody()?.string()
                                Log.e("LOGIN", "Error backend: $error")

                                mostrarError = true
                                mensajeError = "Correo o contraseña incorrectos"
                            }

                        } catch (e: Exception) {
                            Log.e("LOGIN", "Error conexión: ${e.message}")

                            mostrarError = true
                            mensajeError = "Error de conexión con el servidor"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "¿No tienes una cuenta?",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Registrarse",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC77DFF)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    onRegisterClick()
                }
            )
        }
    }
}

@Composable
fun BackArrowButton(
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(),
        label = "backArrowScale"
    )

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 20.dp, start = 18.dp)
            .size(56.dp)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 18.dp else 8.dp,
                shape = RoundedCornerShape(50),
                ambientColor = Color(0xFF7B2CBF),
                spotColor = Color(0xFF9D4EDD)
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
}

@Composable
fun LoginInputLabel(
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
fun PremiumTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean,
    shakeOffset: Float,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val inputInteractionSource = remember { MutableInteractionSource() }
    val isInputFocused by inputInteractionSource.collectIsFocusedAsState()

    val inputBorderColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFFF4D6D)
            isInputFocused -> Color(0xFFC77DFF)
            else -> Color(0xFF7B2CBF)
        },
        label = "inputBorderColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .offset {
                IntOffset(
                    x = (shakeOffset * 12).roundToInt(),
                    y = 0
                )
            }
            .shadow(
                elevation = if (isInputFocused) 18.dp else 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = if (isError) Color(0xFFFF4D6D) else Color(0xFF7B2CBF),
                spotColor = if (isError) Color(0xFFFF4D6D) else Color(0xFF9D4EDD)
            )
            .background(
                color = Color(0xFF14082E),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 2.dp,
                color = inputBorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White
            ),keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            visualTransformation = if (isPassword) {
                PasswordVisualTransformation()
            } else {
                androidx.compose.ui.text.input.VisualTransformation.None
            },
            interactionSource = inputInteractionSource,
            modifier = Modifier.fillMaxWidth()
        )

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = Color(0xFFB8A9FF),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun PremiumLoginButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val buttonInteraction = remember { MutableInteractionSource() }
    val isPressed by buttonInteraction.collectIsPressedAsState()

    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(),
        label = "buttonScale"
    )

    val glow by animateFloatAsState(
        targetValue = if (enabled) 22f else 0f,
        label = "buttonGlow"
    )

    val backgroundBrush = if (enabled) {
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
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .height(58.dp)
            .scale(buttonScale)
            .shadow(
                elevation = glow.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0xFF7B2CBF),
                spotColor = Color(0xFF9D4EDD)
            )
            .background(
                brush = backgroundBrush,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
            interactionSource = buttonInteraction,
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
                letterSpacing = 1.sp
            )
        )
    }
}