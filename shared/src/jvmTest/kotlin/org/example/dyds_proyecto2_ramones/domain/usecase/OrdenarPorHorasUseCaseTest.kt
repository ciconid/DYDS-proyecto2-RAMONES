package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.Juego
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrdenarPorHorasUseCaseTest {

    private val ordenarPorHorasUseCase = OrdenarPorHorasUseCase()

    @Test
    fun `orden descendente por horas`() {
        val juegos = listOf(
            Juego("730", "CS2", 80.0, "", listOf("Shooter")),
            Juego("570", "Dota 2", 120.0, "", listOf("MOBA")),
            Juego("440", "TF2", 42.0, "", listOf("Shooter")),
        )

        val resultado = ordenarPorHorasUseCase(juegos)

        assertEquals(listOf("Dota 2", "CS2", "TF2"), resultado.map { it.nombre })
    }

    @Test
    fun `juegos con mismas horas mantienen sus elementos`() {
        val juegos = listOf(
            Juego("570", "Dota 2", 100.0, "", listOf("MOBA")),
            Juego("730", "CS2", 100.0, "", listOf("Shooter")),
            Juego("440", "TF2", 50.0, "", listOf("Shooter")),
        )

        val resultado = ordenarPorHorasUseCase(juegos)

        assertEquals(3, resultado.size)
        assertEquals(100.0, resultado[0].horasJugadas)
        assertEquals(100.0, resultado[1].horasJugadas)
        assertEquals(50.0, resultado[2].horasJugadas)
    }

    @Test
    fun `lista vacia devuelve lista vacia`() {
        val resultado = ordenarPorHorasUseCase(emptyList())

        assertTrue(resultado.isEmpty())
    }
}

