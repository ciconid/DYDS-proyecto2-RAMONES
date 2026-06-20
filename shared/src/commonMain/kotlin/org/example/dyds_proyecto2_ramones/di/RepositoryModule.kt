package org.example.dyds_proyecto2_ramones.di

import org.example.dyds_proyecto2_ramones.data.repository.BibliotecaRepositoryImpl
import org.example.dyds_proyecto2_ramones.data.repository.DetalleRepositoryImpl
import org.example.dyds_proyecto2_ramones.data.repository.GameBroker
import org.example.dyds_proyecto2_ramones.data.repository.PerfilRepositoryImpl
import org.example.dyds_proyecto2_ramones.domain.repository.BibliotecaRepository
import org.example.dyds_proyecto2_ramones.domain.repository.DetalleRepository
import org.example.dyds_proyecto2_ramones.domain.repository.PerfilRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<PerfilRepository> { PerfilRepositoryImpl(get(), get(named("io"))) }
    single<BibliotecaRepository> { BibliotecaRepositoryImpl(get(), get(named("io"))) }
    single { GameBroker(get(), get(), get(named("io"))) }
    single<DetalleRepository> { DetalleRepositoryImpl(get(), get(), get(named("io"))) }
}

