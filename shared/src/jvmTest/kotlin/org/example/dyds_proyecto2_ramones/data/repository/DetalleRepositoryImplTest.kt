package org.example.dyds_proyecto2_ramones.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamBibliotecaResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamBibliotecaWrapperDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamJuegoDto
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DetalleRepositoryImplTest {

    private val steamRemote: SteamRemoteDataSource = mockk()
    private val gameBroker: GameBroker = mockk()
    private val favoritosRepository: FavoritosRepository = mockk()
    private val repo = DetalleRepositoryImpl(steamRemote, gameBroker, favoritosRepository, Dispatchers.Unconfined)

    @Test
    fun `getDetalle con steamId vacio recupera de favoritos locales`() = runBlocking {
        val appId = "730"
        val detalleLocal = DetalleJuego(
            juego = Juego(appId, "Counter-Strike 2", 120.0, "", listOf("Shooter")),
            descripcion = "Favorito local",
            metacriticScore = null,
            screenshots = emptyList(),
            logros = emptyList(),
        )
        coEvery { favoritosRepository.getDetalleLocal(appId) } returns detalleLocal

        val result = repo.getDetalle("", appId)

        assertTrue(result.isSuccess)
        val detalle = result.getOrThrow()
        assertEquals("Counter-Strike 2", detalle.juego.nombre)
        assertEquals("Favorito local", detalle.descripcion)
    }

    @Test
    fun `getDetalle con steamId vacio y sin favorito local lanza error`() = runBlocking {
        val appId = "999"
        coEvery { favoritosRepository.getDetalleLocal(appId) } returns null

        val result = repo.getDetalle("", appId)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("No se encontro detalle local") == true)
    }

    @Test
    fun `getDetalle con steamId valido consulta steam y construye detalle`() = runBlocking {
        val steamId = "76561198000000000"
        val appId = "570"
        val juegoDto = SteamJuegoDto(570, "Dota 2", 720, "icon")
        val responseDto = SteamBibliotecaResponseDto(
            response = SteamBibliotecaWrapperDto(games = listOf(juegoDto), game_count = 1)
        )
        coEvery { steamRemote.fetchBiblioteca(steamId) } returns Result.success(responseDto)

        val detalleEsperado = DetalleJuego(
            juego = Juego(appId, "Dota 2", 12.0, "", listOf("Strategy")),
            descripcion = "Dota 2 description",
            metacriticScore = 72,
            screenshots = listOf("ss1.jpg"),
            logros = emptyList(),
        )
        coEvery { gameBroker.buildDetalle(steamId, any()) } returns Result.success(detalleEsperado)

        val result = repo.getDetalle(steamId, appId)

        assertTrue(result.isSuccess)
        val detalle = result.getOrThrow()
        assertEquals("Dota 2", detalle.juego.nombre)
        assertEquals(72, detalle.metacriticScore)
    }

    @Test
    fun `getDetalle con steamId valido pero juego no en biblioteca lanza error`() = runBlocking {
        val steamId = "76561198000000000"
        val appId = "999"
        val juegoDto = SteamJuegoDto(570, "Dota 2", 720, "icon")
        val responseDto = SteamBibliotecaResponseDto(
            response = SteamBibliotecaWrapperDto(games = listOf(juegoDto), game_count = 1)
        )
        coEvery { steamRemote.fetchBiblioteca(steamId) } returns Result.success(responseDto)

        val result = repo.getDetalle(steamId, appId)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("No se encontro el juego con appId $appId") == true)
    }

    @Test
    fun `getDetalle propaga error de steam`() = runBlocking {
        val steamId = "76561198000000000"
        val appId = "570"
        val error = IllegalStateException("Steam API error")
        coEvery { steamRemote.fetchBiblioteca(steamId) } returns Result.failure(error)

        val result = repo.getDetalle(steamId, appId)

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `getDetalle propaga error de gameBroker`() = runBlocking {
        val steamId = "76561198000000000"
        val appId = "570"
        val juegoDto = SteamJuegoDto(570, "Dota 2", 720, "icon")
        val responseDto = SteamBibliotecaResponseDto(
            response = SteamBibliotecaWrapperDto(games = listOf(juegoDto), game_count = 1)
        )
        coEvery { steamRemote.fetchBiblioteca(steamId) } returns Result.success(responseDto)

        val brokerError = RuntimeException("RAWG API unavailable")
        coEvery { gameBroker.buildDetalle(steamId, any()) } returns Result.failure(brokerError)

        val result = repo.getDetalle(steamId, appId)

        assertTrue(result.isFailure)
        assertEquals(brokerError, result.exceptionOrNull())
    }

    @Test
    fun `getDetalle con steamId en blanco (espacios) recupera de favoritos locales`() = runBlocking {
        val appId = "730"
        val detalleLocal = DetalleJuego(
            juego = Juego(appId, "Counter-Strike 2", 100.0, "", listOf("Shooter")),
            descripcion = "Local",
            metacriticScore = 80,
            screenshots = emptyList(),
            logros = emptyList(),
        )
        coEvery { favoritosRepository.getDetalleLocal(appId) } returns detalleLocal

        val result = repo.getDetalle("   ", appId)

        assertTrue(result.isSuccess)
        val detalle = result.getOrThrow()
        assertEquals(80, detalle.metacriticScore)
    }
}

