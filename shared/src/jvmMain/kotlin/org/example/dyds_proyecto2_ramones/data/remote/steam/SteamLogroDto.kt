package org.example.dyds_proyecto2_ramones.data.remote.steam

import com.google.gson.annotations.SerializedName

data class SteamLogrosResponseDto(
    @SerializedName("playerstats")
    val playerstats: SteamPlayerStatsDto
)

data class SteamPlayerStatsDto(
    @SerializedName("achievements")
    val achievements: List<SteamLogroDto>?
)

data class SteamLogroDto(
    @SerializedName("apiname")
    val apiname: String,
    @SerializedName("achieved")
    val achieved: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String
)



