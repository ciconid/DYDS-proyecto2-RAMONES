package org.example.dyds_proyecto2_ramones.data.remote.translation

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.withContext
import org.example.dyds_proyecto2_ramones.data.remote.translation.dto.TranslationRequestDto
import org.example.dyds_proyecto2_ramones.data.remote.translation.dto.TranslationResponseDto
import kotlin.coroutines.CoroutineContext

private const val TRANSLATE_URL = "https://translate.shamoun.online/translate"

class TranslationRemoteDataSourceImpl(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineContext,
) : TranslationRemoteDataSource {


    override suspend fun translateToSpanish(text: String): Result<String> =
        withContext(ioDispatcher) {
            if (text.isBlank()) return@withContext Result.success(text)

            runCatching {
                val response = client.post(TRANSLATE_URL) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        TranslationRequestDto(
                            q = text,
                            source = "en",
                            target = "es",
                            format = "text",
                        ),
                    )
                }
                response.body<TranslationResponseDto>().translatedText.trim()
            }
        }
}
