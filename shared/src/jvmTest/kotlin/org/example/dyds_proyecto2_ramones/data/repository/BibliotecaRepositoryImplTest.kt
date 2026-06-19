package org.example.dyds_proyecto2_ramones.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BibliotecaRepositoryImplTest {

    private val steamRemote: SteamRemoteDataSource = mockk()
    private val repo = BibliotecaRepositoryImpl(steamRemote, Dispatchers.Unconfined)

    @Test
    fun `getBiblioteca returns list`() = runBlocking {
        val juego = Juego("570","Dota 2",12.0,"icon", listOf("Action"))
        coEvery { steamRemote.fetchBiblioteca("steamid") } returns Result.success(listOf(juego))

        val result = repo.getBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(1, list.size)
        assertEquals("Dota 2", list[0].nombre)
    }

    @Test
    fun `getBiblioteca returns empty list`() = runBlocking {
        coEvery { steamRemote.fetchBiblioteca("steamid") } returns Result.success(emptyList())

        val result = repo.getBiblioteca("steamid")
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertTrue(list.isEmpty())
    }

    @Test
    fun `getBiblioteca failure`() = runBlocking {
        coEvery { steamRemote.fetchBiblioteca("bad") } returns Result.failure(IllegalStateException("no"))

        val result = repo.getBiblioteca("bad")
        assertTrue(result.isFailure)
    }
}

