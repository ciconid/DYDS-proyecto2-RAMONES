package org.example.dyds_proyecto2_ramones.data.remote.steam

import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.steam.mapper.toDomain
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import kotlin.coroutines.CoroutineContext

class SteamRemoteDataSourceImpl(
    private val apiService: SteamApiService,
    private val apiKey: String,
    private val ioDispatcher: CoroutineContext
) : SteamRemoteDataSource {

    override suspend fun fetchPerfil(steamId: String): Result<Perfil> =
        withContext(ioDispatcher) {
            runCatching {
                val response = apiService.getPlayerSummaries(apiKey, steamId)
                val players = response.response.players
                if (players.isEmpty()) throw IllegalStateException("No player found for steamId: $steamId")
                players.first().toDomain()
            }
        }

    override suspend fun fetchBiblioteca(steamId: String): Result<List<Juego>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = apiService.getOwnedGames(apiKey, steamId, true)
                val wrapper = response.response
                val games = wrapper.games ?: emptyList()
                games.map { it.toDomain() }
            }
        }

    override suspend fun fetchLogros(steamId: String, appId: Long): Result<List<Logro>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = apiService.getPlayerAchievements(apiKey, steamId, appId, "spanish")
                val achievements = response.playerstats.achievements ?: emptyList()
                achievements.map { it.toDomain() }
            }
        }
}



