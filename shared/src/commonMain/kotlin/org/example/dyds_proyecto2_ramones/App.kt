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
import org.koin.java.KoinJavaComponent.inject
import org.example.dyds_proyecto2_ramones.presentation.biblioteca.BibliotecaScreen
import org.example.dyds_proyecto2_ramones.presentation.biblioteca.BibliotecaViewModel
import org.example.dyds_proyecto2_ramones.presentation.busqueda.BusquedaScreen
import org.example.dyds_proyecto2_ramones.presentation.busqueda.BusquedaViewModel
import org.example.dyds_proyecto2_ramones.presentation.common.NavigationRail
import org.example.dyds_proyecto2_ramones.presentation.common.Screen
import org.example.dyds_proyecto2_ramones.presentation.detalle.DetalleScreen
import org.example.dyds_proyecto2_ramones.presentation.detalle.DetalleViewModel
import org.example.dyds_proyecto2_ramones.presentation.favoritos.FavoritosScreen
import org.example.dyds_proyecto2_ramones.presentation.favoritos.FavoritosViewModel
import org.example.dyds_proyecto2_ramones.domain.usecase.GetFavoritosUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.AgregarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.EliminarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetDetalleUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetPerfilUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetBibliotecaUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.FiltrarPorGeneroUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.OrdenarPorHorasUseCase

@Composable
@Preview
fun App() {
    MaterialTheme {
        val bgBase = Color(0xFF0E1117)

        var currentScreen by remember { mutableStateOf<Screen>(Screen.Busqueda) }
        var railExpanded by remember { mutableStateOf(false) }
        var selectedSteamId by remember { mutableStateOf("") }

        val getPerfilUseCase: GetPerfilUseCase by inject(GetPerfilUseCase::class.java)
        val getBibliotecaUseCase: GetBibliotecaUseCase by inject(GetBibliotecaUseCase::class.java)
        val getDetalleUseCase: GetDetalleUseCase by inject(GetDetalleUseCase::class.java)
        val filtrarPorGeneroUseCase: FiltrarPorGeneroUseCase by inject(FiltrarPorGeneroUseCase::class.java)
        val ordenarPorHorasUseCase: OrdenarPorHorasUseCase by inject(OrdenarPorHorasUseCase::class.java)
        val getFavoritosUseCase: GetFavoritosUseCase by inject(GetFavoritosUseCase::class.java)
        val agregarFavoritoUseCase: AgregarFavoritoUseCase by inject(AgregarFavoritoUseCase::class.java)
        val eliminarFavoritoUseCase: EliminarFavoritoUseCase by inject(EliminarFavoritoUseCase::class.java)
        val busquedaViewModel = remember(getPerfilUseCase) { BusquedaViewModel(getPerfilUseCase) }
        val bibliotecaViewModel = remember(getBibliotecaUseCase, filtrarPorGeneroUseCase, ordenarPorHorasUseCase) {
            BibliotecaViewModel(getBibliotecaUseCase, filtrarPorGeneroUseCase, ordenarPorHorasUseCase)
        }
        val detalleViewModel = remember(getDetalleUseCase, getFavoritosUseCase, agregarFavoritoUseCase, eliminarFavoritoUseCase) {
            DetalleViewModel(getDetalleUseCase, getFavoritosUseCase, agregarFavoritoUseCase, eliminarFavoritoUseCase)
        }
        val favoritosViewModel = remember(getFavoritosUseCase, eliminarFavoritoUseCase) {
            FavoritosViewModel(getFavoritosUseCase, eliminarFavoritoUseCase)
        }

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
                        onNavigateBiblioteca = { steamId ->
                            selectedSteamId = steamId
                            currentScreen = Screen.Biblioteca
                        },
                        onNavigateFavoritos = { currentScreen = Screen.Favoritos },
                        viewModel = busquedaViewModel,
                    )

                    is Screen.Biblioteca -> BibliotecaScreen(
                        steamId = selectedSteamId,
                        onNavigateDetalle = { appId -> currentScreen = Screen.Detalle(appId) },
                        onNavigateFavoritos = { currentScreen = Screen.Favoritos },
                        onNavigateBack = { currentScreen = Screen.Busqueda },
                        viewModel = bibliotecaViewModel,
                    )

                    is Screen.Detalle -> DetalleScreen(
                        steamId = selectedSteamId,
                        appId = (currentScreen as Screen.Detalle).appId,
                        onNavigateBack = { currentScreen = Screen.Biblioteca },
                        viewModel = detalleViewModel,
                    )

                    is Screen.Favoritos -> FavoritosScreen(
                        onNavigateDetalle = { appId -> currentScreen = Screen.Detalle(appId) },
                        onNavigateBack = { currentScreen = Screen.Busqueda },
                        viewModel = favoritosViewModel,
                    )
                }
            }
        }
    }
}