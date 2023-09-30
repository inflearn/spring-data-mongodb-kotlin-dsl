import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(exampleLibs.plugins.spring.boot)
    alias(exampleLibs.plugins.spring.dependency.management)
    alias(exampleLibs.plugins.kotlin.spring)
}

dependencies {
    implementation(projects.core)
    implementation(exampleLibs.kotlin.reflect)
    implementation(exampleLibs.spring.data.mongodb)

    testImplementation(exampleLibs.spring.test)
    testImplementation(exampleLibs.kotest.testcontainers)
    testImplementation(platform(exampleLibs.testcontainers))
    testImplementation(exampleLibs.testcontainers.mongodb)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.jar {
    enabled = false
}
