package com.example.ejercicio1_pruebaprogramacionandroid2.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.ejercicio1_pruebaprogramacionandroid2.data.Clase

@Composable
fun ViewScheduleScreen() {
    val database = FirebaseDatabase.getInstance().reference.child("clases")
    val clases = remember { mutableStateListOf<Clase>() }
    val diasFiltrados = remember { mutableStateListOf<Clase>() }
    val dias = listOf("Todos", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val diaSeleccionado = remember { mutableStateOf("Todos") }
    var expanded by remember { mutableStateOf(false) }

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
                actualizarFiltro(diaSeleccionado.value, clases, diasFiltrados)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ViewScheduleScreen", "Error al leer las clases: ${error.message}")
            }
        })
    }

    // Actualizar filtro al cambiar el día seleccionado
    LaunchedEffect(diaSeleccionado.value) {
        actualizarFiltro(diaSeleccionado.value, clases, diasFiltrados)
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Horario Completo",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Filtro por día
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Filtrar por día: ${diaSeleccionado.value}")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dias.forEach { dia ->
                    DropdownMenuItem(
                        text = { Text(text = dia) }, // Corrige la definición de texto
                        onClick = {
                            diaSeleccionado.value = dia
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Lista de clases
        if (diasFiltrados.isEmpty()) {
            Text(text = "No hay clases para este día.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(diasFiltrados) { clase ->
                    ClaseItem(clase)
                }
            }
        }
    }
}

// Función para actualizar el filtro de días
fun actualizarFiltro(diaSeleccionado: String, clases: List<Clase>, diasFiltrados: MutableList<Clase>) {
    diasFiltrados.clear()
    if (diaSeleccionado == "Todos") {
        diasFiltrados.addAll(clases)
    } else {
        diasFiltrados.addAll(clases.filter { it.dia == diaSeleccionado })
    }
}


// Composable para mostrar cada clase
@Composable
fun ClaseItem(clase: Clase) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Asignatura: ${clase.nombre}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Día: ${clase.dia}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Hora: ${clase.hora}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}