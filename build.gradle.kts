plugins {
    alias(libs.plugins.kotiln.jvm)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.ben.manes.versions)
    alias(libs.plugins.version.catalog.update)
}

allprojects {
    group = "com.github.inflab"
    version = "0.0.0-beta.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jmailen.kotlinter")

    dependencies {
        testImplementation(rootProject.libs.kotest)
        testImplementation(rootProject.libs.mockk)
    }

    testing {
        suites {
            val test by getting(JvmTestSuite::class) {
                useJUnitJupiter()
            }
        }
    }
}

versionCatalogUpdate {
    catalogFile.set(file("libs.versions.toml"))

    versionCatalogs {
        create("example") {
            catalogFile.set(file("libs.example.versions.toml"))
        }
    }
}
