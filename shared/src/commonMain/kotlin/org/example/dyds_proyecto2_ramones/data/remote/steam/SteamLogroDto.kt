package org.example.dyds_proyecto2_ramones.data.remote.steam

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SteamLogrosResponseDto(
    @SerialName("playerstats")
    val playerstats: SteamPlayerStatsDto
)

@Serializable
data class SteamPlayerStatsDto(
    @SerialName("achievements")
    val achievements: List<SteamLogroDto>?
)

@Serializable
data class SteamLogroDto(
    @SerialName("apiname")
    val apiname: String,
    @SerialName("achieved")
    val achieved: Int,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String
)




