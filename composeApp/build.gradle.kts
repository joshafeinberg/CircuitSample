import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import java.util.Locale
import kotlin.jvm.java

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    configureCommonMainKsp()
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.circuit.foundation)
            implementation(libs.circuit.di.annotations)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

// This is the worst deprecation replacement in the history of deprecation replacements
fun String.capitalizeUS() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
}

val kspTargets = kotlin.targets.names.map { it.capitalizeUS() }

ksp { arg("circuit.codegen.mode", "metro") }

/*
dependencies {
    for (target in kspTargets) {
        val targetConfigSuffix = if (target == "Metadata") "CommonMainMetadata" else target
        add("ksp${targetConfigSuffix}", libs.circuit.di.processor)
    }
}
*/

fun KotlinMultiplatformExtension.configureCommonMainKsp() {
    dependencies {
        add("kspCommonMainMetadata", libs.circuit.di.processor)
    }

    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }

    project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
        if (name != "kspCommonMainKotlinMetadata") {
            dependsOn("kspCommonMainKotlinMetadata")
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.joshafeinberg.circuitsample.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.joshafeinberg.circuitsample"
            packageVersion = "1.0.0"
        }
    }
}
