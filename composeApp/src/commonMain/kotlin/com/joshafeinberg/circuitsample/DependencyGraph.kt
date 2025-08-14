package com.joshafeinberg.circuitsample

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesGraphExtension
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Scope
import dev.zacsweers.metro.SingleIn
import kotlin.jvm.JvmSuppressWildcards

@ContributesGraphExtension(AppScope::class)
internal interface MyDepGraph {

    val circuit: Circuit

    @ContributesGraphExtension.Factory(ParentScope::class)
    fun interface Factory {
        fun create(): MyDepGraph
    }
}

@Scope
annotation class ParentScope


@DependencyGraph(scope = ParentScope::class)
internal interface ParentGraph {

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides myString: String
        ): ParentGraph
    }
}

@ContributesTo(AppScope::class)
interface CircuitProviders {
    @Multibinds
    fun presenterFactories(): Set<Presenter.Factory>

    @Multibinds
    fun viewFactories(): Set<Ui.Factory>

    @SingleIn(AppScope::class)
    @Provides
    fun provideCircuit(
        presenterFactories: @JvmSuppressWildcards Set<Presenter.Factory>,
        uiFactories: @JvmSuppressWildcards Set<Ui.Factory>,
    ): Circuit {
        return Circuit.Builder()
            .addPresenterFactories(presenterFactories)
            .addUiFactories(uiFactories)
            .build()
    }

}