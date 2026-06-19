package org.example.dyds_proyecto2_ramones.di

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
    fun `resuelve favoritos repository desde app modules`() {
        startKoin {
            modules(appModules)
        }

        val repo: FavoritosRepository = getKoin().get()
        assertNotNull(repo)
    }
}



