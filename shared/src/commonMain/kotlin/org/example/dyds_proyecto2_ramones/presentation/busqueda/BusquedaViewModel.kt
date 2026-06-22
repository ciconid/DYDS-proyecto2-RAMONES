package org.example.dyds_proyecto2_ramones.presentation.busqueda

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import org.example.dyds_proyecto2_ramones.domain.usecase.GetPerfilUseCase
import org.example.dyds_proyecto2_ramones.presentation.common.UiState

class BusquedaViewModel(
    private val getPerfilUseCase: GetPerfilUseCase,
) {
    private val _uiState = MutableStateFlow<UiState<Perfil>>(UiState.Idle)
    val uiState: StateFlow<UiState<Perfil>> = _uiState.asStateFlow()

    private var lastSteamId: String? = null

    suspend fun buscarPerfil(steamIdInput: String) {
        val steamId = steamIdInput.trim()
        if (steamId.isBlank()) {
            _uiState.value = UiState.Error("Ingresa un SteamID para continuar")
            return
        }

        lastSteamId = steamId
        _uiState.value = UiState.Loading

        val result = getPerfilUseCase(steamId)
        _uiState.value = result.fold(
            onSuccess = { perfil -> UiState.Success(perfil) },
            onFailure = { error -> UiState.Error(error.message ?: "No se pudo obtener el perfil") },
        )
    }

    suspend fun reintentar() {
        val steamId = lastSteamId ?: return
        buscarPerfil(steamId)
    }

    @Suppress("unused")
    fun limpiarResultado() {
        _uiState.value = UiState.Idle
    }
}