package org.example.dyds_proyecto2_ramones.data.remote.steam

import com.google.gson.annotations.SerializedName

data class SteamPerfilResponseDto(
    @SerializedName("response")
    val response: SteamPlayerWrapperDto
)

data class SteamPlayerWrapperDto(
    @SerializedName("players")
    val players: List<SteamPlayerSummaryDto>
)

data class SteamPlayerSummaryDto(
    @SerializedName("steamid")
    val steamid: String,
    @SerializedName("personaname")
    val personaname: String,
    @SerializedName("avatarfull")
    val avatarfull: String
)



