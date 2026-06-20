package org.example.dyds_proyecto2_ramones.data.remote.steam

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SteamPerfilResponseDto(
    @SerialName("response")
    val response: SteamPlayerWrapperDto
)

@Serializable
data class SteamPlayerWrapperDto(
    @SerialName("players")
    val players: List<SteamPlayerSummaryDto>
)

@Serializable
data class SteamPlayerSummaryDto(
    @SerialName("steamid")
    val steamid: String,
    @SerialName("personaname")
    val personaname: String,
    @SerialName("avatarfull")
    val avatarfull: String
)




