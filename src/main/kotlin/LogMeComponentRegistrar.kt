package com.github.droidbaza.logmeplugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
class LogMeComponentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(
        project: org.jetbrains.kotlin.com.intellij.mock.MockProject,
        configuration: CompilerConfiguration
    ) {
        IrGenerationExtension.registerExtension(project, LogMePlugin())
    }
}