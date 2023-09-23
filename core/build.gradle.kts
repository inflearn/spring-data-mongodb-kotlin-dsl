plugins {
    `java-library`
}

dependencies {
    implementation(libs.spring.data.mongodb)
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

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}
