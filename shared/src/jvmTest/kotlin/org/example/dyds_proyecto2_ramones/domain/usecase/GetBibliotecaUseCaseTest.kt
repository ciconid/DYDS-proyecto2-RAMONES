package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.BibliotecaRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetBibliotecaUseCaseTest {

    private val bibliotecaRepository: BibliotecaRepository = mockk()
    private val getBibliotecaUseCase = GetBibliotecaUseCase(bibliotecaRepository)

    @Test
    fun `happy path devuelve biblioteca con juegos`() = runBlocking {
        val steamId = "76561198000000000"
        val juegos = listOf(
            Juego(
                appId = "570",
                nombre = "Dota 2",
                horasJugadas = 125.4,
                iconUrl = "https://cdn.test/dota2.png",
                generos = listOf("MOBA"),
            ),
        )
        coEvery { bibliotecaRepository.getBiblioteca(steamId) } returns Result.success(juegos)

        val result = getBibliotecaUseCase(steamId)

        assertTrue(result.isSuccess)
        assertEquals(juegos, result.getOrNull())
        coVerify(exactly = 1) { bibliotecaRepository.getBiblioteca(steamId) }
    }

    @Test
    fun `biblioteca vacia devuelve lista vacia`() = runBlocking {
        val steamId = "76561198000000000"
        coEvery { bibliotecaRepository.getBiblioteca(steamId) } returns Result.success(emptyList())

        val result = getBibliotecaUseCase(steamId)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
        coVerify(exactly = 1) { bibliotecaRepository.getBiblioteca(steamId) }
    }

    @Test
    fun `error de repositorio se propaga`() = runBlocking {
        val steamId = "76561198000000000"
        val expectedError = RuntimeException("Timeout")
        coEvery { bibliotecaRepository.getBiblioteca(steamId) } returns Result.failure(expectedError)

        val result = getBibliotecaUseCase(steamId)

        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
        coVerify(exactly = 1) { bibliotecaRepository.getBiblioteca(steamId) }
    }
}

