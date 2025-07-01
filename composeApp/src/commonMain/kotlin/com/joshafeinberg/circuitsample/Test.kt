package com.joshafeinberg.circuitsample

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.Inject

data object TestScreen : Screen

data class TestScreenState(val text: String) : CircuitUiState

@CircuitInject(TestScreen::class, AppScope::class)
@Composable
fun TestScreen(modifier: Modifier) {
    Text("TestScreen")
}

@Inject
class TestScreenPresenter(
    @Assisted private val navigator: Navigator,
    private val myString: String,
) : Presenter<TestScreenState> {
    @Composable
    override fun present(): TestScreenState {
        return TestScreenState(myString)
    }

    @CircuitInject(TestScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): TestScreenPresenter
    }
}