package org.example.dyds_proyecto2_ramones.presentation.biblioteca

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.BibliotecaRepository
import org.example.dyds_proyecto2_ramones.domain.usecase.FiltrarPorGeneroUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetBibliotecaUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.OrdenarPorHorasUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BibliotecaViewModelTest {

    private val bibliotecaRepository: BibliotecaRepository = mockk()
    private val getBibliotecaUseCase = GetBibliotecaUseCase(bibliotecaRepository)
    private val filtrarPorGeneroUseCase = FiltrarPorGeneroUseCase()
    private val ordenarPorHorasUseCase = OrdenarPorHorasUseCase()

    private fun createViewModel() = BibliotecaViewModel(
        getBibliotecaUseCase = getBibliotecaUseCase,
        filtrarPorGeneroUseCase = filtrarPorGeneroUseCase,
        ordenarPorHorasUseCase = ordenarPorHorasUseCase,
    )

    @Test
    fun `lista completa carga y queda ordenada por horas descendente`() = runBlocking {
        val steamId = "76561198000000000"
        coEvery { bibliotecaRepository.getBiblioteca(steamId) } returns Result.success(sampleJuegos())
        val viewModel = createViewModel()

        viewModel.cargarBiblioteca(steamId)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(listOf("Counter-Strike 2", "Dota 2", "Portal 2"), state.juegosVisibles.map { it.nombre })
        assertTrue(state.generosDisponibles.contains("Shooter"))
        assertTrue(state.generosDisponibles.contains("Estrategia"))
        assertTrue(state.generosDisponibles.contains("Puzzle"))
    }

    @Test
    fun `filtro por genero deja solo juegos del genero seleccionado`() = runBlocking {
        val steamId = "76561198000000000"
        coEvery { bibliotecaRepository.getBiblioteca(steamId) } returns Result.success(sampleJuegos())
        val viewModel = createViewModel()

        viewModel.cargarBiblioteca(steamId)
        viewModel.seleccionarGenero("Shooter")

        val state = viewModel.uiState.value
        assertEquals("Shooter", state.selectedGenero)
        assertEquals(listOf("Counter-Strike 2"), state.juegosVisibles.map { it.nombre })
    }

    @Test
    fun `toggle de orden cambia a ascendente`() = runBlocking {
        val steamId = "76561198000000000"
        coEvery { bibliotecaRepository.getBiblioteca(steamId) } returns Result.success(sampleJuegos())
        val viewModel = createViewModel()

        viewModel.cargarBiblioteca(steamId)
        viewModel.toggleOrden()

        val state = viewModel.uiState.value
        assertFalse(state.sortDesc)
        assertEquals(listOf("Portal 2", "Dota 2", "Counter-Strike 2"), state.juegosVisibles.map { it.nombre })
    }

    @Test
    fun `biblioteca vacia muestra lista vacia sin error`() = runBlocking {
        val steamId = "76561198000000000"
        coEvery { bibliotecaRepository.getBiblioteca(steamId) } returns Result.success(emptyList())
        val viewModel = createViewModel()

        viewModel.cargarBiblioteca(steamId)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertTrue(state.juegosVisibles.isEmpty())
        assertEquals(listOf("Todos"), state.generosDisponibles)
    }

    private fun sampleJuegos(): List<Juego> = listOf(
        Juego(
            appId = "570",
            nombre = "Dota 2",
            horasJugadas = 124.5,
            iconUrl = "",
            generos = listOf("Estrategia"),
        ),
        Juego(
            appId = "730",
            nombre = "Counter-Strike 2",
            horasJugadas = 310.0,
            iconUrl = "",
            generos = listOf("Shooter", "Accion"),
        ),
        Juego(
            appId = "620",
            nombre = "Portal 2",
            horasJugadas = 57.3,
            iconUrl = "",
            generos = listOf("Puzzle"),
        ),
    )
}

