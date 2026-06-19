package org.example.dyds_proyecto2_ramones.presentation.common

object Routes {
    const val STEAM_ID_ARG = "steamId"
    const val APP_ID_ARG = "appId"

    const val BUSQUEDA = "busqueda"
    const val BIBLIOTECA = "biblioteca/{$STEAM_ID_ARG}"
    const val DETALLE = "detalle/{$APP_ID_ARG}"
    const val FAVORITOS = "favoritos"

    fun biblioteca(steamId: String): String = "biblioteca/$steamId"

    fun detalle(appId: String): String = "detalle/$appId"
}

