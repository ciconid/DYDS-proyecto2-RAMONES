package org.example.dyds_proyecto2_ramones.data.local.entity

import org.example.dyds_proyecto2_ramones.domain.model.Juego

data class FavoritoEntity(
    val appId: String,
    val nombre: String,
    val iconUrl: String?,
    val horasJugadas: Double?,
)

fun FavoritoEntity.toDomain(): Juego = Juego(
    appId = appId,
    nombre = nombre,
    horasJugadas = horasJugadas ?: 0.0,
    iconUrl = iconUrl ?: "",
    generos = emptyList(),
)

fun Juego.toEntity(): FavoritoEntity = FavoritoEntity(
    appId = appId,
    nombre = nombre,
    iconUrl = iconUrl.takeIf { it.isNotBlank() },
    horasJugadas = horasJugadas,
)
