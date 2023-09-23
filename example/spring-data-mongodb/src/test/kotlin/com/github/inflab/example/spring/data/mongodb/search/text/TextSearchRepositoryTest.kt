package com.github.inflab.example.spring.data.mongodb.search.text

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldBeEqual
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Ignored
internal class TextSearchRepositoryTest : FreeSpec({
    val connectionString = ""
    val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    val textSearchRepository = TextSearchRepository(mongoTemplate)

    "findTitleSufer" {
        // when
        val result = textSearchRepository.findTitleSufer()

        // then
        result.mappedResults shouldHaveSize 3
        result.mappedResults.map { it.title } shouldBeEqual listOf(
            "Soul Surfer",
            "Little Surfer Girl",
            "Fantastic 4: Rise of the Silver Surfer",
        )
    }
})
