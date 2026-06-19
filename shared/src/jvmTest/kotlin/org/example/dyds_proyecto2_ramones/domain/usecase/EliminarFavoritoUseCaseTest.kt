package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import kotlin.test.Test

class EliminarFavoritoUseCaseTest {

    private val favoritosRepository: FavoritosRepository = mockk()
    private val eliminarFavoritoUseCase = EliminarFavoritoUseCase(favoritosRepository)

    @Test
    fun `eliminar favorito existente llama al repositorio`() = runBlocking {
        val appId = "570"
        coEvery { favoritosRepository.eliminar(appId) } returns Unit

        eliminarFavoritoUseCase(appId)

        coVerify(exactly = 1) { favoritosRepository.eliminar(appId) }
    }

    @Test
    fun `eliminar appId inexistente tambien llama al repositorio`() = runBlocking {
        val appId = "999999"
        coEvery { favoritosRepository.eliminar(appId) } returns Unit

        eliminarFavoritoUseCase(appId)

        coVerify(exactly = 1) { favoritosRepository.eliminar(appId) }
    }
}

