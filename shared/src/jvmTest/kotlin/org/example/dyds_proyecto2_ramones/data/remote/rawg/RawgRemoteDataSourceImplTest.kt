package org.example.dyds_proyecto2_ramones.data.remote.rawg

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RawgRemoteDataSourceImplTest {

    private val apiService: RawgApiService = mockk()
    private val apiKey: String? = null
    private val dataSource = RawgRemoteDataSourceImpl(apiService, apiKey, Dispatchers.Unconfined)

    @Test
    fun `searchGamesByName maps results`() = runBlocking {
        val dto = RawgSearchResponseDto(listOf(RawgGameDto(123, "GameName", 90, "bg.jpg", listOf(RawgGenreDto("Action")))))
        coEvery { apiService.searchGames("GameName", apiKey) } returns dto

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
        val dto = RawgDetalleDto(321, "GameDetail", "A description", 85, "bg2.jpg", listOf(RawgGenreDto("RPG")))
        coEvery { apiService.getGameDetail(321, apiKey) } returns dto

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
        val dto = RawgScreenshotsResponseDto(listOf(RawgScreenshotDto("img1.jpg"), RawgScreenshotDto("img2.jpg")))
        coEvery { apiService.getScreenshots(111, apiKey) } returns dto

        val result = dataSource.getScreenshots(111)
        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(listOf("img1.jpg", "img2.jpg"), list)
    }
}

