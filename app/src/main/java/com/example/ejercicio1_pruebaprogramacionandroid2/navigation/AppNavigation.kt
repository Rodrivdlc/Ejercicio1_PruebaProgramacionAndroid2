package com.example.ejercicio1_pruebaprogramacionandroid2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejercicio1_pruebaprogramacionandroid2.screens.AddClassScreen
import com.example.ejercicio1_pruebaprogramacionandroid2.screens.CurrentClassScreen
import com.example.ejercicio1_pruebaprogramacionandroid2.screens.MainScreen
import com.example.ejercicio1_pruebaprogramacionandroid2.screens.ViewScheduleScreen

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

        // Pantalla para ver el horario
        composable("viewSchedule") {
            ViewScheduleScreen()
        }


        composable("currentClass") {
            CurrentClassScreen()
        }
    }
}
