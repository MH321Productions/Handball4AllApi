plugins {
    id("buildsrc.convention.kotlin-jvm")

    application
    alias(libs.plugins.ktorPlugin)
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "com.github.MH321Productions.Handball4AllApi"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":api"))
    implementation(libs.iCalDAV.client)
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.slf4j.jdk14)
}

application {
    mainClass = "com.github.mh321Productions.handball4AllApi.calDavProvider.MainKt"
}

ktor {
    fatJar {
        archiveFileName = "H4AllCalDav.jar"
    }
}