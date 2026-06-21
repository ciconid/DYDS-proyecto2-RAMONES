package org.example.dyds_proyecto2_ramones.data.remote.rawg

import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgDetalleDto
import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgScreenshotsResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgSearchResponseDto

interface RawgRemoteDataSource {
    suspend fun searchGamesByName(name: String): Result<RawgSearchResponseDto>
    suspend fun getGameDetail(id: Int): Result<RawgDetalleDto>
    suspend fun getScreenshots(id: Int): Result<RawgScreenshotsResponseDto>
}

