package org.example.dyds_proyecto2_ramones

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.example.dyds_proyecto2_ramones.di.appModules

fun main() = application {
    startKoin {
        modules(appModules)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "dyds_proyecto2_ramones",
    ) {
        App()
    }
}