package org.example.dyds_proyecto2_ramones.data.remote.translation.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationRequestDto(
    @SerialName("q")
    val q: String,
    @SerialName("source")
    val source: String,
    @SerialName("target")
    val target: String,
    @SerialName("format")
    val format: String,
)

@Serializable
data class TranslationResponseDto(
    @SerialName("translatedText")
    val translatedText: String,
)
