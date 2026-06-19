package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.repository.DetalleRepository

class GetDetalleUseCase(
    private val detalleRepository: DetalleRepository,
) {
    suspend operator fun invoke(steamId: String, appId: String): Result<DetalleJuego> {
        return detalleRepository.getDetalle(steamId, appId)
    }
}

