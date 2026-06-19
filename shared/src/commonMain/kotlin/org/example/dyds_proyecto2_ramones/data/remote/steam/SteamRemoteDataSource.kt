package org.example.dyds_proyecto2_ramones.data.remote.steam

import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.domain.model.Perfil

interface SteamRemoteDataSource {
    suspend fun fetchPerfil(steamId: String): Result<Perfil>
    suspend fun fetchBiblioteca(steamId: String): Result<List<Juego>>
    suspend fun fetchLogros(steamId: String, appId: Long): Result<List<Logro>>
}

