package com.example.ada.ui.screens


import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ada.R
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar
import java.util.TimeZone
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroInfanteScreen(
    onCancelarClick: () -> Unit,
    onRegistrarseClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var curp by remember { mutableStateOf("") }
    val curpRegex = Regex(
        "^[A-Z][AEIOU][A-Z]{2}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])[HM](AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9]\\d$"
    )
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val curpValida = curp.isBlank() || curpRegex.matches(curp)


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
        center = Offset(1200f - offset, 1200f - offset),
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
        var showDatePicker by remember { mutableStateOf(false) }
        var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }

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
                    curp.isNotBlank()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDatePicker) {

                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val selectedDate = datePickerState.selectedDateMillis

                            if (selectedDate != null) {
                                fechaSeleccionada = selectedDate
                                showDatePicker = false
                            }
                        }) {
                            Text("Aceptar", color = Color(0xFFC77DFF))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                        }) {
                            Text("Cancelar", color = Color(0xFFB8A9FF))
                        }
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = Color(0xFF0C0521)
                    )
                ) {
                    DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors(
                            containerColor = Color(0xFF0C0521),

                            titleContentColor = Color.White,
                            headlineContentColor = Color(0xFFC77DFF),

                            weekdayContentColor = Color(0xFFB8A9FF),
                            subheadContentColor = Color(0xFFB8A9FF),

                            yearContentColor = Color.White,
                            currentYearContentColor = Color(0xFFC77DFF),

                            selectedYearContentColor = Color.White,
                            selectedYearContainerColor = Color(0xFF7B2CBF),

                            dayContentColor = Color.White,
                            disabledDayContentColor = Color(0xFF555555),

                            selectedDayContentColor = Color.White,
                            selectedDayContainerColor = Color(0xFF9D4EDD),

                            todayContentColor = Color(0xFFC77DFF),
                            todayDateBorderColor = Color(0xFFC77DFF)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_ada),
                contentDescription = "Logo Ada",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Registro Infante",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegistroLabel2("Nombre Completo:")
            PremiumTextInput(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = "",
                isError = false,
                shakeOffset = 0f
            )

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel2("Apellido Paterno")
            PremiumTextInput(
                value = apellidoPaterno,
                onValueChange = { apellidoPaterno = it },
                placeholder = "",
                isError = false,
                shakeOffset = 0f
            )

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel2("Apellido Materno")
            PremiumTextInput(
                value = apellidoMaterno,
                onValueChange = { apellidoMaterno = it },
                placeholder = "",
                isError = false,
                shakeOffset = 0f
            )

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel2("Fecha de Nacimiento:")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .background(
                        color = Color(0xFF14082E),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFF7B2CBF),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable {
                        showDatePicker = true
                    }
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (fechaTexto.isEmpty()) "YYYY/MM/DD" else fechaTexto,
                    color = if (fechaTexto.isEmpty()) Color(0xFFB8A9FF) else Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("CURP (opcional):")

            PremiumTextInput(
                value = curp,
                onValueChange = {
                    curp = it.uppercase().take(18)
                },
                placeholder = "Ej: ABCD010203HDFXXX09",
                isError = curp.isNotEmpty() && !curpRegex.matches(curp),
                shakeOffset = 0f
            )

            if (curp.isNotEmpty() && !curpRegex.matches(curp)) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "CURP no válida",
                    color = Color(0xFFFF4D6D),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Spacer(modifier = Modifier.height(38.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                RegistroActionButton2(
                    text = "Cancelar",
                    modifier = Modifier.weight(1f),
                    onClick = onCancelarClick
                )

                RegistroActionButton2(
                    text = "Registrarse",
                    modifier = Modifier.weight(1f),
                    enabled = camposLlenos  && curpValida,                    onClick = {

                        onRegistrarseClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}


@Composable
fun RegistroLabel2(
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
fun RegistroActionButton2(
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
                indication = null,
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
