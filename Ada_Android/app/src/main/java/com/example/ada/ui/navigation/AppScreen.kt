package com.example.ada.ui.navigation
sealed class AppScreen(val route: String) {
    object Welcome : AppScreen("welcome")
    object Tutor : AppScreen("tutor")
    object Infante : AppScreen("infante")

    object ResultadoInfante : AppScreen("resultado_infante")

    object RegistroTutor : AppScreen("registro_tutor")

    object RegistroInfante : AppScreen("registro_infante")
}
