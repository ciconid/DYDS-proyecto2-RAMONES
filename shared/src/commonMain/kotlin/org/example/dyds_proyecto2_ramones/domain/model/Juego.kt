package org.example.dyds_proyecto2_ramones.domain.model

data class Juego(
    val appId: String,
    val nombre: String,
    val horasJugadas: Double,
    val iconUrl: String,
    val generos: List<String>,
)

