package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.date.shouldBeBetween
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@AtlasTest(database = "sample_mflix")
internal class RangeSearchRepositoryTest(
    private val rangeSearchRepository: RangeSearchRepository,
) : FreeSpec({

    "findRuntimeBetween" {
        // when
        val result = rangeSearchRepository.findRuntimeBetween()

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

        val result = rangeSearchRepository.findReleasedBetween()

        // then
        result.mappedResults.take(5).map { it.released }.forAll {
            it.shouldBeBetween(start, end)
        }
    }
})
