package com.github.inflab.example.spring.data.mongodb.search.range

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.date.shouldBeBetween
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import java.time.LocalDateTime

@Ignored
internal class RangeSearchRepositoryTest : FreeSpec({
    val connectionString = "mongodb+srv://<username>:<password>@<host>/sample_mflix?retryWrites=true&w=majority"
    val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    val textSearchRepository = RangeSearchRepository(mongoTemplate)

    "findRuntimeBetween" {
        // when
        val result = textSearchRepository.findRuntimeBetween()

        // then
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "Dots",
            "Sisyphus",
            "The Fly",
            "Andrè and Wally B.",
            "Luxo Jr.",
        )
        result.mappedResults.take(5).map { it.runtime } shouldBe listOf(3, 3, 3, 2, 2)
    }

    "findReleasedBetween" {
        // when
        val start = LocalDateTime.of(2010, 1, 1, 0, 0, 0)
        val end = LocalDateTime.of(2015, 1, 1, 0, 0, 0)

        val result = textSearchRepository.findReleasedBetween()

        // then
        result.mappedResults.take(5).map { it.released }.forAll {
            it.shouldBeBetween(start, end)
        }
    }
})
