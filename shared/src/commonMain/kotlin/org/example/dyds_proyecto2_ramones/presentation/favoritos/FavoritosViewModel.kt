package org.example.dyds_proyecto2_ramones.presentation.favoritos

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.usecase.EliminarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetFavoritosUseCase

data class FavoritosUiState(
    val isLoading: Boolean = true,
    val juegos: List<Juego> = emptyList(),
    val errorMessage: String? = null,
)

class FavoritosViewModel(
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val eliminarFavoritoUseCase: EliminarFavoritoUseCase,
) {
    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()

    suspend fun observarFavoritos() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        runCatching {
            getFavoritosUseCase().collect { favoritos ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    juegos = favoritos,
                    errorMessage = null,
                )
            }
        }.onFailure { error ->
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = error.message ?: "No se pudieron cargar los favoritos",
            )
        }
    }

    suspend fun eliminarFavorito(appId: String) {
        runCatching {
            eliminarFavoritoUseCase(appId)
        }.onFailure { error ->
            _uiState.value = _uiState.value.copy(
                errorMessage = error.message ?: "No se pudo eliminar el favorito",
            )
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

