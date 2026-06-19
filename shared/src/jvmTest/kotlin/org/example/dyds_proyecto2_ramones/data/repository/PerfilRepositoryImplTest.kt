package org.example.dyds_proyecto2_ramones.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PerfilRepositoryImplTest {

    private val steamRemote: SteamRemoteDataSource = mockk()
    private val repo = PerfilRepositoryImpl(steamRemote, Dispatchers.Unconfined)

    @Test
    fun `getPerfil success`() = runBlocking {
        val perfil = Perfil("7656119","Player","http://avatar")
        coEvery { steamRemote.fetchPerfil("7656119") } returns Result.success(perfil)

        val result = repo.getPerfil("7656119")
        assertTrue(result.isSuccess)
        val p = result.getOrThrow()
        assertEquals("Player", p.nombre)
    }

    @Test
    fun `getPerfil failure`() = runBlocking {
        coEvery { steamRemote.fetchPerfil("bad") } returns Result.failure(IllegalStateException("no"))

        val result = repo.getPerfil("bad")
        assertTrue(result.isFailure)
    }
}

