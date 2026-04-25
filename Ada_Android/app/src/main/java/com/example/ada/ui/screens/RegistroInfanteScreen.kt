package com.example.ada.ui.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import com.example.ada.data.model.CrearSupervisadoRequest
import com.example.ada.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroInfanteScreen(
    onEnviarClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val formatter = remember {
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    }

    val fechaTexto = fechaSeleccionada?.let {
        formatter.format(Date(it))
    } ?: ""

    val camposLlenos =
        nombre.isNotBlank() &&
                apellidoPaterno.isNotBlank() &&
                apellidoMaterno.isNotBlank() &&
                fechaSeleccionada != null &&
                !isLoading

    val infiniteTransition = rememberInfiniteTransition(label = "infanteBackground")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    val background = Brush.linearGradient(
        colors = listOf(
            Color(0xFFD9F99D),
            Color(0xFFBBF7D0),
            Color(0xFFA7F3D0)
        ),
        start = Offset(0f, offset * 0.35f),
        end = Offset(offset * 0.65f, 1000f)
    )

    val softGlow = Brush.radialGradient(
        colors = listOf(
            Color(0xFF86EFAC).copy(alpha = 0.30f),
            Color.Transparent
        ),
        center = Offset(offset, offset * 0.55f),
        radius = 850f
    )
    val blueGlow = Brush.radialGradient(
        colors = listOf(
            Color(0xFF7DD3FC).copy(alpha = 0.22f),
            Color.Transparent
        ),
        center = Offset(1100f - offset, 900f - offset * 0.35f),
        radius = 800f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(softGlow)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(blueGlow)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Vamos a crear tu perfil",
                color = Color(0xFF14532D),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Solo te tomará un momento",
                color = Color(0xFF166534),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            YouthInput(
                label = "Nombre",
                value = nombre,
                placeholder = "Escribe tu nombre",
                onChange = { nombre = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            YouthInput(
                label = "Apellido paterno",
                value = apellidoPaterno,
                placeholder = "Escribe tu apellido",
                onChange = { apellidoPaterno = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            YouthInput(
                label = "Apellido materno",
                value = apellidoMaterno,
                placeholder = "Escribe tu apellido",
                onChange = { apellidoMaterno = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fecha de nacimiento",
                color = Color(0xFF14532D),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(26.dp),
                        ambientColor = Color(0xFF86EFAC),
                        spotColor = Color(0xFF4ADE80)
                    )
                    .background(Color.White, RoundedCornerShape(26.dp))
                    .border(
                        width = 2.dp,
                        color = Color(0xFF86EFAC),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .clickable { showDatePicker = true }
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (fechaTexto.isEmpty()) "Selecciona tu fecha" else fechaTexto,
                    color = if (fechaTexto.isEmpty()) Color(0xFF65A30D) else Color(0xFF14532D),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(38.dp))

            YouthButton(
                // Cambia el texto para dar feedback visual
                text = if (isLoading) "Guardando..." else "Crear perfil",
                enabled = camposLlenos,
                onClick = {

                    coroutineScope.launch {
                        isLoading = true
                        try {
                            val request = CrearSupervisadoRequest(
                                nombre = nombre,
                                appat = apellidoPaterno,
                                apmat = apellidoMaterno,
                                fecha_nacimiento = fechaTexto,
                                curp = ""
                            )

                            val response = RetrofitClient.api.crearSupervisado(request)

                            if (response.isSuccessful) {
                                onEnviarClick()
                            } else {
                                Log.e("API", "Error: ${response.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            Log.e("API", "Fallo: ${e.message}")
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(45.dp))
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            fechaSeleccionada = datePickerState.selectedDateMillis
                            showDatePicker = false
                        }
                    ) {
                        Text("Aceptar", color = Color(0xFF16A34A))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancelar", color = Color(0xFF14532D))
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFFF0FDF4)
                )
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color(0xFFF0FDF4),
                        titleContentColor = Color(0xFF14532D),
                        headlineContentColor = Color(0xFF16A34A),
                        weekdayContentColor = Color(0xFF166534),
                        subheadContentColor = Color(0xFF166534),
                        yearContentColor = Color(0xFF14532D),
                        selectedYearContentColor = Color.White,
                        selectedYearContainerColor = Color(0xFF22C55E),
                        dayContentColor = Color(0xFF14532D),
                        selectedDayContentColor = Color.White,
                        selectedDayContainerColor = Color(0xFF22C55E),
                        todayContentColor = Color(0xFF16A34A),
                        todayDateBorderColor = Color(0xFF16A34A)
                    )
                )
            }
        }
    }
}

@Composable
fun YouthInput(
    label: String,
    value: String,
    placeholder: String,
    isError: Boolean = false,
    onChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFFF4D6D)
            isFocused -> Color(0xFF22C55E) // verde activo
            else -> Color(0xFF86EFAC)      // verde suave
        },
        label = "youthInputBorder"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused)
            Color(0xFFF0FDF4) // verde MUY suave (tipo highlight)
        else
            Color.White,
        label = "youthInputBackground"
    )

    val glow by animateFloatAsState(
        targetValue = if (isFocused) 18f else 8f,
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color(0xFF14532D),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(
                    elevation = glow.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = borderColor,
                    spotColor = borderColor
                )
                .background(backgroundColor, RoundedCornerShape(26.dp))                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(26.dp)
                )
                .scale(if (isFocused) 1.01f else 1f)
                .padding(horizontal = 18.dp),

            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onChange,
                interactionSource = interactionSource,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF14532D)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color(0xFF65A30D),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
@Composable
fun YouthButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.94f else 1f,
        animationSpec = spring(),
        label = "youthButtonScale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 12f else 26f,
        label = "buttonElevation"
    )

    val brush = if (enabled) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF4ADE80), // verde claro (inicio)
                Color(0xFF22C55E), // verde principal
                Color(0xFF15803D)  // verde profundo (sombra)
            ),
            start = Offset(0f, 0f),
            end = Offset(600f, 600f)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF94A3B8),
                Color(0xFF64748B)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(66.dp)
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(32.dp),
                ambientColor = Color(0xFF22C55E),
                spotColor = Color(0xFF0EA5E9)
            )
            .background(
                brush = brush,
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 1.5.dp,
                color = Color.White.copy(alpha = 0.25f),
                shape = RoundedCornerShape(32.dp)
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
            color = if (enabled) Color.White else Color(0xFFE2E8F0),
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 1.2.sp
            )
        )
    }
}