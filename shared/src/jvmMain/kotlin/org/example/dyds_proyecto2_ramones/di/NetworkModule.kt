package org.example.dyds_proyecto2_ramones.di

import kotlinx.coroutines.Dispatchers
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgApiService
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSourceImpl
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamApiService
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSourceImpl
import org.example.dyds_proyecto2_ramones.data.repository.DetalleRepositoryImpl
import org.example.dyds_proyecto2_ramones.data.repository.GameBroker
import org.example.dyds_proyecto2_ramones.domain.repository.DetalleRepository
import org.example.dyds_proyecto2_ramones.domain.repository.PerfilRepository
import org.example.dyds_proyecto2_ramones.domain.repository.BibliotecaRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

private const val STEAM_BASE_URL = "https://api.steampowered.com/"
private const val RAWG_BASE_URL = "https://api.rawg.io/api/"

private fun getSteamApiKey(): String =
    System.getProperty("steam.api.key") ?: System.getenv("STEAM_API_KEY") ?: ""

private fun getRawgApiKey(): String =
    System.getProperty("rawg.api.key") ?: System.getenv("RAWG_API_KEY") ?: ""

val networkModule = module {
    single<CoroutineContext>(named("io")) { Dispatchers.IO }

    single {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single(named("steamRetrofit")) {
        Retrofit.Builder()
            .baseUrl(STEAM_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single(named("rawgRetrofit")) {
        Retrofit.Builder()
            .baseUrl(RAWG_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { (get<Retrofit>(named("steamRetrofit"))).create(SteamApiService::class.java) }
    single { (get<Retrofit>(named("rawgRetrofit"))).create(RawgApiService::class.java) }

    single<SteamRemoteDataSource> {
        val api = get<SteamApiService>()
        SteamRemoteDataSourceImpl(api, getSteamApiKey(), get(named("io")))
    }

    single<RawgRemoteDataSource> {
        val api = get<RawgApiService>()
        RawgRemoteDataSourceImpl(api, getRawgApiKey(), get(named("io")))
    }

    single<PerfilRepository> { org.example.dyds_proyecto2_ramones.data.repository.PerfilRepositoryImpl(get(), get(named("io"))) }
    single<BibliotecaRepository> { org.example.dyds_proyecto2_ramones.data.repository.BibliotecaRepositoryImpl(get(), get(named("io"))) }

    single { GameBroker(get(), get(), get(named("io"))) }

    single<DetalleRepository> { DetalleRepositoryImpl(get(), get(), get(named("io"))) }
}