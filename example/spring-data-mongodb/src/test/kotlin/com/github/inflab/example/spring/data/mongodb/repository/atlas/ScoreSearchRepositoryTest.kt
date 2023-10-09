package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.doubles.shouldBeBetween
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_mflix")
internal class ScoreSearchRepositoryTest(
    private val scoreSearchRepository: ScoreSearchRepository,
) : FreeSpec({

    "findTitleAndPlotWithBoost" {
        // when
        val result = scoreSearchRepository.findTitleAndPlotWithBoost()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Kites Over Helsinki",
            "Helsinki-Naples All Night Long",
            "Drifting Clouds",
            "Sairaan kaunis maailma",
            "Bad Luck Love",
        )
        result.mappedResults.map { it.score }.forAll {
            it.shouldBeBetween(3.0, 25.0, 0.0)
        }
    }

    "findTitleAndPlotWithConstant" {
        // when
        val result = scoreSearchRepository.findTitleAndPlotWithConstant()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Tower Block",
            "Tower Heist",
            "Tokyo Tower",
            "The Leaning Tower",
            "Ivory Tower",
        )
        result.mappedResults.map { it.score }.forAll {
            it.shouldBeBetween(5.0, 9.0, 0.0)
        }
    }

    "findTitleWithArithmetic" {
        // when
        val result = scoreSearchRepository.findTitleWithArithmetic()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Men...",
            "X-Men",
            "X-Men",
            "Matchstick Men",
            "The Men",
        )
        result.mappedResults.map { it.score }.forAll {
            it.shouldBeGreaterThan(20.0)
        }
    }

    "findTitleWithGauss" {
        // when
        val result = scoreSearchRepository.findTitleWithGauss()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "The Shop Around the Corner",
            "Exit Through the Gift Shop",
            "Little Shop of Horrors",
            "The Suicide Shop",
            "A Woman, a Gun and a Noodle Shop",
            "Beauty Shop",
        )
        result.mappedResults.map { it.score }.forAll {
            it.shouldBeBetween(0.5, 1.0, 0.0)
        }
    }

    "findTitleWithPath" {
        // when
        val result = scoreSearchRepository.findTitleWithPath()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "The Men Who Built America",
            "No Country for Old Men",
            "X-Men: Days of Future Past",
            "The Best of Men",
            "All the President's Men",
        )
        result.mappedResults.map { it.score }.forAll {
            it.shouldBeBetween(8.0, 9.0, 0.0)
        }
    }

    "findTitleWithLog" {
        // when
        val result = scoreSearchRepository.findTitleWithLog()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "The Men Who Built America",
            "No Country for Old Men",
            "X-Men: Days of Future Past",
            "The Best of Men",
            "All the President's Men",
        )
        result.mappedResults.map { it.score }.forAll {
            it.shouldBeBetween(0.0, 1.0, 0.0)
        }
    }
})
