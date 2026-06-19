package org.example.dyds_proyecto2_ramones.presentation.detalle

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
fun DetalleScreen(
    appId: String,
    onNavigateFavoritos: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Detalle (placeholder)", style = MaterialTheme.typography.headlineSmall)
        Text("AppId: $appId")
        Button(onClick = onNavigateFavoritos) {
            Text("Ir a Favoritos")
        }
        Button(onClick = onNavigateBack) {
            Text("Volver")
        }
    }
}

