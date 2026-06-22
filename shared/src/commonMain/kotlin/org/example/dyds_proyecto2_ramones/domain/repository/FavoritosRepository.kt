package org.example.dyds_proyecto2_ramones.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego

interface FavoritosRepository {
    fun getFavoritos(): Flow<List<Juego>>

    suspend fun agregar(detalle: DetalleJuego)

    suspend fun eliminar(appId: String)

    suspend fun getDetalleLocal(appId: String): DetalleJuego?
}
