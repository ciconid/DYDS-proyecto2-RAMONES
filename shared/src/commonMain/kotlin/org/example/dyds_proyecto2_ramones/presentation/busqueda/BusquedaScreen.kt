package org.example.dyds_proyecto2_ramones.presentation.busqueda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BusquedaScreen(
    onNavigateBiblioteca: (String) -> Unit,
    onNavigateFavoritos: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Busqueda (placeholder)", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { onNavigateBiblioteca("76561198000000000") }) {
            Text("Ir a Biblioteca")
        }
        Button(onClick = onNavigateFavoritos) {
            Text("Ir a Favoritos")
        }
    }
}

