package com.joshafeinberg.circuitsample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import circuitsample.composeapp.generated.resources.Res
import circuitsample.composeapp.generated.resources.compose_multiplatform
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.zacsweers.metro.asContribution
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.createGraphFactory

@Composable
@Preview
fun App() {
    val parentGraph = remember { createGraphFactory<ParentGraph.Factory>().create("my string") }
    val dependencyGraph = remember { parentGraph.asContribution<MyDepGraph.Factory>().create() }
    val circuit = dependencyGraph.circuit

    MaterialTheme {
        CircuitCompositionLocals(circuit) {
            val backstack = rememberSaveableBackStack(TestScreen)
            val navigator = rememberCircuitNavigator(backstack) {}

            NavigableCircuitContent(navigator, backstack)
        }
    }
}