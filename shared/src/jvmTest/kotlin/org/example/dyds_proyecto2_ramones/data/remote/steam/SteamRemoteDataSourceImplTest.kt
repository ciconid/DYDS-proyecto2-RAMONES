package org.example.dyds_proyecto2_ramones.data.remote.steam

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SteamRemoteDataSourceImplTest {

    private val apiService: SteamApiService = mockk()
    private val apiKey = "test-key"
    private val dataSource = SteamRemoteDataSourceImpl(apiService, apiKey, Dispatchers.Unconfined)

    @Test
    fun `fetchPerfil returns Perfil on success`() = runBlocking {
        val dto = SteamPerfilResponseDto(
            SteamPlayerWrapperDto(listOf(
                SteamPlayerSummaryDto("76561198000000000", "PlayerName", "http://avatar")
            ))
        )

        coEvery { apiService.getPlayerSummaries(apiKey, "76561198000000000") } returns dto

        val result = dataSource.fetchPerfil("76561198000000000")
        assertTrue(result.isSuccess)
        val perfil = result.getOrThrow()
        assertEquals("76561198000000000", perfil.steamId)
        assertEquals("PlayerName", perfil.nombre)
        assertEquals("http://avatar", perfil.avatarUrl)
    }

    @Test
    fun `fetchPerfil returns failure when no players`() = runBlocking {
        val dto = SteamPerfilResponseDto(SteamPlayerWrapperDto(emptyList()))
        coEvery { apiService.getPlayerSummaries(apiKey, "nope") } returns dto

        val result = dataSource.fetchPerfil("nope")
        assertTrue(result.isFailure)
        val ex = result.exceptionOrNull()
        assertTrue(ex is IllegalStateException)
    }

    @Test
    fun `fetchBiblioteca returns empty list when games null`() = runBlocking {
        val dto = SteamBibliotecaResponseDto(SteamBibliotecaWrapperDto(null, 0))
        coEvery { apiService.getOwnedGames(apiKey, "steamid", true) } returns dto

        val result = dataSource.fetchBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertTrue(list.isEmpty())
    }

    @Test
    fun `fetchBiblioteca maps games correctly`() = runBlocking {
        val juegoDto = SteamJuegoDto(570, "Dota 2", 720L, "iconpath")
        val dto = SteamBibliotecaResponseDto(SteamBibliotecaWrapperDto(listOf(juegoDto), 1))
        coEvery { apiService.getOwnedGames(apiKey, "steamid", true) } returns dto

        val result = dataSource.fetchBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(1, list.size)
        val juego = list[0]
        assertEquals("570", juego.appId)
        assertEquals("Dota 2", juego.nombre)
        // 720 minutes -> 12.0 hours
        assertEquals(12.0, juego.horasJugadas)
        assertTrue(juego.iconUrl.contains("570"))
        assertEquals(emptyList<String>(), juego.generos)
    }

    @Test
    fun `fetchLogros returns empty list when achievements null`() = runBlocking {
        val dto = SteamLogrosResponseDto(SteamPlayerStatsDto(null))
        coEvery { apiService.getPlayerAchievements(apiKey, "steamid", 570L, "spanish") } returns dto

        val result = dataSource.fetchLogros("steamid", 570L)
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertTrue(list.isEmpty())
    }

    @Test
    fun `fetchLogros maps achievements correctly`() = runBlocking {
        val logroDto = SteamLogroDto("API_NAME", 1, "First Blood", "Do something")
        val dto = SteamLogrosResponseDto(SteamPlayerStatsDto(listOf(logroDto)))
        coEvery { apiService.getPlayerAchievements(apiKey, "steamid", 570L, "spanish") } returns dto

        val result = dataSource.fetchLogros("steamid", 570L)
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(1, list.size)
        val logro = list[0]
        assertEquals("First Blood", logro.nombre)
        assertEquals("Do something", logro.descripcion)
        assertTrue(logro.conseguido)
    }
}

