package com.joshafeinberg.circuitsample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CircuitSample",
    ) {
        App()
    }
}