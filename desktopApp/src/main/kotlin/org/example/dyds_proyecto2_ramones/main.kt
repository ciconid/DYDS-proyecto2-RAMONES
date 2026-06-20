package org.example.dyds_proyecto2_ramones

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.example.dyds_proyecto2_ramones.di.appModules
import org.example.dyds_proyecto2_ramones.di.networkModule

fun main() = application {
    startKoin {
        modules(appModules + networkModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "dyds_proyecto2_ramones",
    ) {
        App()
    }
}