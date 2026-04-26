package com.example.ada.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.ada.R
import com.example.ada.ui.theme.yellowbottom
import com.example.ada.ui.theme.barrasuperior
import com.example.ada.data.ChatNinoUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaChat(navController: NavController) {
    var mostrarMenu by remember { mutableStateOf(false) }
    var modoDesvincular by remember { mutableStateOf(false) }
    val ninosSeleccionados = remember { mutableStateListOf<String>() }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    //pantallas
    val tieneNinosRegistrados = true
    
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
//
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

    val listaNinos = remember {
        mutableStateListOf<ChatNinoUI>().apply {
            if (tieneNinosRegistrados) {
                add(ChatNinoUI("1", "Fernanda", "Aplicacion con riesgo detectado: Instagram", "12:41", R.drawable.logo_ig))
                add(ChatNinoUI("2", "Juliana", "Aplicacion con riesgo detectado: WhatsApp", "12:41", R.drawable.logo_wts))
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(barrasuperior)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (modoDesvincular) {
                        IconButton(onClick = { 
                            modoDesvincular = false
                            ninosSeleccionados.clear()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = "cancelar",
                                tint = Color.White
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.logo_ada),
                        contentDescription = "logo ada",
                        modifier = Modifier.height(60.dp)
                    )

                    if (modoDesvincular) {
                        IconButton(
                            onClick = { 
                                if (ninosSeleccionados.isNotEmpty()) {
                                    mostrarConfirmacion = true
                                }
                            },
                            enabled = ninosSeleccionados.isNotEmpty()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.desvincular),
                                contentDescription = "desvincular",
                                tint = if (ninosSeleccionados.isNotEmpty()) Color.White else Color.Gray,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    } else {
                        Box {
                            IconButton(onClick = { mostrarMenu = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.three_dots),
                                    contentDescription = "opciones",
                                    modifier = Modifier.height(20.dp),
                                    tint = Color.White
                                )
                            }

                            DropdownMenu(
                                expanded = mostrarMenu,
                                onDismissRequest = { mostrarMenu = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                if (listaNinos.isNotEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("Desvincular niños", color = Color.Black) },
                                        onClick = {
                                            mostrarMenu = false
                                            modoDesvincular = true
                                        }
                                    )
                                }

                                DropdownMenuItem(
                                    text = { Text("Cerrar Sesion", color = Color.Black) },
                                    onClick = {
                                        mostrarMenu = false
                                        navController.navigate("pantalla_principal")
                                    }
                                )
                            }
                        }
                    }
                }
                
                // bontones filtro para las apps
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BotonFiltroApp("Instagram")
                    BotonFiltroApp("Whahsapp")
                    BotonFiltroApp("TikTok")
                }
                
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
            }
        },
        floatingActionButton = {
            if (listaNinos.isNotEmpty() && !modoDesvincular) {
                FloatingActionButton(
                    onClick = { navController.navigate("pantalla_codigo") },
                    containerColor = Color(0xFFCE8820),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(70.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add, 
                        contentDescription = "agregar niño",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(baseColor)
                .padding(paddingValues)
        ) {
            // fondos animados
            Box(modifier = Modifier.fillMaxSize().background(softGlow))
            Box(modifier = Modifier.fillMaxSize().background(secondaryGlow))

            if (listaNinos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Aun no tienes a tu hijo registrado",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = { navController.navigate("pantalla_codigo") },
                            colors = ButtonDefaults.buttonColors(containerColor = yellowbottom),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(text = "Empieza ahora!", color = Color.White)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp)
                ) {
                    items(listaNinos) { nino ->
                        val seleccionado = ninosSeleccionados.contains(nino.id)
                        TarjetaChatNino(
                            nino = nino,
                            modoDesvincular = modoDesvincular,
                            seleccionado = seleccionado,
                            onClick = {
                                if (modoDesvincular) {
                                    if (seleccionado) {
                                        ninosSeleccionados.remove(nino.id)
                                    } else {
                                        ninosSeleccionados.add(nino.id)
                                    }
                                } else {
                                    navController.navigate("pantalla_mensajes/${nino.nombreCompleto}")
                                }
                            }
                        )
                    }
                }
            }
        }

        if (mostrarConfirmacion) {
            AlertDialog(
                onDismissRequest = { mostrarConfirmacion = false },
                title = { Text("Confirmación", color = Color.Black) },
                text = { Text("¿Está seguro que desea desvincular a los niños seleccionados?", color = Color.Black) },
                confirmButton = {
                    Button(
                        onClick = {
                            listaNinos.removeAll { ninosSeleccionados.contains(it.id) }
                            ninosSeleccionados.clear()
                            modoDesvincular = false
                            mostrarConfirmacion = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6219D7))
                    ) {
                        Text("ACEPTAR")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarConfirmacion = false }) {
                        Text("CANCELAR", color = Color.Gray)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun BotonFiltroApp(nombre: String) {
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(50)),
        color = Color.Gray.copy(alpha = 0.6f)
    ) {
        Text(
            text = nombre,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TarjetaChatNino(
    nino: ChatNinoUI, 
    modoDesvincular: Boolean = false,
    seleccionado: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // icono de la app (alado del nombre) si existe
            nino.appIcon?.let { iconId ->
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = nino.nombreCompleto,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    
                    if (modoDesvincular) {
                        Checkbox(
                            checked = seleccionado,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFA762DD),
                                uncheckedColor = Color.White
                            )
                        )
                    } else {
                        Text(
                            text = nino.hora,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = nino.ultimoMensaje,
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }
}
