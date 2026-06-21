package org.example.dyds_proyecto2_ramones.data.remote.rawg

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgDetalleDto
import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgScreenshotsResponseDto
import org.example.dyds_proyecto2_ramones.data.remote.rawg.dto.RawgSearchResponseDto
import kotlin.coroutines.CoroutineContext

private const val RAWG_BASE_URL = "https://api.rawg.io/api/"

class RawgRemoteDataSourceImpl(
    private val client: HttpClient,
    private val apiKey: String?,
    private val ioDispatcher: CoroutineContext
) : RawgRemoteDataSource {

    override suspend fun searchGamesByName(name: String): Result<RawgSearchResponseDto> =
        withContext(ioDispatcher) {
            runCatching {
                client.get("${RAWG_BASE_URL}games") {
                    parameter("search", name)
                    apiKey?.takeIf { it.isNotBlank() }?.let { parameter("key", it) }
                }.body<RawgSearchResponseDto>()
            }
        }

    override suspend fun getGameDetail(id: Int): Result<RawgDetalleDto> =
        withContext(ioDispatcher) {
            runCatching {
                client.get("${RAWG_BASE_URL}games/$id") {
                    apiKey?.takeIf { it.isNotBlank() }?.let { parameter("key", it) }
                }.body<RawgDetalleDto>()
            }
        }

    override suspend fun getScreenshots(id: Int): Result<RawgScreenshotsResponseDto> =
        withContext(ioDispatcher) {
            runCatching {
                client.get("${RAWG_BASE_URL}games/$id/screenshots") {
                    apiKey?.takeIf { it.isNotBlank() }?.let { parameter("key", it) }
                }.body<RawgScreenshotsResponseDto>()
            }
        }
}


