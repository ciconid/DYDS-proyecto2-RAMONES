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
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

private fun getSteamApiKey(): String =
    System.getProperty("steam.api.key")
        ?: System.getenv("STEAM_API_KEY")
        ?: "855078138592C196E044AAA58B1649A1"

private fun getRawgApiKey(): String =
    System.getProperty("rawg.api.key") ?: System.getenv("RAWG_API_KEY") ?: ""

val networkModule = module {
    single<CoroutineContext>(named("io")) { Dispatchers.IO }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    single<SteamRemoteDataSource> {
        SteamRemoteDataSourceImpl(get<HttpClient>(), getSteamApiKey(), get(named("io")))
    }

    single<RawgRemoteDataSource> {
        RawgRemoteDataSourceImpl(get<HttpClient>(), getRawgApiKey(), get(named("io")))
    }
}

