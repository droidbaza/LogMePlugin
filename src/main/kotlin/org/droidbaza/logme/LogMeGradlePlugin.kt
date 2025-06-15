package org.droidbaza.logme

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class LogMeGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            project.tasks.withType(KotlinCompile::class.java).configureEach {
                if (it.name.contains("debug", ignoreCase = true)) {
                    val pluginJar = this@LogMeGradlePlugin.javaClass.protectionDomain.codeSource.location.path
                    it.compilerOptions {
                        freeCompilerArgs.add("-Xplugin=$pluginJar")
                    }
                }
            }
        }
    }
}