package org.example.dyds_proyecto2_ramones.data.remote.translation

interface TranslationRemoteDataSource {
    suspend fun translateToSpanish(text: String): Result<String>
}
