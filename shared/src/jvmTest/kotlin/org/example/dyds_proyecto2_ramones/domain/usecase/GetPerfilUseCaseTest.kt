package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import org.example.dyds_proyecto2_ramones.domain.repository.PerfilRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPerfilUseCaseTest {

    private val perfilRepository: PerfilRepository = mockk()
    private val getPerfilUseCase = GetPerfilUseCase(perfilRepository)

    @Test
    fun `happy path devuelve perfil encontrado`() = runBlocking {
        val steamId = "76561198000000000"
        val perfil = Perfil(
            steamId = steamId,
            nombre = "Usuario Test",
            avatarUrl = "https://avatar.test/1.png",
        )
        coEvery { perfilRepository.getPerfil(steamId) } returns Result.success(perfil)

        val result = getPerfilUseCase(steamId)

        assertTrue(result.isSuccess)
        assertEquals(perfil, result.getOrNull())
        coVerify(exactly = 1) { perfilRepository.getPerfil(steamId) }
    }

    @Test
    fun `steamid invalido devuelve error y no llama al repositorio`() = runBlocking {
        val result = getPerfilUseCase("   ")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        coVerify(exactly = 0) { perfilRepository.getPerfil(any()) }
    }

    @Test
    fun `error de repositorio se propaga`() = runBlocking {
        val steamId = "76561198000000000"
        val expectedError = RuntimeException("Error de red")
        coEvery { perfilRepository.getPerfil(steamId) } returns Result.failure(expectedError)

        val result = getPerfilUseCase(steamId)

        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
        coVerify(exactly = 1) { perfilRepository.getPerfil(steamId) }
    }
}


