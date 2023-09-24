package com.github.inflab.example.spring.data.mongodb.search

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Ignored
class PhraseSearchRepositoryTest : FreeSpec({
    val connectionString = "mongodb+srv://<username>:<password>@<host>/sample_mflix?retryWrites=true&w=majority"
    val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    val phraseSearchRepository = PhraseSearchRepository(mongoTemplate)

    "findTitleWithNewYork" {
        // when
        val result = phraseSearchRepository.findTitleWithNewYork()

        // then
        result.mappedResults.take(10).map { it.title } shouldBe listOf(
            "New York, New York",
            "New York",
            "New York Stories",
            "New York Minute",
            "Synecdoche, New York",
            "New York Doll",
            "Little New York",
            "Escape from New York",
            "Naked in New York",
            "Autumn in New York",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyDecreasing()
    }

    "findTitleWithManAndMoon" {
        // when
        val result = phraseSearchRepository.findTitleWithManAndMoon()

        // then
        result.mappedResults.take(10).map { it.title } shouldBe listOf(
            "The Man in the Moon",
            "Shoot the Moon",
            "Kick the Moon",
            "The Man",
            "The Moon and Sixpence",
            "The Moon Is Blue",
            "Racing with the Moon",
            "Mountains of the Moon",
            "Man on the Moon",
            "Castaway on the Moon",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyDecreasing()
    }

    "findTitleWithMenWomenBySlop" {
        // when
        val result = phraseSearchRepository.findTitleWithMenWomenBySlop()

        // then
        result.mappedResults.map { it.title } shouldBeEqual listOf(
            "Men Without Women",
            "Men Vs Women",
            "Good Men, Good Women",
            "The War Between Men and Women",
            "Women Without Men",
            "Women Vs Men",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyDecreasing()
    }
})
