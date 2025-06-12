plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "com.github.droidbaza"
version = "1.0.0-alpha01"

gradlePlugin {
    website = "https://github.com/droidbaza/logmeplugin"
    vcsUrl =  "https://github.com/droidbaza/logmeplugin.git"
    plugins {
        create("logMePlugin") {
            id = "com.github.droidbaza.logmeplugin"
            displayName = "LogMe Plugin"
            description = "Easy logging everything!"
            implementationClass = "LogMePlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("compiler-embeddable"))
}
