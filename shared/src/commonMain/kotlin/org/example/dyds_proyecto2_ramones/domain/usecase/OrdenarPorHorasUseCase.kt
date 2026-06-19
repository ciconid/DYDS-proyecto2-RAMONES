package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.Juego

class OrdenarPorHorasUseCase {
    operator fun invoke(juegos: List<Juego>): List<Juego> {
        return juegos.sortedByDescending { it.horasJugadas }
    }
}

