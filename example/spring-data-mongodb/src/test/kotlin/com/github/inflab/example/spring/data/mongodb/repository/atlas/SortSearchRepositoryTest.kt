package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.collections.shouldBeMonotonicallyIncreasing
import io.kotest.matchers.shouldBe

@AtlasTest("sample_mflix")
internal class SortSearchRepositoryTest(
    private val sortSearchRepository: SortSearchRepository,
) : FreeSpec({

    "sortByDate" {
        // when
        val result = sortSearchRepository.sortByDate()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Cold in July",
            "The Gambler",
            "Force Majeure",
            "LFO",
            "Peace After Marriage",
        )
    }

    "sortByNumber" {
        // when
        val result = sortSearchRepository.sortByNumber()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "12 Years a Slave",
            "Gravity",
            "Gravity",
            "Birdman: Or (The Unexpected Virtue of Ignorance)",
            "Boyhood",
        )
    }

    "sortByString" {
        // when
        val result = sortSearchRepository.sortByString()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "A Country Called Home",
            "A Month in the Country",
            "A Quiet Place in the Country",
            "A Sunday in the Country",
            "Another Country",
        )
    }

    "sortByCompound" {
        // when
        val result = sortSearchRepository.sortByCompound()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Shall We Dance?",
            "Shall We Dance?",
            "War Dance",
            "Dance with the Devil",
            "Save the Last Dance",
            "Dance with a Stranger",
            "The Baby Dance",
            "Three-Step Dance",
            "Cats Don't Dance",
            "Dance Me Outside",
        )
    }

    "sortByFacet" {
        // when
        val result = sortSearchRepository.sortByFacet()

        // then
        result.mappedResults.first().apply {
            docs.map { it.title } shouldBe listOf(
                "Cold in July",
                "The Gambler",
                "Force Majeure",
                "LFO",
                "Peace After Marriage",
            )
            meta.count.lowerBound shouldBe 4821
            meta.facet.releasedFacet.buckets.map { it.count } shouldBe listOf(857L, 909L, 903L, 1063L, 1089L)
            meta.facet.awardsFacet.buckets.map { it.count } shouldBe listOf(2330L, 604L, 233L)
        }
    }

    "sortByScoreAscending" {
        // when
        val result = sortSearchRepository.sortByScoreAscending()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Do You Believe in Miracles? The Story of the 1980 U.S. Hockey Team",
            "Once in a Lifetime: The Extraordinary Story of the New York Cosmos",
            "The Source: The Story of the Beats and the Beat Generation",
            "If These Knishes Could Talk: The Story of the NY Accent",
            "Dream Deceivers: The Story Behind James Vance vs. Judas Priest",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyIncreasing()
    }

    "sortByScoreUnique" {
        // when
        val result = sortSearchRepository.sortByScoreUnique()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Prince",
            "Prince Avalanche",
            "The Prince",
            "Prince of Foxes",
            "The Oil Prince",
        )
        result.mappedResults.mapNotNull { it.score }.shouldBeMonotonicallyDecreasing()
    }
})
