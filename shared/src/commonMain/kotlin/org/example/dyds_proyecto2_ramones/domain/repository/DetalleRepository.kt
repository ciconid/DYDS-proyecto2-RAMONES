package org.example.dyds_proyecto2_ramones.domain.repository

import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego

interface DetalleRepository {
    suspend fun getDetalle(steamId: String, appId: String): Result<DetalleJuego>
}

