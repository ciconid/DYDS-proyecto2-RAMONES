package org.example.dyds_proyecto2_ramones.di

import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.example.dyds_proyecto2_ramones.domain.usecase.GetFavoritosUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.AgregarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.EliminarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.FiltrarPorGeneroUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetBibliotecaUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetDetalleUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetPerfilUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.OrdenarPorHorasUseCase

val useCaseModule = module {
    singleOf(::GetBibliotecaUseCase)
    singleOf(::GetDetalleUseCase)
    singleOf(::GetPerfilUseCase)
    singleOf(::GetFavoritosUseCase)
    singleOf(::AgregarFavoritoUseCase)
    singleOf(::EliminarFavoritoUseCase)
    singleOf(::FiltrarPorGeneroUseCase)
    singleOf(::OrdenarPorHorasUseCase)
}


