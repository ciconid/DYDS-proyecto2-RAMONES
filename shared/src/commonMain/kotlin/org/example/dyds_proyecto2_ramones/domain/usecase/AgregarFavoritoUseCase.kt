package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository

class AgregarFavoritoUseCase(
    private val favoritosRepository: FavoritosRepository,
) {
    suspend operator fun invoke(detalle: DetalleJuego) {
        favoritosRepository.agregar(detalle)
    }
}
