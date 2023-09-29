package com.github.inflab.example.spring.data.mongodb.extension

import io.kotest.core.annotation.AutoScan
import io.kotest.core.extensions.ConstructorExtension
import io.kotest.core.spec.Spec
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@AutoScan
internal object AtlasTestConstructorExtension : ConstructorExtension {
    private val ATLAS_DOMAIN by lazy {
        val property = YamlPropertySourceLoader().load("env", ClassPathResource("application.yml")).first()
        val username = property.getProperty("spring.data.mongodb.username") ?: throw IllegalStateException("spring.data.mongodb.username is not set")
        val password = property.getProperty("spring.data.mongodb.password") ?: throw IllegalStateException("spring.data.mongodb.password is not set")
        val host = property.getProperty("spring.data.mongodb.host") ?: throw IllegalStateException("spring.data.mongodb.host is not set")

        "$username:$password@$host"
    }

    override fun <T : Spec> instantiate(clazz: KClass<T>): Spec? {
        val atlasTest = clazz.annotations.find { it is AtlasTest } as AtlasTest? ?: return null

        val testConstructor = clazz.primaryConstructor
        if (testConstructor == null || testConstructor.parameters.isEmpty()) {
            return null
        }

        val connectionString = "mongodb+srv://$ATLAS_DOMAIN/${atlasTest.database}?retryWrites=true&w=majority"
        val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))

        val parameters = testConstructor.parameters.associateWith { parameter ->
            val parameterClass = parameter.type.classifier as KClass<*>
            check(parameterClass.annotations.any { it is org.springframework.stereotype.Repository }) {
                "The parameter type of constructor must be annotated with @Repository but ${parameterClass.qualifiedName}"
            }
            val repositoryConstructor = checkNotNull(parameterClass.primaryConstructor) {
                "The parameter type of constructor must have primary constructor but ${parameterClass.qualifiedName}"
            }

            val repositoryParameters = repositoryConstructor.parameters.associateWith { repositoryParameter ->
                val repositoryParameterClass = repositoryParameter.type.classifier as KClass<*>
                check(repositoryParameterClass == MongoTemplate::class) {
                    "The parameter type of repository constructor must be MongoTemplate but ${repositoryParameterClass.qualifiedName} from ${parameterClass.qualifiedName}"
                }

                mongoTemplate
            }

            repositoryConstructor.callBy(repositoryParameters)
        }

        return testConstructor.callBy(parameters)
    }
}
