package org.example.dyds_proyecto2_ramones.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.remote.translation.TranslationRemoteDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TranslateDescriptionUseCaseTest {

    private val translationRemoteDataSource: TranslationRemoteDataSource = mockk()
    private val useCase = TranslateDescriptionUseCase(translationRemoteDataSource)

    @Test
    fun `invoca con descripcion vacia devuelve success sin llamar api`() = runBlocking {
        val result = useCase("")

        assertTrue(result.isSuccess)
        assertEquals("", result.getOrThrow())
        coVerify(exactly = 0) { translationRemoteDataSource.translateToSpanish(any()) }
    }

    @Test
    fun `invoca con descripcion en blanco devuelve success sin llamar api`() = runBlocking {
        val result = useCase("   ")

        assertTrue(result.isSuccess)
        assertEquals("   ", result.getOrThrow())
        coVerify(exactly = 0) { translationRemoteDataSource.translateToSpanish(any()) }
    }

    @Test
    fun `invoca con descripcion valida llama api y devuelve traduccion`() = runBlocking {
        val englishDesc = "This is a great game"
        val spanishDesc = "Este es un gran juego"
        coEvery { translationRemoteDataSource.translateToSpanish(englishDesc) } returns Result.success(spanishDesc)

        val result = useCase(englishDesc)

        assertTrue(result.isSuccess)
        assertEquals(spanishDesc, result.getOrThrow())
        coVerify(exactly = 1) { translationRemoteDataSource.translateToSpanish(englishDesc) }
    }

    @Test
    fun `invoca con descripcion valida propaga error de api`() = runBlocking {
        val englishDesc = "Some description"
        val error = RuntimeException("Translation service unavailable")
        coEvery { translationRemoteDataSource.translateToSpanish(englishDesc) } returns Result.failure(error)

        val result = useCase(englishDesc)

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
        coVerify(exactly = 1) { translationRemoteDataSource.translateToSpanish(englishDesc) }
    }
}

