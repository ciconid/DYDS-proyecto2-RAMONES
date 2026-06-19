package org.example.dyds_proyecto2_ramones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.example.dyds_proyecto2_ramones.presentation.biblioteca.BibliotecaScreen
import org.example.dyds_proyecto2_ramones.presentation.busqueda.BusquedaScreen
import org.example.dyds_proyecto2_ramones.presentation.common.NavigationRail
import org.example.dyds_proyecto2_ramones.presentation.common.Screen
import org.example.dyds_proyecto2_ramones.presentation.detalle.DetalleScreen
import org.example.dyds_proyecto2_ramones.presentation.favoritos.FavoritosScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val bgBase = Color(0xFF0E1117)

        var currentScreen by remember { mutableStateOf<Screen>(Screen.Busqueda) }
        var railExpanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBase),
        ) {
            NavigationRail(
                expanded = railExpanded,
                onToggle = { railExpanded = !railExpanded },
                currentScreen = currentScreen,
                onNavigate = { currentScreen = it },
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgBase),
            ) {
                when (currentScreen) {
                    is Screen.Busqueda -> BusquedaScreen(
                        onNavigateBiblioteca = { currentScreen = Screen.Biblioteca },
                        onNavigateFavoritos = { currentScreen = Screen.Favoritos },
                    )

                    is Screen.Biblioteca -> BibliotecaScreen(
                        steamId = "76561198000000000",
                        onNavigateDetalle = { appId -> currentScreen = Screen.Detalle(appId) },
                        onNavigateFavoritos = { currentScreen = Screen.Favoritos },
                        onNavigateBack = { currentScreen = Screen.Busqueda },
                    )

                    is Screen.Detalle -> DetalleScreen(
                        appId = (currentScreen as Screen.Detalle).appId,
                        onNavigateFavoritos = { currentScreen = Screen.Favoritos },
                        onNavigateBack = { currentScreen = Screen.Biblioteca },
                    )

                    is Screen.Favoritos -> FavoritosScreen(
                        onNavigateDetalle = { appId -> currentScreen = Screen.Detalle(appId) },
                        onNavigateBack = { currentScreen = Screen.Busqueda },
                    )
                }
            }
        }
    }
}