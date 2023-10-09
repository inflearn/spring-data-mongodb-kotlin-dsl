package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_mflix")
internal class QueryStringSearchRepositoryTest(
    private val queryStringSearchRepository: QueryStringSearchRepository,
) : FreeSpec({

    "findFullplotWithPlot" {
        // when
        val result = queryStringSearchRepository.findFullplotWithPlot()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Star Trek: Generations",
            "Star Trek V: The Final Frontier",
            "Star Trek: The Motion Picture",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyDecreasing()
    }

    "findPoltWithTitleRange" {
        // when
        val result = queryStringSearchRepository.findPoltWithTitleRange()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "The Great Train Robbery",
            "A Corner in Wheat",
            "Winsor McCay, the Famous Cartoonist of the N.Y. Herald and His Moving Comics",
            "Traffic in Souls",
            "Gertie the Dinosaur",
            "In the Land of the Head Hunters",
            "The Perils of Pauline",
            "The Italian",
            "Regeneration",
            "Where Are My Children?",
        )
    }

    "findTitleByFuzzy" {
        // when
        val result = queryStringSearchRepository.findTitleByFuzzy()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Catch-22",
            "Catch That Girl",
            "Catch That Kid",
            "Catch a Fire",
            "Catch Me Daddy",
            "Death Watch",
            "Patch Adams",
            "Batch '81",
            "Briar Patch",
            "Night Watch",
        )
    }

    "findTitleByWildcard" {
        // when
        val result = queryStringSearchRepository.findTitleByWildcard()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Diary of a Country Priest",
            "Cry, the Beloved Country",
            "Raintree County",
            "Ride the High Country",
            "The Courtship of Eddie's Father",
        )
    }

    "findTitleByRegex" {
        // when
        val result = queryStringSearchRepository.findTitleByRegex()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "The Italian",
            "Marriage Italian Style",
            "Jealousy, Italian Style",
            "My Voyage to Italy",
            "Italian for Beginners",
        )
    }
})
