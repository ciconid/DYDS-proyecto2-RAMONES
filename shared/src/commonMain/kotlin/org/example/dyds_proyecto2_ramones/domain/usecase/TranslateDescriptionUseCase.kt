package org.example.dyds_proyecto2_ramones.domain.usecase

import org.example.dyds_proyecto2_ramones.data.remote.translation.TranslationRemoteDataSource

class TranslateDescriptionUseCase(
    private val translationRemoteDataSource: TranslationRemoteDataSource,
) {
    suspend operator fun invoke(englishDescription: String): Result<String> {
        if (englishDescription.isBlank()) {
            return Result.success(englishDescription)
        }
        return translationRemoteDataSource.translateToSpanish(englishDescription)
    }
}
