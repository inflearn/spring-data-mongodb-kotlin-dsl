package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_mflix")
internal class TextSearchRepositoryTest(
    private val textSearchRepository: TextSearchRepository,
) : FreeSpec({

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
