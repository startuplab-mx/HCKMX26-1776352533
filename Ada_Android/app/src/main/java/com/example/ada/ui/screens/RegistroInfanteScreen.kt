package com.example.ada.ui.screens

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

    val formatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    val fechaTexto = fechaSeleccionada?.let {
        formatter.format(Date(it))
    } ?: ""

    val camposLlenos =
        nombre.isNotBlank() &&
                apellidoPaterno.isNotBlank() &&
                apellidoMaterno.isNotBlank() &&
                fechaSeleccionada != null

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFD9F99D), // verde clarito
            Color(0xFFBBF7D0), // verde menta
            Color(0xFFA7F3D0)  // aqua suave
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Registrate!!!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF14532D),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            KidInput("Tu nombre", nombre) { nombre = it }
            Spacer(modifier = Modifier.height(16.dp))

            KidInput("Apellido paterno", apellidoPaterno) { apellidoPaterno = it }
            Spacer(modifier = Modifier.height(16.dp))

            KidInput("Apellido materno", apellidoMaterno) { apellidoMaterno = it }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🎂 Tu cumpleaños",
                color = Color(0xFF14532D),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                    text = if (fechaTexto.isEmpty()) "Toca aquí 🐢" else fechaTexto,
                    color = if (fechaTexto.isEmpty()) Color(0xFF65A30D) else Color(0xFF14532D),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            KidButton(
                text = "🚀 ¡LISTO!",
                enabled = camposLlenos,
                onClick = onEnviarClick
            )
        }

        // 📅 Calendario
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        fechaSeleccionada = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }) {
                        Text("Aceptar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
@Composable
fun KidInput(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF3A0CA3),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
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
                .padding(horizontal = 18.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF14532D)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun KidButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(60.dp)
            .background(
                if (enabled) Color(0xFF22C55E) else Color(0xFF94A3B8),
                RoundedCornerShape(30.dp)
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge
        )
    }
}