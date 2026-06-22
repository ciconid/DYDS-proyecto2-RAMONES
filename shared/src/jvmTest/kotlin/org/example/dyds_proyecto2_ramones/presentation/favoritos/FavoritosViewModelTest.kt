package org.example.dyds_proyecto2_ramones.presentation.favoritos

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import org.example.dyds_proyecto2_ramones.domain.usecase.EliminarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetFavoritosUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FavoritosViewModelTest {

    private val favoritosRepository: FavoritosRepository = mockk()
    private val getFavoritosUseCase = GetFavoritosUseCase(favoritosRepository)
    private val eliminarFavoritoUseCase = EliminarFavoritoUseCase(favoritosRepository)

    private fun createViewModel() = FavoritosViewModel(
        getFavoritosUseCase = getFavoritosUseCase,
        eliminarFavoritoUseCase = eliminarFavoritoUseCase,
    )

    @Test
    fun `lista con items se refleja en uiState`() = runBlocking {
        val favoritos = listOf(
            Juego("730", "Counter-Strike 2", 120.0, "", listOf("Shooter")),
            Juego("570", "Dota 2", 220.0, "", listOf("MOBA")),
        )
        every { favoritosRepository.getFavoritos() } returns flowOf(favoritos)

        val viewModel = createViewModel()
        viewModel.observarFavoritos()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.juegos.size)
        assertEquals("Counter-Strike 2", state.juegos.first().nombre)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun `lista vacia se refleja en uiState`() = runBlocking {
        every { favoritosRepository.getFavoritos() } returns flowOf(emptyList())

        val viewModel = createViewModel()
        viewModel.observarFavoritos()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.juegos.isEmpty())
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun `eliminar favorito actualiza la lista`() = runBlocking {
        val favoritosFlow = MutableStateFlow(
            listOf(
                Juego("730", "Counter-Strike 2", 120.0, "", listOf("Shooter")),
                Juego("570", "Dota 2", 220.0, "", listOf("MOBA")),
            )
        )
        every { favoritosRepository.getFavoritos() } returns favoritosFlow
        coEvery { favoritosRepository.eliminar(any()) } answers {
            val appId = firstArg<String>()
            favoritosFlow.value = favoritosFlow.value.filterNot { it.appId == appId }
        }

        val viewModel = createViewModel()
        val collectJob = launch {
            viewModel.observarFavoritos()
        }

        yield()
        viewModel.eliminarFavorito("730")
        yield()

        val state = viewModel.uiState.value
        assertEquals(1, state.juegos.size)
        assertEquals("570", state.juegos.first().appId)
        coVerify(exactly = 1) { favoritosRepository.eliminar("730") }

        collectJob.cancel()
    }
}

