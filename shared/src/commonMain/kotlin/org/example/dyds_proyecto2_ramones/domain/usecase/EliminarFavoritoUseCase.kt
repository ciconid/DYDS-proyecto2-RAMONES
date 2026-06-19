package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository

class EliminarFavoritoUseCase(
    private val favoritosRepository: FavoritosRepository,
) {
    suspend operator fun invoke(appId: String) {
        favoritosRepository.eliminar(appId)
    }
}

