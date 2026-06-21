package org.example.dyds_proyecto2_ramones.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.*
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamLogrosResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamPlayerStatsDto
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameBrokerTest {

    private val steamRemote: org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource = mockk()
    private val rawgRemote: org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSource = mockk()
    private val broker = GameBroker(steamRemote, rawgRemote, Dispatchers.Unconfined)

    @Test
    fun `buildDetalle uses rawg when available and steam logros`() = runBlocking {
        val juego = Juego("570", "Dota 2", 12.0, "", emptyList())
        val mockSearchResponse = RawgSearchResponseDto(
            results = listOf(
                RawgGameDto(id = 570, name = "Dota 2", metacritic = 90, background_image = "bg.jpg", genres = listOf(RawgGenreDto("Action")))
            )
        )
        coEvery { rawgRemote.searchGamesByName("Dota 2") } returns Result.success(mockSearchResponse)
        val mockDetalleDto = RawgDetalleDto(
            id = 570,
            name = "Dota 2",
            description_raw = "desc",
            metacritic = 90,
            background_image = "bg.jpg",
            genres = listOf(RawgGenreDto("Action"))
        )
        coEvery { rawgRemote.getGameDetail(570) } returns Result.success(mockDetalleDto)
        val mockScreenshotsResponse = RawgScreenshotsResponseDto(
            results = listOf(
                RawgScreenshotDto("s1.jpg"),
                RawgScreenshotDto("s2.jpg")
            )
        )
        coEvery { rawgRemote.getScreenshots(570) } returns Result.success(mockScreenshotsResponse)

        val mockLogrosResponseDto = SteamLogrosResponseDto(
            playerstats = SteamPlayerStatsDto(achievements = null)
        )
        coEvery { steamRemote.fetchLogros(any(), 570L) } returns Result.success(mockLogrosResponseDto)

        val result = broker.buildDetalle("steamId", juego)
        assertTrue(result.isSuccess)
        val detalle = result.getOrThrow()
        assertEquals(juego, detalle.juego)
        assertEquals("desc", detalle.descripcion)
        assertEquals(90, detalle.metacriticScore)
        assertEquals(listOf("s1.jpg", "s2.jpg"), detalle.screenshots)
    }

    @Test
    fun `buildDetalle falls back when rawg not found`() = runBlocking {
        val juego = Juego("570", "Unknown Game", 1.0, "", emptyList())
        coEvery { rawgRemote.searchGamesByName(any()) } returns Result.success(RawgSearchResponseDto(results = emptyList()))
        val mockLogrosResponseDto = SteamLogrosResponseDto(
            playerstats = SteamPlayerStatsDto(achievements = null)
        )
        coEvery { steamRemote.fetchLogros(any(), 570L) } returns Result.success(mockLogrosResponseDto)

        val result = broker.buildDetalle("steamId", juego)
        assertTrue(result.isSuccess)
        val detalle = result.getOrThrow()
        assertEquals("", detalle.descripcion)
        assertEquals(null, detalle.metacriticScore)
        assertEquals(emptyList<String>(), detalle.screenshots)
    }
}