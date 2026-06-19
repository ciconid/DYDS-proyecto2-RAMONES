package org.example.dyds_proyecto2_ramones.data.remote.steam.mapper

import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamJuegoDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamLogroDto
import org.example.dyds_proyecto2_ramones.data.remote.steam.SteamPlayerSummaryDto
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.domain.model.Perfil

internal fun SteamPlayerSummaryDto.toDomain(): Perfil = Perfil(
    steamId = this.steamid,
    nombre = this.personaname,
    avatarUrl = this.avatarfull
)

internal fun SteamJuegoDto.toDomain(): Juego = Juego(
    appId = this.appid.toString(),
    nombre = this.name,
    horasJugadas = this.playtime_forever / 60.0,
    iconUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/${this.appid}/${this.img_icon_url}.jpg",
    generos = emptyList()
)

internal fun SteamLogroDto.toDomain(): Logro = Logro(
    nombre = this.name,
    descripcion = this.description,
    conseguido = this.achieved == 1,
    iconUrl = "" // Steam logro DTO no trae URL de icono en esta respuesta
)


