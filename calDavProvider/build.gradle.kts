plugins {
    id("buildsrc.convention.kotlin-jvm")

    application
    alias(libs.plugins.ktorPlugin)
}

group = "com.github.MH321Productions.Handball4AllApi"
version = "1.0.0"

dependencies {
    implementation(project(":api"))
    implementation(libs.iCalDAV.client)
}

application {
    mainClass = "com.github.mh321Productions.handball4AllApi.calDavProvider.MainKt"
}

ktor {
    fatJar {
        archiveFileName = "H4AllCalDav.jar"
    }
}