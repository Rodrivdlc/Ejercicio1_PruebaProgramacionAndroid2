package com.example.ejercicio1_pruebaprogramacionandroid2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejercicio1_pruebaprogramacionandroid2.screens.AddClassScreen
import com.example.ejercicio1_pruebaprogramacionandroid2.screens.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController() // Controlador de navegación

    NavHost(navController = navController, startDestination = "main") {
        // Pantalla principal
        composable("main") {
            MainScreen(
                navigateToAddClass = { navController.navigate("addClass") },
                navigateToViewSchedule = { navController.navigate("viewSchedule") },
                navigateToCurrentClass = { navController.navigate("currentClass") }
            )
        }

        // Pantalla para añadir clases
        composable("addClass") {
            AddClassScreen(
                onClassAdded = { navController.navigate("main") } // Redirige a la pantalla principal después de añadir una clase
            )
        }

        // Pantalla para ver el horario (pendiente de implementar)
        composable("viewSchedule") {
            //ViewScheduleScreen() // Implementaremos esta pantalla luego
        }

        // Pantalla para consultar qué clase toca ahora (pendiente de implementar)
        composable("currentClass") {
            //CurrentClassScreen() // Implementaremos esta pantalla luego
        }
    }
}