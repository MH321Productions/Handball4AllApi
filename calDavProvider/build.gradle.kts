plugins {
    id("buildsrc.convention.kotlin-jvm")

    application
    alias(libs.plugins.ktorPlugin)
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "com.github.MH321Productions.Handball4AllApi"
version = "1.0.0"

dependencies {
    implementation(project(":api"))
    implementation(libs.iCalDAV.client)
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.slf4j.jdk14)
    runtimeOnly(libs.dnsjava) //Manually specify newer version of dnsjava to protect against vulnerabilities
}

application {
    mainClass = "com.github.mh321Productions.handball4AllApi.calDavProvider.MainKt"
}

ktor {
    fatJar {
        archiveFileName = "H4AllCalDav.jar"
    }
}