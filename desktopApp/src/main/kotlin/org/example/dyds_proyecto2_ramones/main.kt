package org.example.dyds_proyecto2_ramones

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "dyds_proyecto2_ramones",
    ) {
        App()
    }
}