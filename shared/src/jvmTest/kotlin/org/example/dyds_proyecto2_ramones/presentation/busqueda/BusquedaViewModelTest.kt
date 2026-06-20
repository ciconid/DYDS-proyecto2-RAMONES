package org.example.dyds_proyecto2_ramones.presentation.busqueda

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import org.example.dyds_proyecto2_ramones.domain.repository.PerfilRepository
import org.example.dyds_proyecto2_ramones.domain.usecase.GetPerfilUseCase
import org.example.dyds_proyecto2_ramones.presentation.common.UiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BusquedaViewModelTest {

    private val perfilRepository: PerfilRepository = mockk()
    private val getPerfilUseCase = GetPerfilUseCase(perfilRepository)
    private val viewModel = BusquedaViewModel(getPerfilUseCase)

    @Test
    fun `happy path actualiza a success con perfil`() = runBlocking {
        val steamId = "76561198000000000"
        val perfil = Perfil(
            steamId = steamId,
            nombre = "Usuario Test",
            avatarUrl = "https://avatar.test/1.png",
        )
        coEvery { perfilRepository.getPerfil(steamId) } returns Result.success(perfil)

        viewModel.buscarPerfil(steamId)

        val state = viewModel.uiState.value
        assertIs<UiState.Success<Perfil>>(state)
        assertEquals(perfil, state.data)
        coVerify(exactly = 1) { perfilRepository.getPerfil(steamId) }
    }

    @Test
    fun `steamid vacio devuelve error y no llama al repositorio`() = runBlocking {
        viewModel.buscarPerfil("   ")

        val state = viewModel.uiState.value
        assertIs<UiState.Error>(state)
        assertEquals("Ingresa un SteamID para continuar", state.message)
        coVerify(exactly = 0) { perfilRepository.getPerfil(any()) }
    }

    @Test
    fun `error de red actualiza a estado error`() = runBlocking {
        val steamId = "76561198000000000"
        coEvery { perfilRepository.getPerfil(steamId) } returns Result.failure(RuntimeException("Error de red"))

        viewModel.buscarPerfil(steamId)

        val state = viewModel.uiState.value
        assertIs<UiState.Error>(state)
        assertEquals("Error de red", state.message)
        coVerify(exactly = 1) { perfilRepository.getPerfil(steamId) }
    }
}

