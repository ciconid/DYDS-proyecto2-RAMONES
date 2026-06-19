package org.example.dyds_proyecto2_ramones.data.repository

import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.repository.BibliotecaRepository
import kotlin.coroutines.CoroutineContext

class BibliotecaRepositoryImpl(
    private val steamRemote: SteamRemoteDataSource,
    private val ioDispatcher: CoroutineContext
) : BibliotecaRepository {

    override suspend fun getBiblioteca(steamId: String): Result<List<Juego>> =
        withContext(ioDispatcher) {
            runCatching {
                steamRemote.fetchBiblioteca(steamId).getOrThrow()
            }
        }
}

