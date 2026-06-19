package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import kotlin.test.Test

class AgregarFavoritoUseCaseTest {

    private val favoritosRepository: FavoritosRepository = mockk()
    private val agregarFavoritoUseCase = AgregarFavoritoUseCase(favoritosRepository)

    @Test
    fun `agregar juego nuevo llama al repositorio`() = runBlocking {
        val juego = Juego("570", "Dota 2", 120.0, "", listOf("MOBA"))
        coEvery { favoritosRepository.agregar(juego) } returns Unit

        agregarFavoritoUseCase(juego)

        coVerify(exactly = 1) { favoritosRepository.agregar(juego) }
    }

    @Test
    fun `agregar juego duplicado tambien llama al repositorio`() = runBlocking {
        val juego = Juego("570", "Dota 2", 120.0, "", listOf("MOBA"))
        coEvery { favoritosRepository.agregar(juego) } returns Unit

        agregarFavoritoUseCase(juego)
        agregarFavoritoUseCase(juego)

        coVerify(exactly = 2) { favoritosRepository.agregar(juego) }
    }
}

