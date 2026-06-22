package org.example.dyds_proyecto2_ramones.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.dyds_proyecto2_ramones.data.local.entity.toDetalleLocal
import org.example.dyds_proyecto2_ramones.data.local.entity.toDomain
import org.example.dyds_proyecto2_ramones.data.local.entity.FavoritoEntity
import org.example.dyds_proyecto2_ramones.data.local.entity.toGeneroStorage
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
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

    suspend fun insertar(detalle: DetalleJuego) {
        val juego = detalle.juego
        val descripcionPersistida = resolverDescripcionPersistida(
            appId = juego.appId,
            descripcionNueva = detalle.descripcion,
        )
        db.favoritosQueries.insertFavorito(
            appId = juego.appId,
            nombre = juego.nombre,
            iconUrl = juego.iconUrl,
            horasJugadas = juego.horasJugadas,
            descripcion = descripcionPersistida,
            generos = juego.generos.toGeneroStorage(),
        )
        refresh()
    }

    suspend fun eliminar(appId: String) {
        db.favoritosQueries.deleteFavorito(appId)
        refresh()
    }

    suspend fun getDetalleLocal(appId: String): DetalleJuego? =
        db.favoritosQueries.selectFavoritoByAppId(
            appId = appId,
            mapper = { favAppId, nombre, iconUrl, horasJugadas, descripcion, generos ->
                FavoritoEntity(
                    appId = favAppId,
                    nombre = nombre,
                    iconUrl = iconUrl,
                    horasJugadas = horasJugadas,
                    descripcion = descripcion,
                    generos = generos,
                ).toDetalleLocal()
            },
        ).executeAsOneOrNull()

    private fun refresh() {
        _favoritos.value = db.favoritosQueries.selectAllFavoritos(
            mapper = { appId, nombre, iconUrl, horasJugadas, descripcion, generos ->
                FavoritoEntity(
                    appId = appId,
                    nombre = nombre,
                    iconUrl = iconUrl,
                    horasJugadas = horasJugadas,
                    descripcion = descripcion,
                    generos = generos,
                ).toDomain()
            },
        ).executeAsList()
    }

    private fun resolverDescripcionPersistida(appId: String, descripcionNueva: String): String {
        if (descripcionNueva.isNotBlank()) return descripcionNueva

        val descripcionActual = db.favoritosQueries.selectFavoritoByAppId(
            appId = appId,
            mapper = { _, _, _, _, descripcion, _ -> descripcion },
        ).executeAsOneOrNull()

        return descripcionActual.orEmpty()
    }
}
