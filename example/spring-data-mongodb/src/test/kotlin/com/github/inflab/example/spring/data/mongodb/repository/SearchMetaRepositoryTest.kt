package com.github.inflab.example.spring.data.mongodb.repository

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Ignored
internal class SearchMetaRepositoryTest : FreeSpec({
    val connectionString = "mongodb+srv://<username>:<password>@<host>/sample_mflix?retryWrites=true&w=majority"
    val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    val searchMetaRepository = SearchMetaRepository(mongoTemplate)

    "findCountBetweenYear" {
        // when
        val result = searchMetaRepository.findCountBetweenYear()

        // then
        result.mappedResults.first().count.total.shouldBeGreaterThan(500)
    }
})
