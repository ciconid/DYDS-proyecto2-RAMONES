package org.example.dyds_proyecto2_ramones.domain.repository

import org.example.dyds_proyecto2_ramones.domain.model.Juego

interface BibliotecaRepository {
    suspend fun getBiblioteca(steamId: String): Result<List<Juego>>
}

