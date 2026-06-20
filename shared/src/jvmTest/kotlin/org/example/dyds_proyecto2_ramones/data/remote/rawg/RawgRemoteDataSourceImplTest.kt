package org.example.dyds_proyecto2_ramones.data.remote.rawg

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RawgRemoteDataSourceImplTest {

    private val apiKey: String? = null

    private fun createClient(
        json: String,
        assertRequest: (String, Map<String, List<String>>) -> Unit = { _, _ -> }
    ): HttpClient = HttpClient(MockEngine { request ->
        assertRequest(
            request.url.encodedPath,
            request.url.parameters.entries().associate { it.key to it.value }
        )
        respond(
            content = json,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    }) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    @Test
    fun `searchGamesByName maps results`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "results": [
                    {
                      "id": 123,
                      "name": "GameName",
                      "metacritic": 90,
                      "background_image": "bg.jpg",
                      "genres": [{ "name": "Action" }]
                    }
                  ]
                }
            """.trimIndent(),
            assertRequest = { path, parameters ->
                assertEquals("/api/games", path)
                assertEquals(listOf("GameName"), parameters["search"])
            }
        )
        val dataSource = RawgRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.searchGamesByName("GameName")
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(1, list.size)
        val preview = list[0]
        assertEquals(123, preview.id)
        assertEquals("GameName", preview.name)
        assertEquals("bg.jpg", preview.backgroundImage)
    }

    @Test
    fun `getGameDetail maps detail correctly`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "id": 321,
                  "name": "GameDetail",
                  "description_raw": "A description",
                  "metacritic": 85,
                  "background_image": "bg2.jpg",
                  "genres": [{ "name": "RPG" }]
                }
            """.trimIndent(),
            assertRequest = { path, _ ->
                assertEquals("/api/games/321", path)
            }
        )
        val dataSource = RawgRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.getGameDetail(321)
        assertTrue(result.isSuccess)
        val detail = result.getOrThrow()
        assertEquals(321, detail.id)
        assertEquals("GameDetail", detail.name)
        assertEquals("A description", detail.description)
        assertEquals(85, detail.metacritic)
        assertEquals(listOf("RPG"), detail.genres)
        assertEquals("bg2.jpg", detail.backgroundImage)
    }

    @Test
    fun `getScreenshots maps images`() = runBlocking {
        val client = createClient(
            json = """
                {
                  "results": [
                    { "image": "img1.jpg" },
                    { "image": "img2.jpg" }
                  ]
                }
            """.trimIndent(),
            assertRequest = { path, _ ->
                assertEquals("/api/games/111/screenshots", path)
            }
        )
        val dataSource = RawgRemoteDataSourceImpl(client, apiKey, Dispatchers.Unconfined)

        val result = dataSource.getScreenshots(111)
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(listOf("img1.jpg", "img2.jpg"), list)
    }
}

