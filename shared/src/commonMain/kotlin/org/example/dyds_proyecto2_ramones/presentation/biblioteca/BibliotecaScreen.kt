package org.example.dyds_proyecto2_ramones.presentation.biblioteca

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
fun BibliotecaScreen(
    steamId: String,
    onNavigateDetalle: (String) -> Unit,
    onNavigateFavoritos: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Biblioteca (placeholder)", style = MaterialTheme.typography.headlineSmall)
        Text("SteamId: $steamId")
        Button(onClick = { onNavigateDetalle("570") }) {
            Text("Ir a Detalle")
        }
        Button(onClick = onNavigateFavoritos) {
            Text("Ir a Favoritos")
        }
        Button(onClick = onNavigateBack) {
            Text("Volver")
        }
    }
}

