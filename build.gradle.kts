plugins {
    kotlin("jvm") version libs.versions.kotlin
}

group = "pl.org.qwerty"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.kotest)
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveFileName = "kotest-extensions-parallel.jar"
}

kotlin {
    jvmToolchain(8)
}
