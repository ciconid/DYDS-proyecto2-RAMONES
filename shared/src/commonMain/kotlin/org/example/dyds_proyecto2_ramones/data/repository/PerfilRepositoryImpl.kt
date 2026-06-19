package org.example.dyds_proyecto2_ramones.data.repository

import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import org.example.dyds_proyecto2_ramones.domain.repository.PerfilRepository
import kotlin.coroutines.CoroutineContext

class PerfilRepositoryImpl(
    private val steamRemote: SteamRemoteDataSource,
    private val ioDispatcher: CoroutineContext
) : PerfilRepository {

    override suspend fun getPerfil(steamId: String): Result<Perfil> =
        withContext(ioDispatcher) {
            runCatching {
                steamRemote.fetchPerfil(steamId).getOrThrow()
            }
        }
}

