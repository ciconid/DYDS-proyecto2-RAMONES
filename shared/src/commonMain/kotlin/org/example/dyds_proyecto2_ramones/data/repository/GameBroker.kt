package org.example.dyds_proyecto2_ramones.data.repository

import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgGameDetail
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import kotlin.coroutines.CoroutineContext

class GameBroker(
    private val steamRemote: SteamRemoteDataSource,
    private val rawgRemote: RawgRemoteDataSource,
    private val ioDispatcher: CoroutineContext
) {

    suspend fun buildDetalle(steamId: String, juego: Juego): Result<DetalleJuego> =
        withContext(ioDispatcher) {
            runCatching {
                val rawgDetail = searchRawgDetail(juego.nombre)
                val screenshots = fetchScreenshots(rawgDetail)
                val (descripcion, metacritic) = parseRawgData(rawgDetail)
                val logros = fetchLogrosFromSteam(steamId, juego.appId)

                DetalleJuego(
                    juego = juego,
                    descripcion = descripcion,
                    metacriticScore = metacritic,
                    screenshots = screenshots,
                    logros = logros
                )
            }
        }

    private suspend fun searchRawgDetail(gameName: String): RawgGameDetail? {
        val searchResult = rawgRemote.searchGamesByName(gameName)
        if (!searchResult.isSuccess) return null

        val previews = searchResult.getOrThrow()
        if (previews.isEmpty()) return null

        val gameId = previews.first().id
        val detailResult = rawgRemote.getGameDetail(gameId)
        return if (detailResult.isSuccess) detailResult.getOrThrow() else null
    }

    private suspend fun fetchScreenshots(rawgDetail: RawgGameDetail?): List<String> {
        if (rawgDetail == null) return emptyList()
        return rawgRemote.getScreenshots(rawgDetail.id).getOrElse { emptyList() }
    }

    private fun parseRawgData(rawgDetail: RawgGameDetail?): Pair<String, Int?> {
        if (rawgDetail == null) return "" to null
        return rawgDetail.description to rawgDetail.metacritic
    }

    private suspend fun fetchLogrosFromSteam(steamId: String, appId: String): List<Logro> {
        val appIdLong = appId.toLongOrNull()
            ?: throw IllegalArgumentException("Invalid appId: $appId")
        return steamRemote.fetchLogros(steamId, appIdLong).getOrElse { emptyList() }
    }
}

