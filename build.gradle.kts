plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradle.plugin-publish") version "1.3.0"
}
repositories{
    mavenCentral()
}
dependencies {
    compileOnly(kotlin("compiler"))
    compileOnly(kotlin("gradle-plugin"))
}

group = "org.droidbaza"
version = "1.0.0-alpha03"

gradlePlugin {
    website = "https://github.com/droidbaza/logmeplugin"
    vcsUrl =  "https://github.com/droidbaza/logmeplugin.git"
    plugins {
        create("logMePlugin") {
            id = "org.droidbaza.logme"
            displayName = "LogMe Plugin"
            tags = listOf("log","logger")
            description = "Easy logging everything!"
            implementationClass = "org.droidbaza.logme.LogMeGradlePlugin"
        }
    }
}

kotlin {
    compilerOptions {
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        optIn.add("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
    }
}
