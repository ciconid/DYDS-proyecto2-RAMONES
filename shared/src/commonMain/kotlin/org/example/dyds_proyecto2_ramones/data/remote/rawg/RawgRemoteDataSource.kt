package org.example.dyds_proyecto2_ramones.data.remote.rawg

interface RawgRemoteDataSource {
    suspend fun searchGamesByName(name: String): Result<List<RawgGamePreview>>
    suspend fun getGameDetail(id: Int): Result<RawgGameDetail>
    suspend fun getScreenshots(id: Int): Result<List<String>>
}

data class RawgGamePreview(
    val id: Int,
    val name: String,
    val backgroundImage: String?
)

data class RawgGameDetail(
    val id: Int,
    val name: String,
    val description: String,
    val metacritic: Int?,
    val genres: List<String>,
    val backgroundImage: String?
)

