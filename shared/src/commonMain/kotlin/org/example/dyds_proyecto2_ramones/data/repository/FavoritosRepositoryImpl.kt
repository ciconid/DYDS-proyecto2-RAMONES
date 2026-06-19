package org.example.dyds_proyecto2_ramones.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.dyds_proyecto2_ramones.data.local.FavoritosLocalDataSource
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository

class FavoritosRepositoryImpl(
    private val local: FavoritosLocalDataSource,
) : FavoritosRepository {
    override fun getFavoritos(): Flow<List<Juego>> = local.getFavoritosFlow()

    override suspend fun agregar(juego: Juego) = local.insertar(juego)

    override suspend fun eliminar(appId: String) = local.eliminar(appId)
}
