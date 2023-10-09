package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import com.github.inflab.spring.data.mongodb.core.aggregation.search.TokenOrder
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_mflix")
internal class AutocompleteSearchRepositoryTest(
    private val autocompleteSearchRepository: AutocompleteSearchRepository,
) : FreeSpec({

    "findByTitle" {
        // when
        val result = autocompleteSearchRepository.findByTitle()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Off Beat",
            "Off the Map",
            "Off and Running",
            "Hands off Mississippi",
            "Taking Off",
            "Noises Off...",
            "Brassed Off",
            "Face/Off",
            "Benji: Off the Leash!",
            "Set It Off",
        )
    }

    "findByTitleWithFuzzy" {
        // when
        val result = autocompleteSearchRepository.findByTitleWithFuzzy()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "The Perils of Pauline",
            "The Blood of a Poet",
            "The Private Life of Henry VIII.",
            "The Private Life of Don Juan",
            "The Prisoner of Shark Island",
            "The Prince and the Pauper",
            "The Prisoner of Zenda",
            "Dance Program",
            "The Pied Piper",
            "Prelude to War",
        )
    }

    "findByTitleWithTokenOrder" {
        // when
        val result = autocompleteSearchRepository.findByTitleWithTokenOrder(TokenOrder.ANY)

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Men with Guns",
            "Men with Brooms",
            "Men Without Women",
            "Brief Interviews with Hideous Men",
        )
    }

    "findByTitleWithCompound" {
        // when
        val result = autocompleteSearchRepository.findByTitleWithCompound()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Interior. Leather Bar.",
            "Interview with the Assassin",
            "Brief Interviews with Hideous Men",
            "Austin Powers: International Man of Mystery",
            "Interstate 60: Episodes of the Road",
            "Interview with the Vampire: The Vampire Chronicles",
            "The Internet\'s Own Boy: The Story of Aaron Swartz",
            "Interrupted Melody",
            "Richness of Internal Space",
            "Interiors",
        )
    }

    "findByTitleWithBucketResults" {
        // when
        val result = autocompleteSearchRepository.findByTitleWithBucketResults()

        // then
        result.mappedResults.first().apply {
            count.lowerBound shouldBe 5
            facet.titleFacet.buckets.map { it.id } shouldBe listOf(
                "Gravity",
                "Defying Gravity",
                "Laws of Gravity",
            )
            facet.titleFacet.buckets.map { it.count } shouldBe listOf(
                3, 1, 1,
            )
        }
    }
})
