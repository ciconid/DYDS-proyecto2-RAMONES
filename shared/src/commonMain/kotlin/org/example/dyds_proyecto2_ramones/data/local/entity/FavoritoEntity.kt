package org.example.dyds_proyecto2_ramones.data.local.entity

import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego

data class FavoritoEntity(
    val appId: String,
    val nombre: String,
    val iconUrl: String?,
    val horasJugadas: Double?,
    val descripcion: String,
    val generos: String,
)

fun FavoritoEntity.toDomain(): Juego = Juego(
    appId = appId,
    nombre = nombre,
    horasJugadas = horasJugadas ?: 0.0,
    iconUrl = iconUrl ?: "",
    generos = generos.toGenerosList(),
)

fun FavoritoEntity.toDetalleLocal(): DetalleJuego = DetalleJuego(
    juego = toDomain(),
    descripcion = descripcion.ifBlank { "Sin descripcion disponible" },
    metacriticScore = null,
    screenshots = emptyList(),
    logros = emptyList(),
)

private fun String.toGenerosList(): List<String> =
    split("|")
        .map { it.trim() }
        .filter { it.isNotBlank() }

fun List<String>.toGeneroStorage(): String =
    map { it.trim() }
        .filter { it.isNotBlank() }
        .joinToString(separator = "|")
