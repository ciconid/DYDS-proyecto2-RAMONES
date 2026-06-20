package org.example.dyds_proyecto2_ramones.presentation.biblioteca

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.usecase.FiltrarPorGeneroUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetBibliotecaUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.OrdenarPorHorasUseCase

data class BibliotecaUiState(
    val steamId: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val juegosVisibles: List<Juego> = emptyList(),
    val generosDisponibles: List<String> = listOf(TODOS_GENEROS),
    val selectedGenero: String = TODOS_GENEROS,
    val sortDesc: Boolean = true,
)

private const val TODOS_GENEROS = "Todos"

class BibliotecaViewModel(
    private val getBibliotecaUseCase: GetBibliotecaUseCase,
    private val filtrarPorGeneroUseCase: FiltrarPorGeneroUseCase,
    private val ordenarPorHorasUseCase: OrdenarPorHorasUseCase,
) {
    private val _uiState = MutableStateFlow(BibliotecaUiState())
    val uiState: StateFlow<BibliotecaUiState> = _uiState.asStateFlow()

    private var juegosOriginales: List<Juego> = emptyList()
    private var lastSteamId: String? = null

    suspend fun cargarBiblioteca(steamId: String) {
        val cleanSteamId = steamId.trim()
        if (cleanSteamId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                steamId = "",
                isLoading = false,
                errorMessage = "Ingresa un SteamID para cargar la biblioteca",
                juegosVisibles = emptyList(),
                generosDisponibles = listOf(TODOS_GENEROS),
                selectedGenero = TODOS_GENEROS,
            )
            return
        }

        lastSteamId = cleanSteamId
        _uiState.value = _uiState.value.copy(
            steamId = cleanSteamId,
            isLoading = true,
            errorMessage = null,
            juegosVisibles = emptyList(),
            generosDisponibles = listOf(TODOS_GENEROS),
            selectedGenero = TODOS_GENEROS,
            sortDesc = true,
        )

        val result = getBibliotecaUseCase(cleanSteamId)
        result.fold(
            onSuccess = { juegos ->
                juegosOriginales = juegos
                val generos = listOf(TODOS_GENEROS) + juegos
                    .flatMap { it.generos }
                    .filter { it.isNotBlank() }
                    .distinctBy { it.lowercase() }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    generosDisponibles = generos,
                    selectedGenero = TODOS_GENEROS,
                    sortDesc = true,
                )
                actualizarListaVisible()
            },
            onFailure = { error ->
                juegosOriginales = emptyList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo cargar la biblioteca",
                    juegosVisibles = emptyList(),
                    generosDisponibles = listOf(TODOS_GENEROS),
                    selectedGenero = TODOS_GENEROS,
                    sortDesc = true,
                )
            },
        )
    }

    fun seleccionarGenero(genero: String) {
        val targetGenero = if (genero.isBlank()) TODOS_GENEROS else genero
        _uiState.value = _uiState.value.copy(selectedGenero = targetGenero)
        actualizarListaVisible()
    }

    fun toggleOrden() {
        _uiState.value = _uiState.value.copy(sortDesc = !_uiState.value.sortDesc)
        actualizarListaVisible()
    }

    suspend fun reintentar() {
        val steamId = lastSteamId ?: return
        cargarBiblioteca(steamId)
    }

    private fun actualizarListaVisible() {
        val state = _uiState.value
        val filtrados = if (state.selectedGenero == TODOS_GENEROS) {
            juegosOriginales
        } else {
            filtrarPorGeneroUseCase(juegosOriginales, state.selectedGenero)
        }

        val ordenados = ordenarPorHorasUseCase(filtrados)
        val visibles = if (state.sortDesc) ordenados else ordenados.reversed()

        _uiState.value = state.copy(
            juegosVisibles = visibles,
            errorMessage = null,
        )
    }
}

