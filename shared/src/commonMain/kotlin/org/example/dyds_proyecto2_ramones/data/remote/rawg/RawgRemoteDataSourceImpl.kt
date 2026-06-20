package org.example.dyds_proyecto2_ramones.data.remote.rawg

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

private const val RAWG_BASE_URL = "https://api.rawg.io/api/"

class RawgRemoteDataSourceImpl(
    private val client: HttpClient,
    private val apiKey: String?,
    private val ioDispatcher: CoroutineContext
) : RawgRemoteDataSource {

    override suspend fun searchGamesByName(name: String): Result<List<RawgGamePreview>> =
        withContext(ioDispatcher) {
            runCatching {
                val response: RawgSearchResponseDto = client.get("${RAWG_BASE_URL}games") {
                    parameter("search", name)
                    apiKey?.takeIf { it.isNotBlank() }?.let { parameter("key", it) }
                }.body()
                response.results.map { RawgGamePreview(it.id, it.name, it.background_image) }
            }
        }

    override suspend fun getGameDetail(id: Int): Result<RawgGameDetail> =
        withContext(ioDispatcher) {
            runCatching {
                val dto: RawgDetalleDto = client.get("${RAWG_BASE_URL}games/$id") {
                    apiKey?.takeIf { it.isNotBlank() }?.let { parameter("key", it) }
                }.body()
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
                val resp: RawgScreenshotsResponseDto = client.get("${RAWG_BASE_URL}games/$id/screenshots") {
                    apiKey?.takeIf { it.isNotBlank() }?.let { parameter("key", it) }
                }.body()
                resp.results.map { it.image }
            }
        }
}


