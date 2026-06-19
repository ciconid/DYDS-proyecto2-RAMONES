package org.example.dyds_proyecto2_ramones.data.remote.steam

import com.google.gson.annotations.SerializedName

data class SteamBibliotecaResponseDto(
    @SerializedName("response")
    val response: SteamBibliotecaWrapperDto
)

data class SteamBibliotecaWrapperDto(
    @SerializedName("games")
    val games: List<SteamJuegoDto>?,
    @SerializedName("game_count")
    val game_count: Int
)

data class SteamJuegoDto(
    @SerializedName("appid")
    val appid: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("playtime_forever")
    val playtime_forever: Long,
    @SerializedName("img_icon_url")
    val img_icon_url: String
)



