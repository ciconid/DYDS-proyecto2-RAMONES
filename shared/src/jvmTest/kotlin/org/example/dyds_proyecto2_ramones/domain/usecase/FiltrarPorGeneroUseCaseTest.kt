package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.Juego
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FiltrarPorGeneroUseCaseTest {

    private val filtrarPorGeneroUseCase = FiltrarPorGeneroUseCase()

    @Test
    fun `genero existente devuelve juegos filtrados`() {
        val juegos = listOf(
            Juego("570", "Dota 2", 120.0, "", listOf("MOBA", "Estrategia")),
            Juego("730", "CS2", 80.0, "", listOf("Shooter")),
            Juego("440", "TF2", 42.0, "", listOf("Shooter", "Accion")),
        )

        val resultado = filtrarPorGeneroUseCase(juegos, "moba")

        assertEquals(1, resultado.size)
        assertEquals("Dota 2", resultado.first().nombre)
    }

    @Test
    fun `genero sin resultados devuelve lista vacia`() {
        val juegos = listOf(
            Juego("570", "Dota 2", 120.0, "", listOf("MOBA")),
            Juego("730", "CS2", 80.0, "", listOf("Shooter")),
        )

        val resultado = filtrarPorGeneroUseCase(juegos, "Puzzle")

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun `lista vacia devuelve lista vacia`() {
        val resultado = filtrarPorGeneroUseCase(emptyList(), "MOBA")

        assertTrue(resultado.isEmpty())
    }
}

