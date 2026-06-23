package org.example.dyds_proyecto2_ramones.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSourceImpl
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSourceImpl
import org.example.dyds_proyecto2_ramones.data.remote.translation.TranslationRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.translation.TranslationRemoteDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext
import org.example.dyds_proyecto2_ramones.data.BuildKonfig

val networkModule = module {
    single<CoroutineContext>(named("io")) { Dispatchers.IO }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
        }
    }

    single<SteamRemoteDataSource> {
        SteamRemoteDataSourceImpl(
            client = get<HttpClient>(),
            apiKey = BuildKonfig.STEAM_KEY,
            ioDispatcher = get(named("io"))
        )
    }

    single<RawgRemoteDataSource> {
        RawgRemoteDataSourceImpl(
            client = get<HttpClient>(),
            apiKey = BuildKonfig.RAWG_KEY,
            ioDispatcher = get(named("io"))
        )
    }

    single<TranslationRemoteDataSource> {
        TranslationRemoteDataSourceImpl(
            client = get<HttpClient>(),
            ioDispatcher = get(named("io"))
        )
    }
}