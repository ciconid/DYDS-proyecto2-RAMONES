package org.example.dyds_proyecto2_ramones.data.remote.steam

import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamBibliotecaResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamLogrosResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.dto.SteamPerfilResponseDto

interface SteamRemoteDataSource {
    suspend fun fetchPerfil(steamId: String): Result<SteamPerfilResponseDto>
    suspend fun fetchBiblioteca(steamId: String): Result<SteamBibliotecaResponseDto>
    suspend fun fetchLogros(steamId: String, appId: Long): Result<SteamLogrosResponseDto>
}