plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
        create("exampleLibs") {
            from(files("libs.example.versions.toml"))
        }
    }
}

rootProject.name = "spring-data-mongodb-kotlin-dsl"
module(name = ":core", path = "core")
module(name = ":example", path = "example")
module(name = ":spring-data-mongodb-example", path = "example/spring-data-mongodb")

fun module(name: String, path: String) {
    include(name)
    project(name).projectDir = file(path)
}
