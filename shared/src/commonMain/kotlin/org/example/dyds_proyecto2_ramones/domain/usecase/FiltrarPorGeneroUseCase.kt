package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.Juego

class FiltrarPorGeneroUseCase {
    operator fun invoke(juegos: List<Juego>, genero: String): List<Juego> {
        return juegos.filter { juego ->
            juego.generos.any { it.equals(genero, ignoreCase = true) }
        }
    }
}

