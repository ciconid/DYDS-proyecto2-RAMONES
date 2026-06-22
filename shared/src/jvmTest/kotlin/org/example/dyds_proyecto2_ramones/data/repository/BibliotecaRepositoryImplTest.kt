package org.example.dyds_proyecto2_ramones.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamBibliotecaResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamBibliotecaWrapperDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamJuegoDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BibliotecaRepositoryImplTest {

    private val steamRemote: SteamRemoteDataSource = mockk()
    private val gameBroker: GameBroker = mockk()
    private val repo = BibliotecaRepositoryImpl(steamRemote, gameBroker, Dispatchers.Unconfined)

    @Test
    fun `getBiblioteca returns list`() = runBlocking {
        val juegoDto = SteamJuegoDto(570, "Dota 2", 720, "icon")
        val responseDto = SteamBibliotecaResponseDto(
            response = SteamBibliotecaWrapperDto(
                games = listOf(juegoDto),
                game_count = 1
            )
        )
        coEvery { steamRemote.fetchBiblioteca("steamid") } returns Result.success(responseDto)
        coEvery { gameBroker.enrichBibliotecaGeneros(any()) } answers { firstArg() }

        val result = repo.getBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(1, list.size)
        assertEquals("Dota 2", list[0].nombre)
    }

    @Test
    fun `getBiblioteca returns empty list`() = runBlocking {
        val responseDto = SteamBibliotecaResponseDto(
            response = SteamBibliotecaWrapperDto(
                games = null,
                game_count = 0
            )
        )
        coEvery { steamRemote.fetchBiblioteca("steamid") } returns Result.success(responseDto)
        coEvery { gameBroker.enrichBibliotecaGeneros(any()) } answers { firstArg() }

        val result = repo.getBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertTrue(list.isEmpty())
    }

    @Test
    fun `getBiblioteca failure`() = runBlocking {
        coEvery { steamRemote.fetchBiblioteca("bad") } returns Result.failure(IllegalStateException("no"))
        coEvery { gameBroker.enrichBibliotecaGeneros(any()) } answers { firstArg() }

        val result = repo.getBiblioteca("bad")
        assertTrue(result.isFailure)
    }
}

