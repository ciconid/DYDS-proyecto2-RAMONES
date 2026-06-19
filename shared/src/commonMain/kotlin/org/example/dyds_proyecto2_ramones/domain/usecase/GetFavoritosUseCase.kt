package org.example.dyds_proyecto2_ramones.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository

class GetFavoritosUseCase(
    private val favoritosRepository: FavoritosRepository,
) {
    operator fun invoke(): Flow<List<Juego>> {
        return favoritosRepository.getFavoritos()
    }
}

