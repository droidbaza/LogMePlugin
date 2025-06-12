plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "com.github.droidbaza"
version = "1.0.0-alpha01"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("compiler-embeddable"))
}
