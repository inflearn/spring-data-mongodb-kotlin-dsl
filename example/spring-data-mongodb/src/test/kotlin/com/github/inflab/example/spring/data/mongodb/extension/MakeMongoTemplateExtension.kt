package com.github.inflab.example.spring.data.mongodb.extension

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.testcontainers.ContainerExtension
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

internal fun FreeSpec.makeMongoTemplate(): MongoTemplate {
    val container = install(ContainerExtension(MongoDBContainer(DockerImageName.parse("mongo"))))

    return MongoTemplate(SimpleMongoClientDatabaseFactory(container.replicaSetUrl))
}
