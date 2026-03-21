plugins {
    kotlin("jvm") version "2.3.20"
    kotlin("plugin.serialization") version "2.3.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "one.cheily"
version = "3.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.maven.apache.org/maven2") }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:6.1.7")
    testImplementation("io.kotest:kotest-runner-junit5:6.1.7")
    testImplementation("io.github.oshai:kotlin-logging:8.0.01")
    testImplementation("ch.qos.logback:logback-classic:1.5.32")
    testImplementation("org.wiremock:wiremock:3.3.1")
    testImplementation("io.kotest:kotest-extensions-wiremock:6.1.7")

    // core
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.sksamuel.hoplite:hoplite-core:2.9.0")
    implementation("com.sksamuel.hoplite:hoplite-yaml:2.9.0")
    implementation("io.ktor:ktor-client-core:3.4.1")
    implementation("io.ktor:ktor-client-cio:3.4.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.4.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.1")

    // cli
    implementation("com.github.ajalt.clikt:clikt:4.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(24)
}

application {
    mainClass.set("dustgrain.RunKt")
}

//tasks {
//    shadowJar {
//        mergeServiceFiles()
//    }
//}