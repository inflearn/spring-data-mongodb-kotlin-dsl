plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "spring-data-mongodb-kotlin-dsl"
include("spring-data-mongodb-kotlin-dsl")
