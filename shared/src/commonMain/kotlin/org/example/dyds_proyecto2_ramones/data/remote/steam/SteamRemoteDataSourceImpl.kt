package org.example.dyds_proyecto2_ramones.data.remote.steam

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.steam.mapper.toDomain
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import kotlin.coroutines.CoroutineContext

private const val STEAM_BASE_URL = "https://api.steampowered.com/"

class SteamRemoteDataSourceImpl(
    private val client: HttpClient,
    private val apiKey: String,
    private val ioDispatcher: CoroutineContext
) : SteamRemoteDataSource {

    override suspend fun fetchPerfil(steamId: String): Result<Perfil> =
        withContext(ioDispatcher) {
            runCatching {
                val response: SteamPerfilResponseDto = client.get("${STEAM_BASE_URL}ISteamUser/GetPlayerSummaries/v0002/") {
                    parameter("key", apiKey)
                    parameter("steamids", steamId)
                }.body()
                val players = response.response.players
                if (players.isEmpty()) throw IllegalStateException("No player found for steamId: $steamId")
                players.first().toDomain()
            }
        }

    override suspend fun fetchBiblioteca(steamId: String): Result<List<Juego>> =
        withContext(ioDispatcher) {
            runCatching {
                val response: SteamBibliotecaResponseDto = client.get("${STEAM_BASE_URL}IPlayerService/GetOwnedGames/v0001/") {
                    parameter("key", apiKey)
                    parameter("steamid", steamId)
                    parameter("include_appinfo", true)
                    parameter("include_played_free_games", true)
                }.body()
                val wrapper = response.response
                val games = wrapper.games ?: emptyList()
                games.map { it.toDomain() }
            }
        }

    override suspend fun fetchLogros(steamId: String, appId: Long): Result<List<Logro>> =
        withContext(ioDispatcher) {
            runCatching {
                val response: SteamLogrosResponseDto = client.get("${STEAM_BASE_URL}ISteamUserStats/GetPlayerAchievements/v0001/") {
                    parameter("key", apiKey)
                    parameter("steamid", steamId)
                    parameter("appid", appId)
                    parameter("l", "spanish")
                }.body()
                val achievements = response.playerstats.achievements ?: emptyList()
                achievements.map { it.toDomain() }
            }
        }
}






