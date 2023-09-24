package com.github.inflab.example.spring.data.mongodb.repository

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.doubles.shouldBeBetween
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Ignored
internal class ScoreSearchRepositoryTest : FreeSpec({
    val connectionString = "mongodb+srv://<username>:<password>@<host>/sample_mflix?retryWrites=true&w=majority"
    val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    val scoreSearchRepository = ScoreSearchRepository(mongoTemplate)

    "findTitleAndPlotWithBoost" {
        // when
        val result = scoreSearchRepository.findTitleAndPlotWithBoost()

        // then
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "Kites Over Helsinki",
            "Helsinki-Naples All Night Long",
            "Drifting Clouds",
            "Sairaan kaunis maailma",
            "Bad Luck Love",
        )
        result.mappedResults.take(5).map { it.score }.forAll {
            it.shouldBeBetween(3.0, 25.0, 0.0)
        }
    }

    "findTitleAndPlotWithConstant" {
        // when
        val result = scoreSearchRepository.findTitleAndPlotWithConstant()

        // then
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "Tower Block",
            "Tower Heist",
            "Tokyo Tower",
            "The Leaning Tower",
            "Ivory Tower",
        )
        result.mappedResults.take(5).map { it.score }.forAll {
            it.shouldBeBetween(5.0, 9.0, 0.0)
        }
    }

    "findTitleWithArithmetic" {
        // when
        val result = scoreSearchRepository.findTitleWithArithmetic()

        // then
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "Men...",
            "X-Men",
            "X-Men",
            "Matchstick Men",
            "The Men",
        )
        result.mappedResults.take(5).map { it.score }.forAll {
            it.shouldBeGreaterThan(20.0)
        }
    }

    "findTitleWithGauss" {
        // when
        val result = scoreSearchRepository.findTitleWithGauss()

        // then
        result.mappedResults.take(10).map { it.title } shouldBe listOf(
            "The Shop Around the Corner",
            "Exit Through the Gift Shop",
            "Little Shop of Horrors",
            "The Suicide Shop",
            "A Woman, a Gun and a Noodle Shop",
            "Beauty Shop",
        )
        result.mappedResults.take(10).map { it.score }.forAll {
            it.shouldBeBetween(0.5, 1.0, 0.0)
        }
    }

    "findTitleWithPath" {
        // when
        val result = scoreSearchRepository.findTitleWithPath()

        // then
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "The Men Who Built America",
            "No Country for Old Men",
            "X-Men: Days of Future Past",
            "The Best of Men",
            "All the President's Men",
        )
        result.mappedResults.take(5).map { it.score }.forAll {
            it.shouldBeBetween(8.0, 9.0, 0.0)
        }
    }

    "findTitleWithLog" {
        // when
        val result = scoreSearchRepository.findTitleWithLog()

        // then
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "The Men Who Built America",
            "No Country for Old Men",
            "X-Men: Days of Future Past",
            "The Best of Men",
            "All the President's Men",
        )
        result.mappedResults.take(5).map { it.score }.forAll {
            it.shouldBeBetween(0.0, 1.0, 0.0)
        }
    }
})
