package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.domain.model.Perfil
import org.example.dyds_proyecto2_ramones.domain.repository.PerfilRepository

class GetPerfilUseCase(
    private val perfilRepository: PerfilRepository,
) {
    suspend operator fun invoke(steamId: String): Result<Perfil> {
        if (steamId.isBlank()) {
            return Result.failure(IllegalArgumentException("SteamID no puede estar vacio"))
        }

        return perfilRepository.getPerfil(steamId)
    }
}

