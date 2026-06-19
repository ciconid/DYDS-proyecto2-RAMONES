package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.domain.repository.DetalleRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetDetalleUseCaseTest {

    private val detalleRepository: DetalleRepository = mockk()
    private val getDetalleUseCase = GetDetalleUseCase(detalleRepository)

    @Test
    fun `happy path devuelve detalle completo`() = runBlocking {
        val steamId = "76561198000000000"
        val appId = "570"
        val detalle = DetalleJuego(
            juego = Juego(appId, "Dota 2", 120.0, "", listOf("MOBA")),
            descripcion = "A complex strategy game",
            metacriticScore = 89,
            screenshots = listOf("https://img1.jpg", "https://img2.jpg"),
            logros = listOf(
                Logro("First Blood", "Get first kill", true, ""),
                Logro("Rampage", "Get 5 kills", false, ""),
            ),
        )
        coEvery { detalleRepository.getDetalle(steamId, appId) } returns Result.success(detalle)

        val result = getDetalleUseCase(steamId, appId)

        assertTrue(result.isSuccess)
        assertEquals(detalle, result.getOrNull())
        coVerify(exactly = 1) { detalleRepository.getDetalle(steamId, appId) }
    }

    @Test
    fun `detalle sin datos de RAWG tiene metacritic nulo`() = runBlocking {
        val steamId = "76561198000000000"
        val appId = "570"
        val detalle = DetalleJuego(
            juego = Juego(appId, "Dota 2", 120.0, "", listOf("MOBA")),
            descripcion = "",
            metacriticScore = null,
            screenshots = emptyList(),
            logros = emptyList(),
        )
        coEvery { detalleRepository.getDetalle(steamId, appId) } returns Result.success(detalle)

        val result = getDetalleUseCase(steamId, appId)

        assertTrue(result.isSuccess)
        assertEquals(detalle, result.getOrNull())
        assertEquals(null, result.getOrNull()?.metacriticScore)
    }

    @Test
    fun `error de repositorio se propaga`() = runBlocking {
        val steamId = "76561198000000000"
        val appId = "570"
        val expectedError = RuntimeException("API Error")
        coEvery { detalleRepository.getDetalle(steamId, appId) } returns Result.failure(expectedError)

        val result = getDetalleUseCase(steamId, appId)

        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
        coVerify(exactly = 1) { detalleRepository.getDetalle(steamId, appId) }
    }
}

