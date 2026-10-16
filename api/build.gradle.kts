plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "com.github.MH321Productions.Handball4AllApi"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)

    testImplementation(kotlin("test"))
}