package org.example.dyds_proyecto2_ramones.domain.model

data class DetalleJuego(
    val juego: Juego,
    val descripcion: String,
    val metacriticScore: Int?,
    val screenshots: List<String>,
    val logros: List<Logro>,
)

