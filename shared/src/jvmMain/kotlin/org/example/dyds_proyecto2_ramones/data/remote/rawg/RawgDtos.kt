package org.example.dyds_proyecto2_ramones.data.remote.rawg

import com.google.gson.annotations.SerializedName

data class RawgSearchResponseDto(
    @SerializedName("results")
    val results: List<RawgGameDto>
)

data class RawgGameDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("metacritic")
    val metacritic: Int?,
    @SerializedName("background_image")
    val background_image: String?,
    @SerializedName("genres")
    val genres: List<RawgGenreDto> = emptyList()
)

data class RawgGenreDto(
    @SerializedName("name")
    val name: String
)

data class RawgDetalleDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description_raw")
    val description_raw: String,
    @SerializedName("metacritic")
    val metacritic: Int?,
    @SerializedName("background_image")
    val background_image: String?,
    @SerializedName("genres")
    val genres: List<RawgGenreDto> = emptyList(),
)

data class RawgScreenshotsResponseDto(
    @SerializedName("results")
    val results: List<RawgScreenshotDto>
)

data class RawgScreenshotDto(
    @SerializedName("image")
    val image: String
)

