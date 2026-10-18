plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "com.github.MH321Productions.Handball4AllApi"
version = "1.0.0"

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)

    testImplementation(kotlin("test"))
}