package org.example.dyds_proyecto2_ramones.data.remote.rawg

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    @GET("/games")
    suspend fun searchGames(
        @Query("search") search: String,
        @Query("key") key: String? = null
    ): RawgSearchResponseDto

    @GET("/games/{id}")
    suspend fun getGameDetail(
        @Path("id") id: Int,
        @Query("key") key: String? = null
    ): RawgDetalleDto

    @GET("/games/{id}/screenshots")
    suspend fun getScreenshots(
        @Path("id") id: Int,
        @Query("key") key: String? = null
    ): RawgScreenshotsResponseDto
}

