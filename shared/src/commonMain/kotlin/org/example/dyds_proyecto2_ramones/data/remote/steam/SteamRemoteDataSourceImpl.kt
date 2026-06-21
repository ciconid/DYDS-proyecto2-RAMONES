package org.example.dyds_proyecto2_ramones.data.remote.steam

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamBibliotecaResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamLogrosResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamPerfilResponseDto
import kotlin.coroutines.CoroutineContext

private const val STEAM_BASE_URL = "https://api.steampowered.com/"

class SteamRemoteDataSourceImpl(
    private val client: HttpClient,
    private val apiKey: String,
    private val ioDispatcher: CoroutineContext
) : SteamRemoteDataSource {

    override suspend fun fetchPerfil(steamId: String): Result<SteamPerfilResponseDto> =
        withContext(ioDispatcher) {
            runCatching {
                client.get("${STEAM_BASE_URL}ISteamUser/GetPlayerSummaries/v0002/") {
                    parameter("key", apiKey)
                    parameter("steamids", steamId)
                }.body<SteamPerfilResponseDto>()
            }
        }

    override suspend fun fetchBiblioteca(steamId: String): Result<SteamBibliotecaResponseDto> =
        withContext(ioDispatcher) {
            runCatching {
                client.get("${STEAM_BASE_URL}IPlayerService/GetOwnedGames/v0001/") {
                    parameter("key", apiKey)
                    parameter("steamid", steamId)
                    parameter("include_appinfo", true)
                    parameter("include_played_free_games", true)
                }.body<SteamBibliotecaResponseDto>()
            }
        }

    override suspend fun fetchLogros(steamId: String, appId: Long): Result<SteamLogrosResponseDto> =
        withContext(ioDispatcher) {
            runCatching {
                client.get("${STEAM_BASE_URL}ISteamUserStats/GetPlayerAchievements/v0001/") {
                    parameter("key", apiKey)
                    parameter("steamid", steamId)
                    parameter("appid", appId)
                    parameter("l", "spanish")
                }.body<SteamLogrosResponseDto>()
            }
        }
}






