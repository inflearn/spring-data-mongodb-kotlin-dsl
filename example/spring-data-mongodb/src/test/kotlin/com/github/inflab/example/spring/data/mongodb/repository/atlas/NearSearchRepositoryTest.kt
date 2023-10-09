package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

@AtlasTest(database = "sample_mflix")
class NearSearchRepositoryTest(
    private val nearSearchRepository: NearSearchRepository,
) : FreeSpec({

    "findByRuntime" {
        // when
        val result = nearSearchRepository.findByRuntime()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "The Kingdom",
            "The Jinx: The Life and Deaths of Robert Durst",
            "Shoah",
            "Les Misèrables",
            "Tokyo Trial",
        )

        result.mappedResults.map { it.runtime } shouldBe listOf(
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
        result.mappedResults.map { it.title } shouldBe listOf(
            "Regeneration",
            "The Cheat",
            "Hell's Hinges",
        )
    }

    "findByGeo" {
        // when
        val result = nearSearchRepository.findByGeo()

        // then
        result.mappedResults.map { it.name } shouldBe listOf(
            "Ribeira Charming Duplex",
            "DB RIBEIRA - Grey Apartment",
            "Ribeira 24 (4)",
        )
    }

    "findByGeoWithCompound" {
        // when
        val result = nearSearchRepository.findByGeoWithCompound()

        // then
        result.mappedResults.map { it.propertyType } shouldBe listOf(
            "Apartment",
            "Apartment",
            "Apartment",
        )

        result.mappedResults.map { it.address.location } shouldBe listOf(
            GeoJsonPoint(114.15027, 22.28158),
            GeoJsonPoint(114.15082, 22.28161),
            GeoJsonPoint(114.15007, 22.28215),
        )
    }
})
