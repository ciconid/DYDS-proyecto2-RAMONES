package org.example.dyds_proyecto2_ramones.data.remote.steam.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SteamBibliotecaResponseDto(
    @SerialName("response")
    val response: SteamBibliotecaWrapperDto
)

@Serializable
data class SteamBibliotecaWrapperDto(
    @SerialName("games")
    val games: List<SteamJuegoDto>? = emptyList(),
    @SerialName("game_count")
    val game_count: Int = 0
)

@Serializable
data class SteamJuegoDto(
    @SerialName("appid")
    val appid: Long,
    @SerialName("name")
    val name: String,
    @SerialName("playtime_forever")
    val playtime_forever: Long,
    @SerialName("img_icon_url")
    val img_icon_url: String
)




