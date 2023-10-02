package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_mflix")
class NearSearchRepositoryTest(
    private val nearSearchRepository: NearSearchRepository,
) : FreeSpec({

    "findByRuntime" {
        // when
        val result = nearSearchRepository.findByRuntime()

        // then
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "The Kingdom",
            "The Jinx: The Life and Deaths of Robert Durst",
            "Shoah",
            "Les Misèrables",
            "Tokyo Trial",
        )

        result.mappedResults.take(5).map { it.runtime } shouldBe listOf(
            279,
            279,
            280,
            281,
            277,
        )
    }

    "findByDate" {
        // when
        val result = nearSearchRepository.findByDate()

        // then
        result.mappedResults.take(3).map { it.title } shouldBe listOf(
            "Regeneration",
            "The Cheat",
            "Hell's Hinges",
        )
    }
})
