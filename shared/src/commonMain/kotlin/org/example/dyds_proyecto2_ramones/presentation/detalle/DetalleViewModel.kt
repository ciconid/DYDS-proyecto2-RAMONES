package org.example.dyds_proyecto2_ramones.presentation.detalle

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.usecase.AgregarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.EliminarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetDetalleUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetFavoritosUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.TranslateDescriptionUseCase
import org.example.dyds_proyecto2_ramones.presentation.common.UiState

class DetalleViewModel(
    private val getDetalleUseCase: GetDetalleUseCase,
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val agregarFavoritoUseCase: AgregarFavoritoUseCase,
    private val eliminarFavoritoUseCase: EliminarFavoritoUseCase,
    private val translateDescriptionUseCase: TranslateDescriptionUseCase,
) {
    private val _uiState = MutableStateFlow<UiState<DetalleJuego>>(UiState.Idle)
    val uiState: StateFlow<UiState<DetalleJuego>> = _uiState.asStateFlow()

    private val _esFavorito = MutableStateFlow(false)
    val esFavorito: StateFlow<Boolean> = _esFavorito.asStateFlow()

    private val _descripcionTraducida = MutableStateFlow<String?>(null)
    val descripcionTraducida: StateFlow<String?> = _descripcionTraducida.asStateFlow()

    private var lastSteamId: String? = null
    private var lastAppId: String? = null
    private var detalleActual: DetalleJuego? = null

    suspend fun cargarDetalle(steamIdInput: String, appIdInput: String) {
        val steamId = steamIdInput.trim()
        val appId = appIdInput.trim()

        if (appId.isBlank()) {
            _uiState.value = UiState.Error("No se encontro appId para cargar el detalle")
            return
        }

        if (appId == lastAppId && detalleActual != null) return

        lastSteamId = steamId
        lastAppId = appId
        _uiState.value = UiState.Loading
        _descripcionTraducida.value = null

        val result = getDetalleUseCase(steamId, appId)

        result.onSuccess { detalle ->
            detalleActual = detalle
            actualizarFavorito(appId)
            _uiState.value = UiState.Success(detalle)
            traducirDescripcionActual()
        }.onFailure { error ->
            detalleActual = null
            _esFavorito.value = false
            _descripcionTraducida.value = null
            _uiState.value = UiState.Error(error.message ?: "No se pudo cargar el detalle")
        }
    }

    suspend fun toggleFavorito() {
        val detalle = detalleActual ?: return
        runCatching {
            if (_esFavorito.value) {
                eliminarFavoritoUseCase(detalle.juego.appId)
            } else {
                agregarFavoritoUseCase(detalle)
            }
        }.onSuccess {
            actualizarFavorito(detalle.juego.appId)
        }.onFailure { error ->
            _uiState.value = UiState.Error(error.message ?: "No se pudo actualizar favoritos")
        }
    }

    suspend fun reintentar() {
        val steamId = lastSteamId ?: return
        val appId = lastAppId ?: return
        cargarDetalle(steamId, appId)
    }

    suspend fun traducirDescripcionActual() {
        val detalle = detalleActual ?: return
        val descripcion = detalle.descripcion
        if (descripcion.isBlank()) return
        if (_descripcionTraducida.value != null) return

        translateDescriptionUseCase(descripcion)
            .onSuccess { traducida -> _descripcionTraducida.value = traducida }
            .onFailure { _descripcionTraducida.value = null }
    }

    private suspend fun actualizarFavorito(appId: String) {
        val favoritos = getFavoritosUseCase().first()
        _esFavorito.value = favoritos.any { it.appId == appId }
    }
}
