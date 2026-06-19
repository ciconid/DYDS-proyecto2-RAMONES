package org.example.dyds_proyecto2_ramones.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.dyds_proyecto2_ramones.data.local.entity.toDomain
import org.example.dyds_proyecto2_ramones.data.local.entity.FavoritoEntity
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase

class FavoritosLocalDataSource(
    private val db: AppDatabase,
) {
    private val _favoritos = MutableStateFlow<List<Juego>>(emptyList())

    init {
        refresh()
    }

    fun getFavoritosFlow(): Flow<List<Juego>> = _favoritos

    suspend fun insertar(juego: Juego) {
        db.favoritosQueries.insertFavorito(juego.appId, juego.nombre, juego.iconUrl, juego.horasJugadas)
        refresh()
    }

    suspend fun eliminar(appId: String) {
        db.favoritosQueries.deleteFavorito(appId)
        refresh()
    }

    private fun refresh() {
        _favoritos.value = db.favoritosQueries.selectAllFavoritos(
            mapper = { appId, nombre, iconUrl, horasJugadas ->
                FavoritoEntity(
                    appId = appId,
                    nombre = nombre,
                    iconUrl = iconUrl,
                    horasJugadas = horasJugadas,
                ).toDomain()
            },
        ).executeAsList()
    }
}

