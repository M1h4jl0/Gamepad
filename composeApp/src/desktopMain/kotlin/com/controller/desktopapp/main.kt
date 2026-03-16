package com.controller.desktopapp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Controller App",
        resizable = false,
        state = rememberWindowState(
            width = 330.dp,
            height = 430.dp
        )
    ) {
        App()
    }
}
