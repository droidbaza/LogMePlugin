plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.droidbaza"
version = "1.0.0-alpha02"

repositories{
    mavenCentral()
}
dependencies {
    implementation(kotlin("compiler-embeddable"))
}
