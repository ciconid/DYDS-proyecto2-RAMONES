package org.example.dyds_proyecto2_ramones.domain.repository

import org.example.dyds_proyecto2_ramones.domain.model.Perfil

interface PerfilRepository {
    suspend fun getPerfil(steamId: String): Result<Perfil>
}

