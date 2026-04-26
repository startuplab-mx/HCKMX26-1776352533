package com.example.ada_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ada_1.R
import com.example.ada_1.ui.theme.barrasuperior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDesvincularNino(navController: NavController) {
    val listaHijos = remember {
        mutableStateListOf<ChatNinoUI>(
            ChatNinoUI("1", "Fernanda", "ada: último mensaje", "12:41", null),
            ChatNinoUI("2", "Juliana", "ada: último mensaje", "12:41", null),
            ChatNinoUI("3", "David", "ada: último mensaje", "12:41", null)
        )
    }

    var hijoSeleccionadoParaDesvincular by remember { mutableStateOf<ChatNinoUI?>(null) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(barrasuperior)
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_chat),
                        contentDescription = "regresar",
                        tint = Color.White
                    )
                }
                Text("Desvincular", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(48.dp))
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0C0521))
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(listaHijos) { nino ->
                    val esNinoSeleccionado = hijoSeleccionadoParaDesvincular?.id == nino.id

                    TarjetaChatNinoConSeleccion(
                        nino = nino,
                        esSeleccionado = esNinoSeleccionado,
                        alSeleccionar = {
                            hijoSeleccionadoParaDesvincular = nino
                        }
                    )
                }
            }
        }

        hijoSeleccionadoParaDesvincular?.let { nino ->
            AlertDialog(
                onDismissRequest = { hijoSeleccionadoParaDesvincular = null },
                title = { Text("Confirmación", color = Color.Black) },
                text = { Text("¿Está seguro que desea desvincular a '${nino.nombreCompleto}'?", color = Color.Black) },
                confirmButton = {
                    Button(
                        onClick = {
                            listaHijos.remove(nino)
                            hijoSeleccionadoParaDesvincular = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6219D7))
                    ) {
                        Text("ACEPTAR")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { hijoSeleccionadoParaDesvincular = null }) {
                        Text("CANCELAR", color = Color.Gray)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
fun TarjetaChatNinoConSeleccion(
    nino: ChatNinoUI,
    esSeleccionado: Boolean,
    alSeleccionar: () -> Unit
) {
    val colorFondo = if (esSeleccionado) Color(0xFF3C096C).copy(alpha = 0.4f) else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorFondo)
            .clickable { alSeleccionar() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
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
            Text(
                text = nino.hora,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nino.ultimoMensaje,
                color = Color.Gray,
                fontSize = 15.sp
            )

            Checkbox(
                checked = esSeleccionado,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFA762DD),
                    uncheckedColor = Color.White
                )
            )
        }
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)
    }
}
