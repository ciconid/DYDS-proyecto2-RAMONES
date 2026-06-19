package org.example.dyds_proyecto2_ramones.data.remote.steam

import retrofit2.http.GET
import retrofit2.http.Query

interface SteamApiService {

    @GET("ISteamUser/GetPlayerSummaries/v0002/")
    suspend fun getPlayerSummaries(
        @Query("key") key: String,
        @Query("steamids") steamids: String
    ): SteamPerfilResponseDto

    @GET("IPlayerService/GetOwnedGames/v0001/")
    suspend fun getOwnedGames(
        @Query("key") key: String,
        @Query("steamid") steamid: String,
        @Query("include_appinfo") includeAppInfo: Boolean
    ): SteamBibliotecaResponseDto

    @GET("ISteamUserStats/GetPlayerAchievements/v0001/")
    suspend fun getPlayerAchievements(
        @Query("key") key: String,
        @Query("steamid") steamid: String,
        @Query("appid") appid: Long,
        @Query("l") lang: String
    ): SteamLogrosResponseDto
}


