package com.github.inflab.example.spring.data.mongodb.search.text

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Ignored
internal class TextSearchRepositoryTest : FreeSpec({
    val connectionString = "mongodb+srv://<username>:<password>@<host>/sample_mflix?retryWrites=true&w=majority"
    val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    val textSearchRepository = TextSearchRepository(mongoTemplate)

    "findTitleWithSufer" {
        // when
        val result = textSearchRepository.findTitleWithSufer()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Soul Surfer",
            "Little Surfer Girl",
            "Fantastic 4: Rise of the Silver Surfer",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyDecreasing()
    }

    "findTitleWithNawYarkByFuzzy" {
        // when
        val result = textSearchRepository.findTitleWithNawYarkByFuzzy()

        // then
        result.mappedResults.take(8).map { it.title } shouldBe listOf(
            "The Longest Yard",
            "The Longest Yard",
            "Stomp the Yard",
            "Naz & Maalik",
            "La nao capitana",
            "Kabhi Haan Kabhi Naa",
            "Kaho Naa... Pyaar Hai",
            "Oysters at Nam Kee's",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyDecreasing()
    }
})
