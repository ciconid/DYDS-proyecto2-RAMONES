package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.BibliotecaRepository

class GetBibliotecaUseCase(
    private val bibliotecaRepository: BibliotecaRepository,
) {
    suspend operator fun invoke(steamId: String): Result<List<Juego>> {
        return bibliotecaRepository.getBiblioteca(steamId)
    }
}

