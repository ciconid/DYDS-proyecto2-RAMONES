package org.example.dyds_proyecto2_ramones.data.remote.rawg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawgSearchResponseDto(
    @SerialName("results")
    val results: List<RawgGameDto>
)

@Serializable
data class RawgGameDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("metacritic")
    val metacritic: Int?,
    @SerialName("background_image")
    val background_image: String?,
    @SerialName("genres")
    val genres: List<RawgGenreDto> = emptyList()
)

@Serializable
data class RawgGenreDto(
    @SerialName("name")
    val name: String
)

@Serializable
data class RawgDetalleDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("description_raw")
    val description_raw: String,
    @SerialName("metacritic")
    val metacritic: Int?,
    @SerialName("background_image")
    val background_image: String?,
    @SerialName("genres")
    val genres: List<RawgGenreDto> = emptyList(),
)

@Serializable
data class RawgScreenshotsResponseDto(
    @SerialName("results")
    val results: List<RawgScreenshotDto>
)

@Serializable
data class RawgScreenshotDto(
    @SerialName("image")
    val image: String
)


