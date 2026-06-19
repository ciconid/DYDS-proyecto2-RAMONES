package org.example.dyds_proyecto2_ramones.data.remote.rawg

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RawgRemoteDataSourceImpl(
    private val apiService: RawgApiService,
    private val apiKey: String?,
    private val ioDispatcher: CoroutineContext
) : RawgRemoteDataSource {

    override suspend fun searchGamesByName(name: String): Result<List<RawgGamePreview>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = apiService.searchGames(name, apiKey)
                response.results.map { RawgGamePreview(it.id, it.name, it.background_image) }
            }
        }

    override suspend fun getGameDetail(id: Int): Result<RawgGameDetail> =
        withContext(ioDispatcher) {
            runCatching {
                val dto = apiService.getGameDetail(id, apiKey)
                RawgGameDetail(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description_raw,
                    metacritic = dto.metacritic,
                    genres = dto.genres.map { it.name },
                    backgroundImage = dto.background_image
                )
            }
        }

    override suspend fun getScreenshots(id: Int): Result<List<String>> =
        withContext(ioDispatcher) {
            runCatching {
                val resp = apiService.getScreenshots(id, apiKey)
                resp.results.map { it.image }
            }
        }
}

