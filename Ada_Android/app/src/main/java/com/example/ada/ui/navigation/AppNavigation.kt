package com.example.ada.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.ada.ui.screens.BienvenidaScreen
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


                    //Ejemplo de como poner para que vaya a la otra pagina despues
                    //del login y ya no pueda volver atras
                    ///navController.navigate("NOMBRE DE LA PAGINA") {
                    //    popUpTo(AppScreen.Tutor.route) {
                    //        inclusive = true
                    //    }
                    //}
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

    }
}