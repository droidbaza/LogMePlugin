plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "org.droidbaza"
version = "1.0.0-alpha02"

gradlePlugin {
    website = "https://github.com/droidbaza/logmeplugin"
    vcsUrl =  "https://github.com/droidbaza/logmeplugin.git"
    plugins {
        create("logMePlugin") {
            id = "org.droidbaza.logmeplugin"
            displayName = "LogMe Plugin"
            tags = listOf("log","logger")
            description = "Easy logging everything!"
            implementationClass = "com.github.droidbaza.logmeplugin.LogMePlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("compiler-embeddable"))
}
