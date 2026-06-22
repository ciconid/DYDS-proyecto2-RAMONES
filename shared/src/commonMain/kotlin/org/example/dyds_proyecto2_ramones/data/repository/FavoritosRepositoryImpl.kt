package org.example.dyds_proyecto2_ramones.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.dyds_proyecto2_ramones.data.local.FavoritosLocalDataSource
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository

class FavoritosRepositoryImpl(
    private val local: FavoritosLocalDataSource,
) : FavoritosRepository {
    override fun getFavoritos(): Flow<List<Juego>> = local.getFavoritosFlow()

    override suspend fun agregar(detalle: DetalleJuego) = local.insertar(detalle)

    override suspend fun eliminar(appId: String) = local.eliminar(appId)

    override suspend fun getDetalleLocal(appId: String): DetalleJuego? = local.getDetalleLocal(appId)
}
