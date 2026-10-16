plugins {
    id("buildsrc.convention.kotlin-jvm")

    application
    libs.plugins.ktorPlugin
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(project(":api"))
}

application {
    mainClass = "com.github.MH321Productions.app.AppKt"
}
