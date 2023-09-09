version = "0.0.0-beta.1"
group = "com.github.inflab"

plugins {
    alias(libs.plugins.kotiln.jvm)
    alias(libs.plugins.kotlinter)

    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.data.mongodb)

    testImplementation(libs.kotest)
    testImplementation(libs.mockk)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to "spring-data-mongodb-kotlin-dsl",
                "Implementation-Version" to project.version,
            ),
        )
    }
}
