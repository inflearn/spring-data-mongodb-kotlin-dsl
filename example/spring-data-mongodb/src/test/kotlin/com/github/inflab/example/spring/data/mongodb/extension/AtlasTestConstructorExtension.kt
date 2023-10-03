package com.github.inflab.example.spring.data.mongodb.extension

import com.github.inflab.example.spring.data.mongodb.annotation.Database
import io.kotest.core.annotation.AutoScan
import io.kotest.core.extensions.ConstructorExtension
import io.kotest.core.spec.Spec
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.stereotype.Repository
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

@AutoScan
internal object AtlasTestConstructorExtension : ConstructorExtension {
    private val ATLAS_DOMAIN by lazy {
        val property = YamlPropertySourceLoader().load("env", ClassPathResource("application.yml")).first()
        val username = checkNotNull(property.getProperty("spring.data.mongodb.username")) { "spring.data.mongodb.username is not set" }
        val password = checkNotNull(property.getProperty("spring.data.mongodb.password")) { "spring.data.mongodb.password is not set" }
        val host = checkNotNull(property.getProperty("spring.data.mongodb.host")) { "spring.data.mongodb.host is not set" }

        "$username:$password@$host"
    }

    override fun <T : Spec> instantiate(clazz: KClass<T>): Spec? {
        val atlasTest = clazz.findAnnotation<AtlasTest>() ?: return null

        val testConstructor = clazz.primaryConstructor
        if (testConstructor == null || testConstructor.parameters.isEmpty()) {
            return null
        }

        val connectionString = getConnectionString(atlasTest.database)
        val mongoTemplate = getMongoTemplate(connectionString)

        val parameters = testConstructor.parameters.associateWith { parameter ->
            val parameterClass = parameter.type.classifier as KClass<*>

            checkNotNull(parameterClass.findAnnotation<Repository>()) {
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

                repositoryParameter.findAnnotation<Database>()?.let {
                    getMongoTemplate(getConnectionString(it.value))
                } ?: mongoTemplate
            }

            repositoryConstructor.callBy(repositoryParameters)
        }

        return testConstructor.callBy(parameters)
    }

    private fun getConnectionString(databaseName: String): String {
        return "mongodb+srv://$ATLAS_DOMAIN/$databaseName?retryWrites=true&w=majority"
    }

    private fun getMongoTemplate(connectionString: String): MongoTemplate {
        return MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    }
}
