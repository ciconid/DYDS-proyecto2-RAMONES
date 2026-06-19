package org.example.dyds_proyecto2_ramones

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform