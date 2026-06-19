package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetFavoritosUseCaseTest {

    private val favoritosRepository: FavoritosRepository = mockk()
    private val getFavoritosUseCase = GetFavoritosUseCase(favoritosRepository)

    @Test
    fun `lista con items devuelve flow de favoritos`() = runBlocking {
        val favoritos = listOf(
            Juego("570", "Dota 2", 120.0, "", listOf("MOBA")),
            Juego("730", "CS2", 80.0, "", listOf("Shooter")),
        )
        every { favoritosRepository.getFavoritos() } returns flowOf(favoritos)

        val flow = getFavoritosUseCase()
        val resultado = flow.toList()

        assertEquals(1, resultado.size)
        assertEquals(favoritos, resultado.first())
    }

    @Test
    fun `lista vacia devuelve flow vacio`() = runBlocking {
        every { favoritosRepository.getFavoritos() } returns flowOf(emptyList())

        val flow = getFavoritosUseCase()
        val resultado = flow.toList()

        assertEquals(1, resultado.size)
        assertTrue(resultado.first().isEmpty())
    }
}

