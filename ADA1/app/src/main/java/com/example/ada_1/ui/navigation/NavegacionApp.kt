package com.example.ada.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ada.ui.screens.PantallaChat
import com.example.ada.ui.screens.PantallaCodigo
import com.example.ada.ui.screens.PantallaDesvincularNino
import com.example.ada.ui.screens.PantallaMensajes

@Composable
fun NavegacionApp() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "pantalla_chat") {

        composable("pantalla_chat") {
            PantallaChat(navController)
        }

        composable("pantalla_codigo") {
            PantallaCodigo(navController)
        }
        composable("pantalla_desvincular") {
            PantallaDesvincularNino(navController)
        }
        composable("pantalla_mensajes/{nombreNino}") { backStackEntry ->
            val nombreNino = backStackEntry.arguments?.getString("nombreNino") ?: ""
            PantallaMensajes(navController, nombreNino)
        }
        composable("pantalla_principal") {
            // aqui va la pantalla que esta haciendo tu compañero
            // PantallaPrincipal(navController)
        }
    }
}
