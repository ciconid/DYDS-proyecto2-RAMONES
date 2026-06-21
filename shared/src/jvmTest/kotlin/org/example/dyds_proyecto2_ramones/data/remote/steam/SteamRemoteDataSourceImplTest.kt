package org.example.dyds_proyecto2_ramones.data.remote.steam

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SteamRemoteDataSourceImplTest {

    private val apiKey = "test-key"

    private fun createClient(
        json: String,
        assertRequest: (String, Map<String, List<String>>) -> Unit = { _, _ -> }
    ): HttpClient = HttpClient(MockEngine { request ->
        assertRequest(
            request.url.encodedPath,
            request.url.parameters.entries().associate { it.key to it.value }
        )
        respond(
            content = json,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    }) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    @Test
    fun `fetchPerfil returns SteamPerfilResponseDto on success`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "response": {
                    "players": [
                      {
                        "steamid": "76561198000000000",
                        "personaname": "PlayerName",
                        "avatarfull": "http://avatar"
                      }
                    ]
                  }
                }
            """.trimIndent(),
            assertRequest = { path, parameters ->
                assertEquals("/ISteamUser/GetPlayerSummaries/v0002/", path)
                assertEquals(listOf(apiKey), parameters["key"])
                assertEquals(listOf("76561198000000000"), parameters["steamids"])
            }
        )
        val dataSource = SteamRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.fetchPerfil("76561198000000000")
        assertTrue(result.isSuccess)
        val responseDto = result.getOrThrow()
        assertEquals(1, responseDto.response.players.size)
        val player = responseDto.response.players.first()
        assertEquals("76561198000000000", player.steamid)
        assertEquals("PlayerName", player.personaname)
        assertEquals("http://avatar", player.avatarfull)
    }

    @Test
    fun `fetchPerfil returns empty players when no players`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "response": {
                    "players": []
                  }
                }
            """.trimIndent()
        )
        val dataSource = SteamRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.fetchPerfil("nope")
        assertTrue(result.isSuccess)
        val responseDto = result.getOrThrow()
        assertEquals(0, responseDto.response.players.size)
    }

    @Test
    fun `fetchBiblioteca returns empty list when games null`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "response": {
                    "games": null,
                    "game_count": 0
                  }
                }
            """.trimIndent(),
            assertRequest = { path, parameters ->
                assertEquals("/IPlayerService/GetOwnedGames/v0001/", path)
                assertEquals(listOf(apiKey), parameters["key"])
                assertEquals(listOf("steamid"), parameters["steamid"])
                assertEquals(listOf("true"), parameters["include_appinfo"])
            }
        )
        val dataSource = SteamRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.fetchBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val responseDto = result.getOrThrow()
        val games = responseDto.response.games
        assertTrue(games == null || games.isEmpty())
    }

    @Test
    fun `fetchBiblioteca returns SteamBibliotecaResponseDto correctly`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "response": {
                    "games": [
                      {
                        "appid": 570,
                        "name": "Dota 2",
                        "playtime_forever": 720,
                        "img_icon_url": "iconpath"
                      }
                    ],
                    "game_count": 1
                  }
                }
            """.trimIndent()
        )
        val dataSource = SteamRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.fetchBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val responseDto = result.getOrThrow()
        val games = responseDto.response.games
        assertEquals(1, games?.size)
        val game = games?.first()
        assertEquals(570, game?.appid)
        assertEquals("Dota 2", game?.name)
        assertEquals(720, game?.playtime_forever)
        assertEquals("iconpath", game?.img_icon_url)
    }

    @Test
    fun `fetchLogros returns SteamLogrosResponseDto with empty achievements`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "playerstats": {
                    "achievements": null
                  }
                }
            """.trimIndent(),
            assertRequest = { path, parameters ->
                assertEquals("/ISteamUserStats/GetPlayerAchievements/v0001/", path)
                assertEquals(listOf(apiKey), parameters["key"])
                assertEquals(listOf("steamid"), parameters["steamid"])
                assertEquals(listOf("570"), parameters["appid"])
                assertEquals(listOf("spanish"), parameters["l"])
            }
        )
        val dataSource = SteamRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.fetchLogros("steamid", 570L)
        assertTrue(result.isSuccess)
        val responseDto = result.getOrThrow()
        val achievements = responseDto.playerstats.achievements
        assertTrue(achievements == null || achievements.isEmpty())
    }

    @Test
    fun `fetchLogros returns SteamLogrosResponseDto with achievements`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "playerstats": {
                    "achievements": [
                      {
                        "apiname": "API_NAME",
                        "achieved": 1,
                        "name": "First Blood",
                        "description": "Do something"
                      }
                    ]
                  }
                }
            """.trimIndent()
        )
        val dataSource = SteamRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.fetchLogros("steamid", 570L)
        assertTrue(result.isSuccess)
        val responseDto = result.getOrThrow()
        val achievements = responseDto.playerstats.achievements
        assertEquals(1, achievements?.size)
        val achievement = achievements?.first()
        assertEquals("First Blood", achievement?.name)
        assertEquals("Do something", achievement?.description)
        assertEquals(1, achievement?.achieved)
    }
}