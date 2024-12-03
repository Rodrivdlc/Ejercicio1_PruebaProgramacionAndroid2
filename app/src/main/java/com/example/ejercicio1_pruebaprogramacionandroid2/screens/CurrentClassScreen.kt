package com.example.ejercicio1_pruebaprogramacionandroid2.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
import android.util.Log
import com.example.ejercicio1_pruebaprogramacionandroid2.data.Clase

@Composable
fun CurrentClassScreen() {
    val database = FirebaseDatabase.getInstance().reference.child("clases")
    val clases = remember { mutableStateListOf<Clase>() }
    val claseActual = remember { mutableStateOf<Clase?>(null) }
    val horaActual = remember { mutableStateOf("") }
    val diaActual = remember { mutableStateOf("") }

    // Obtener la fecha y hora actuales
    LaunchedEffect(Unit) {
        val calendar = java.util.Calendar.getInstance()
        diaActual.value = obtenerDiaSemana(calendar.get(java.util.Calendar.DAY_OF_WEEK))
        horaActual.value = String.format(
            "%02d:%02d",
            calendar.get(java.util.Calendar.HOUR_OF_DAY),
            calendar.get(java.util.Calendar.MINUTE)
        )
    }

    // Leer datos de Realtime Database
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clases.clear()
                for (child in snapshot.children) {
                    val clase = child.getValue(Clase::class.java)
                    if (clase != null) {
                        clases.add(clase)
                    }
                }
                // Filtrar la clase actual
                claseActual.value = filtrarClaseActual(diaActual.value, horaActual.value, clases)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CurrentClassScreen", "Error al leer las clases: ${error.message}")
            }
        })
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Qué toca ahora?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar día y hora actual
        Text(
            text = "Hoy es ${diaActual.value}, ${horaActual.value}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        // Mostrar clase actual o mensaje de no hay clases
        if (claseActual.value != null) {
            Text(
                text = "Estás en clase de ${claseActual.value!!.nombre}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Hora: ${claseActual.value!!.hora}",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Text(
                text = "No hay clases en curso en este momento.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
// Función para obtener el día actual en texto
fun obtenerDiaSemana(diaSemana: Int): String {
    return when (diaSemana) {
        java.util.Calendar.MONDAY -> "Lunes"
        java.util.Calendar.TUESDAY -> "Martes"
        java.util.Calendar.WEDNESDAY -> "Miércoles"
        java.util.Calendar.THURSDAY -> "Jueves"
        java.util.Calendar.FRIDAY -> "Viernes"
        java.util.Calendar.SATURDAY -> "Sábado"
        java.util.Calendar.SUNDAY -> "Domingo"
        else -> "Desconocido"
    }
}

// Función para filtrar la clase actual según día y hora
fun filtrarClaseActual(dia: String, hora: String, clases: List<Clase>): Clase? {
    return clases
        .filter { it.dia == dia }
        .find { clase ->
            val horaClase = clase.hora.split(":").map { it.toInt() }
            val horaActual = hora.split(":").map { it.toInt() }
            horaActual[0] >= horaClase[0] && horaActual[1] >= horaClase[1] // Filtra si está en la hora actual
        }
}