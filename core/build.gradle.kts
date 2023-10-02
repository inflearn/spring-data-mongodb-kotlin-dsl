plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    implementation(libs.spring.data.mongodb)
    implementation(kotlin("reflect"))
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
