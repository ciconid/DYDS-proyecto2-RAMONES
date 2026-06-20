package org.example.dyds_proyecto2_ramones.di

import org.example.dyds_proyecto2_ramones.data.remote.rawg.RawgRemoteDataSource
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamRemoteDataSource
import org.example.dyds_proyecto2_ramones.domain.repository.DetalleRepository
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinWiringTest {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `resuelve dependencias principales desde app modules`() {
        startKoin {
            modules(appModules)
        }

        val favoritosRepository: FavoritosRepository = getKoin().get()
        val steamRemoteDataSource: SteamRemoteDataSource = getKoin().get()
        val rawgRemoteDataSource: RawgRemoteDataSource = getKoin().get()
        val detalleRepository: DetalleRepository = getKoin().get()

        assertNotNull(favoritosRepository)
        assertNotNull(steamRemoteDataSource)
        assertNotNull(rawgRemoteDataSource)
        assertNotNull(detalleRepository)
    }
}



