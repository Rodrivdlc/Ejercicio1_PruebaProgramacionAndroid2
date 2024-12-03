package com.example.ejercicio1_pruebaprogramacionandroid2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicio1_pruebaprogramacionandroid2.navigation.AppNavigation
import com.example.ejercicio1_pruebaprogramacionandroid2.screens.MainScreen
import com.example.ejercicio1_pruebaprogramacionandroid2.ui.theme.Ejercicio1_PruebaProgramacionAndroid2Theme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AppNavigation()

        }
    }
}
