package org.example.dyds_proyecto2_ramones

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.example.dyds_proyecto2_ramones.presentation.biblioteca.BibliotecaScreen
import org.example.dyds_proyecto2_ramones.presentation.busqueda.BusquedaScreen
import org.example.dyds_proyecto2_ramones.presentation.common.Routes
import org.example.dyds_proyecto2_ramones.presentation.detalle.DetalleScreen
import org.example.dyds_proyecto2_ramones.presentation.favoritos.FavoritosScreen


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Routes.BUSQUEDA,
        ) {
            composable(route = Routes.BUSQUEDA) {
                BusquedaScreen(
                    onNavigateBiblioteca = { steamId -> navController.navigate(Routes.biblioteca(steamId)) },
                    onNavigateFavoritos = { navController.navigate(Routes.FAVORITOS) },
                )
            }

            composable(
                route = Routes.BIBLIOTECA,
                arguments = listOf(navArgument(Routes.STEAM_ID_ARG) { type = NavType.StringType }),
            ) {
                BibliotecaScreen(
                    steamId = "{${Routes.STEAM_ID_ARG}}",
                    onNavigateDetalle = { appId -> navController.navigate(Routes.detalle(appId)) },
                    onNavigateFavoritos = { navController.navigate(Routes.FAVORITOS) },
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            composable(
                route = Routes.DETALLE,
                arguments = listOf(navArgument(Routes.APP_ID_ARG) { type = NavType.StringType }),
            ) {
                DetalleScreen(
                    appId = "{${Routes.APP_ID_ARG}}",
                    onNavigateFavoritos = { navController.navigate(Routes.FAVORITOS) },
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            composable(route = Routes.FAVORITOS) {
                FavoritosScreen(
                    onNavigateDetalle = { appId -> navController.navigate(Routes.detalle(appId)) },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
        }
    }
}