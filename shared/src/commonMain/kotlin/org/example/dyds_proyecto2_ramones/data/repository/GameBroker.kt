package org.example.dyds_proyecto2_ramones.data.repository

import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgDetalleDto
import org.example.dyds_proyecto2_ramones.data.remote.rawg.mapper.toDatosExtra
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.steam.mapper.toDomain
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import kotlin.coroutines.CoroutineContext

class GameBroker(
    private val steamRemote: SteamRemoteDataSource,
    private val rawgRemote: RawgRemoteDataSource,
    private val ioDispatcher: CoroutineContext
) {

    suspend fun enrichBibliotecaGeneros(juegos: List<Juego>): List<Juego> =
        withContext(ioDispatcher) {
            juegos.map { juego ->
                val generos = fetchGenresFromRawg(juego.nombre)
                if (generos.isEmpty()) juego else juego.copy(generos = generos)
            }
        }

    suspend fun buildDetalle(steamId: String, juego: Juego): Result<DetalleJuego> =
        withContext(ioDispatcher) {
            runCatching {
                val rawgDetail = searchRawgDetail(juego.nombre)
                val screenshots = rawgDetail?.let { fetchScreenshots(it.id) } ?: emptyList()
                val logros = fetchLogrosFromSteam(steamId, juego.appId)
                val datosExtra = rawgDetail?.toDatosExtra()
                val juegoConGeneros = if (datosExtra?.generos.isNullOrEmpty()) juego else juego.copy(generos = datosExtra.generos)

                DetalleJuego(
                    juego = juegoConGeneros,
                    descripcion = datosExtra?.descripcion.orEmpty(),
                    metacriticScore = datosExtra?.metacriticScore,
                    screenshots = screenshots,
                    logros = logros
                )
            }
        }

    private suspend fun fetchGenresFromRawg(gameName: String): List<String> {
        val searchResult = rawgRemote.searchGamesByName(gameName).getOrNull() ?: return emptyList()
        val match = searchResult.results.firstOrNull { it.name.equals(gameName, ignoreCase = true) }
            ?: searchResult.results.firstOrNull()
            ?: return emptyList()
        return match.genres.map { it.name }.filter { it.isNotBlank() }.distinctBy { it.lowercase() }
    }

    private suspend fun searchRawgDetail(gameName: String): RawgDetalleDto? {
        val searchResult = rawgRemote.searchGamesByName(gameName).getOrNull() ?: return null
        if (searchResult.results.isEmpty()) return null

        val gameId = searchResult.results.first().id
        return rawgRemote.getGameDetail(gameId).getOrNull()
    }

    private suspend fun fetchScreenshots(rawgId: Int): List<String> {
        return rawgRemote.getScreenshots(rawgId)
            .map { response -> response.results.map { it.image } }
            .getOrElse { emptyList() }
    }

    private suspend fun fetchLogrosFromSteam(steamId: String, appId: String): List<Logro> {
        val appIdLong = appId.toLongOrNull()
            ?: throw IllegalArgumentException("Invalid appId: $appId")
        return steamRemote.fetchLogros(steamId, appIdLong)
            .map { responseDto ->
                val achievements = responseDto.playerstats.achievements ?: emptyList()
                achievements.map { it.toDomain() }
            }
            .getOrElse { emptyList() }
    }
}