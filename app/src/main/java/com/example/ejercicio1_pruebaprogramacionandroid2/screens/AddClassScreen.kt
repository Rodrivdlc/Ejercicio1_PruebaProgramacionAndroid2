package com.example.ejercicio1_pruebaprogramacionandroid2.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.ejercicio1_pruebaprogramacionandroid2.data.Clase
import com.google.firebase.database.FirebaseDatabase

@Composable
fun AddClassScreen(onClassAdded: () -> Unit = {}) {
    val database = FirebaseDatabase.getInstance().reference
    val nombre = remember { mutableStateOf("") }
    val dia = remember { mutableStateOf("Lunes") } // Día predeterminado
    val hora = remember { mutableStateOf("") }
    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Añadir Clase", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text("Asignatura") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        // Selector para el día
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Día: ${dia.value}")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dias.forEach { diaSeleccionado ->
                    DropdownMenuItem(
                        text = { Text(text = diaSeleccionado) },
                        onClick = {
                            dia.value = diaSeleccionado
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = hora.value,
            onValueChange = { hora.value = it },
            label = { Text("Hora (ej. 08:00)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (nombre.value.isNotBlank() && hora.value.isNotBlank()) {
                    val nuevaClase = mapOf(
                        "nombre" to nombre.value,
                        "dia" to dia.value,
                        "hora" to hora.value
                    )
                    database.child("clases").push().setValue(nuevaClase)
                        .addOnSuccessListener {
                            Log.d("AddClassScreen", "Clase añadida correctamente")
                            onClassAdded()
                        }
                        .addOnFailureListener {
                            Log.e("AddClassScreen", "Error al añadir clase: ${it.message}")
                        }
                } else {
                    Log.e("AddClassScreen", "Por favor, rellena todos los campos obligatorios")
                }
            }) {
                Text("Añadir")
            }

            Button(onClick = { nombre.value = ""; dia.value = "Lunes"; hora.value = "" }) {
                Text("Cancelar")
            }
        }
    }
}

