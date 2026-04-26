package com.example.ada.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.ada.ui.screens.BienvenidaScreen
import com.example.ada.ui.screens.InfanteScreen
import com.example.ada.ui.screens.PantallaChat
import com.example.ada.ui.screens.PantallaCodigo
import com.example.ada.ui.screens.PantallaMensajes
import com.example.ada.ui.screens.ResultadoInfanteScreen
import com.example.ada.ui.screens.TutorLoginScreen
import com.example.ada.ui.screens.RegistroTutorScreen
import com.example.ada.ui.screens.RegistroInfanteScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Welcome.route
    ) {

        composable(AppScreen.Welcome.route) {
            BienvenidaScreen(
                onTutorClick = {
                    navController.navigate(AppScreen.Tutor.route)
                },
                onInfanteClick = {
                    navController.navigate(AppScreen.RegistroInfante.route)
                }
            )
        }


        composable(AppScreen.Tutor.route) {
            TutorLoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    // Aquí después mandarás al layout principal del tutor
                },
                onRegisterClick = {
                    navController.navigate(AppScreen.RegistroTutor.route)
                }
            )
        }

        composable(AppScreen.ResultadoInfante.route) {
            ResultadoInfanteScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(AppScreen.RegistroTutor.route) {
            RegistroTutorScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCancelarClick = {
                    navController.popBackStack()
                },
                onRegistrarseClick = {
                    navController.navigate(AppScreen.Tutor.route) {
                        popUpTo(AppScreen.RegistroTutor.route) {
                            inclusive = true
                        }
                    }                }
            )
        }

        composable(AppScreen.RegistroInfante.route) {
            RegistroInfanteScreen(

                onCancelarClick = {
                    navController.popBackStack()
                },
                onRegistrarseClick = {
                    navController.navigate(AppScreen.ResultadoInfante.route) {
                        popUpTo(AppScreen.ResultadoInfante.route) {
                            inclusive = true
                        }
                    }}
            )
        }
    composable("pantalla_chat") {
            PantallaChat(navController)
        }

        composable("pantalla_codigo") {
            PantallaCodigo(navController)
        }

        /*
        composable("pantalla_desvincular") {
            PantallaDesvincularNino(navController)
        }*/

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
