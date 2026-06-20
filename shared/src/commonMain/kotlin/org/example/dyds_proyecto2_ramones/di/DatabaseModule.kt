package org.example.dyds_proyecto2_ramones.di

import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.example.dyds_proyecto2_ramones.data.local.FavoritosLocalDataSource
import org.example.dyds_proyecto2_ramones.data.local.createDatabase
import org.example.dyds_proyecto2_ramones.data.repository.FavoritosRepositoryImpl
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository

val databaseModule = module {
    single { createDatabase() }

    singleOf(::FavoritosLocalDataSource)

    single<FavoritosRepository> {
        FavoritosRepositoryImpl(get<FavoritosLocalDataSource>())
    }
}



