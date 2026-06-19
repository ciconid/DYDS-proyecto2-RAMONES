package org.example.dyds_proyecto2_ramones.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.dyds_proyecto2_ramones.domain.model.Juego

interface FavoritosRepository {
    fun getFavoritos(): Flow<List<Juego>>

    suspend fun agregar(juego: Juego)

    suspend fun eliminar(appId: String)
}

