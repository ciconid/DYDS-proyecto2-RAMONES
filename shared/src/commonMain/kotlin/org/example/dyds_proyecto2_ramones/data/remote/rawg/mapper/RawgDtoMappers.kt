package org.example.dyds_proyecto2_ramones.data.remote.rawg.mapper

import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgDetalleDto

internal data class RawgDatosExtra(
    val descripcion: String,
    val metacriticScore: Int?,
    val generos: List<String>
)

internal fun RawgDetalleDto.toDatosExtra(): RawgDatosExtra {
    return RawgDatosExtra(
        descripcion = this.description_raw,
        metacriticScore = this.metacritic,
        generos = this.genres.map { it.name }.filter { it.isNotBlank() }.distinctBy { it.lowercase() }
    )
}