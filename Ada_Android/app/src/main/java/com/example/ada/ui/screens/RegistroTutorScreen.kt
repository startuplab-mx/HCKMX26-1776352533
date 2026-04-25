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
@Composable
@OptIn(ExperimentalMaterial3Api::class) // Doumentar esto es para poner que salga un calendario en fecha de nacimiento
fun RegistroTutorScreen(
    onBackClick: () -> Unit,
    onCancelarClick: () -> Unit,
    onRegistrarseClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var curp by remember { mutableStateOf("") }
    var ineUri by remember { mutableStateOf<Uri?>(null) }
    var ineBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mostrarErrorContrasena by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val correoValido = android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    val curpRegex = Regex(
        "^[A-Z][AEIOU][A-Z]{2}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])[HM](AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9]\\d$"
    )

    val curpValida = curpRegex.matches(curp)
    val ineCargada = ineUri != null || ineBitmap != null



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
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        }

        val fechaTexto = fechaSeleccionada?.let {
            formatter.format(Date(it))
        } ?: ""

        val camposLlenos =
            nombre.isNotBlank() &&
                    apellidoPaterno.isNotBlank() &&
                    apellidoMaterno.isNotBlank() &&
                    fechaSeleccionada != null &&
                    correo.isNotBlank() &&
                    curp.isNotBlank() &&
                    ineCargada &&
                    contrasena.isNotBlank() &&
                    confirmarContrasena.isNotBlank()

        val contrasenasCoinciden = contrasena.trim() == confirmarContrasena.trim()
        val galeriaLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            ineUri = uri
            ineBitmap = null
        }

        val camaraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            ineBitmap = bitmap
            ineUri = null
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.add(Calendar.YEAR, -18)

            val maxDateMillis = calendar.timeInMillis
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            return utcTimeMillis <= maxDateMillis
                        }

                        override fun isSelectableYear(year: Int): Boolean {
                            val maxYear = calendar.get(Calendar.YEAR)
                            return year <= maxYear
                        }
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val selectedDate = datePickerState.selectedDateMillis

                            if (selectedDate != null && selectedDate <= maxDateMillis) {
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
                        containerColor = Color(0xFF0C0521) // fondo oscuro
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
                text = "Registro",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegistroLabel("Nombre Completo:")
            PremiumTextInput(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = "",
                isError = false,
                shakeOffset = 0f
            )

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("Apellido Paterno")
            PremiumTextInput(
                value = apellidoPaterno,
                onValueChange = { apellidoPaterno = it },
                placeholder = "",
                isError = false,
                shakeOffset = 0f
            )

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("Apellido Materno")
            PremiumTextInput(
                value = apellidoMaterno,
                onValueChange = { apellidoMaterno = it },
                placeholder = "",
                isError = false,
                shakeOffset = 0f
            )

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("Fecha de Nacimiento:")
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
                    text = if (fechaTexto.isEmpty()) "DD/MM/YYYY" else fechaTexto,
                    color = if (fechaTexto.isEmpty()) Color(0xFFB8A9FF) else Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("Correo electronico:")
            PremiumTextInput(
                value = correo,
                onValueChange = { correo = it },
                placeholder = "ejemplo@correo.com",
                isError = !correoValido && correo.isNotEmpty(),
                shakeOffset = 0f,
                keyboardType = KeyboardType.Email
            )

            if (!correoValido && correo.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Correo no válido",
                    color = Color(0xFFFF4D6D),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("CURP:")
            PremiumTextInput(
                value = curp,
                onValueChange = {
                    curp = it.uppercase().take(18)
                },
                placeholder = "18 caracteres",
                isError = curp.isNotEmpty() && !curpValida,
                shakeOffset = 0f
            )

            if (curp.isNotEmpty() && !curpValida) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "CURP no válida. Debe tener 18 caracteres y formato oficial.",
                    color = Color(0xFFFF4D6D),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("INE:")

            IneSelector(
                ineUri = ineUri,
                ineBitmap = ineBitmap,
                onGaleriaClick = {
                    galeriaLauncher.launch("image/*")
                },
                onCamaraClick = {
                    camaraLauncher.launch(null)
                }
            )

            if (!ineCargada) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Agrega una foto de tu INE",
                    color = Color(0xFFB8A9FF),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            RegistroLabel("Contraseña:")
            PremiumTextInput(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    mostrarErrorContrasena = false
                },
                placeholder = "",
                isError = mostrarErrorContrasena,
                shakeOffset = 0f,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            RegistroLabel("Confirmar contraseña:")
            PremiumTextInput(
                value = confirmarContrasena,
                onValueChange = {
                    confirmarContrasena = it
                    mostrarErrorContrasena = false
                },
                placeholder = "",
                isError = mostrarErrorContrasena,
                shakeOffset = 0f,
                isPassword = true
            )

            if (mostrarErrorContrasena) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Las contraseñas no coinciden",
                    color = Color(0xFFFF4D6D),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(38.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                RegistroActionButton(
                    text = "Cancelar",
                    modifier = Modifier.weight(1f),
                    onClick = onCancelarClick
                )

                RegistroActionButton(
                    text = "Registrarse",
                    modifier = Modifier.weight(1f),
                    enabled = camposLlenos && correoValido && curpValida,                    onClick = {
                        if (!contrasenasCoinciden) {
                            mostrarErrorContrasena = true
                            return@RegistroActionButton
                        }

                        mostrarErrorContrasena = false
                        onRegistrarseClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Composable
fun IneSelector(
    ineUri: Uri?,
    ineBitmap: Bitmap?,
    onGaleriaClick: () -> Unit,
    onCamaraClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = Color(0xFF7B2CBF),
                    spotColor = Color(0xFF9D4EDD)
                )
                .background(
                    color = Color(0xFF14082E),
                    shape = RoundedCornerShape(18.dp)
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFF7B2CBF),
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                ineBitmap != null -> {
                    Image(
                        bitmap = ineBitmap.asImageBitmap(),
                        contentDescription = "Foto INE",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                ineUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(ineUri),
                        contentDescription = "Foto INE",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Text(
                        text = "Sin foto seleccionada",
                        color = Color(0xFFB8A9FF),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RegistroActionButton(
                text = "Galería",
                modifier = Modifier.weight(1f),
                onClick = onGaleriaClick
            )

            RegistroActionButton(
                text = "Cámara",
                modifier = Modifier.weight(1f),
                onClick = onCamaraClick
            )
        }
    }
}
