package org.example.dyds_proyecto2_ramones.data.repository

import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.steam.mapper.toDomain
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.repository.DetalleRepository
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import kotlin.coroutines.CoroutineContext

class DetalleRepositoryImpl(
    private val steamRemote: SteamRemoteDataSource,
    private val gameBroker: GameBroker,
    private val favoritosRepository: FavoritosRepository,
    private val ioDispatcher: CoroutineContext
) : DetalleRepository {

    override suspend fun getDetalle(steamId: String, appId: String): Result<DetalleJuego> =
        withContext(ioDispatcher) {
            runCatching {
                if (steamId.isBlank()) {
                    return@runCatching favoritosRepository.getDetalleLocal(appId)
                        ?: throw IllegalStateException("No se encontro detalle local para el appId $appId")
                }

                val responseDto = steamRemote.fetchBiblioteca(steamId).getOrThrow()
                val games = responseDto.response.games ?: emptyList()
                val juegos = games.map { it.toDomain() }
                val juego = juegos.find { it.appId == appId }
                    ?: throw IllegalStateException("No se encontro el juego con appId $appId en la biblioteca del usuario $steamId")

                gameBroker.buildDetalle(steamId, juego).getOrElse { throw it }
            }
        }
}
