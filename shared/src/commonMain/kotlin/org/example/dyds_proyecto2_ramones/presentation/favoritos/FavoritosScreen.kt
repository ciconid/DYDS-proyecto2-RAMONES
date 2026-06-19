package org.example.dyds_proyecto2_ramones.presentation.favoritos

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
fun FavoritosScreen(
    onNavigateDetalle: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Favoritos (placeholder)", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { onNavigateDetalle("730") }) {
            Text("Abrir detalle placeholder")
        }
        Button(onClick = onNavigateBack) {
            Text("Volver")
        }
    }
}

