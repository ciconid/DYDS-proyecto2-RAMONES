package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import kotlin.test.Test

class AgregarFavoritoUseCaseTest {

    private val favoritosRepository: FavoritosRepository = mockk()
    private val agregarFavoritoUseCase = AgregarFavoritoUseCase(favoritosRepository)

    @Test
    fun `agregar juego nuevo llama al repositorio`() = runBlocking {
        val detalle = detalle()
        coEvery { favoritosRepository.agregar(detalle) } returns Unit

        agregarFavoritoUseCase(detalle)

        coVerify(exactly = 1) { favoritosRepository.agregar(detalle) }
    }

    @Test
    fun `agregar juego duplicado tambien llama al repositorio`() = runBlocking {
        val detalle = detalle()
        coEvery { favoritosRepository.agregar(detalle) } returns Unit

        agregarFavoritoUseCase(detalle)
        agregarFavoritoUseCase(detalle)

        coVerify(exactly = 2) { favoritosRepository.agregar(detalle) }
    }

    private fun detalle() = DetalleJuego(
        juego = Juego("570", "Dota 2", 120.0, "", listOf("MOBA")),
        descripcion = "Descripcion local",
        metacriticScore = null,
        screenshots = emptyList(),
        logros = emptyList(),
    )
}
